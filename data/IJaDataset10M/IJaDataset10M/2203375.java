package com.electionpredictor.seats;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AreaTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testArea() {
        try {
            new Area(null);
            Assert.fail("Null not valid");
        } catch (final IllegalArgumentException e) {
        }
        try {
            new Area("");
            Assert.fail("empty not valid");
        } catch (final IllegalArgumentException e) {
        }
        new Area("test");
    }

    @Test
    public void testCompareTo() {
        final Area test = new Area("test");
        final Area test1 = new Area("test");
        final Area test2 = new Area("stuff");
        Assert.assertTrue(test.compareTo(test) == 0);
        Assert.assertTrue(test.compareTo(test1) == 0);
        Assert.assertTrue(test1.compareTo(test) == 0);
        Assert.assertTrue(test2.compareTo(test1) != 0);
        Assert.assertTrue(test2.compareTo(test) != 0);
        Assert.assertTrue(test1.compareTo(test2) != 0);
        Assert.assertTrue(test.compareTo(test2) != 0);
    }

    @Test
    public void testEqualsObject() {
        final Area test = new Area("test");
        final Area test1 = new Area("test");
        final Area test2 = new Area("stuff");
        Assert.assertTrue(test.equals(test));
        Assert.assertTrue(test.equals(test1));
        Assert.assertTrue(test1.equals(test));
        Assert.assertTrue(!test2.equals(test1));
        Assert.assertTrue(!test2.equals(test));
        Assert.assertTrue(!test1.equals(test2));
        Assert.assertTrue(!test.equals(test2));
        try {
            test.equals("");
            Assert.fail();
        } catch (final IllegalArgumentException e) {
        }
        try {
            test.equals(null);
            Assert.fail();
        } catch (final IllegalArgumentException e) {
        }
    }

    @Test
    public void testHashCode() {
        final Area test = new Area("test");
        final Area test1 = new Area("test");
        final Area test2 = new Area("stuff");
        Assert.assertEquals(test.hashCode(), test.hashCode());
        Assert.assertEquals(test1.hashCode(), test.hashCode());
        Assert.assertEquals(test.hashCode(), test1.hashCode());
        Assert.assertTrue(!(test.hashCode() == test2.hashCode()));
        Assert.assertTrue(!(test2.hashCode() == test.hashCode()));
    }

    @Test
    public void testToString() {
        final Area test = new Area("test");
        final Area test1 = new Area("test");
        final Area test2 = new Area("stuff");
        Assert.assertEquals("test", test.toString());
        Assert.assertEquals("test", test1.toString());
        Assert.assertEquals("stuff", test2.toString());
    }
}
