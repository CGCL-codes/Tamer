package com.rhythm.commons.cache;

import com.rhythm.commons.cache.CacheTable;
import com.rhythm.commons.cache.eviction.LfuEviction;
import com.rhythm.commons.collections.Maps;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michael J. Lee
 */
public class CacheTableTest {

    public CacheTableTest() {
    }

    @Test
    public void testCacheTableDoesntExceedMaxItemsUsingLfuEviction() {
        final int maxItems = 10;
        CacheTable cache = new CacheTable(5000, maxItems, LfuEviction.getInstance());
        for (int i = 0; i < 20; i++) {
            cache.put(i, i);
        }
        assertEquals(maxItems, cache.size());
    }

    @Test
    public void testCacheTableRemovesOnlyExpiringItems() throws InterruptedException {
        CacheTable cache = new CacheTable(5000);
        Integer item1 = 1;
        Integer item2 = 2;
        cache.put(item1, item1);
        cache.put(item2, item2);
        assertEquals(2, cache.size());
        Thread.sleep(2000);
        assertNotNull(cache.get(1));
        Thread.sleep(4000);
        assertEquals(1, cache.size());
        assertNotNull(cache.get(1));
        assertNull(cache.get(2));
    }

    @Test
    public void testCacheTableRemovesAfterExpiring() throws InterruptedException {
        CacheTable cache = new CacheTable(5000);
        Integer item1 = 1;
        Integer item2 = 2;
        cache.put(item1, item1);
        cache.put(item2, item2);
        assertEquals(2, cache.size());
        Thread.sleep(8000);
        assertEquals(0, cache.size());
    }
}
