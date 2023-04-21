package net.sf.dozer.util.mapping.cache;

import net.sf.dozer.util.mapping.AbstractDozerTest;

/**
 * @author tierney.matt
 */
public class CacheEntryTest extends AbstractDozerTest {

    public void testConstructor() throws Exception {
        String key = getRandomString();
        String value = getRandomString();
        CacheEntry cacheEntry = new CacheEntry(key, value);
        assertEquals("invalid key", key, cacheEntry.getKey());
        assertEquals("invalid value", value, cacheEntry.getValue());
        assertTrue("Creation time should be not null", cacheEntry.getCreationTime() > 0);
    }

    public void testHashCodeAndEquals() throws Exception {
        String key = getRandomString();
        String value = getRandomString();
        CacheEntry cacheEntry = new CacheEntry(key, value);
        CacheEntry cacheEntry2 = new CacheEntry(key, value);
        assertEquals("cache entries hash code should have been equal", cacheEntry.hashCode(), cacheEntry2.hashCode());
        assertEquals("cache entries should have been equal", cacheEntry, cacheEntry2);
    }
}
