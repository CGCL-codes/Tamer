package testCasesCompareProducts;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import PageObjects.BasePageClass;
import PageObjects.EnhanceSearchPageSPR;
import PageObjects.HomePage;
import codebase.WDriver;
import codebase.Constants;

public class testcaseCompareButtonPresentSPRUI extends TestCase {

    WebDriver driver;

    String CompareButton = Constants.topCompareButton;

    @Before
    public void setUp() throws Exception {
        driver = WDriver.getBrowser();
    }

    public void testCompareButton() throws Exception {
        BasePageClass currentPage = new BasePageClass(driver);
        driver = currentPage.QuickSearch("chair");
        if (currentPage.isElementPresent(By.id(CompareButton))) System.out.println("Compare Button Found");
        assertEquals(currentPage.isElementPresent(By.id(CompareButton)), true);
    }

    @After
    public void tearDown() throws Exception {
    }
}
