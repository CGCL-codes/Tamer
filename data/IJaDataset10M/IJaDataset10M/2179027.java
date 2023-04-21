package org.objectstyle.cayenne;

import java.util.List;
import org.objectstyle.art.ArtGroup;
import org.objectstyle.art.Artist;
import org.objectstyle.cayenne.access.DataContext;
import org.objectstyle.cayenne.exp.ExpressionFactory;
import org.objectstyle.cayenne.query.SelectQuery;
import org.objectstyle.cayenne.unit.TestCaseDataFactory;
import org.objectstyle.cayenne.util.Util;

public class CayenneDataObjectFlattenedRelTst extends CayenneDOTestBase {

    public void testReadFlattenedRelationship() throws Exception {
        TestCaseDataFactory.createArtistBelongingToGroups(artistName, new String[] {});
        Artist a1 = fetchArtist();
        List groupList = a1.getGroupArray();
        assertNotNull(groupList);
        assertEquals(0, groupList.size());
    }

    public void testReadFlattenedRelationship2() throws Exception {
        TestCaseDataFactory.createArtistBelongingToGroups(artistName, new String[] { groupName });
        Artist a1 = fetchArtist();
        List groupList = a1.getGroupArray();
        assertNotNull(groupList);
        assertEquals(1, groupList.size());
        assertEquals(PersistenceState.COMMITTED, ((ArtGroup) groupList.get(0)).getPersistenceState());
        assertEquals(groupName, ((ArtGroup) groupList.get(0)).getName());
    }

    public void testAddToFlattenedRelationship() throws Exception {
        TestCaseDataFactory.createArtist(artistName);
        TestCaseDataFactory.createUnconnectedGroup(groupName);
        Artist a1 = fetchArtist();
        assertEquals(0, a1.getGroupArray().size());
        SelectQuery q = new SelectQuery(ArtGroup.class, ExpressionFactory.matchExp("name", groupName));
        List results = ctxt.performQuery(q);
        assertEquals(1, results.size());
        assertFalse(ctxt.hasChanges());
        ArtGroup group = (ArtGroup) results.get(0);
        a1.addToGroupArray(group);
        assertTrue(ctxt.hasChanges());
        List groupList = a1.getGroupArray();
        assertEquals(1, groupList.size());
        assertEquals(groupName, ((ArtGroup) groupList.get(0)).getName());
        a1.getDataContext().commitChanges();
        assertFalse(ctxt.hasChanges());
        ctxt = createDataContext();
        a1 = fetchArtist();
        groupList = a1.getGroupArray();
        assertEquals(1, groupList.size());
        assertEquals(groupName, ((ArtGroup) groupList.get(0)).getName());
    }

    public void testDoubleCommitAddToFlattenedRelationship() throws Exception {
        TestCaseDataFactory.createArtistBelongingToGroups(artistName, new String[] {});
        TestCaseDataFactory.createUnconnectedGroup(groupName);
        Artist a1 = fetchArtist();
        SelectQuery q = new SelectQuery(ArtGroup.class, ExpressionFactory.matchExp("name", groupName));
        List results = ctxt.performQuery(q);
        assertEquals(1, results.size());
        ArtGroup group = (ArtGroup) results.get(0);
        a1.addToGroupArray(group);
        List groupList = a1.getGroupArray();
        assertEquals(1, groupList.size());
        assertEquals(groupName, ((ArtGroup) groupList.get(0)).getName());
        a1.getDataContext().commitChanges();
        try {
            a1.getDataContext().commitChanges();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Should not have thrown an exception");
        }
    }

    public void testRemoveFromFlattenedRelationship() throws Exception {
        TestCaseDataFactory.createArtistBelongingToGroups(artistName, new String[] { groupName });
        Artist a1 = fetchArtist();
        ArtGroup group = (ArtGroup) a1.getGroupArray().get(0);
        a1.removeFromGroupArray(group);
        List groupList = a1.getGroupArray();
        assertEquals(0, groupList.size());
        a1.getDataContext().commitChanges();
        groupList = a1.getGroupArray();
        assertEquals(0, groupList.size());
    }

    public void testRemoveFlattenedRelationshipAndRootRecord() throws Exception {
        TestCaseDataFactory.createArtistBelongingToGroups(artistName, new String[] { groupName });
        Artist a1 = fetchArtist();
        DataContext dc = a1.getDataContext();
        ArtGroup group = (ArtGroup) a1.getGroupArray().get(0);
        a1.removeFromGroupArray(group);
        dc.deleteObject(a1);
        try {
            dc.commitChanges();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Should not have thrown the exception :" + e.getMessage());
        }
    }

    public void testAddRemoveAddFlattenedRelationship() throws Exception {
        String specialGroupName = "Special Group2";
        TestCaseDataFactory.createArtistBelongingToGroups(artistName, new String[] {});
        TestCaseDataFactory.createUnconnectedGroup(specialGroupName);
        Artist a1 = fetchArtist();
        SelectQuery q = new SelectQuery(ArtGroup.class, ExpressionFactory.matchExp("name", specialGroupName));
        List results = ctxt.performQuery(q);
        assertEquals(1, results.size());
        ArtGroup group = (ArtGroup) results.get(0);
        a1.addToGroupArray(group);
        group.removeFromArtistArray(a1);
        try {
            ctxt.commitChanges();
        } catch (Exception e) {
            Util.unwindException(e).printStackTrace();
            fail("Should not have thrown the exception " + e.getMessage());
        }
        ctxt = createDataContext();
        results = ctxt.performQuery(q);
        assertEquals(1, results.size());
        group = (ArtGroup) results.get(0);
        assertEquals(0, group.getArtistArray().size());
    }
}
