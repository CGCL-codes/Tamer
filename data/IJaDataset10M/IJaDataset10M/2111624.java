package net.sf.kdgcommons.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import junit.framework.TestCase;
import net.sf.kdgcommons.collections.HashMultimap.Behavior;

public class TestHashMultimap extends TestCase {

    public void testInitialCapacityComputation() throws Exception {
        HashMultimap<String, String> map1 = new HashMultimap<String, String>();
        assertEquals(8, map1.getTableSize());
        HashMultimap<String, String> map2 = new HashMultimap<String, String>(Behavior.SET, 4, .75);
        assertEquals(8, map2.getTableSize());
        HashMultimap<String, String> map3 = new HashMultimap<String, String>(Behavior.SET, 18, .75);
        assertEquals(32, map3.getTableSize());
    }

    public void testPutAndGetSingleValue() throws Exception {
        final String key1 = "foo";
        final String val1 = "argle";
        final String key2 = "bar";
        final String val2 = "bargle";
        final String key3 = "baz";
        HashMultimap<String, String> map = new HashMultimap<String, String>();
        assertEquals(0, map.size());
        assertEquals(8, map.getTableSize());
        map.put(key1, val1);
        assertEquals(1, map.size());
        assertEquals(val1, map.get(key1));
        map.put(key2, val2);
        assertEquals(2, map.size());
        assertEquals(val1, map.get(key1));
        assertEquals(val2, map.get(key2));
        assertNull(map.get(key3));
    }

    public void testPutAndGetMultipleValuesSetBehavior() throws Exception {
        final String key1 = "foo";
        final String val1a = "argle";
        final String val1b = "wargle";
        HashMultimap<String, String> map = new HashMultimap<String, String>(Behavior.SET);
        assertEquals(0, map.size());
        map.put(key1, val1a);
        assertEquals(1, map.size());
        assertEquals(CollectionUtil.asSet(val1a), map.getAll(key1));
        map.put(key1, val1b);
        assertEquals(2, map.size());
        assertEquals(CollectionUtil.asSet(val1a, val1b), map.getAll(key1));
        map.put(key1, val1a);
        assertEquals(2, map.size());
        assertEquals(CollectionUtil.asSet(val1a, val1b), map.getAll(key1));
    }

    public void testPutAndGetMultipleValuesListBehavior() throws Exception {
        final String key1 = "foo";
        final String val1a = "argle";
        final String val1b = "wargle";
        HashMultimap<String, String> map = new HashMultimap<String, String>(Behavior.LIST);
        assertEquals(0, map.size());
        map.put(key1, val1a);
        assertEquals(1, map.size());
        assertEquals(Arrays.asList(val1a), map.getAll(key1));
        assertEquals(val1a, map.get(key1));
        map.put(key1, val1b);
        assertEquals(2, map.size());
        assertEquals(Arrays.asList(val1a, val1b), map.getAll(key1));
        assertEquals(val1a, map.get(key1));
        map.put(key1, val1a);
        assertEquals(3, map.size());
        assertEquals(Arrays.asList(val1a, val1b, val1a), map.getAll(key1));
        assertEquals(val1a, map.get(key1));
    }

    public void testPutAndGetWhereKeysHaveSameHashcode() throws Exception {
        final String key1 = "AAABB";
        final String val1a = "argle";
        final String val1b = "wargle";
        final String key2 = "AAAAa";
        final String val2a = "argle";
        final String val2b = "wargle";
        HashMultimap<String, String> map = new HashMultimap<String, String>(Behavior.LIST);
        map.put(key1, val1a);
        map.put(key2, val2a);
        map.put(key1, val1b);
        map.put(key2, val2b);
        assertEquals(4, map.size());
        assertEquals(Arrays.asList(val1a, val1b), map.getAll(key1));
        assertEquals(Arrays.asList(val2a, val2b), map.getAll(key2));
    }

