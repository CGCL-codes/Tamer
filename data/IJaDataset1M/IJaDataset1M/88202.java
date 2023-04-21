package org.tripcom.security.util;

import org.junit.Test;

public class LRUCacheTest {

    @Test
    public void test() {
        LRUCache<String, String> cache = new LRUCache<String, String>(1000000, 320000);
        long t0 = System.nanoTime();
        for (int i = 0; i < 10000; ++i) {
            String suffix = String.format("%6d", i);
            cache.cache("key" + suffix, "val" + suffix);
        }
        long t1 = System.nanoTime();
        System.out.println(((double) (t1 - t0)) / 10000);
        long estimatedSize = cache.estimateMemorySize();
        int realSize = SizeEstimator.getObjectSize(cache);
        System.out.println("*** entries: " + cache.estimateSize() + ", est: " + estimatedSize + ", real: " + realSize + ", error: " + (estimatedSize - realSize) + " (" + (((double) Math.abs(estimatedSize - realSize)) / realSize) + ")");
    }
}
