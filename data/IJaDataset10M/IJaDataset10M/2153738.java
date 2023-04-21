package org.objectstyle.cayenne.access;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.objectstyle.art.Artist;
import org.objectstyle.art.Painting;
import org.objectstyle.cayenne.DataObject;
import org.objectstyle.cayenne.DataObjectUtils;
import org.objectstyle.cayenne.DataRow;
import org.objectstyle.cayenne.ObjectId;
import org.objectstyle.cayenne.PersistenceState;
import org.objectstyle.cayenne.Persistent;
import org.objectstyle.cayenne.query.ObjectIdQuery;
import org.objectstyle.cayenne.unit.CayenneTestCase;

/**
 * Tests objects registration in DataContext, transferring objects between contexts and
 * such.
 * 
 * @author Andrus Adamchik
 */
public class DataContextObjectTrackingTst extends CayenneTestCase {

    public void testUnregisterObject() {
        DataContext context = createDataContext();
        DataRow row = new DataRow(10);
        row.put("ARTIST_ID", new Integer(1));
        row.put("ARTIST_NAME", "ArtistXYZ");
        row.put("DATE_OF_BIRTH", new Date());
        DataObject obj = context.objectFromDataRow(Artist.class, row, false);
        ObjectId oid = obj.getObjectId();
        assertEquals(PersistenceState.COMMITTED, obj.getPersistenceState());
        assertSame(context, obj.getDataContext());
        assertSame(obj, context.getGraphManager().getNode(oid));
        context.unregisterObjects(Collections.singletonList(obj));
        assertEquals(PersistenceState.TRANSIENT, obj.getPersistenceState());
        assertNull(obj.getDataContext());
        assertNull(obj.getObjectId());
        assertNull(context.getGraphManager().getNode(oid));
        assertNull(context.getObjectStore().getCachedSnapshot(oid));
    }

    public void testInvalidateObject() {
        DataContext context = createDataContext();
        DataRow row = new DataRow(10);
        row.put("ARTIST_ID", new Integer(1));
        row.put("ARTIST_NAME", "ArtistXYZ");
        row.put("DATE_OF_BIRTH", new Date());
        DataObject obj = context.objectFromDataRow(Artist.class, row, false);
        ObjectId oid = obj.getObjectId();
        assertEquals(PersistenceState.COMMITTED, obj.getPersistenceState());
        assertSame(context, obj.getDataContext());
        assertSame(obj, context.getGraphManager().getNode(oid));
        context.invalidateObjects(Collections.singletonList(obj));
        assertEquals(PersistenceState.HOLLOW, obj.getPersistenceState());
        assertSame(context, obj.getDataContext());
        assertSame(oid, obj.getObjectId());
        assertNull(context.getObjectStore().getCachedSnapshot(oid));
        assertNotNull(context.getGraphManager().getNode(oid));
    }

    public void testLocalObjectPeerContextMap() throws Exception {
        deleteTestData();
        createTestData("testArtists");
        DataContext context = createDataContext();
        DataContext peerContext = createDataContext();
        DataObject _new = context.createAndRegisterNewObject(Artist.class);
        Persistent hollow = context.localObject(new ObjectId("Artist", Artist.ARTIST_ID_PK_COLUMN, 33001), null);
        DataObject committed = (DataObject) DataObjectUtils.objectForQuery(context, new ObjectIdQuery(new ObjectId("Artist", Artist.ARTIST_ID_PK_COLUMN, 33002)));
        int modifiedId = 33003;
        Artist modified = (Artist) DataObjectUtils.objectForQuery(context, new ObjectIdQuery(new ObjectId("Artist", Artist.ARTIST_ID_PK_COLUMN, modifiedId)));
        modified.setArtistName("MODDED");
        DataObject deleted = (DataObject) DataObjectUtils.objectForQuery(context, new ObjectIdQuery(new ObjectId("Artist", Artist.ARTIST_ID_PK_COLUMN, 33004)));
        context.deleteObject(deleted);
        assertEquals(PersistenceState.HOLLOW, hollow.getPersistenceState());
        assertEquals(PersistenceState.COMMITTED, committed.getPersistenceState());
        assertEquals(PersistenceState.MODIFIED, modified.getPersistenceState());
        assertEquals(PersistenceState.DELETED, deleted.getPersistenceState());
        assertEquals(PersistenceState.NEW, _new.getPersistenceState());
        blockQueries();
        try {
            Persistent hollowPeer = peerContext.localObject(hollow.getObjectId(), null);
            assertEquals(PersistenceState.HOLLOW, hollowPeer.getPersistenceState());
            assertEquals(hollow.getObjectId(), hollowPeer.getObjectId());
            assertSame(peerContext, hollowPeer.getObjectContext());
            assertSame(context, hollow.getObjectContext());
            Persistent committedPeer = peerContext.localObject(committed.getObjectId(), null);
            assertEquals(PersistenceState.HOLLOW, committedPeer.getPersistenceState());
            assertEquals(committed.getObjectId(), committedPeer.getObjectId());
            assertSame(peerContext, committedPeer.getObjectContext());
            assertSame(context, committed.getDataContext());
            Persistent modifiedPeer = peerContext.localObject(modified.getObjectId(), null);
            assertEquals(PersistenceState.HOLLOW, modifiedPeer.getPersistenceState());
            assertEquals(modified.getObjectId(), modifiedPeer.getObjectId());
            assertSame(peerContext, modifiedPeer.getObjectContext());
            assertSame(context, modified.getDataContext());
            Persistent deletedPeer = peerContext.localObject(deleted.getObjectId(), null);
            assertEquals(PersistenceState.HOLLOW, deletedPeer.getPersistenceState());
            assertEquals(deleted.getObjectId(), deletedPeer.getObjectId());
            assertSame(peerContext, deletedPeer.getObjectContext());
            assertSame(context, deleted.getDataContext());
        } finally {
            unblockQueries();
        }
    }