    public void testRemoveByKey() throws Exception {
        HashMultimap<String, String> map = new HashMultimap<String, String>(Behavior.LIST);
        assertEquals(0, map.size());
        map.put("foo", "bar");
        map.put("foo", "baz");
        map.put("foo", "bar");
        assertEquals(3, map.size());
        assertEquals("bar", map.remove("foo"));
        assertEquals(2, map.size());
        assertEquals(Arrays.asList("baz", "bar"), map.getAll("foo"));
        assertEquals("baz", map.remove("foo"));
        assertEquals(1, map.size());
        assertEquals(Arrays.asList("bar"), map.getAll("foo"));
        assertEquals("bar", map.remove("foo"));
        assertEquals(0, map.size());
        assertEquals(Arrays.asList(), map.getAll("foo"));
        assertEquals(null, map.remove("foo"));
        assertEquals(0, map.size());
        assertEquals(Arrays.asList(), map.getAll("foo"));
    }

    public void testRemoveByKeyAndValue() throws Exception {
        HashMultimap<String, String> map = new HashMultimap<String, String>(Behavior.LIST);
        assertEquals(0, map.size());
        map.put("foo", "bar");
        map.put("foo", "baz");
        map.put("foo", "bar");
        assertEquals(3, map.size());
        assertTrue(map.remove("foo", "baz"));
        assertEquals(2, map.size());
        assertEquals(Arrays.asList("bar", "bar"), map.getAll("foo"));
        assertFalse(map.remove("foo", "baz"));
        assertTrue(map.remove("foo", "bar"));
        assertEquals(1, map.size());
        assertEquals(Arrays.asList("bar"), map.getAll("foo"));
        assertTrue(map.remove("foo", "bar"));
        assertEquals(0, map.size());
        assertEquals(Arrays.asList(), map.getAll("foo"));
        assertFalse(map.remove("foo", "bar"));
        assertEquals(0, map.size());
        assertEquals(Arrays.asList(), map.getAll("foo"));
    }

    public void testRemoveAll() throws Exception {
        HashMultimap<String, String> map = new HashMultimap<String, String>(Behavior.LIST);
        assertEquals(0, map.size());
        map.put("foo", "bar");
        map.put("foo", "baz");
        map.put("argle", "bargle");
        assertEquals(3, map.size());
        assertEquals(Arrays.asList("bar", "baz"), map.getAll("foo"));
        assertEquals(Arrays.asList("bar", "baz"), map.removeAll("foo"));
        assertEquals(1, map.size());
        assertEquals(Arrays.asList(), map.getAll("foo"));
        assertEquals(Arrays.asList(), map.removeAll("foo"));
        assertEquals(Arrays.asList("bargle"), map.getAll("argle"));
        assertEquals(Arrays.asList("bargle"), map.removeAll("argle"));
        assertEquals(0, map.size());
        assertEquals(Arrays.asList(), map.getAll("argle"));
        assertEquals(Arrays.asList(), map.removeAll("argle"));
    }

    public void testGetIterator() throws Exception {
        final String key1 = "foo";
        final String val1a = "argle";
        final String val1b = "wargle";
        HashMultimap<String, String> map = new HashMultimap<String, String>(Behavior.LIST);
        map.put(key1, val1a);
        map.put(key1, val1b);
        assertEquals(2, map.size());
        assertEquals(Arrays.asList(val1a, val1b), map.getAll(key1));
        Iterator<String> itx1 = map.getIterator(key1);
        assertEquals(val1a, itx1.next());
        assertEquals(val1b, itx1.next());
        assertFalse(itx1.hasNext());
        Iterator<String> itx2 = map.getIterable(key1).iterator();
        assertEquals(val1a, itx2.next());
        assertEquals(val1b, itx2.next());
        assertFalse(itx2.hasNext());
    }

    public void testIteratorRemove() throws Exception {
        final String key1 = "foo";
        final String val1a = "argle";
        final String val1b = "wargle";
        HashMultimap<String, String> map = new HashMultimap<String, String>(Behavior.LIST);
        map.put(key1, val1a);
        map.put(key1, val1b);
        assertEquals(2, map.size());
        Iterator<String> itx = map.getIterator(key1);
        assertEquals(val1a, itx.next());
        itx.remove();
        assertEquals(1, map.size());
        assertEquals(val1b, itx.next());
        itx.remove();
        assertEquals(0, map.size());
        assertFalse(itx.hasNext());
        itx = map.getIterator(key1);
        assertFalse(itx.hasNext());
    }

