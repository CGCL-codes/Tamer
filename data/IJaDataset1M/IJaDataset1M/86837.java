package org.acegisecurity.acl.basic.cache;

import junit.framework.TestCase;
import net.sf.ehcache.Cache;
import org.acegisecurity.MockApplicationContext;
import org.acegisecurity.acl.basic.AclObjectIdentity;
import org.acegisecurity.acl.basic.BasicAclEntry;
import org.acegisecurity.acl.basic.NamedEntityObjectIdentity;
import org.acegisecurity.acl.basic.SimpleAclEntry;
import org.springframework.context.ApplicationContext;

/**
 * Tests {@link EhCacheBasedAclEntryCache}.
 *
 * @author Ben Alex
 * @version $Id: EhCacheBasedAclEntryCacheTests.java,v 1.4 2006/01/27 04:42:39 benalex Exp $
 */
public class EhCacheBasedAclEntryCacheTests extends TestCase {

    private static final AclObjectIdentity OBJECT_100 = new NamedEntityObjectIdentity("OBJECT", "100");

    private static final AclObjectIdentity OBJECT_200 = new NamedEntityObjectIdentity("OBJECT", "200");

    private static final BasicAclEntry OBJECT_100_MARISSA = new SimpleAclEntry("marissa", OBJECT_100, null, 2);

    private static final BasicAclEntry OBJECT_100_SCOTT = new SimpleAclEntry("scott", OBJECT_100, null, 4);

    private static final BasicAclEntry OBJECT_200_PETER = new SimpleAclEntry("peter", OBJECT_200, null, 4);

    public EhCacheBasedAclEntryCacheTests() {
        super();
    }

    public EhCacheBasedAclEntryCacheTests(String arg0) {
        super(arg0);
    }

    private Cache getCache() {
        ApplicationContext ctx = MockApplicationContext.getContext();
        return (Cache) ctx.getBean("eHCacheBackend");
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(EhCacheBasedAclEntryCacheTests.class);
    }

    public final void setUp() throws Exception {
        super.setUp();
    }

    public void testCacheOperation() throws Exception {
        EhCacheBasedAclEntryCache cache = new EhCacheBasedAclEntryCache();
        cache.setCache(getCache());
        cache.afterPropertiesSet();
        cache.putEntriesInCache(new BasicAclEntry[] { OBJECT_100_SCOTT, OBJECT_100_MARISSA });
        cache.putEntriesInCache(new BasicAclEntry[] { OBJECT_200_PETER });
        assertEquals(OBJECT_100_SCOTT, cache.getEntriesFromCache(new NamedEntityObjectIdentity("OBJECT", "100"))[0]);
        assertEquals(OBJECT_100_MARISSA, cache.getEntriesFromCache(new NamedEntityObjectIdentity("OBJECT", "100"))[1]);
        assertEquals(OBJECT_200_PETER, cache.getEntriesFromCache(new NamedEntityObjectIdentity("OBJECT", "200"))[0]);
        assertNull(cache.getEntriesFromCache(new NamedEntityObjectIdentity("OBJECT", "NOT_IN_CACHE")));
        cache.removeEntriesFromCache(new NamedEntityObjectIdentity("OBJECT", "100"));
        assertNull(cache.getEntriesFromCache(new NamedEntityObjectIdentity("OBJECT", "100")));
    }

    public void testStartupDetectsMissingCache() throws Exception {
        EhCacheBasedAclEntryCache cache = new EhCacheBasedAclEntryCache();
        try {
            cache.afterPropertiesSet();
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
        Cache myCache = getCache();
        cache.setCache(myCache);
        assertEquals(myCache, cache.getCache());
    }
}
