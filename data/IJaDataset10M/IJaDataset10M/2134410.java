package com.gargoylesoftware.htmlunit.javascript;

import java.util.ArrayList;
import java.util.List;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HTMLCollection}.
 *
 * @version $Revision: 1615 $
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HTMLCollectionTest extends WebTestCase {

    /**
     * Create an instance
     * @param name The name of the test
     */
    public HTMLCollectionTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testImplicitToStringConversion() throws Exception {
        final String content = "<html><head><title>foo</title><script>" + "function test() {\n" + "    alert(document.links != 'foo')\n" + "}\n" + "</script></head><body onload='test()'>" + "<a href='bla.html'>link</a>" + "</body></html>";
        final String[] expectedAlerts = { "true" };
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test that toString is accessible
     * @throws Exception if the test fails
     */
    public void testToStringFunction() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String content = "<html><head><title>foo</title><script>" + "function test() {\n" + "    alert(typeof document.links.toString)\n" + "}\n" + "</script></head><body onload='test()'>" + "<a href='bla.html'>link</a>" + "</body></html>";
        final String[] expectedAlerts = { "function" };
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetElements() throws Exception {
        final String firstContent = "<html><head><title>foo</title><script>" + "function doTest() {\n" + "    alert(document.all.length);\n" + "    document.appendChild(document.createElement('div'));\n" + "    alert(document.all.length);\n" + "}\n" + "</script></head><body onload='doTest()'>\n" + "</body></html>";
        final List collectedAlerts = new ArrayList();
        loadPage(firstContent, collectedAlerts);
        final String[] expectedAlerts = { "5", "6" };
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
