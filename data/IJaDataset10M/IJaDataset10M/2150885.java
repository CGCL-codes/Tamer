package com.tcmj.pm.conflicts.data;

import com.tcmj.pm.conflicts.bars.SimpleBar;
import com.tcmj.pm.conflicts.bars.Bar;
import com.tcmj.pm.conflicts.data.SortItem.SortItemType;
import java.util.Date;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Testcase for SortItem.
 * @author tcmj - Thomas Deutsch
 */
public class SortItemTest {

    /** Start date of the time period (bar). */
    private Date start = new Date();

    /** Dummy Bar item. */
    private Bar baritem = new SimpleBar("mybar", start, new Date(), 1.32D);

    public SortItemTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getType method, of class SortItem.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        SortItem instance = new SortItem(SortItemType.STARTITEM, 1L, baritem);
        SortItemType expResult = SortItemType.STARTITEM;
        SortItemType result = instance.getType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getBaritem method, of class SortItem.
     */
    @Test
    public void testGetBaritem() {
        System.out.println("getBaritem");
        SortItem instance = new SortItem(SortItemType.STARTITEM, 1L, baritem);
        Bar expResult = baritem;
        Bar result = instance.getBaritem();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDate method, of class SortItem.
     */
    @Test
    public void testGetDate() {
        System.out.println("getDate");
        SortItem instance = new SortItem(SortItemType.STARTITEM, 1L, baritem);
        Date expResult = start;
        Date result = instance.getDate();
        assertEquals(expResult, result);
    }

    /**
     * Test of getWeightAsLong method, of class SortItem.
     */
    @Test
    public void testGetWeightAsLong() {
        System.out.println("getWeightAsLong");
        SortItem instance = new SortItem(SortItemType.STARTITEM, 1320L, baritem);
        long expResult = 1320L;
        long result = instance.getWeightAsLong();
        assertEquals(expResult, result);
    }

    /**
     * Test of setWeight method, of class SortItem.
     */
    @Test
    public void testSetWeight() {
        System.out.println("setWeight");
        SortItem instance = new SortItem(SortItemType.STARTITEM, 500L, baritem);
        assertEquals(500L, instance.getWeightAsLong());
    }

    /**
     * Test of toString method, of class SortItem.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        SortItem instance = new SortItem(SortItemType.STARTITEM, 1, baritem);
        String result = instance.toString();
        assertNotNull(result);
    }

    /**
     * Test of compareTo method, of class SortItem.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        SortItem other = new SortItem(SortItemType.STARTITEM, 0, baritem);
        SortItem instance = new SortItem(SortItemType.STARTITEM, 0, baritem);
        int expResult = 0;
        int result = instance.compareTo(other);
        assertEquals(expResult, result);
        assertTrue("(x.compareTo(y)==0) == (x.equals(y))", (instance.compareTo(other) == 0) == (instance.equals(other)));
        assertEquals(instance.hashCode(), other.hashCode());
    }
}
