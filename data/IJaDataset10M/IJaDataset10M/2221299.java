package com.gnizr.core.search;

import java.util.List;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import com.gnizr.core.GnizrCoreTestBase;
import com.gnizr.core.bookmark.TestBookmarkManager;
import com.gnizr.db.dao.Bookmark;

public class TestBookmarkSearchResult extends GnizrCoreTestBase {

    private BookmarkSearchResult searchResult;

    public TestBookmarkSearchResult(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSet(TestBookmarkManager.class.getResourceAsStream("/TestLinkTextSearchResult-input.xml"));
    }

    public void testGetLength() throws Exception {
        searchResult = new BookmarkSearchResult(getGnizrDao(), "mysql");
        assertEquals(2, searchResult.length());
        assertEquals(2, searchResult.length());
    }

    public void testGetResult() throws Exception {
        searchResult = new BookmarkSearchResult(getGnizrDao(), "MySQL Boolean");
        Bookmark link1 = searchResult.getResult(0);
        assertNotNull(link1);
        assertEquals(3, link1.getId());
        Bookmark link2 = searchResult.getResult(1);
        assertNotNull(link2);
        assertNotNull(link2.getUser().getUsername());
    }

    public void testGetResults() throws Exception {
        searchResult = new BookmarkSearchResult(getGnizrDao(), "fulltext mysql -Boolean");
        List<Bookmark> links = searchResult.getResults(0, 10);
        assertEquals(1, links.size());
        Bookmark link1 = links.get(0);
        assertNotNull(link1);
        assertEquals(4, link1.getId());
        assertNotNull(link1.getUser().getUsername());
    }
}
