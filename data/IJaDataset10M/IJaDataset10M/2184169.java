package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLOptionElement}.
 *
 * @version $Revision: 6701 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLOptionElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SELECT")
    public void click() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n" + "  function init() {\n" + "    var s = document.getElementById('s');\n" + "    if (s.addEventListener) {\n" + "      s.addEventListener('click', handle, false);\n" + "    } else if (s.attachEvent) {\n" + "      s.attachEvent('onclick', handle);\n" + "    }\n" + "  }\n" + "  function handle(event) {\n" + "    if (event.target)\n" + "      alert(event.target.nodeName);\n" + "    else\n" + "      alert(event.srcElement.nodeName);\n" + "  }\n" + "</script></head><body onload='init()'>\n" + "  <select id='s'>\n" + "    <option value='a'>A</option>\n" + "    <option id='opb' value='b'>B</option>\n" + "    <option value='c'>C</option>\n" + "  </select>\n" + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("s")).click();
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "ab", IE8 = "abb", IE = "ab", CHROME = "")
    @NotYetImplemented(Browser.FF)
    public void click2() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n" + "  function init() {\n" + "    var s = document.getElementById('s');\n" + "    if (s.addEventListener) {\n" + "      s.addEventListener('click', handle, false);\n" + "    } else if (s.attachEvent) {\n" + "      s.attachEvent('onclick', handle);\n" + "    }\n" + "  }\n" + "  function handle(event) {\n" + "    document.getElementById('input').value += s.options[s.selectedIndex].value;\n" + "  }\n" + "</script></head><body onload='init()'>\n" + "  <select id='s'>\n" + "    <option value='a'>A</option>\n" + "    <option id='opb' value='b'>B</option>\n" + "    <option value='c'>C</option>\n" + "  </select>\n" + "  <input id='input'>\n" + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("s")).click();
        driver.findElement(By.id("opb")).click();
        assertEquals(getExpectedAlerts()[0], driver.findElement(By.id("input")).getAttribute("value"));
    }

    /**
     * Regression test for 3171569: unselecting the selected option should select the first one (FF)
     * or have no effect (IE).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "option1", "0" }, IE = { "1", "option2", "1" })
    public void unselectResetToFirstOption() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n" + "function doTest() {\n" + "  var sel = document.form1.select1;\n" + "  alert(sel.selectedIndex);\n" + "  sel.options[1].selected = false;\n" + "  alert(sel.value);\n" + "  alert(sel.selectedIndex);\n" + "}</script></head><body onload='doTest()'>\n" + "<form name='form1'>\n" + "    <select name='select1'>\n" + "        <option value='option1' name='option1'>One</option>\n" + "        <option value='option2' name='option2' selected>Two</option>\n" + "    </select>\n" + "</form>\n" + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void selectFromJSTriggersNoFocusEvent() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n" + "function doTest() {\n" + "  var sel = document.form1.select1;\n" + "  sel.options[1].selected = true;\n" + "  alert(sel.selectedIndex);\n" + "}</script></head><body onload='doTest()'>\n" + "<form name='form1'>\n" + "    <select name='select1' onfocus='alert(\"focus\")'>\n" + "        <option value='option1' name='option1'>One</option>\n" + "        <option value='option2' name='option2'>Two</option>\n" + "    </select>\n" + "</form>\n" + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
