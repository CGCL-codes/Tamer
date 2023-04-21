package com.gargoylesoftware.htmlunit.html;

import java.util.Iterator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.helpers.AttributesImpl;
import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link DefaultElementFactory}.
 *
 * @version $Revision: 6701 $
 * @author <a href="mailto:marvin.java@gmail.com">Marcos Vinicius B. de Souza</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @since 1.2
 */
@RunWith(BrowserRunner.class)
public class DefaultElementFactoryTest extends WebTestCase {

    /**
     * Test that the attribute order is the same as the provided one.
     * @throws Exception if the test fails
     */
    @Test
    public void attributeOrder() throws Exception {
        final String html = "<html><head><title>test page</title></head>\n" + "<body><div>test message</div></body></html>";
        final HtmlPage htmlPage = loadPage(html);
        final AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(null, "href", "href", null, "http://www.google.com");
        atts.addAttribute(null, "tabindex", "tabindex", null, "2");
        atts.addAttribute(null, "accesskey", "accesskey", null, "F");
        final DefaultElementFactory defaultElementFactory = new DefaultElementFactory();
        final HtmlAnchor anchor = (HtmlAnchor) defaultElementFactory.createElement(htmlPage, "a", atts);
        verifyAttributes(anchor);
    }

    /**
     * @param anchor the anchor which attributes should be checked
     */
    private void verifyAttributes(final HtmlAnchor anchor) {
        final Iterator<DomAttr> attributeEntriesIterator = anchor.getAttributesMap().values().iterator();
        DomAttr htmlAttr = attributeEntriesIterator.next();
        assertEquals("href", htmlAttr.getNodeName());
        assertEquals("http://www.google.com", htmlAttr.getValue());
        htmlAttr = attributeEntriesIterator.next();
        assertEquals("tabindex", htmlAttr.getNodeName());
        assertEquals("2", htmlAttr.getValue());
        htmlAttr = attributeEntriesIterator.next();
        assertEquals("accesskey", htmlAttr.getNodeName());
        assertEquals("F", htmlAttr.getValue());
    }

    /**
     * Test the order of attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void attributeOrderLive() throws Exception {
        final String html = "<html><body>\n" + "<a href='http://www.google.com' tabindex='2' accesskey='F'>foo</a>\n" + "</body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlAnchor anchor = page.getAnchorByText("foo");
        verifyAttributes(anchor);
    }
}
