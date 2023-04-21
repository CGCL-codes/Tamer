package com.phloc.commons.microdom.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.MicroException;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.xml.CXML;

/**
 * Test class for class {@link MicroDocument}.
 * 
 * @author philip
 */
public final class MicroDocumentTest {

    @Test
    public void testNew() {
        final IMicroDocument e = new MicroDocument();
        assertNotNull(e);
        assertNull(e.getDocType());
        assertNull(e.getDocumentElement());
        assertFalse(e.hasParent());
        assertFalse(e.hasChildren());
        assertTrue(e.isStandalone());
        assertTrue(e.isEqualContent(e));
        assertFalse(e.isEqualContent(null));
        assertFalse(e.isEqualContent(new MicroText("any")));
        assertTrue(e.isEqualContent(e.getClone()));
        assertTrue(new MicroDocument().isEqualContent(new MicroDocument()));
        assertFalse(new MicroDocument().isEqualContent(new MicroDocument(new MicroDocumentType("any", "public", "system"))));
        e.appendElement("root");
        assertTrue(e.isEqualContent(e.getClone()));
    }

    @Test
    public void testNewWithDocType() {
        final IMicroDocument d = new MicroDocument(new MicroDocumentType("html", "public ID", "system ID"));
        assertNotNull(d);
        assertNotNull(d.getDocType());
        assertNull(d.getDocumentElement());
        assertTrue(d.hasChildren());
        assertFalse(d.isStandalone());
        assertEquals(1, d.getChildren().size());
    }

    @Test
    public void testAppendToRoot() {
        IMicroDocument d = new MicroDocument();
        assertNotNull(d.appendElement("root"));
        try {
            d.appendEntityReference("lt");
            fail();
        } catch (final MicroException ex) {
        }
        try {
            d.appendElement("root2");
            fail();
        } catch (final MicroException ex) {
        }
        d = new MicroDocument();
        assertNotNull(d.appendComment("This is a root comment"));
        assertNotNull(d.appendComment("Well I forgot something"));
        assertNotNull(d.appendElement("root"));
        assertNotNull(d.appendComment("Some more comment after the root element"));
    }

    @Test
    public void testToString() {
        final IMicroDocument d = new MicroDocument();
        PhlocTestUtils.testToStringImplementation(d);
    }

    @Test
    public void testIsStandalone() {
        final IMicroDocument aDoc = new MicroDocument();
        assertTrue(aDoc.isStandalone());
        final IMicroElement eRoot = aDoc.appendElement("root");
        assertTrue(aDoc.isStandalone());
        eRoot.setAttribute("any", "Value");
        assertTrue(aDoc.isStandalone());
        eRoot.setAttribute("xml:lang", "de");
        assertTrue(aDoc.isStandalone());
        eRoot.setAttribute("xmlns:foo", "http://www.phloc.com/foo");
        assertTrue(aDoc.isStandalone());
        eRoot.setAttribute("xmlns:xsi", CXML.XML_NS_XSI);
        assertTrue(aDoc.isStandalone());
        eRoot.setAttribute("xsi:schemaLocation", "my URL");
        assertFalse(aDoc.isStandalone());
    }
}
