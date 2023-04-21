package com.volantis.mcs.dom;

import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import com.volantis.mcs.dom.debug.DOMUtilities;

/**
 * Test cases for {@link Node}.
 */
public class NodeIntegrationTestCase extends MockTestCaseAbstract {

    /**
     * Ensure {@link Node#replaceWith(NodeSequence)} method works correctly
     * with empty sequence
     */
    public void testReplaceWithEmpty() throws Exception {
        Document document = DOMUtilities.read("<root><marker/></root>");
        Element root = (Element) document.getRootElement();
        assertEquals("root", root.getName());
        Element marker = (Element) root.getHead();
        assertEquals("marker", marker.getName());
        NodeSequence insert = DOMUtilities.readSequence("");
        marker.replaceWith(insert);
        String result = DOMUtilities.toString(document);
        assertEquals("<root/>", result);
    }

    /**
     * Ensure {@link Node#replaceWith(NodeSequence)} method works correctly
     * with a non emtpy sequence.
     */
    public void testReplaceWithNonEmpty() throws Exception {
        Document document = DOMUtilities.read("<root><marker/></root>");
        Element root = (Element) document.getRootElement();
        assertEquals("root", root.getName());
        Element marker = (Element) root.getHead();
        assertEquals("marker", marker.getName());
        NodeSequence insert = DOMUtilities.readSequence("Text<element/>");
        marker.replaceWith(insert);
        String result = DOMUtilities.toString(document);
        assertEquals("<root>Text<element/></root>", result);
    }
}
