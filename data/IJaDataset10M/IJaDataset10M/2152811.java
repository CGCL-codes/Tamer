package de.mguennewig.pobjects.tests;

import de.mguennewig.pobjects.*;

/** Tests in-memory rollback.
 *
 * @author Michael G�nnewig
 */
public class TestRollback extends BaseTestCase {

    private Container db;

    public TestRollback(String name) {
        super(name);
        db = null;
    }

    /** {@inheritDoc} */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        db = openContainer(true);
        db.beginTransaction();
        db.deleteAll(new Room().getClassDecl());
        db.deleteAll(new Location().getClassDecl());
        db.commitTransaction();
    }

    /** Tests {@link PObject#revert}. */
    public void testPObjectRevert() {
        Location l = new Location("Dortmund");
        Room r = new Room(null, "1.42");
        r.setLocation(l);
        r.revert();
        assertNull("Room.location not rolled back", r.getLocation());
        assertEquals("Room.name rolled back", "1.42", r.getName());
        assertEquals("Dortmund", l.getName());
    }

    public void testRollbackAfterCommitNoChanges() {
        try {
            Location l;
            Room r;
            db.beginTransaction();
            {
                l = new Location();
                l.setName("Dortmund");
                db.makePersistent(l);
                l.store();
                r = new Room();
                r.setLocation(l);
                r.setName("1.42");
                db.makePersistent(r);
                r.store();
            }
            db.commitTransaction();
            db.beginTransaction();
            {
                l.store();
                r.store();
            }
            db.rollbackTransaction();
            assertEquals("Dortmund", l.getName());
            assertNotNull("Room.location became null", r.getLocation());
            assertSame("Room.location changed to other object", l, r.getLocation());
            assertEquals("1.42", r.getName());
        } catch (PObjException e) {
            fail(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void testRollbackAfterCommitWithChanges() {
        try {
            Location l;
            Room r;
            db.beginTransaction();
            {
                l = new Location();
                l.setName("Dortmund");
                db.makePersistent(l);
                l.store();
                r = new Room();
                r.setLocation(l);
                r.setName("1.42");
                db.makePersistent(r);
                r.store();
            }
            db.commitTransaction();
            db.beginTransaction();
            {
                r.setName("0.10");
                r.setLocation(null);
                r.store();
                l.store();
            }
            db.rollbackTransaction();
            assertEquals("Dortmund", l.getName());
            assertEquals("1.42", r.getName());
            assertNotNull("Room.location not rolled back", r.getLocation());
            assertSame("Room.location changed to other object", l, r.getLocation());
        } catch (PObjException e) {
            fail(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void testRollbackDueToException() {
        try {
            Location l1;
            Location l2;
            db.beginTransaction();
            {
                l1 = new Location();
                l1.setName("Dortmund");
                db.makePersistent(l1);
                l1.store();
                l2 = new Location();
                l2.setName("Frankfurt");
                db.makePersistent(l2);
                l2.store();
            }
            db.commitTransaction();
            try {
                db.beginTransaction();
                {
                    l2.setName("Dortmund");
                    l2.store();
                }
                db.commitTransaction();
                fail("unique constraint violated but accepted");
            } catch (PObjConstraintException e) {
                assertNotNull("Wrong constraint fire", e.errorMsgForColumn(Location.attrName));
            } finally {
                db.rollbackTransaction();
            }
            assertEquals("Dortmund", l1.getName());
            assertEquals("Frankfurt", l2.getName());
        } catch (PObjException e) {
            fail(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
