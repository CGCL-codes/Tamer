package com.lasalletech.openfast.util;

import junit.framework.TestCase;

public class BasicCacheTest extends TestCase {

    public void testStore() {
        BasicCache<String> cache = new BasicCache<String>(2);
        cache.store("abcd");
        cache.store("efgh");
        assertEquals("abcd", cache.lookup(0));
        cache.store("wxyz");
        assertEquals("wxyz", cache.lookup(0));
    }
}
