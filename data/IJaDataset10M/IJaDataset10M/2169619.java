package org.objectstyle.cayenne.access;

import java.util.List;
import org.objectstyle.art.Artist;
import org.objectstyle.cayenne.DataObject;
import org.objectstyle.cayenne.PersistenceState;
import org.objectstyle.cayenne.ValueHolder;
import org.objectstyle.cayenne.exp.Expression;
import org.objectstyle.cayenne.exp.ExpressionFactory;
import org.objectstyle.cayenne.query.SelectQuery;

/**
 * @author Andrei Adamchik
 */
public class IncrementalFaultListPrefetchTst extends DataContextTestBase {

    protected void setUp() throws Exception {
        super.setUp();
        createTestData("testPaintings");
    }

    /**
     * Test that all queries specified in prefetch are executed with a single prefetch
     * path.
     */
    public void testPrefetch1() {
        Expression e = ExpressionFactory.likeExp("artistName", "artist1%");
        SelectQuery q = new SelectQuery("Artist", e);
        q.addPrefetch("paintingArray");
        q.setPageSize(4);
        IncrementalFaultList result = (IncrementalFaultList) context.performQuery(q);
        assertEquals(11, result.size());
        assertEquals(result.size(), result.getUnfetchedObjects());
        getDomain().restartQueryCounter();
        for (int i = 4; i < 8; i++) {
            result.get(i);
            assertEquals(1, getDomain().getQueryCount());
        }
    }

    /**
     * Test that a to-many relationship is initialized.
     */
    public void testPrefetch3() {
        Expression e = ExpressionFactory.likeExp("artistName", "artist1%");
        SelectQuery q = new SelectQuery("Artist", e);
        q.addPrefetch("paintingArray");
        q.setPageSize(4);
        IncrementalFaultList result = (IncrementalFaultList) context.performQuery(q);
        assertEquals(11, result.size());
        assertEquals(result.size(), result.getUnfetchedObjects());
        for (int i = 4; i < 8; i++) {
            Artist a = (Artist) result.get(i);
            List paintings = a.getPaintingArray();
            assertFalse(((ValueHolder) paintings).isFault());
            assertEquals(1, paintings.size());
        }
    }

    /**
     * Test that a to-one relationship is initialized.
     */
    public void testPrefetch4() {
        SelectQuery q = new SelectQuery("Painting");
        q.setPageSize(4);
        q.addPrefetch("toArtist");
        IncrementalFaultList result = (IncrementalFaultList) context.performQuery(q);
        DataObject p1 = (DataObject) result.get(q.getPageSize());
        blockQueries();
        try {
            Object toOnePrefetch = p1.readNestedProperty("toArtist");
            assertNotNull(toOnePrefetch);
            assertTrue("Expected DataObject, got: " + toOnePrefetch.getClass().getName(), toOnePrefetch instanceof DataObject);
            DataObject a1 = (DataObject) toOnePrefetch;
            assertEquals(PersistenceState.COMMITTED, a1.getPersistenceState());
        } finally {
            unblockQueries();
        }
    }
}