    public void testIteratorThrowsWhenNextCalledAtEnd() throws Exception {
        HashMultimap<String, String> map = new HashMultimap<String, String>();
        Iterator<String> itx = map.getIterator("foo");
        assertFalse(itx.hasNext());
        try {
            itx.next();
            fail("next() succeeded when no next element");
        } catch (NoSuchElementException ex) {
        }
    }

    public void testIteratorIsFailFast() throws Exception {
        HashMultimap<String, String> map = new HashMultimap<String, String>(Behavior.LIST);
        map.put("foo", "bar");
        map.put("foo", "baz");
        Iterator<String> itx = map.getIterator("foo");
        assertEquals("bar", itx.next());
        map.put("argle", "bargle");
        try {
            assertEquals("baz", itx.next());
            fail("iterator continued to work after modification");
        } catch (ConcurrentModificationException ex) {
        }
    }

    public void testNullValues() throws Exception {
        final String key1 = "foo";
        final String val1a = "argle";
        final String val1b = "wargle";
        HashMultimap<String, String> map = new HashMultimap<String, String>(HashMultimap.Behavior.SET);
        assertEquals(0, map.size());
        map.put(key1, val1a);
        assertEquals(1, map.size());
        assertEquals(CollectionUtil.asSet(val1a), map.getAll(key1));
        map.put(key1, null);
        assertEquals(2, map.size());
        assertEquals(CollectionUtil.asSet(val1a, null), map.getAll(key1));
        map.put(key1, val1b);
        assertEquals(3, map.size());
        assertEquals(CollectionUtil.asSet(val1a, null, val1b), map.getAll(key1));
    }

    public void testClearAndIsEmpty() throws Exception {
        HashMultimap<String, String> map = new HashMultimap<String, String>();
        map.put("foo", "bar");
        map.put("foo", "baz");
        map.put("argle", "bargle");
        assertEquals(3, map.size());
        assertFalse(map.isEmpty());
        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertNull(map.get("foo"));
    }

    public void testContains() throws Exception {
        HashMultimap<String, String> map = new HashMultimap<String, String>();
        map.put("foo", "bar");
        map.put("foo", "baz");
        map.put("argle", "bargle");
        assertTrue(map.containsKey("argle"));
        assertFalse(map.containsKey("bargle"));
        assertTrue(map.containsMapping("argle", "bargle"));
        assertFalse(map.containsMapping("argle", "biff"));
        assertTrue(map.containsMapping("foo", "baz"));
    }

    public void testKeySet() throws Exception {
        HashMultimap<String, String> map = new HashMultimap<String, String>();
        map.put("foo", "bar");
        map.put("foo", "baz");
        map.put("argle", "bargle");
        Set<String> keys = map.keySet();
        assertEquals(2, keys.size());
        assertTrue(keys.contains("foo"));
        assertTrue(keys.contains("argle"));
        map.put("bargle", "wargle");
        assertEquals(2, keys.size());
        assertFalse(keys.contains("bargle"));
        keys.remove("foo");
        assertEquals(4, map.size());
        assertTrue(map.containsKey("foo"));
    }

    public void testIterateEntries() throws Exception {
        HashMultimap<String, String> map = new HashMultimap<String, String>();
        map.put("foo", "bar");
        map.put("foo", "baz");
        map.put("argle", "bargle");
        Collection<Map.Entry<String, String>> entries = map.entries();
        assertEquals(3, entries.size());
    }

    public void testResize() throws Exception {
        HashMultimap<Integer, String> map = new HashMultimap<Integer, String>(Behavior.LIST, 8, .5);
        assertEquals(8, map.getTableSize());
        map.put(1, "A1");
        map.put(1, "A2");
        map.put(1, "A3");
        map.put(1, "A4");
        map.put(1, "A5");
        assertEquals(8, map.getTableSize());
        map.put(2, "B");
        map.put(3, "C");
        map.put(4, "D");
        assertEquals(8, map.getTableSize());
        map.put(5, "E");
        assertEquals(16, map.getTableSize());
        assertEquals(Arrays.asList("A1", "A2", "A3", "A4", "A5"), map.getAll(1));
        assertEquals("B", map.get(2));
        assertEquals("C", map.get(3));
        assertEquals("D", map.get(4));
        assertEquals("E", map.get(5));
    }
}
