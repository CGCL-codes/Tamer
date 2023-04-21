package org.itracker.model;

import static org.itracker.Assert.assertEntityComparator;
import static org.itracker.Assert.assertEntityComparatorEquals;
import java.util.Date;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class AbstractEntityTest extends TestCase {

    private Entity ae;

    @Test
    public void testGetCreateDate() {
        Date date = new Date(1000);
        ae.setCreateDate(date);
        assertEquals("create date", date, ae.getCreateDate());
        assertNotSame("create date", ae.getCreateDate(), ae.getCreateDate());
        ae.setCreateDate(null);
        assertNotNull("create date", ae.getCreateDate());
    }

    @Test
    public void testSetCreateDate() {
        Date date = new Date(1000);
        ae.setCreateDate(date);
        assertEquals("create date", date, ae.getCreateDate());
        assertNotSame("lastModifiedDate", date, ae.getCreateDate());
        ae.setCreateDate(null);
        assertNotNull("create date", ae.getCreateDate());
    }

    @Test
    public void testGetLastModifiedDate() {
        Date date = new Date(1000);
        ae.setLastModifiedDate(date);
        assertEquals("lastModifiedDate", date, ae.getLastModifiedDate());
        assertNotSame("lastModifiedDate", ae.getLastModifiedDate(), ae.getLastModifiedDate());
        ae.setLastModifiedDate(null);
        assertNotNull("lastModifiedDate", ae.getLastModifiedDate());
    }

    @Test
    public void testSetLastModifiedDate() {
        Date date = new Date(1000);
        ae.setLastModifiedDate(date);
        assertEquals("lastModifiedDate", date, ae.getLastModifiedDate());
        assertNotSame("lastModifiedDate", date, ae.getLastModifiedDate());
        ae.setLastModifiedDate(null);
        assertNotNull("lastModifiedDate", ae.getLastModifiedDate());
    }

    @Test
    public void testEquals() {
        Entity aeCopy = ae;
        assertTrue(ae.equals(aeCopy));
        assertFalse(ae.equals(null));
        aeCopy = new TestAbstractEntity();
        assertFalse(ae.equals(aeCopy));
        ae.setId(1000);
        assertFalse(ae.equals(aeCopy));
        aeCopy.setId(1000);
        assertTrue(ae.equals(aeCopy));
        assertFalse(ae.equals(new User()));
    }

    @Test
    public void testCompareTo() {
        ae.setId(1000);
        AbstractEntity aeCopy = new TestAbstractEntity();
        aeCopy.setId(1000);
        assertEquals(0, ae.compareTo(aeCopy));
        aeCopy.setId(2000);
        assertEquals(-1, ae.compareTo(aeCopy));
        try {
            ae.compareTo(null);
            fail("did not throw NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testIsNew() {
        ae.setId(1000);
        assertFalse(ae.isNew());
        ae.setId(null);
        assertTrue(ae.isNew());
    }

    @Test
    public void testHashCode() {
        assertNotNull(ae.hashCode());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Object clone = ((AbstractEntity) ae).clone();
        assertTrue(clone instanceof AbstractEntity);
    }

    @Test
    public void testIdComparator() {
        AbstractEntity entityA = new TestAbstractEntity();
        AbstractEntity entityB = new TestAbstractEntity();
        entityA.setId(1);
        entityB.setId(2);
        assertEntityComparator("id comparator", AbstractEntity.ID_COMPARATOR, entityA, entityB);
        assertEntityComparator("id comparator", AbstractEntity.ID_COMPARATOR, entityA, null);
        entityA.setId(2);
        assertEntityComparatorEquals("id comparator", AbstractEntity.ID_COMPARATOR, entityA, entityB);
        assertEntityComparatorEquals("id comparator", AbstractEntity.ID_COMPARATOR, entityA, entityA);
        assertEntityComparator("id comparator", AbstractEntity.ID_COMPARATOR, entityA, null);
    }

    @Ignore
    @Test
    public void testCreateDateComparator() throws Exception {
        AbstractEntity entityA = new TestAbstractEntity();
        Thread.sleep(1);
        AbstractEntity entityB = new TestAbstractEntity();
        assertEntityComparator("create date comparator", AbstractEntity.CREATE_DATE_COMPARATOR, entityA, entityB);
        assertEntityComparator("create date comparator", AbstractEntity.CREATE_DATE_COMPARATOR, entityA, null);
        entityA.setCreateDate(entityB.getCreateDate());
        assertEquals(entityA.getCreateDate(), entityB.getCreateDate());
        assertEntityComparatorEquals("create date comparator", AbstractEntity.CREATE_DATE_COMPARATOR, entityA, entityB);
        assertEntityComparatorEquals("create date comparator", AbstractEntity.CREATE_DATE_COMPARATOR, entityA, entityA);
        assertEntityComparator("create date comparator", AbstractEntity.CREATE_DATE_COMPARATOR, entityA, null);
    }

    @Ignore
    @Test
    public void testLastModifiedDateComparator() throws Exception {
        AbstractEntity entityA = new TestAbstractEntity();
        Thread.sleep(1);
        AbstractEntity entityB = new TestAbstractEntity();
        assertEntityComparator("last modified date comparator", AbstractEntity.LAST_MODIFIED_DATE_COMPARATOR, entityA, entityB);
        assertEntityComparator("last modified date comparator", AbstractEntity.LAST_MODIFIED_DATE_COMPARATOR, entityA, null);
        entityA.setLastModifiedDate(entityB.getLastModifiedDate());
        assertEquals(entityB.getLastModifiedDate(), entityB.getLastModifiedDate());
        assertEntityComparatorEquals("last modified date comparator", AbstractEntity.LAST_MODIFIED_DATE_COMPARATOR, entityA, entityB);
        assertEntityComparatorEquals("last modified date comparator", AbstractEntity.LAST_MODIFIED_DATE_COMPARATOR, entityA, entityA);
        assertEntityComparator("last modified date comparator", AbstractEntity.LAST_MODIFIED_DATE_COMPARATOR, entityA, null);
    }

    @Before
    protected void setUp() throws Exception {
        ae = new TestAbstractEntity();
    }

    @After
    protected void tearDown() throws Exception {
        ae = null;
    }

    /**
	 * Simple Entity for Test
	 *
	 */
    private class TestAbstractEntity extends AbstractEntity {

        private static final long serialVersionUID = 1L;
    }
}
