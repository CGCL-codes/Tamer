package net.sourceforge.kwaai.samples.login;

import net.sourceforge.kwaai.test.PageRunner;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.inject.annotation.TestedObject;
import javax.servlet.http.HttpServletRequest;

public class LoginControllerTest extends UnitilsJUnit4 {

    @TestedObject
    private LoginController controller = new LoginController();

    private HttpServletRequest request = new MockHttpServletRequest();

    private PageRunner runner;

    @Before
    public void setUp() throws Exception {
        runner = new PageRunner() {

            public String collectComponents(Model model) {
                return controller.view(request, model);
            }
        };
        runner.start();
    }

    @After
    public void tearDown() throws Exception {
        runner.stop();
    }

    @Test
    public void testView() throws Exception {
        String namespace = PageRunner.TEST_NAMESPACE;
        EasyMockUnitils.replay();
        WebDriver driver = new HtmlUnitDriver();
        driver.get("http://localhost:" + runner.getPort());
        WebElement name = driver.findElement(By.id("name"));
        assertNotNull(name);
        WebElement pass = driver.findElement(By.id("password"));
        assertNotNull(pass);
        pass.submit();
        assertEquals("http://localhost:" + runner.getPort() + "/", driver.getCurrentUrl());
    }
}
