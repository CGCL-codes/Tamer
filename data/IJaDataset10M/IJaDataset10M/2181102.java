package com.google.code.jahath;

import org.junit.Assert;
import org.junit.Test;

public class IPv4AddressTest {

    private static final byte[] ADDRESS_1 = { 10, 25, 0, 1 };

    private static final byte[] ADDRESS_2 = { 10, 24, 1, 0 };

    @Test
    public void testEqualsPositive() {
        Assert.assertTrue(new IPv4Address(ADDRESS_1).equals(new IPv4Address(ADDRESS_1)));
    }

    @Test
    public void testEqualsNegative() {
        Assert.assertFalse(new IPv4Address(ADDRESS_1).equals(new IPv4Address(ADDRESS_2)));
    }

    @Test
    public void testEqualsNull() {
        Assert.assertFalse(new IPv4Address(ADDRESS_1).equals(null));
    }

    @Test
    public void testHashCode1() {
        Assert.assertTrue(new IPv4Address(ADDRESS_1).hashCode() == new IPv4Address(ADDRESS_1).hashCode());
    }

    @Test
    public void testHashCode2() {
        Assert.assertTrue(new IPv4Address(ADDRESS_1).hashCode() != new IPv4Address(ADDRESS_2).hashCode());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("10.25.0.1", new IPv4Address(ADDRESS_1).toString());
    }
}
