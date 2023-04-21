package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlDivision}.
 *
 * @version $Revision: 6701 $
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HtmlDivisionTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "[object HTMLDivElement]", IE = "[object]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n" + "<script>\n" + "  function test() {\n" + "    alert(document.getElementById('myId'));\n" + "  }\n" + "</script>\n" + "</head><body onload='test()'>\n" + "  <div id='myId'/>\n" + "</body></html>";
        final HtmlPage page = loadPage(html);
        assertTrue(HtmlDivision.class.isInstance(page.getHtmlElementById("myId")));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
        String expected = "hello" + LINE_SEPARATOR + "world";
        testAsText(expected, "<div>hello</div>world");
        testAsText(expected, "<div>hello<br/></div>world");
        expected = "hello" + LINE_SEPARATOR + LINE_SEPARATOR + "world";
        testAsText(expected, "<div>hello<br/><br/></div>world");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText_contiguousBlocks() throws Exception {
        final String expected = "hello" + LINE_SEPARATOR + "world";
        testAsText(expected, "<div><table><tr><td>hello</td></tr><tr><td>world<br/></td></tr></table></div>");
        testAsText(expected, "<div>hello</div><div>world</div>");
        testAsText(expected, "<div>hello</div><div><div>world</div></div>");
        testAsText(expected, "<div><table><tr><td>hello</td></tr><tr><td>world<br/></td></tr></table></div>");
    }

    private void testAsText(final String expected, final String htmlSnippet) throws Exception {
        final String html = "<html><head></head><body>\n" + htmlSnippet + "</body></html>";
        final HtmlPage page = loadPage(html);
        assertEquals(expected, page.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asTextDiv() throws Exception {
        final String html = "<html><head></head><body>\n" + "<div id='foo'>\n \n hello </div>" + "</body></html>";
        final HtmlPage page = loadPage(html);
        assertEquals("hello", page.asText());
        final HtmlDivision div = page.getHtmlElementById("foo");
        assertEquals("hello", div.asText());
    }
}
