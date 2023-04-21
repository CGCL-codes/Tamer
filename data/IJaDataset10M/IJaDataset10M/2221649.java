package com.goodcodeisbeautiful.archtea.search.rss20;

import com.goodcodeisbeautiful.syndic8.rss20.Rss20Item;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class Rss20FoundItemTest extends TestCase {

    public static Test suite() {
        return new TestSuite(Rss20FoundItemTest.class);
    }

    private Rss20FoundItem m_item;

    protected void setUp() throws Exception {
        super.setUp();
        Rss20Item item = new Rss20Item();
        item.setTitle("rss20Title");
        item.setDescription("rss20Summary");
        item.setLink("http://localhost/rss20");
        m_item = new Rss20FoundItem(11, item);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRss20FoundItem() {
        assertNotNull(m_item);
    }

    public void testGetIndexNumber() {
        assertEquals("11", m_item.getIndexNumber());
    }

    public void testGetTitle() {
        assertEquals("rss20Title", m_item.getTitle());
    }

    public void testGetSummary() {
        assertEquals("rss20Summary", m_item.getSummary());
    }

    public void testGetUrl() {
        assertEquals("http://localhost/rss20", m_item.getUrl());
    }

    public void testGetSummarizedUrl() {
        assertEquals("http://localhost/rss20", m_item.getSummarizedUrl());
    }

    public void testGetSize() {
        assertEquals("", m_item.getSize());
    }
}
