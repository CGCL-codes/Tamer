package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for Style.
 *
 * @version  $Revision: 867 $
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class StyleTest extends WebTestCase {

    /**
     * Create an instance
     * @param name The name of the test
     */
    public StyleTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testStyle_OneCssAttribute() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        final String firstContent = "<html><head><title>First</title><script>\n" + "function doTest() {\n" + "    var style = document.getElementById('div1').style;\n" + "    alert(style.color);\n" + "    style.color = 'pink';\n" + "    alert(style.color);\n" + "}\n</script></head>" + "<body onload='doTest()'><div id='div1' style='color: black'>foo</div></body></html>";
        webConnection.setResponse(URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST);
        client.setWebConnection(webConnection);
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        final List expectedAlerts = Arrays.asList(new String[] { "black", "pink" });
        assertEquals(expectedAlerts, collectedAlerts);
        assertEquals("color: pink; ", page.getHtmlElementById("div1").getAttributeValue("style"));
    }

    /**
     * @throws Exception if the test fails
     */
    public void testStyle_MultipleCssAttributes() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        final String firstContent = "<html><head><title>First</title><script>\n" + "function doTest() {\n" + "    var style = document.getElementById('div1').style;\n" + "    alert(style.color);\n" + "    style.color = 'pink';\n" + "    alert(style.color);\n" + "}\n</script></head>" + "<body onload='doTest()'>" + "<div id='div1' style='color: black;background:blue;foo:bar'>foo</div></body></html>";
        webConnection.setResponse(URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST);
        client.setWebConnection(webConnection);
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        final List expectedAlerts = Arrays.asList(new String[] { "black", "pink" });
        assertEquals(expectedAlerts, collectedAlerts);
        assertEquals("background: blue; color: pink; foo: bar; ", page.getHtmlElementById("div1").getAttributeValue("style"));
    }

    /**
     * @throws Exception if the test fails
     */
    public void testStyle_OneUndefinedCssAttribute() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        final String firstContent = "<html><head><title>First</title><script>\n" + "function doTest() {\n" + "    var style = document.getElementById('div1').style;\n" + "    alert(document.getElementById('nonexistingid'));\n" + "    alert(style.color);\n" + "    style.color = 'pink';\n" + "    alert(style.color);\n" + "}\n</script></head>" + "<body onload='doTest()'><div id='div1'>foo</div></body></html>";
        webConnection.setResponse(URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST);
        client.setWebConnection(webConnection);
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        final List expectedAlerts = Arrays.asList(new String[] { "null", "", "pink" });
        assertEquals(expectedAlerts, collectedAlerts);
        assertEquals("color: pink; ", page.getHtmlElementById("div1").getAttributeValue("style"));
    }
}
