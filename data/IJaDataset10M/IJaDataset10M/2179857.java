package org.objectstyle.cayenne.map;

import java.util.List;
import org.objectstyle.cayenne.CayenneRuntimeException;
import org.objectstyle.cayenne.exp.ExpressionException;
import org.objectstyle.cayenne.unit.CayenneTestCase;
import org.objectstyle.cayenne.util.Util;

public class ObjRelationshipTst extends CayenneTestCase {

    protected DbEntity artistDBEntity = getDbEntity("ARTIST");

    protected DbEntity artistExhibitDBEntity = getDbEntity("ARTIST_EXHIBIT");

    protected DbEntity exhibitDBEntity = getDbEntity("EXHIBIT");

    protected DbEntity paintingDbEntity = getDbEntity("PAINTING");

    protected DbEntity galleryDBEntity = getDbEntity("GALLERY");

    public void testSerializability() throws Exception {
        ObjRelationship r1 = new ObjRelationship("r1");
        r1.setDbRelationshipPath("aaaa");
        ObjRelationship r2 = (ObjRelationship) Util.cloneViaSerialization(r1);
        assertEquals(r1.getName(), r2.getName());
        assertEquals(r1.getDbRelationshipPath(), r2.getDbRelationshipPath());
    }

    public void testGetClientRelationship() {
        final ObjEntity target = new ObjEntity("te1");
        ObjRelationship r1 = new ObjRelationship("r1") {

            public Entity getTargetEntity() {
                return target;
            }
        };
        r1.setDeleteRule(DeleteRule.DENY);
        r1.setTargetEntityName("te1");
        ObjRelationship r2 = r1.getClientRelationship();
        assertNotNull(r2);
        assertEquals(r1.getName(), r2.getName());
        assertEquals(r1.getTargetEntityName(), r2.getTargetEntityName());
        assertEquals(r1.getDeleteRule(), r2.getDeleteRule());
    }

    public void testGetReverseDbRelationshipPath() {
        ObjEntity artistObjEnt = getObjEntity("Artist");
        ObjEntity paintingObjEnt = getObjEntity("Painting");
        ObjRelationship r1 = (ObjRelationship) artistObjEnt.getRelationship("paintingArray");
        assertEquals("toArtist", r1.getReverseDbRelationshipPath());
        ObjRelationship r2 = (ObjRelationship) paintingObjEnt.getRelationship("toArtist");
        assertEquals("paintingArray", r2.getReverseDbRelationshipPath());
    }

    public void testSetDbRelationshipPath() {
        ObjRelationship relationship = new ObjRelationship();
        relationship.dbRelationshipsRefreshNeeded = false;
        relationship.setDbRelationshipPath("dummy.path");
        assertTrue(relationship.dbRelationshipsRefreshNeeded);
        assertEquals("dummy.path", relationship.getDbRelationshipPath());
        assertTrue(relationship.dbRelationshipsRefreshNeeded);
    }

    public void testRefreshFromPath() {
        ObjRelationship relationship = new ObjRelationship();
        relationship.setDbRelationshipPath("dummy.path");
        try {
            relationship.refreshFromPath(false);
            fail("refresh without source entity should have failed.");
        } catch (CayenneRuntimeException ex) {
        }
        DataMap map = new DataMap();
        ObjEntity entity = new ObjEntity("Test");
        map.addObjEntity(entity);
        relationship.setSourceEntity(entity);
        try {
            relationship.refreshFromPath(false);
            fail("refresh over a dummy path should have failed.");
        } catch (ExpressionException ex) {
        }
        DbEntity dbEntity1 = new DbEntity("TEST1");
        DbEntity dbEntity2 = new DbEntity("TEST2");
        DbEntity dbEntity3 = new DbEntity("TEST3");
        map.addDbEntity(dbEntity1);
        map.addDbEntity(dbEntity2);
        map.addDbEntity(dbEntity3);
        entity.setDbEntityName("TEST1");
        DbRelationship dummyR = new DbRelationship("dummy");
        dummyR.setTargetEntityName("TEST2");
        dummyR.setSourceEntity(dbEntity1);
        DbRelationship pathR = new DbRelationship("path");
        pathR.setTargetEntityName("TEST3");
        pathR.setSourceEntity(dbEntity2);
        dbEntity1.addRelationship(dummyR);
        dbEntity2.addRelationship(pathR);
        relationship.refreshFromPath(false);
        assertFalse(relationship.dbRelationshipsRefreshNeeded);
        List resolvedPath = relationship.getDbRelationships();
        assertEquals(2, resolvedPath.size());
        assertSame(dummyR, resolvedPath.get(0));
        assertSame(pathR, resolvedPath.get(1));
    }

