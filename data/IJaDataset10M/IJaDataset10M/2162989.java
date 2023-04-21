package org.datanucleus.tests;

import java.util.Collection;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.datanucleus.tests.JDOPersistenceTestCase;
import org.jpox.samples.enums.Colour;
import org.jpox.samples.enums.Palette;

/**
 * Tests for mapping JDK1.5 enums with JDO.
 *
 * @version $Revision: 1.3 $
 */
public class EnumMappingTest extends JDOPersistenceTestCase {

    /**
     * @param name
     */
    public EnumMappingTest(String name) {
        super(name);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        clean(Palette.class);
    }

    /**
     * Test persistence of an enum as a String.
     */
    public void testStringEnum() {
        Palette p;
        Object id = null;
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            p = new Palette();
            p.setAmount(100);
            p.setColour(Colour.RED);
            p.setColourOrdinal(Colour.GREEN);
            p.setColourSerialized(Colour.BLUE);
            pm.makePersistent(p);
            id = JDOHelper.getObjectId(p);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try {
            tx.begin();
            p = (Palette) pm.getObjectById(id, true);
            assertEquals(100, p.getAmount());
            assertEquals(Colour.RED, p.getColour());
            assertEquals(Colour.GREEN, p.getColourOrdinal());
            assertEquals(Colour.BLUE, p.getColourSerialized());
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try {
            tx.begin();
            p = new Palette();
            p.setAmount(101);
            p.setColour(null);
            p.setColourOrdinal(null);
            p.setColourSerialized(null);
            pm.makePersistent(p);
            id = JDOHelper.getObjectId(p);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try {
            tx.begin();
            p = (Palette) pm.getObjectById(id, true);
            assertEquals(101, p.getAmount());
            assertNull(p.getColour());
            assertNull(p.getColourOrdinal());
            assertNull(p.getColourSerialized());
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try {
            tx.begin();
            p = new Palette();
            p.setAmount(102);
            p.setColour(Colour.GREEN);
            p.setColourOrdinal(Colour.GREEN);
            p.setColourSerialized(Colour.GREEN);
            pm.makePersistent(p);
            id = JDOHelper.getObjectId(p);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try {
            tx.begin();
            p = (Palette) pm.getObjectById(id, true);
            assertEquals(102, p.getAmount());
            assertEquals(Colour.GREEN, p.getColour());
            assertEquals(Colour.GREEN, p.getColourOrdinal());
            assertEquals(Colour.GREEN, p.getColourSerialized());
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }

    /**
     * Test use of JDOQL matches with enums stored as Strings.
     */
    public void testQueryStringEnumMatches() {
        Palette p[];
        Object id[];
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        p = new Palette[5];
        id = new Object[5];
        try {
            tx.begin();
            p[0] = new Palette();
            p[0].setAmount(100);
            p[0].setColour(Colour.RED);
            p[0].setColourOrdinal(Colour.RED);
            p[1] = new Palette();
            p[1].setAmount(101);
            p[1].setColour(null);
            p[2] = new Palette();
            p[2].setAmount(102);
            p[2].setColour(Colour.GREEN);
            p[2].setColourOrdinal(Colour.GREEN);
            p[3] = new Palette();
            p[3].setAmount(103);
            p[3].setColour(Colour.BLUE);
            p[3].setColourOrdinal(Colour.BLUE);
            p[4] = new Palette();
            p[4].setAmount(104);
            p[4].setColour(Colour.RED);
            p[4].setColourOrdinal(Colour.RED);
            pm.makePersistentAll(p);
            id[0] = JDOHelper.getObjectId(p[0]);
            id[1] = JDOHelper.getObjectId(p[1]);
            id[2] = JDOHelper.getObjectId(p[2]);
            id[3] = JDOHelper.getObjectId(p[3]);
            id[4] = JDOHelper.getObjectId(p[4]);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try {
            tx.begin();
            Collection c = (Collection) pm.newQuery(Palette.class, "colour.matches('BLUE')").execute();
            assertEquals(1, c.size());
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }

    /**
     * Test use of JDOQL and enums for all types.
     */
    public void testQueryStringEnumAll() {
        Palette p[];
        Object id[];
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        p = new Palette[5];
        id = new Object[5];
        try {
            tx.begin();
            p[0] = new Palette();
            p[0].setAmount(100);
            p[0].setColour(Colour.RED);
            p[0].setColourOrdinal(Colour.RED);
            p[1] = new Palette();
            p[1].setAmount(101);
            p[1].setColour(null);
            p[2] = new Palette();
            p[2].setAmount(102);
            p[2].setColour(Colour.GREEN);
            p[2].setColourOrdinal(Colour.GREEN);
            p[3] = new Palette();
            p[3].setAmount(103);
            p[3].setColour(Colour.BLUE);
            p[3].setColourOrdinal(Colour.BLUE);
            p[4] = new Palette();
            p[4].setAmount(104);
            p[4].setColour(Colour.RED);
            p[4].setColourOrdinal(Colour.RED);
            pm.makePersistentAll(p);
            id[0] = JDOHelper.getObjectId(p[0]);
            id[1] = JDOHelper.getObjectId(p[1]);
            id[2] = JDOHelper.getObjectId(p[2]);
            id[3] = JDOHelper.getObjectId(p[3]);
            id[4] = JDOHelper.getObjectId(p[4]);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try {
            tx.begin();
            Collection c = (Collection) pm.newQuery(Palette.class, "colourOrdinal == 2 && colour == 'BLUE'").execute();
            assertEquals(1, c.size());
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }

    /**
     * Test use of JDOQL and enums stored as ints.
     */
    public void testQueryStringEnumOrdinal() {
        Palette p[];
        Object id[];
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        p = new Palette[5];
        id = new Object[5];
        try {
            tx.begin();
            p[0] = new Palette();
            p[0].setAmount(100);
            p[0].setColour(Colour.RED);
            p[0].setColourOrdinal(Colour.RED);
            p[1] = new Palette();
            p[1].setAmount(101);
            p[1].setColour(null);
            p[2] = new Palette();
            p[2].setAmount(102);
            p[2].setColour(Colour.GREEN);
            p[2].setColourOrdinal(Colour.GREEN);
            p[3] = new Palette();
            p[3].setAmount(103);
            p[3].setColour(Colour.BLUE);
            p[3].setColourOrdinal(Colour.BLUE);
            p[4] = new Palette();
            p[4].setAmount(104);
            p[4].setColour(Colour.RED);
            p[4].setColourOrdinal(Colour.RED);
            pm.makePersistentAll(p);
            id[0] = JDOHelper.getObjectId(p[0]);
            id[1] = JDOHelper.getObjectId(p[1]);
            id[2] = JDOHelper.getObjectId(p[2]);
            id[3] = JDOHelper.getObjectId(p[3]);
            id[4] = JDOHelper.getObjectId(p[4]);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try {
            tx.begin();
            Collection c = (Collection) pm.newQuery(Palette.class, "colourOrdinal == 0").execute();
            assertEquals(2, c.size());
            tx.commit();
        } catch (Exception e) {
            LOG.error("Exception thrown execting Enum ordinal query", e);
            fail("Exception thrown executing Enum ordinal query : " + e.getMessage());
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try {
            tx.begin();
            Collection c = (Collection) pm.newQuery(Palette.class, "colourOrdinal == :param").execute(Colour.RED);
            assertEquals(2, c.size());
            tx.commit();
        } catch (Exception e) {
            LOG.error("Exception thrown execting Enum query", e);
            fail("Exception thrown executing Enum query : " + e.getMessage());
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }

    /**
     * Test use of JDOQL with enums stored as Strings.
     */
    public void testQueryStringEnum() {
        Palette p[];
        Object id[];
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        p = new Palette[5];
        id = new Object[5];
        try {
            tx.begin();
            p[0] = new Palette();
            p[0].setAmount(100);
            p[0].setColour(Colour.RED);
            p[0].setColourOrdinal(Colour.RED);
            p[1] = new Palette();
            p[1].setAmount(101);
            p[1].setColour(null);
            p[2] = new Palette();
            p[2].setAmount(102);
            p[2].setColour(Colour.GREEN);
            p[2].setColourOrdinal(Colour.GREEN);
            p[3] = new Palette();
            p[3].setAmount(103);
            p[3].setColour(Colour.BLUE);
            p[3].setColourOrdinal(Colour.BLUE);
            p[4] = new Palette();
            p[4].setAmount(104);
            p[4].setColour(Colour.RED);
            p[4].setColourOrdinal(Colour.RED);
            pm.makePersistentAll(p);
            id[0] = JDOHelper.getObjectId(p[0]);
            id[1] = JDOHelper.getObjectId(p[1]);
            id[2] = JDOHelper.getObjectId(p[2]);
            id[3] = JDOHelper.getObjectId(p[3]);
            id[4] = JDOHelper.getObjectId(p[4]);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try {
            tx.begin();
            Collection c = (Collection) pm.newQuery(Palette.class, "colour == 'RED'").execute();
            assertEquals(2, c.size());
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }
}
