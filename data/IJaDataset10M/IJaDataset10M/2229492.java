package net.sf.clairv.search.cache;

import net.sf.ehcache.CacheManager;

/**
 * Cache provider backed by EhCache.
 * 
 * @author qiuyin
 *
 */
public class EhCacheProvider implements CacheProvider {

    private CacheManager manager;

    public EhCacheProvider() {
        this.manager = new CacheManager();
    }

    public EhCacheProvider(String configPath) {
        this.manager = new CacheManager(configPath);
    }

    public Cache getCache(String name) {
        net.sf.ehcache.Cache ehCache = manager.getCache(name);
        if (ehCache == null) {
            manager.addCache(name);
            ehCache = manager.getCache(name);
        }
        return new EhCache(ehCache);
    }

    public void shutdown() {
        manager.shutdown();
    }
}
