package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link com.gargoylesoftware.htmlunit.javascript.host.Option}.
 *
 * @version  $Revision: 1129 $
 * @author Marc Guillemot
 */
public class OptionTest extends WebTestCase {

    /**
     * Create an instance
     * @param name The name of the test
     */
    public OptionTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testReadPropsBeforeAdding() throws Exception {
        final String content = "<html><head><title>foo</title><script>" + "function doTest() {\n" + "    var oOption = new Option('some text', 'some value');\n" + "    alert(oOption.text);\n" + "    alert(oOption.value);\n" + "    alert(oOption.selected);\n" + "    oOption.text = 'some other text';\n" + "    oOption.value = 'some other value';\n" + "    oOption.selected = true;\n" + "    alert(oOption.text);\n" + "    alert(oOption.value);\n" + "    alert(oOption.selected);\n" + "}</script></head><body onload='doTest()'>" + "<p>hello world</p>" + "</body></html>";
        final List expectedAlerts = Arrays.asList(new String[] { "some text", "some value", "false", "some other text", "some other value", "true" });
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug 1323425
     * http://sourceforge.net/tracker/index.php?func=detail&aid=1323425&group_id=47038&atid=448266
     * @throws Exception if the test fails
     */
    public void testSelectingOrphanedOptionCreatedByDocument() throws Exception {
        final String content = "<html>" + "<body>" + "<form name='myform'/>" + "<script language='javascript'>" + "var select = document.createElement('select');" + "var opt = document.createElement('option');" + "opt.value = 'x';" + "opt.selected = true;" + "select.appendChild(opt);" + "document.myform.appendChild(select);" + "</script>" + "</body></html>";
        loadPage(content);
    }
}
