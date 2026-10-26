
package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final String LOGIN_URL =
            "https://practicetestautomation.com/practice-test-login/";

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();

        // Run Chrome without a visible browser window (GitHub Actions).
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.get(LOGIN_URL);
    }

    @Test
    public void validLoginTest() {
        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("submit")).click();

        wait.until(ExpectedConditions.urlContains(
                "/logged-in-successfully/"
        ));

        Assert.assertTrue(
                driver.getCurrentUrl().contains("/logged-in-successfully/"),
                "Login redirect failed"
        );

        Assert.assertTrue(
                driver.getPageSource().contains("Congratulations"),
                "Success message was not displayed"
        );

        System.out.println("PASS: Valid login test");
    }

    @Test
    public void invalidPasswordTest() {
        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("WrongPassword");
        driver.findElement(By.id("submit")).click();

        String error = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("error")
                )
        ).getText();

        Assert.assertTrue(
                error.contains("Your password is invalid!"),
                "Expected invalid password message"
        );

        System.out.println("PASS: Invalid password test");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
