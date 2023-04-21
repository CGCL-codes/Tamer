package com.ar4j.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.ar4j.util.ObjectCache;

/**
 * Tests for the ObjectCache EHCache wrapper
 */
public class ObjectCacheTest {

    @Test
    public void testBasicFunctionality() {
        String referenceKey = "testy";
        Long referenceValue = 123L;
        ObjectCache<String, Long> cache = new ObjectCache<String, Long>("testCache", 3, 60);
        assertEquals("testCache", cache.getName());
        assertNotNull(cache.getUnderlyingCache());
        cache.put(referenceKey, referenceValue);
        assertTrue(cache.contains(referenceKey));
        assertEquals(referenceValue, cache.get(referenceKey));
        cache.put(referenceKey, referenceValue + 1);
        assertFalse(referenceValue.equals(cache.get(referenceKey)));
        assertTrue(referenceValue.equals(cache.get(referenceKey) - 1));
    }

    @Test
    public void testLru() {
        String referenceKey = "testy";
        Long referenceValue = 123L;
        ObjectCache<String, Long> cache = new ObjectCache<String, Long>("testCache2", 1, 60);
        cache.put(referenceKey, referenceValue);
        assertTrue(cache.contains(referenceKey));
        assertEquals(referenceValue, cache.get(referenceKey));
        cache.put("other", 456L);
        assertFalse(cache.contains(referenceKey));
        assertNull(cache.get(referenceKey));
    }

    @Test
    public void testExpiration() {
        String referenceKey = "testy";
        Long referenceValue = 123L;
        ObjectCache<String, Long> cache = new ObjectCache<String, Long>("testCache3", 10, 1);
        cache.put(referenceKey, referenceValue);
        assertTrue(cache.contains(referenceKey));
        assertEquals(referenceValue, cache.get(referenceKey));
        try {
            Thread.sleep(3000);
        } catch (Throwable e) {
        }
        assertFalse(cache.contains(referenceKey));
        assertNull(cache.get(referenceKey));
    }
}
