package org.happy.collections.ver1x0.maps.decorators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;
import org.happy.collections.decorators.UnmodifiableCollection_1x0;
import org.happy.collections.decorators.UnmodifiableStrategy_1x0;
import org.happy.collections.maps.decorators.UnmodifiableMap_1x0;
import org.happy.collections.sets.decorators.UnmodifiableSet_1x0;
import org.junit.Test;

public class UnmodifiableMap_1x0Test {

    @Test
    public void testSetStrategy() {
        UnmodifiableMap_1x0<Integer, String> map = new UnmodifiableMap_1x0<Integer, String>(new HashMap<Integer, String>(), UnmodifiableStrategy_1x0.Modifiable);
        map.put(1, "first");
        map.put(2, "second");
        map.put(4, "four");
        assertEquals(3, map.size());
        map.setStrategy(UnmodifiableStrategy_1x0.RemoveAllowed);
        try {
            map.put(3, "third");
            fail("unsuported-operation");
        } catch (UnsupportedOperationException e) {
        }
        assertEquals(3, map.size());
        try {
            map.put(2, "second");
            fail("unsuported-operation");
        } catch (UnsupportedOperationException e) {
        }
        assertEquals(3, map.size());
        map.remove(2);
        assertEquals(2, map.size());
        map.remove(3);
        assertEquals(2, map.size());
        map.setStrategy(UnmodifiableStrategy_1x0.AddAllowed);
        try {
            map.remove(3);
            fail("unsuported-operation");
        } catch (UnsupportedOperationException e) {
        }
        assertEquals(2, map.size());
        try {
            map.remove(2);
            fail("unsuported-operation");
        } catch (UnsupportedOperationException e) {
        }
        assertEquals(2, map.size());
        try {
            map.clear();
            fail("unsuported-operation");
        } catch (UnsupportedOperationException e) {
        }
        assertEquals(2, map.size());
        map.put(3, "third");
        assertEquals(3, map.size());
        map.setStrategy(UnmodifiableStrategy_1x0.Unmodifiable);
        try {
            map.remove(3);
            fail("unsuported-operation");
        } catch (UnsupportedOperationException e) {
        }
        try {
            map.remove(2);
            fail("unsuported-operation");
        } catch (UnsupportedOperationException e) {
        }
        try {
            map.clear();
            fail("unsuported-operation");
        } catch (UnsupportedOperationException e) {
        }
        try {
            map.put(3, "third");
            fail("unsuported-operation");
        } catch (UnsupportedOperationException e) {
        }
        try {
            map.put(2, "second");
            fail("unsuported-operation");
        } catch (UnsupportedOperationException e) {
        }
        assertEquals(3, map.size());
    }

    @Test
    public void testClear() {
        UnmodifiableMap_1x0<Integer, String> map = new UnmodifiableMap_1x0<Integer, String>(new HashMap<Integer, String>(), UnmodifiableStrategy_1x0.Modifiable);
        map.put(1, "first");
        map.put(2, "second");
        map.put(3, "three");
        map.setStrategy(UnmodifiableStrategy_1x0.Unmodifiable);
        try {
            map.clear();
            fail("unsuported-operation");
        } catch (UnsupportedOperationException e) {
        }
        assertEquals(3, map.size());
        map.setStrategy(UnmodifiableStrategy_1x0.AddAllowed);
        try {
            map.clear();
            fail("unsuported-operation");
        } catch (UnsupportedOperationException e) {
        }
        assertEquals(3, map.size());
        map.setStrategy(UnmodifiableStrategy_1x0.RemoveAllowed);
        try {
            map.clear();
        } catch (UnsupportedOperationException e) {
            fail("suported-operation");
        }
        assertEquals(0, map.size());
        map.setStrategy(UnmodifiableStrategy_1x0.Modifiable);
        map.put(1, "first");
        map.put(2, "second");
        map.put(3, "three");
        try {
            map.clear();
        } catch (UnsupportedOperationException e) {
            fail("suported-operation");
        }
        assertEquals(0, map.size());
    }

    @Test
    public void testPut() {
        UnmodifiableMap_1x0<Integer, String> map = new UnmodifiableMap_1x0<Integer, String>(new HashMap<Integer, String>());
        try {
            map.put(1, "1");
            fail("unsuported-operation");
        } catch (UnsupportedOperationException e) {
        }
        assertEquals(0, map.size());
        map.setStrategy(UnmodifiableStrategy_1x0.RemoveAllowed);
        try {
            map.put(1, "1");
            fail("unsuported-operation");
        } catch (UnsupportedOperationException e) {
        }
        assertEquals(0, map.size());
        map.setStrategy(UnmodifiableStrategy_1x0.AddAllowed);
        try {
            map.put(1, "1");
        } catch (UnsupportedOperationException e) {
            fail("suported-operation");
        }
        assertEquals(1, map.size());
        map.setStrategy(UnmodifiableStrategy_1x0.Modifiable);
        try {
            map.put(2, "2");
        } catch (UnsupportedOperationException e) {
            fail("suported-operation");
        }
        assertEquals(2, map.size());
    }

