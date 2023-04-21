package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HTMLOptionElement}.
 *
 * @version $Revision: 2637 $
 * @author Marc Guillemot
 */
public class HTMLOptionElementTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testReadPropsBeforeAdding() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n" + "function doTest() {\n" + "    var oOption = new Option('some text', 'some value');\n" + "    alert(oOption.text);\n" + "    alert(oOption.value);\n" + "    alert(oOption.selected);\n" + "    oOption.text = 'some other text';\n" + "    oOption.value = 'some other value';\n" + "    oOption.selected = true;\n" + "    alert(oOption.text);\n" + "    alert(oOption.value);\n" + "    alert(oOption.selected);\n" + "}</script></head><body onload='doTest()'>\n" + "<p>hello world</p>\n" + "</body></html>";
        final String[] expectedAlerts = { "some text", "some value", "false", "some other text", "some other value", "true" };
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug 1323425.
     * See http://sourceforge.net/tracker/index.php?func=detail&aid=1323425&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Test
    public void testSelectingOrphanedOptionCreatedByDocument() throws Exception {
        final String content = "<html>\n" + "<body>\n" + "<form name='myform'/>\n" + "<script language='javascript'>\n" + "var select = document.createElement('select');\n" + "var opt = document.createElement('option');\n" + "opt.value = 'x';\n" + "opt.selected = true;\n" + "select.appendChild(opt);\n" + "document.myform.appendChild(select);\n" + "</script>\n" + "</body></html>";
        loadPage(content);
    }

    /**
     * Regression test for 1592728.
     * @throws Exception if the test fails
     */
    @Test
    public void testSetSelected() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n" + "function doTest() {\n" + "  var sel = document.form1.select1;\n" + "  alert(sel.selectedIndex);\n" + "  sel.options[0].selected = false;\n" + "  alert(sel.selectedIndex);\n" + "}</script></head><body onload='doTest()'>\n" + "<form name='form1'>\n" + "    <select name='select1' onchange='this.form.submit()'>\n" + "        <option value='option1' name='option1'>One</option>\n" + "        <option value='option2' name='option2'>Two</option>\n" + "        <option value='option3' name='option3' selected>Three</option>\n" + "    </select>\n" + "</form>\n" + "</body></html>";
        final String[] expectedAlerts = { "2", "2" };
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for 1672048.
     * @throws Exception if the test fails
     */
    @Test
    public void testSetAttribute() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n" + "function doTest() {\n" + "  document.getElementById('option1').setAttribute('class', 'bla bla');\n" + "  var o = new Option('some text', 'some value');\n" + "  o.setAttribute('class', 'myClass');\n" + "}</script></head><body onload='doTest()'>\n" + "<form name='form1'>\n" + "    <select name='select1'>\n" + "        <option value='option1' id='option1' name='option1'>One</option>\n" + "    </select>\n" + "</form>\n" + "</body></html>";
        final String[] expectedAlerts = {};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testOptionIndexOutOfBound() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n" + "function doTest()\n" + "{\n" + "  var options = document.getElementById('testSelect').options;\n" + "  alert(options[55]);\n" + "  try\n" + "  {\n" + "    alert(options[-55]);\n" + "  }\n" + "  catch (e)\n" + "  {\n" + "    alert('caught exception for negative index');\n" + "  }\n" + "}\n" + "</script></head><body onload='doTest()'>\n" + "<form name='form1'>\n" + "    <select name='select1' id='testSelect'>\n" + "        <option value='option1' name='option1'>One</option>\n" + "    </select>\n" + "</form>\n" + "</body></html>";
        final String[] expectedAlerts = { "undefined", "caught exception for negative index" };
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testConstructor() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n" + "function dumpOption(_o) {\n" + "  return 'text: ' + _o.text\n" + " + ', label: ' + _o.label\n" + " + ', value: ' + _o.value\n" + " + ', defaultSelected: ' + _o.defaultSelected\n" + " + ', selected: ' + _o.selected;\n" + "}\n" + "function doTest() {\n" + "   var o2 = new Option('Option 2', '2');\n" + "   alert('o2: ' + dumpOption(o2));\n" + "   var o3 = new Option('Option 3', '3', true, false);\n" + "   alert('o3: ' + dumpOption(o3));\n" + "   document.form1.select1.appendChild(o3);\n" + "   alert(document.form1.select1.options.selectedIndex);\n" + "   document.form1.reset();\n" + "   alert(document.form1.select1.options.selectedIndex);\n" + "}\n" + "</script></head><body onload='doTest()'>\n" + "<form name='form1'>\n" + "    <select name='select1' id='testSelect'>\n" + "        <option value='option1' name='option1'>One</option>\n" + "    </select>\n" + "</form>\n" + "</body></html>";
        final String[] expectedAlerts = { "o2: text: Option 2, label: , value: 2, defaultSelected: false, selected: false", "o3: text: Option 3, label: , value: 3, defaultSelected: true, selected: false", "0", "1" };
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