    public void testCalculateToMany() {
        DataMap map = new DataMap();
        ObjEntity entity = new ObjEntity("Test");
        map.addObjEntity(entity);
        DbEntity dbEntity1 = new DbEntity("TEST1");
        DbEntity dbEntity2 = new DbEntity("TEST2");
        DbEntity dbEntity3 = new DbEntity("TEST3");
        map.addDbEntity(dbEntity1);
        map.addDbEntity(dbEntity2);
        map.addDbEntity(dbEntity3);
        entity.setDbEntityName("TEST1");
        DbRelationship dummyR = new DbRelationship("dummy");
        dummyR.setTargetEntityName("TEST2");
        dummyR.setSourceEntity(dbEntity1);
        DbRelationship pathR = new DbRelationship("path");
        pathR.setTargetEntityName("TEST3");
        pathR.setSourceEntity(dbEntity2);
        dbEntity1.addRelationship(dummyR);
        dbEntity2.addRelationship(pathR);
        ObjRelationship relationship = new ObjRelationship();
        relationship.setSourceEntity(entity);
        relationship.dbRelationshipsRefreshNeeded = false;
        relationship.dbRelationships.add(dummyR);
        assertFalse(relationship.isToMany());
        dummyR.setToMany(true);
        relationship.calculateToManyValue();
        assertTrue(relationship.isToMany());
        dummyR.setToMany(false);
        relationship.calculateToManyValue();
        assertFalse(relationship.isToMany());
        relationship.dbRelationships.add(pathR);
        assertFalse(relationship.isToMany());
        pathR.setToMany(true);
        relationship.calculateToManyValue();
        assertTrue(relationship.isToMany());
    }

    public void testCalculateToManyFromPath() {
        DataMap map = new DataMap();
        ObjEntity entity = new ObjEntity("Test");
        map.addObjEntity(entity);
        DbEntity dbEntity1 = new DbEntity("TEST1");
        DbEntity dbEntity2 = new DbEntity("TEST2");
        DbEntity dbEntity3 = new DbEntity("TEST3");
        map.addDbEntity(dbEntity1);
        map.addDbEntity(dbEntity2);
        map.addDbEntity(dbEntity3);
        entity.setDbEntityName("TEST1");
        DbRelationship dummyR = new DbRelationship("dummy");
        dummyR.setTargetEntityName("TEST2");
        dummyR.setSourceEntity(dbEntity1);
        DbRelationship pathR = new DbRelationship("path");
        pathR.setTargetEntityName("TEST3");
        pathR.setSourceEntity(dbEntity2);
        dbEntity1.addRelationship(dummyR);
        dbEntity2.addRelationship(pathR);
        ObjRelationship relationship = new ObjRelationship();
        relationship.setSourceEntity(entity);
        relationship.setDbRelationshipPath("dummy");
        assertFalse(relationship.isToMany());
        dummyR.setToMany(true);
        relationship.setDbRelationshipPath(null);
        relationship.setDbRelationshipPath("dummy");
        assertTrue(relationship.isToMany());
        dummyR.setToMany(false);
        relationship.setDbRelationshipPath(null);
        relationship.setDbRelationshipPath("dummy");
        assertFalse(relationship.isToMany());
        relationship.setDbRelationshipPath(null);
        relationship.setDbRelationshipPath("dummy.path");
        assertFalse(relationship.isToMany());
        pathR.setToMany(true);
        relationship.setDbRelationshipPath(null);
        relationship.setDbRelationshipPath("dummy.path");
        assertTrue(relationship.isToMany());
    }

    public void testTargetEntity() throws Exception {
        ObjRelationship relationship = new ObjRelationship("some_rel");
        relationship.setTargetEntityName("targ");
        try {
            relationship.getTargetEntity();
            fail("Without a container, getTargetEntity() must fail.");
        } catch (CayenneRuntimeException ex) {
        }
        DataMap map = new DataMap();
        ObjEntity src = new ObjEntity("src");
        map.addObjEntity(src);
        src.addRelationship(relationship);
        assertNull(relationship.getTargetEntity());
        ObjEntity target = new ObjEntity("targ");
        map.addObjEntity(target);
        assertSame(target, relationship.getTargetEntity());
    }

    public void testGetReverseRel1() {
        ObjEntity artistObjEnt = getObjEntity("Artist");
        ObjEntity paintingObjEnt = getObjEntity("Painting");
        ObjRelationship r1 = (ObjRelationship) artistObjEnt.getRelationship("paintingArray");
        ObjRelationship r2 = r1.getReverseRelationship();
        assertNotNull(r2);
        assertSame(paintingObjEnt.getRelationship("toArtist"), r2);
    }

    public void testGetReverseRel2() {
        ObjEntity artistEnt = getObjEntity("Artist");
        ObjEntity paintingEnt = getObjEntity("Painting");
        ObjRelationship r1 = (ObjRelationship) paintingEnt.getRelationship("toArtist");
        ObjRelationship r2 = r1.getReverseRelationship();
        assertNotNull(r2);
        assertSame(artistEnt.getRelationship("paintingArray"), r2);
    }