    @Test
    public void testRemove() {
        UnmodifiableMap_1x0<Integer, String> map = new UnmodifiableMap_1x0<Integer, String>(new HashMap<Integer, String>(), UnmodifiableStrategy_1x0.AddAllowed);
        map.put(1, "first");
        map.put(2, "second");
        map.put(3, "three");
        try {
            map.remove(4);
            fail("unsuported-operation");
        } catch (UnsupportedOperationException e) {
        }
        assertEquals(3, map.size());
        try {
            map.remove(3);
            fail("unsuported-operation");
        } catch (UnsupportedOperationException e) {
        }
        assertEquals(3, map.size());
        map.setStrategy(UnmodifiableStrategy_1x0.RemoveAllowed);
        try {
            map.remove(5);
        } catch (UnsupportedOperationException e) {
            fail("suported-operation");
        }
        try {
            map.remove(3);
        } catch (UnsupportedOperationException e) {
            fail("suported-operation");
        }
        assertEquals(2, map.size());
        map.setStrategy(UnmodifiableStrategy_1x0.AddAllowed);
        try {
            map.remove(1);
            fail("unsuported-operation");
        } catch (UnsupportedOperationException e) {
        }
        assertEquals(2, map.size());
        map.setStrategy(UnmodifiableStrategy_1x0.Modifiable);
        try {
            map.remove(2);
        } catch (UnsupportedOperationException e) {
            fail("suported-operation");
        }
        assertEquals(1, map.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testKeySet() {
        UnmodifiableMap_1x0<Integer, String> map = new UnmodifiableMap_1x0<Integer, String>(new HashMap<Integer, String>(), UnmodifiableStrategy_1x0.AddAllowed);
        map.put(1, "first");
        map.put(2, "second");
        map.put(3, "three");
        Set<Integer> keySet = map.keySet();
        assertEquals(3, keySet.size());
        assertEquals(UnmodifiableSet_1x0.class, keySet.getClass());
        UnmodifiableSet_1x0 set = (UnmodifiableSet_1x0) keySet;
        assertEquals(map.getStrategy(), set.getStrategy());
        map.setStrategy(UnmodifiableStrategy_1x0.Modifiable);
        assertEquals(map.getStrategy(), set.getStrategy());
        map.setStrategy(UnmodifiableStrategy_1x0.Unmodifiable);
        assertEquals(map.getStrategy(), set.getStrategy());
        map.setStrategy(UnmodifiableStrategy_1x0.RemoveAllowed);
        assertEquals(map.getStrategy(), set.getStrategy());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testValues() {
        UnmodifiableMap_1x0<Integer, String> map = new UnmodifiableMap_1x0<Integer, String>(new HashMap<Integer, String>(), UnmodifiableStrategy_1x0.AddAllowed);
        map.put(1, "first");
        map.put(2, "second");
        map.put(3, "three");
        Collection<String> values = map.values();
        assertEquals(3, values.size());
        assertEquals(UnmodifiableSet_1x0.class, values.getClass());
        UnmodifiableCollection_1x0 c = (UnmodifiableCollection_1x0) values;
        assertEquals(map.getStrategy(), c.getStrategy());
        map.setStrategy(UnmodifiableStrategy_1x0.Modifiable);
        assertEquals(map.getStrategy(), c.getStrategy());
        map.setStrategy(UnmodifiableStrategy_1x0.Unmodifiable);
        assertEquals(map.getStrategy(), c.getStrategy());
        map.setStrategy(UnmodifiableStrategy_1x0.RemoveAllowed);
        assertEquals(map.getStrategy(), c.getStrategy());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEntrySet() {
        UnmodifiableMap_1x0<Integer, String> map = new UnmodifiableMap_1x0<Integer, String>(new HashMap<Integer, String>(), UnmodifiableStrategy_1x0.AddAllowed);
        map.put(1, "first");
        map.put(2, "second");
        map.put(3, "three");
        Set<Entry<Integer, String>> entrySet = map.entrySet();
        assertEquals(3, entrySet.size());
        assertEquals(UnmodifiableSet_1x0.class, entrySet.getClass());
        UnmodifiableSet_1x0 set = (UnmodifiableSet_1x0) entrySet;
        assertEquals(map.getStrategy(), set.getStrategy());
        map.setStrategy(UnmodifiableStrategy_1x0.Modifiable);
        assertEquals(map.getStrategy(), set.getStrategy());
        map.setStrategy(UnmodifiableStrategy_1x0.Unmodifiable);
        assertEquals(map.getStrategy(), set.getStrategy());
        map.setStrategy(UnmodifiableStrategy_1x0.RemoveAllowed);
        assertEquals(map.getStrategy(), set.getStrategy());
    }
}
