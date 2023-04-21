package com.gnizr.web.action.clustermap;

import java.util.List;
import java.util.Map;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import com.gnizr.core.tag.TagManager;
import com.gnizr.core.web.junit.GnizrWebappTestBase;
import com.gnizr.db.dao.Bookmark;
import com.gnizr.db.dao.Tag;
import com.opensymphony.xwork.ActionSupport;

public class TestClusterUserBookmark2 extends GnizrWebappTestBase {

    private ClusterUserBookmark action;

    protected void setUp() throws Exception {
        super.setUp();
        TagManager tagManager = new TagManager(getGnizrDao());
        action = new ClusterUserBookmark();
        action.setTagManager(tagManager);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSet(TestClusterUserBookmark2.class.getResourceAsStream("/TestClusterUserBookmark-input.xml"));
    }

    public void testGo() throws Exception {
        action.setUsername("hchen1");
        String result = action.execute();
        assertEquals(ActionSupport.SUCCESS, result);
        List<Bookmark> bookmarks = action.getBookmarks();
        assertEquals(5, bookmarks.size());
        Map<String, List<Integer>> cluster = action.getCluster();
        assertEquals(6, cluster.keySet().size());
        List<Integer> root = cluster.get("root");
        assertEquals(5, root.size());
        assertTrue(root.contains(300));
        assertTrue(root.contains(301));
        assertTrue(root.contains(302));
        assertTrue(root.contains(303));
        List<Integer> c1 = cluster.get("1");
        assertEquals(1, c1.size());
        assertTrue(c1.contains(302));
        List<Integer> c2 = cluster.get("2");
        assertEquals(2, c2.size());
        assertTrue(c2.contains(300));
        assertTrue(c2.contains(301));
        List<Integer> c3 = cluster.get("3");
        assertEquals(1, c3.size());
        assertTrue(c3.contains(300));
        List<Integer> c4 = cluster.get("4");
        assertEquals(1, c4.size());
        assertTrue(c4.contains(303));
        List<Integer> c0 = cluster.get("0");
        assertEquals(1, c0.size());
        assertTrue(c0.contains(304));
        List<Tag> tags = action.getTags();
        assertEquals(5, tags.size());
    }
}
