package org.jcvi.common.core.util;

import java.util.Map;
import org.jcvi.common.core.util.Caches;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestLRUCache {

    String first = "first";

    String second = "second";

    String third = "third";

    String fourth = "fourth";

    protected Map<String, String> sut;

    protected Map<String, String> createLRUCache(int size) {
        return Caches.createLRUCache(size);
    }

    @Before
    public void setup() {
        sut = createLRUCache(3);
        sut.put(first, first);
        sut.put(second, second);
        sut.put(third, third);
    }

    @Test
    public void initalState() {
        assertEquals(sut.size(), 3);
        assertTrue(sut.containsKey(first));
        assertTrue(sut.containsKey(second));
        assertTrue(sut.containsKey(third));
    }

    @Test
    public void insertOverflowShouldRemoveEldest() {
        sut.put(fourth, fourth);
        assertEquals(sut.size(), 3);
        assertTrue(sut.containsKey(second));
        assertTrue(sut.containsKey(third));
        assertTrue(sut.containsKey(fourth));
        assertEquals(second, sut.get(second));
        assertEquals(third, sut.get(third));
        assertEquals(fourth, sut.get(fourth));
    }

    @Test
    public void remove() {
        sut.remove(third);
        sut.put(fourth, fourth);
        assertTrue(sut.containsKey(first));
        assertTrue(sut.containsKey(second));
        assertTrue(sut.containsKey(fourth));
        assertEquals(first, sut.get(first));
        assertEquals(second, sut.get(second));
        assertEquals(fourth, sut.get(fourth));
    }
}
