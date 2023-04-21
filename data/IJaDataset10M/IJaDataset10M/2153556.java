package net.sf.dozer.util.mapping.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.sf.dozer.util.mapping.AbstractDozerTest;

/**
 * @author tierney.matt
 */
public class CacheKeyFactoryTest extends AbstractDozerTest {

    public void testCreateKey() throws Exception {
        List args = new ArrayList();
        args.add(String.class);
        args.add(Long.class);
        args.add(new String("hello"));
        Object cacheKey = CacheKeyFactory.createKey(args.toArray());
        Object cacheKey2 = CacheKeyFactory.createKey(new ArrayList(args).toArray());
        assertEquals("cache keys should have been equal", cacheKey, cacheKey2);
        assertEquals("cache key hash codes should have been equal", cacheKey.hashCode(), cacheKey2.hashCode());
    }

    public void testCreateKey2() throws Exception {
        String arg1 = "test string";
        Long arg2 = new Long(55);
        List arg3 = new ArrayList();
        arg3.add("list entry");
        Class arg4 = Random.class;
        Object cacheKey = CacheKeyFactory.createKey(new Object[] { arg1, arg2, arg3, arg4 });
        Object cacheKey2 = CacheKeyFactory.createKey(new Object[] { arg1, arg2, arg3, arg4 });
        assertEquals("cache keys should have been equal", cacheKey, cacheKey2);
        assertEquals("cache key hash codes should have been equal", cacheKey.hashCode(), cacheKey2.hashCode());
    }
}
