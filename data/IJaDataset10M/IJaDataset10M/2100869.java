package com.electionpredictor.seats;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RegionTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCompareTo() {
        final Region region = new Region("test");
        final Region region1 = new Region("test");
        final Region region2 = new Region("stuff");
        Assert.assertTrue(region.compareTo(region) == 0);
        Assert.assertTrue(region.compareTo(region1) == 0);
        Assert.assertTrue(region1.compareTo(region) == 0);
        Assert.assertTrue(region.compareTo(region2) != 0);
        Assert.assertTrue(region2.compareTo(region) != 0);
        Assert.assertTrue(region1.compareTo(region2) != 0);
        Assert.assertTrue(region2.compareTo(region1) != 0);
    }

    @Test
    public void testEqualsObject() {
        final Region region = new Region("test");
        final Region region1 = new Region("test");
        final Region region2 = new Region("stuff");
        Assert.assertTrue(region.equals(region));
        Assert.assertTrue(region.equals(region1));
        Assert.assertTrue(region1.equals(region));
        Assert.assertTrue(!region.equals(region2));
        Assert.assertTrue(!region2.equals(region));
        Assert.assertTrue(!region1.equals(region2));
        Assert.assertTrue(!region2.equals(region1));
        try {
            region.equals("");
            Assert.fail();
        } catch (final IllegalArgumentException e) {
        }
        try {
            region.equals(null);
            Assert.fail();
        } catch (final IllegalArgumentException e) {
        }
    }

    @Test
    public void testHashCode() {
        final Region region = new Region("test");
        final Region region1 = new Region("test");
        final Region region2 = new Region("stuff");
        Assert.assertTrue(region.hashCode() == region1.hashCode());
        Assert.assertTrue(region1.hashCode() == region.hashCode());
        Assert.assertTrue(region.hashCode() != region2.hashCode());
        Assert.assertTrue(region2.hashCode() != region.hashCode());
    }

    @Test
    public void testRegion() {
        try {
            new Region(null);
            Assert.fail("Null is invalid");
        } catch (final Exception e) {
        }
        try {
            new Region("");
            Assert.fail("Empty is invalid");
        } catch (final Exception e) {
        }
        new Region("test");
    }

    @Test
    public void testToString() {
        final Region region = new Region("test");
        final Region region1 = new Region("test");
        final Region region2 = new Region("stuff");
        Assert.assertEquals("test", region.toString());
        Assert.assertEquals("test", region1.toString());
        Assert.assertEquals("stuff", region2.toString());
    }
}
