package com.gargoylesoftware.htmlunit.html;

import java.util.Collections;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlPasswordInput}.
 *
 * @version $Revision: 6701 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlPasswordInputTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void type() throws Exception {
        final String html = "<html><head></head><body><input type='password' id='p'/></body></html>";
        final WebDriver driver = loadPage2(html);
        final WebElement p = driver.findElement(By.id("p"));
        p.sendKeys("abc");
        assertEquals("abc", p.getAttribute("value"));
        p.sendKeys("\b");
        assertEquals("ab", p.getAttribute("value"));
        p.sendKeys("\b");
        assertEquals("a", p.getAttribute("value"));
        p.sendKeys("\b");
        assertEquals("", p.getAttribute("value"));
        p.sendKeys("\b");
        assertEquals("", p.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeWhileDisabled() throws Exception {
        final String html = "<html><body><input type='password' id='p' disabled='disabled'/></body></html>";
        final WebDriver driver = loadPage2(html);
        final WebElement p = driver.findElement(By.id("p"));
        try {
            p.sendKeys("abc");
            Assert.fail();
        } catch (final InvalidElementStateException e) {
        }
        assertEquals("", p.getAttribute("value"));
    }

    /**
     * How could this test be migrated to WebDriver? How to select the field's content?
     * @throws Exception if an error occurs
     */
    @Test
    public void typeWhileSelected() throws Exception {
        final String html = "<html><head></head><body>\n" + "<input type='password' id='myInput' value='Hello world'><br>\n" + "</body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlPasswordInput input = page.getHtmlElementById("myInput");
        input.select();
        input.type("Bye World");
        assertEquals("Bye World", input.getValueAttribute());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void preventDefault_OnKeyDown() throws Exception {
        final String html = "<html><head><script>\n" + "  function handler(e) {\n" + "    if (e && e.target.value.length > 2)\n" + "      e.preventDefault();\n" + "    else if (!e && window.event.srcElement.value.length > 2)\n" + "      return false;\n" + "  }\n" + "  function init() {\n" + "    document.getElementById('p').onkeydown = handler;\n" + "  }\n" + "</script></head>\n" + "<body onload='init()'>\n" + "<input type='password' id='p'></input>\n" + "</body></html>";
        final WebDriver driver = loadPage2(html);
        final WebElement p = driver.findElement(By.id("p"));
        p.sendKeys("abcd");
        assertEquals("abc", p.getAttribute("value"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void preventDefault_OnKeyPress() throws Exception {
        final String html = "<html><head><script>\n" + "  function handler(e) {\n" + "    if (e && e.target.value.length > 2)\n" + "      e.preventDefault();\n" + "    else if (!e && window.event.srcElement.value.length > 2)\n" + "      return false;\n" + "  }\n" + "  function init() {\n" + "    document.getElementById('p').onkeypress = handler;\n" + "  }\n" + "</script></head>\n" + "<body onload='init()'>\n" + "<input type='password' id='p'></input>\n" + "</body></html>";
        final WebDriver driver = loadPage2(html);
        final WebElement p = driver.findElement(By.id("p"));
        p.sendKeys("abcd");
        assertEquals("abc", p.getAttribute("value"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void typeOnChange() throws Exception {
        final String html = "<html><head></head><body>\n" + "<input type='password' id='p' value='Hello world'" + " onChange='alert(\"foo\");alert(event.type);'" + " onBlur='alert(\"boo\");alert(event.type);'" + "><br>\n" + "<button id='b'>some button</button>\n" + "</body></html>";
        final WebDriver driver = loadPage2(html);
        final WebElement p = driver.findElement(By.id("p"));
        p.sendKeys("HtmlUnit");
        assertEquals(Collections.emptyList(), getCollectedAlerts(driver));
        driver.findElement(By.id("b")).click();
        final String[] expectedAlerts1 = { "foo", "change", "boo", "blur" };
        assertEquals(expectedAlerts1, getCollectedAlerts(driver));
        p.click();
        assertEquals(expectedAlerts1, getCollectedAlerts(driver));
        driver.findElement(By.id("b")).click();
        final String[] expectedAlerts2 = { "foo", "change", "boo", "blur", "boo", "blur" };
        assertEquals(expectedAlerts2, getCollectedAlerts(driver));
    }
}
