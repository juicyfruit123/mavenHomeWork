import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test {
    private static final List<String> STRINGS = Arrays.asList("Ivanov", "Ivan", "18.05.1987", "Иванов", "Иван", "Иванович", "12.07.1995", "6726", "435353", "12.04.2017", "Отделением УФМС России по Нижнему Тагилу");
    private static WebDriver webDriver = null;
    private static JavascriptExecutor JAVASCRIPT_EXECUTOR = null;

    @BeforeAll
    static void testUP() throws InterruptedException {
        String browser = java.lang.System.getProperties().getProperty("webbrowser");

        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");
        System.setProperty("webdriver.gecko.driver", "drivers/geckodriver");
        webDriver = browser.equals("firefox") ? new FirefoxDriver() : new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        webDriver.manage().window().maximize();
        JAVASCRIPT_EXECUTOR = (JavascriptExecutor) webDriver;
        webDriver.get("http://sberbank.ru/ru/person");
        WebElement element1 = webDriver.findElement(By.xpath("//span[text()='Страхование']"));
        element1.click();
        WebElement element = webDriver.findElement(By.xpath("//a[@class='lg-menu__sub-link' and text()='Страхование путешественников']"));
        Actions actions = new Actions(webDriver);
        actions.moveToElement(element1);
        actions.moveToElement(element);
        actions.click().perform();

        WebElement button = webDriver.findElement(By.xpath("//b[text()='Оформить онлайн']"));
        JAVASCRIPT_EXECUTOR.executeScript("return arguments[0].scrollIntoView(true);", button);
        button.click();
        webDriver.findElement(By.xpath("//h3[text()='Минимальная']")).click();
        Thread.sleep(5000);
        webDriver.findElement(By.xpath("//button[text()='Оформить']")).click();

    }

    @AfterAll
    static void cleanUp() {
        webDriver.close();
        webDriver.quit();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Иван", "Петя", "Ахмед"})
    void rGS(String name) throws InterruptedException {

        WebElement surname = webDriver.findElement(By.xpath("//input[@id='surname_vzr_ins_0']"));
        WebElement givenName = webDriver.findElement(By.xpath("//input[@id='name_vzr_ins_0']"));
        WebElement birthDate = webDriver.findElement(By.xpath("//input[@id='birthDate_vzr_ins_0']"));
        WebElement lastName = webDriver.findElement(By.xpath("//input[@id='person_lastName']"));
        WebElement firstName = webDriver.findElement(By.xpath("//input[@id='person_firstName']"));
        WebElement middleName = webDriver.findElement(By.xpath("//input[@id='person_middleName']"));
        WebElement birthDatePerson = webDriver.findElement(By.xpath("//input[@id='person_birthDate']"));
        webDriver.findElement(By.xpath("//label[text()='Мужской']")).click();
        WebElement passportSeries = webDriver.findElement(By.xpath("//input[@id='passportSeries']"));
        WebElement passportNumber = webDriver.findElement(By.xpath("//input[@id='passportNumber']"));
        WebElement documentDate = webDriver.findElement(By.xpath("//input[@id='documentDate']"));
        WebElement docmentIssue = webDriver.findElement(By.xpath("//input[@id='documentIssue']"));

        surname.sendKeys("Ivanov");
        givenName.sendKeys("Ivan");
        birthDate.sendKeys("18051987");

        Thread.sleep(500);
        lastName.sendKeys("");
        lastName.sendKeys("Иванов");
        firstName.sendKeys(name);
        middleName.sendKeys("Иванович");
        birthDatePerson.sendKeys("12071995");
        passportSeries.sendKeys("");
        passportSeries.sendKeys("6726");
        passportNumber.sendKeys("435353");
        documentDate.sendKeys("12042017");
        docmentIssue.sendKeys("");
        docmentIssue.sendKeys("Отделением УФМС России по Нижнему Тагилу");
        List<WebElement> webElements = webDriver.findElements(By.xpath("//input[not(@id='phone') and not(@type='email') and not(@type='checkbox')]"));

        for (int i = 0; i < webElements.size(); i++) {
            if (webElements.get(i).getAttribute("value").equals(name)) {
                continue;
            }
            assertEquals(STRINGS.get(i), webElements.get(i).getAttribute("value"));
        }
        webDriver.findElement(By.xpath("//button[@type='submit']")).click();
        WebElement alert = webDriver.findElement(By.xpath("//div[@role='alert-form']"));
        assertEquals("При заполнении данных произошла ошибка", alert.getText());
        webDriver.get("https://online.sberbankins.ru/store/travel/forming");
    }


}
