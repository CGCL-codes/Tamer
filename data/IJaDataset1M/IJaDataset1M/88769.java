package com.goodcodeisbeautiful.syndic8.atom10;

import java.util.HashMap;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class Atom10CategoryTest extends TestCase {

    public static Test suite() {
        return new TestSuite(Atom10CategoryTest.class);
    }

    Atom10Category m_category;

    protected void setUp() throws Exception {
        super.setUp();
        m_category = new Atom10Category();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAtom10Category() {
        assertNotNull(new Atom10Category());
    }

    public void testAtom10CategoryAtom10CommonAttributes() {
        Atom10Feed feed = new Atom10Feed();
        Atom10Category c = new Atom10Category(feed);
        assertEquals(feed, c.getParentCommonAttributes());
    }

    public void testGetTerm() {
        assertNull(m_category.getTerm());
        m_category.setTerm("aTerm");
        assertEquals("aTerm", m_category.getTerm());
        m_category.setTerm(null);
        assertNull(m_category.getTerm());
    }

    public void testSetTerm() {
        testGetTerm();
    }

    public void testGetScheme() {
        assertNull(m_category.getScheme());
        m_category.setScheme("aScheme");
        assertEquals("aScheme", m_category.getScheme());
        m_category.setScheme(null);
        assertNull(m_category.getScheme());
    }

    public void testSetScheme() {
        testGetScheme();
    }

    public void testGetLabel() {
        assertNull(m_category.getLabel());
        m_category.setLabel("aLabel");
        assertEquals("aLabel", m_category.getLabel());
        m_category.setLabel(null);
        assertNull(m_category.getLabel());
    }

    public void testSetLabel() {
        testGetLabel();
    }

    public void testGetText() {
        assertNull(m_category.getText());
        m_category.setText("text");
        assertEquals("text", m_category.getText());
        m_category.setText(null);
        assertNull(m_category.getText());
    }

    public void testSetText() {
        testGetText();
    }

    public void testSetAttributes() {
        assertNull(m_category.getTerm());
        assertNull(m_category.getScheme());
        assertNull(m_category.getLabel());
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Atom10Category.ATTR_TERM, "aTerm");
        map.put(Atom10Category.ATTR_SCHEME, "http://example.org/");
        map.put(Atom10Category.ATTR_LABEL, "aLabel");
        m_category.setAttributes(map);
        assertEquals("aTerm", m_category.getTerm());
        assertEquals("http://example.org/", m_category.getScheme());
        assertEquals("aLabel", m_category.getLabel());
    }
}