    public void testLocalObjectPeerContextNoOverride() throws Exception {
        deleteTestData();
        createTestData("testArtists");
        DataContext context = createDataContext();
        DataContext peerContext = createDataContext();
        int modifiedId = 33003;
        Artist modified = (Artist) DataObjectUtils.objectForQuery(context, new ObjectIdQuery(new ObjectId("Artist", Artist.ARTIST_ID_PK_COLUMN, modifiedId)));
        Artist peerModified = (Artist) DataObjectUtils.objectForQuery(peerContext, new ObjectIdQuery(new ObjectId("Artist", Artist.ARTIST_ID_PK_COLUMN, modifiedId)));
        modified.setArtistName("M1");
        peerModified.setArtistName("M2");
        assertEquals(PersistenceState.MODIFIED, modified.getPersistenceState());
        assertEquals(PersistenceState.MODIFIED, peerModified.getPersistenceState());
        blockQueries();
        try {
            Persistent peerModified2 = peerContext.localObject(modified.getObjectId(), null);
            assertSame(peerModified, peerModified2);
            assertEquals(PersistenceState.MODIFIED, peerModified2.getPersistenceState());
            assertEquals("M2", peerModified.getArtistName());
            assertEquals("M1", modified.getArtistName());
        } finally {
            unblockQueries();
        }
    }

    /**
     * @deprecated since 1.2 as localObjects is deprecated.
     */
    public void testLocalObjectsPeerContextDifferentEntities() throws Exception {
        deleteTestData();
        createTestData("testMix");
        DataContext context = createDataContext();
        DataContext peerContext = createDataContext();
        Artist artist = (Artist) DataObjectUtils.objectForQuery(context, new ObjectIdQuery(new ObjectId("Artist", Artist.ARTIST_ID_PK_COLUMN, 33003)));
        Painting painting = (Painting) DataObjectUtils.objectForQuery(context, new ObjectIdQuery(new ObjectId("Painting", Painting.PAINTING_ID_PK_COLUMN, 33003)));
        List objects = Arrays.asList(new Object[] { artist, painting });
        blockQueries();
        try {
            List locals = peerContext.localObjects(objects);
            assertEquals(2, locals.size());
            assertTrue(locals.get(0) instanceof Artist);
            assertTrue(locals.get(1) instanceof Painting);
        } finally {
            unblockQueries();
        }
    }

    /**
     * @deprecated since 1.2 as localObjects is deprecated.
     */
    public void testLocalObjectsPeerContextDifferentContexts() throws Exception {
        deleteTestData();
        createTestData("testMix");
        DataContext context1 = createDataContext();
        DataContext context2 = createDataContext();
        DataContext peerContext = createDataContext();
        Artist artist = (Artist) DataObjectUtils.objectForQuery(context1, new ObjectIdQuery(new ObjectId("Artist", Artist.ARTIST_ID_PK_COLUMN, 33003)));
        Painting painting = (Painting) DataObjectUtils.objectForQuery(context2, new ObjectIdQuery(new ObjectId("Painting", Painting.PAINTING_ID_PK_COLUMN, 33003)));
        List objects = Arrays.asList(new Object[] { artist, painting });
        blockQueries();
        try {
            List locals = peerContext.localObjects(objects);
            assertEquals(2, locals.size());
            Iterator it = locals.iterator();
            while (it.hasNext()) {
                DataObject o = (DataObject) it.next();
                assertSame(peerContext, o.getDataContext());
            }
        } finally {
            unblockQueries();
        }
    }
}
