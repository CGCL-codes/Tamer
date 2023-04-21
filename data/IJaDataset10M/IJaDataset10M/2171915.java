package org.dhcpdj.test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import junit.framework.JUnit4TestAdapter;
import org.dhcp4java.Util;
import org.dhcpdj.struct.AddressRange;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class AddressRangeTest {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AddressRangeTest.class);
    }

    private static InetAddress adr1v6;

    private static InetAddress adr1;

    private static InetAddress adr2;

    private static InetAddress adr3;

    @BeforeClass
    public static void initAdr() throws UnknownHostException {
        adr1v6 = InetAddress.getByName("1080:0:0:0:8:800:200C:417A");
        adr1 = InetAddress.getByName("192.168.1.25");
        adr2 = InetAddress.getByName("192.168.1.45");
        adr3 = InetAddress.getByName("192.168.2.1");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNull() {
        new AddressRange(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorStartIpv6() {
        new AddressRange(adr1v6, adr1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEndIpv6() {
        new AddressRange(adr1, adr1v6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorReverseSide() {
        new AddressRange(adr2, adr1);
    }

    @Test
    public void testConstructor() {
        AddressRange ar = new AddressRange(adr1, adr2);
        assertEquals(adr1, ar.getRangeStart());
        assertEquals(adr2, ar.getRangeEnd());
        assertEquals(Util.inetAddress2Long(adr1), ar.getRangeStartLong());
        assertEquals(Util.inetAddress2Long(adr2), ar.getRangeEndLong());
    }

    @Test
    public void testIsInRange() throws UnknownHostException {
        AddressRange ar = new AddressRange(adr1, adr2);
        assertTrue(ar.isInRange(adr1));
        assertTrue(ar.isInRange(adr2));
        assertFalse(ar.isInRange(adr3));
        assertTrue(ar.isInRange(InetAddress.getByName("192.168.1.30")));
        assertFalse(ar.isInRange(InetAddress.getByName("0.0.0.0")));
        assertFalse(ar.isInRange(InetAddress.getByName("255.255.255.255")));
        assertFalse(ar.isInRange(InetAddress.getByName("10.0.0.1")));
    }

    @Test(expected = NullPointerException.class)
    public void testIsInRangeNull() {
        AddressRange ar = new AddressRange(adr1, adr2);
        ar.isInRange((InetAddress) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInRangeV6() {
        AddressRange ar = new AddressRange(adr1, adr2);
        ar.isInRange(adr1v6);
    }

    @Test
    public void testIsInRangeSubrange() throws UnknownHostException {
        AddressRange ar = new AddressRange(adr1, adr2);
        assertTrue(ar.isInRange(ar));
        assertTrue(ar.isInRange(new AddressRange(adr1, adr1)));
        assertTrue(ar.isInRange(new AddressRange(adr2, adr2)));
        assertTrue(ar.isInRange(new AddressRange(InetAddress.getByName("192.168.1.26"), InetAddress.getByName("192.168.1.26"))));
        assertFalse(ar.isInRange(new AddressRange(InetAddress.getByName("192.168.1.24"), InetAddress.getByName("192.168.1.24"))));
        assertFalse(ar.isInRange(new AddressRange(InetAddress.getByName("192.168.1.24"), InetAddress.getByName("192.168.1.26"))));
        assertTrue(ar.isInRange(new AddressRange(InetAddress.getByName("192.168.1.25"), InetAddress.getByName("192.168.1.26"))));
        assertTrue(ar.isInRange(new AddressRange(InetAddress.getByName("192.168.1.44"), InetAddress.getByName("192.168.1.44"))));
        assertFalse(ar.isInRange(new AddressRange(InetAddress.getByName("192.168.1.46"), InetAddress.getByName("192.168.1.46"))));
        assertFalse(ar.isInRange(new AddressRange(InetAddress.getByName("192.168.1.44"), InetAddress.getByName("192.168.1.46"))));
        assertTrue(ar.isInRange(new AddressRange(InetAddress.getByName("192.168.1.44"), InetAddress.getByName("192.168.1.45"))));
    }

    @Test(expected = NullPointerException.class)
    public void testIsInRangeSubrangeNull() {
        AddressRange ar = new AddressRange(adr1, adr2);
        ar.isInRange((AddressRange) null);
    }

    @Test
    public void testEqualsCompare() {
        AddressRange ar = new AddressRange(adr1, adr3);
        AddressRange ar2 = new AddressRange(adr1, adr3);
        AddressRange ar3 = new AddressRange(adr1, adr2);
        AddressRange ar4 = new AddressRange(adr2, adr3);
        assertTrue(ar.equals(ar));
        assertTrue(ar2.equals(ar));
        assertTrue(ar.equals(ar2));
        assertFalse(ar.equals(ar3));
        assertFalse(ar.equals(ar4));
        assertFalse(ar.equals(null));
        assertFalse(ar.equals(new Integer(1)));
        assertEquals(0, ar.compareTo(ar));
        assertEquals(0, ar.compareTo(ar2));
        assertEquals(0, ar2.compareTo(ar));
        assertEquals(1, ar.compareTo(ar3));
        assertEquals(-1, ar3.compareTo(ar));
        assertEquals(-1, ar.compareTo(ar4));
        assertEquals(1, ar4.compareTo(ar));
    }

    @Test(expected = NullPointerException.class)
    public void testCompareNull() {
        AddressRange ar = new AddressRange(adr1, adr3);
        ar.compareTo(null);
    }

    @Test
    public void testHashcode() {
        AddressRange ar = new AddressRange(adr1, adr3);
        AddressRange ar2 = new AddressRange(adr1, adr3);
        AddressRange ar3 = new AddressRange(adr1, adr2);
        assertTrue(ar.hashCode() != 0);
        assertEquals(ar.hashCode(), ar2.hashCode());
        assertTrue(ar.hashCode() != ar3.hashCode());
    }

    @Test
    public void testToString() {
        AddressRange ar = new AddressRange(adr1, adr3);
        assertEquals("192.168.1.25-192.168.2.1", ar.toString());
    }

    @Test
    public void testIsSorted() {
        AddressRange ar = new AddressRange(adr1, adr3);
        AddressRange ar3 = new AddressRange(adr1, adr2);
        AddressRange ar4 = new AddressRange(adr2, adr3);
        assertTrue(AddressRange.isSorted(null));
        List<AddressRange> list1 = new ArrayList<AddressRange>();
        list1.add(ar3);
        list1.add(ar);
        list1.add(ar4);
        assertTrue(AddressRange.isSorted(list1));
        list1.add(ar4);
        assertFalse(AddressRange.isSorted(list1));
    }

    @Test(expected = NullPointerException.class)
    public void testIsSortedNull() {
        List<AddressRange> list1 = new ArrayList<AddressRange>();
        list1.add(null);
        AddressRange.isSorted(list1);
    }

    @Test
    public void testCheckNoOverlap() {
        AddressRange.checkNoOverlap(null);
    }
}
