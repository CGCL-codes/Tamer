package com.goodcodeisbeautiful.archtea.search.os;

import com.goodcodeisbeautiful.syndic8.atom10.Atom10Content;
import com.goodcodeisbeautiful.syndic8.atom10.Atom10Entry;
import com.goodcodeisbeautiful.syndic8.atom10.Atom10Link;
import com.goodcodeisbeautiful.syndic8.atom10.Atom10TextConstruct;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class Atom10FoundItemTest extends TestCase {

    public static Test suite() {
        return new TestSuite(Atom10FoundItemTest.class);
    }

    Atom10Entry m_entry;

    Atom10FoundItem m_item;

    protected void setUp() throws Exception {
        super.setUp();
        m_entry = new Atom10Entry();
        Atom10TextConstruct title = new Atom10TextConstruct();
        title.setText("title");
        title.setType("text");
        m_entry.setTitle(title);
        Atom10Content summary = new Atom10Content();
        summary.setText("summary");
        summary.setType("text");
        m_entry.setContent(summary);
        Atom10Link link = new Atom10Link();
        link.setHref("http://localhost:12345/link1");
        m_entry.addLink(link);
        m_item = new Atom10FoundItem(15, m_entry);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        m_entry = null;
        m_item = null;
    }

    public void testAtom10FoundItem() {
        assertNotNull(m_item);
    }

    public void testGetIndexNumber() {
        assertEquals("" + 15, m_item.getIndexNumber());
    }

    public void testGetTitle() {
        assertEquals("title", m_item.getTitle());
    }

    public void testGetSummary() {
        assertEquals("summary", m_item.getSummary());
    }

    public void testGetUrl() {
        assertEquals("http://localhost:12345/link1", m_item.getUrl());
    }

    public void testGetSummarizedUrl() {
        assertEquals("http://localhost:12345/link1", m_item.getSummarizedUrl());
    }

    public void testGetSize() {
        assertNull(m_item.getSize());
    }

    public void testGetUrl2() {
        m_entry = new Atom10Entry();
        m_entry.setXmlBase("http://localhost:12345");
        Atom10Link link = new Atom10Link();
        link.setHref("/link1");
        m_entry.addLink(link);
        m_item = new Atom10FoundItem(15, m_entry);
        assertEquals("http://localhost:12345/link1", m_item.getUrl());
        assertEquals("http://localhost:12345/link1", m_item.getSummarizedUrl());
    }
}
