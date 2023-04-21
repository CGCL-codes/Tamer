package org.actorsguildframework.immutable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import junit.framework.Assert;
import org.actorsguildframework.ActorRuntimeException;
import org.junit.Before;
import org.junit.Test;

public class ImmutableMapTest {

    HashMap<Integer, Integer> m1, m2;

    HashMap<Date, Date> m3, m4;

    @Before
    public void setUp() {
        m1 = new HashMap<Integer, Integer>();
        m1.put(2, 3);
        m1.put(1, null);
        m1.put(null, 2);
        m1.put(33, 1);
        m2 = new HashMap<Integer, Integer>();
        m2.put(15, 2);
        m2.put(1, 2);
        m3 = new HashMap<Date, Date>();
        m3.put(new Date(2), new Date(2));
        m3.put(new Date(1), new Date(3));
        m3.put(null, new Date(3));
        m3.put(new Date(101), null);
        m4 = new HashMap<Date, Date>();
        m4.put(new Date(3), new Date(223));
        m4.put(new Date(551), new Date(4));
    }

    @Test
    public void testEquals() {
        ImmutableMap<Integer, Integer> l1 = new ImmutableMap<Integer, Integer>(m1);
        ImmutableMap<Integer, Integer> l2 = new ImmutableMap<Integer, Integer>(m1);
        ImmutableMap<Integer, Integer> l3 = new ImmutableMap<Integer, Integer>(m2);
        ImmutableMap<Integer, Integer> l4 = ImmutableMap.emptyMap();
        Assert.assertTrue(l1.equals(l2));
        Assert.assertTrue(l2.equals(l1));
        Assert.assertTrue(l1.equals(l1));
        Assert.assertTrue(l4.equals(l4));
        Assert.assertFalse(l1.equals(l3));
        Assert.assertFalse(l1.equals(l4));
        Assert.assertFalse(l3.equals(l1));
        Assert.assertFalse(l4.equals(l1));
        Assert.assertFalse(l3.equals(l4));
        Assert.assertFalse(l3.equals(l4));
        Assert.assertFalse(l1.equals(null));
        Assert.assertFalse(l4.equals(null));
    }

    @Test
    public void testHashCode() {
        ImmutableMap<Integer, Integer> l1 = new ImmutableMap<Integer, Integer>(m1);
        ImmutableMap<Integer, Integer> l2 = new ImmutableMap<Integer, Integer>(m2);
        Assert.assertTrue(l1.hashCode() != l2.hashCode());
    }

    @Test
    public void testConstructor() {
        ImmutableMap<Integer, Integer> l1 = new ImmutableMap<Integer, Integer>(m1);
        Assert.assertEquals(m1, l1.toMap());
        ImmutableMap<Integer, Integer> l2 = new ImmutableMap<Integer, Integer>(new HashMap<Integer, Integer>());
        Assert.assertEquals(0, l2.size());
        Assert.assertTrue(l2.isEmpty());
        ImmutableMap<Integer, Integer> l3 = new ImmutableMap<Integer, Integer>(m2);
        Assert.assertEquals(m2, l3.toMap());
    }

    @Test
    public void testConstructorMutable() {
        ImmutableMap<Date, Date> l1 = new ImmutableMap<Date, Date>(m3);
        Assert.assertEquals(m3, l1.toMap());
        ImmutableMap<Date, Date> l2 = new ImmutableMap<Date, Date>(m4);
        Assert.assertEquals(m4, l2.toMap());
    }

    @Test
    public void testContainsKey() {
        ImmutableMap<Integer, Integer> l1 = new ImmutableMap<Integer, Integer>(m1);
        Assert.assertFalse(l1.containsKey(54));
        Assert.assertTrue(l1.containsKey(null));
        Assert.assertTrue(l1.containsKey(1));
        Assert.assertTrue(l1.containsKey(2));
        Assert.assertTrue(l1.containsKey(33));
        ImmutableMap<Integer, Integer> l2 = new ImmutableMap<Integer, Integer>(m2);
        Assert.assertFalse(l2.containsKey(null));
        ImmutableMap<Integer, Integer> l3 = ImmutableMap.emptyMap();
        Assert.assertFalse(l3.containsKey(0));
        Assert.assertFalse(l3.containsKey(null));
    }

