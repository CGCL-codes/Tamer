package org.tamacat.httpd.filter.acl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AccessUrlCacheTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAccessUrlCache() {
        AccessUrlCache cache = new AccessUrlCache(3, 10000);
        cache.put("", new AccessUrl() {

            @Override
            public boolean isCacheExpired(long expire) {
                return false;
            }

            @Override
            public boolean isSuccess() {
                return false;
            }
        });
    }
}
