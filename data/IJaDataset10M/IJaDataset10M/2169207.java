package com.gnizr.core;

import java.util.List;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import com.gnizr.core.bookmark.TestBookmarkManager;
import com.gnizr.core.link.LinkManager;
import com.gnizr.db.dao.Bookmark;
import com.gnizr.db.dao.Link;

public class TestLinkHistory extends GnizrCoreTestBase {

    private LinkManager linkHistory;

    protected void setUp() throws Exception {
        super.setUp();
        linkHistory = new LinkManager(getGnizrDao());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSet(TestBookmarkManager.class.getResourceAsStream("/TestLinkHistory-input.xml"));
    }

    public void testGetInfo() throws Exception {
        Link link = linkHistory.getInfo("97014960532642d4a2038b7f7361efab");
        assertEquals("http://www.springframework.org/docs/reference/beans.html", link.getUrl());
    }

    public void testGetHistory() throws Exception {
        Link link = new Link(208);
        List<Bookmark> result = linkHistory.getHistory(link);
        assertEquals(1, result.size());
        Bookmark bm = result.get(0);
        assertEquals("Wii Play: Cow Racing", bm.getTitle());
    }
}
