package org.kaleidofoundry.core.cache;

import java.io.IOException;
import org.junit.After;
import org.junit.Before;

/**
 * Test ehCache(c) Cache Manager
 * 
 * @author Jerome RADUGET
 */
public class EhCacheTest extends AbstractCacheTest {

    private CacheManager cacheManager;

    @Before
    public void setup() {
        cacheManager = CacheManagerFactory.provides(CacheProvidersEnum.ehCache.name(), "classpath:/cache/ehcache-local.xml");
        cache = cacheManager.getCache(Person.class.getName());
    }

    @After
    public void destroyAll() throws IOException {
        if (cacheManager != null) {
            LOGGER.info(cacheManager.printStatistics());
            cacheManager.destroyAll();
        }
    }
}
