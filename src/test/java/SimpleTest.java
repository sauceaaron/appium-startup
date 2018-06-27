import com.saucelabs.saucerest.SauceREST;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SimpleTest
{
	public static String SAUCE_USERNAME = System.getenv("SAUCE_USERNAME");
	public static String SAUCE_ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");

	public static String SAUCE_URL = "https://SAUCE_USERNAME:SAUCE_ACCESS_KEY@ondemand.saucelabs.com:443/wd/hub"
			.replace("SAUCE_USERNAME", SAUCE_USERNAME)
			.replace("SAUCE_ACCESS_KEY", SAUCE_ACCESS_KEY);
	@Test
	public void openSauceHomePage() throws MalformedURLException
	{
		URL sauceURL = new URL(SAUCE_URL);
		System.out.println("URL : " + sauceURL);

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("appiumVersion", "1.8.1");
		capabilities.setCapability("platformName","Android");
		capabilities.setCapability("platformVersion", "6.0");
		capabilities.setCapability("browserName", "chrome");
		capabilities.setCapability("deviceName","Android Emulator");
		capabilities.setCapability("deviceOrientation", "portrait");
		capabilities.setCapability("name", "open sauce home page on android emulator");
		System.out.println("DESIRED CAPABILITIES: " + capabilities);

		RemoteWebDriver driver = new RemoteWebDriver(sauceURL, capabilities);
		System.out.println("ACTUAL CAPABILITIES: " + driver.getCapabilities());

		driver.get("https://saucelabs.com");
		System.out.println("TITLE: " + driver.getTitle());

		driver.quit();
	}

	@Test
	public void launchExampleApp() throws IOException
	{
		uploadToSauceStorage("HelloSauceAndroid.apk");

		URL sauceURL = new URL(SAUCE_URL);
		System.out.println("URL : " + sauceURL);

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("appiumVersion", "1.8.1");
		capabilities.setCapability("platformName","Android");
		capabilities.setCapability("platformVersion", "6.0");
		capabilities.setCapability("app", "sauce-storage:HelloSauceAndroid.apk");
		capabilities.setCapability("deviceName","Android Emulator");
		capabilities.setCapability("deviceOrientation", "portrait");
		capabilities.setCapability("name", "open native app on android emulator");
		System.out.println("DESIRED CAPABILITIES: " + capabilities);

		AppiumDriver driver = new AndroidDriver(sauceURL, capabilities);
		System.out.println("ACTUAL CAPABILITIES: " + driver.getCapabilities());

		System.out.println("CONTEXT: " + driver.getContext());

		MobileElement titleBar = (MobileElement) driver.findElementById("com.saucelabs.hellosauceandroid:id/action_bar_container");
		System.out.println("TITLE: " + titleBar.getText());

		driver.quit();
	}

	public void uploadToSauceStorage(String filename) throws IOException
	{
		SauceREST api = new SauceREST(SAUCE_USERNAME, SAUCE_ACCESS_KEY);
		ClassLoader loader = this.getClass().getClassLoader();
		File localApk = new File(loader.getResource("HelloSauceAndroid.apk").getFile());
		String uploadResponse = api.uploadFile(localApk);
		System.out.println("UPLOADED FILE MD5: " + uploadResponse);
	}
}