    public void testSingleDbRelationship() {
        ObjRelationship relationship = new ObjRelationship();
        DbRelationship r1 = new DbRelationship();
        relationship.addDbRelationship(r1);
        assertEquals(1, relationship.getDbRelationships().size());
        assertEquals(r1, relationship.getDbRelationships().get(0));
        assertFalse(relationship.isFlattened());
        assertFalse(relationship.isReadOnly());
        assertEquals(r1.isToMany(), relationship.isToMany());
        relationship.removeDbRelationship(r1);
        assertEquals(0, relationship.getDbRelationships().size());
    }

    public void testFlattenedRelationship() {
        DbRelationship r1 = new DbRelationship();
        DbRelationship r2 = new DbRelationship();
        r1.setSourceEntity(artistDBEntity);
        r1.setTargetEntity(artistExhibitDBEntity);
        r1.setToMany(true);
        r2.setSourceEntity(artistExhibitDBEntity);
        r2.setTargetEntity(exhibitDBEntity);
        r2.setToMany(false);
        ObjRelationship relationship = new ObjRelationship();
        relationship.addDbRelationship(r1);
        relationship.addDbRelationship(r2);
        assertTrue(relationship.isToMany());
        assertEquals(2, relationship.getDbRelationships().size());
        assertEquals(r1, relationship.getDbRelationships().get(0));
        assertEquals(r2, relationship.getDbRelationships().get(1));
        assertTrue(relationship.isFlattened());
        relationship.removeDbRelationship(r1);
        assertFalse(relationship.isToMany());
        assertEquals(1, relationship.getDbRelationships().size());
        assertEquals(r2, relationship.getDbRelationships().get(0));
        assertFalse(relationship.isFlattened());
        assertFalse(relationship.isReadOnly());
    }

    public void testReadOnlyMoreThan3DbRelsRelationship() {
        DbRelationship r1 = new DbRelationship();
        DbRelationship r2 = new DbRelationship();
        DbRelationship r3 = new DbRelationship();
        r1.setSourceEntity(artistDBEntity);
        r1.setTargetEntity(artistExhibitDBEntity);
        r1.setToMany(true);
        r2.setSourceEntity(artistExhibitDBEntity);
        r2.setTargetEntity(exhibitDBEntity);
        r2.setToMany(false);
        r3.setSourceEntity(exhibitDBEntity);
        r3.setTargetEntity(galleryDBEntity);
        r3.setToMany(false);
        ObjRelationship relationship = new ObjRelationship();
        relationship.addDbRelationship(r1);
        relationship.addDbRelationship(r2);
        relationship.addDbRelationship(r3);
        assertTrue(relationship.isFlattened());
        assertTrue(relationship.isReadOnly());
        assertTrue(relationship.isToMany());
    }

    public void testIncorrectSequenceReadOnlyRelationship() {
        DbRelationship r1 = new DbRelationship();
        DbRelationship r2 = new DbRelationship();
        r1.setSourceEntity(artistDBEntity);
        r1.setTargetEntity(paintingDbEntity);
        r1.setToMany(true);
        r2.setSourceEntity(paintingDbEntity);
        r2.setTargetEntity(galleryDBEntity);
        r2.setToMany(false);
        ObjRelationship relationship = new ObjRelationship();
        relationship.addDbRelationship(r1);
        relationship.addDbRelationship(r2);
        assertTrue(relationship.isFlattened());
        assertTrue(relationship.isReadOnly());
        assertTrue(relationship.isToMany());
    }

    public void testKnownFlattenedRelationship() {
        ObjEntity artistEnt = getObjEntity("Artist");
        ObjRelationship theRel = (ObjRelationship) artistEnt.getRelationship("groupArray");
        assertNotNull(theRel);
        assertTrue(theRel.isFlattened());
        assertFalse(theRel.isReadOnly());
    }

    public void testBadDeleteRuleValue() {
        ObjRelationship relationship = new ObjRelationship();
        try {
            relationship.setDeleteRule(999);
            fail("Should have failed with IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testOkDeleteRuleValue() {
        ObjRelationship relationship = new ObjRelationship();
        try {
            relationship.setDeleteRule(DeleteRule.CASCADE);
            relationship.setDeleteRule(DeleteRule.DENY);
            relationship.setDeleteRule(DeleteRule.NULLIFY);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("Should not have thrown an exception :" + e.getMessage());
        }
    }

    public void testWatchesDbRelChanges() {
        ObjRelationship relationship = new ObjRelationship();
        DbRelationship r1 = new DbRelationship();
        r1.setToMany(true);
        relationship.addDbRelationship(r1);
        assertTrue(relationship.isToMany());
        r1.setToMany(false);
        assertFalse(relationship.isToMany());
    }
}