    @Test
    public void testContainsValue() {
        ImmutableMap<Integer, Integer> l1 = new ImmutableMap<Integer, Integer>(m1);
        Assert.assertFalse(l1.containsKey(54));
        Assert.assertTrue(l1.containsKey(null));
        Assert.assertTrue(l1.containsKey(1));
        Assert.assertTrue(l1.containsKey(2));
        Assert.assertTrue(l1.containsKey(33));
        ImmutableMap<Integer, Integer> l2 = new ImmutableMap<Integer, Integer>(m2);
        Assert.assertFalse(l2.containsKey(null));
        ImmutableMap<Integer, Integer> l3 = ImmutableMap.emptyMap();
        Assert.assertFalse(l3.containsKey(0));
        Assert.assertFalse(l3.containsKey(null));
    }

    @Test
    public void testIsEmpty() {
        ImmutableMap<Integer, Integer> l1 = new ImmutableMap<Integer, Integer>(m1);
        Assert.assertFalse(l1.isEmpty());
        ImmutableMap<Integer, Integer> l2 = ImmutableMap.emptyMap();
        Assert.assertTrue(l2.isEmpty());
    }

    @Test
    public void testSize() {
        ImmutableMap<Integer, Integer> l1 = new ImmutableMap<Integer, Integer>(m1);
        Assert.assertEquals(m1.size(), l1.size());
        ImmutableMap<Integer, Integer> l2 = ImmutableMap.emptyMap();
        Assert.assertEquals(0, l2.size());
    }

    @Test
    public void testGet() {
        ImmutableMap<Integer, Integer> l1 = new ImmutableMap<Integer, Integer>(m1);
        Assert.assertNull(l1.get(548587));
        Assert.assertNull(l1.get(1));
        Assert.assertEquals(new Integer(3), l1.get(2));
        Assert.assertEquals(new Integer(2), l1.get(null));
        Assert.assertEquals(new Integer(1), l1.get(33));
        ImmutableMap<Integer, Integer> l2 = ImmutableMap.emptyMap();
        Assert.assertNull(l2.get(548587));
        Assert.assertNull(l2.get(null));
    }

    @Test
    public void testGetMutable() {
        ImmutableMap<Date, Date> l1 = new ImmutableMap<Date, Date>(m3);
        Assert.assertNull(l1.get(new Date(548587)));
        Assert.assertNull(l1.get(new Date(101)));
        Assert.assertEquals(new Date(2), l1.get(new Date(2)));
        Assert.assertEquals(new Date(3), l1.get(null));
    }

    @Test
    public void testKeySet() {
        ImmutableMap<Integer, Integer> l1 = ImmutableMap.emptyMap();
        ImmutableMap<Integer, Integer> l2 = new ImmutableMap<Integer, Integer>(m1);
        Assert.assertEquals(0, l1.keySet().size());
        Assert.assertEquals(m1.size(), l2.keySet().size());
        Assert.assertEquals(m1.keySet(), l2.keySet().toSet());
    }

    @Test
    public void testKeySetMutable() {
        ImmutableMap<Date, Date> l2 = new ImmutableMap<Date, Date>(m3);
        Assert.assertEquals(m3.size(), l2.keySet().size());
        Assert.assertEquals(m3.keySet(), l2.keySet().toSet());
    }

    @Test
    public void testToMap() {
        ImmutableMap<Integer, Integer> l1 = ImmutableMap.emptyMap();
        ImmutableMap<Integer, Integer> l2 = new ImmutableMap<Integer, Integer>(m1);
        Assert.assertEquals(0, l1.toMap().size());
        Assert.assertEquals(m1.size(), l2.toMap().size());
        Assert.assertEquals(m1, l2.toMap());
    }

    @Test
    public void testToMapMutable() {
        ImmutableMap<Date, Date> l2 = new ImmutableMap<Date, Date>(m3);
        Assert.assertEquals(m3.size(), l2.toMap().size());
        Assert.assertEquals(m3, l2.toMap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCtorNullArg() {
        new ImmutableMap(null);
    }

    @Test(expected = ActorRuntimeException.class)
    public void testToMapBroken() {
        HashMap<ImmutableListTest.BrokenSerializer, ImmutableListTest.BrokenSerializer> m = new HashMap<ImmutableListTest.BrokenSerializer, ImmutableListTest.BrokenSerializer>();
        m.put(new ImmutableListTest.BrokenSerializer(), new ImmutableListTest.BrokenSerializer());
        ImmutableMap<ImmutableListTest.BrokenSerializer, ImmutableListTest.BrokenSerializer> l = new ImmutableMap<ImmutableListTest.BrokenSerializer, ImmutableListTest.BrokenSerializer>(m);
        l.toMap();
    }
}
