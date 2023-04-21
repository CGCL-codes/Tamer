package org.vardb.util.cache;

import java.io.Serializable;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.vardb.util.CStringHelper;
import org.springframework.beans.factory.annotation.Required;

public class CEhCacheServiceImpl implements ICacheService {

    private CacheManager cacheManager = null;

    public CacheManager getCacheManager() {
        return this.cacheManager;
    }

    @Required
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void clearCache() {
        this.cacheManager.clearAll();
    }

    public void clearCache(String name) {
        Cache cache = this.cacheManager.getCache(name);
        cache.removeAll();
    }

    public void cacheItem(String name, String key, Object obj) {
        Cache cache = this.cacheManager.getCache(name);
        Element element = new Element(key, obj);
        cache.put(element);
    }

    public Object getCachedItem(String name, Serializable key) {
        Cache cache = this.cacheManager.getCache(name);
        if (cache == null) {
            CStringHelper.println("cache " + name + " is null");
            return null;
        }
        Element element = cache.get(key);
        if (element == null) {
            return null;
        }
        return element.getObjectValue();
    }

    public void removeFromCache(String name, Serializable key) {
        Cache cache = this.cacheManager.getCache(name);
        cache.remove(key);
    }

    public CCacheBean getCache(String name) {
        CCacheBean bean = new CCacheBean(name);
        Cache cache = this.cacheManager.getCache(name);
        for (Object key : cache.getKeysWithExpiryCheck()) {
            Element element = cache.getQuiet(key);
            Object obj = element.getObjectValue();
            String cls = obj.getClass().getSimpleName();
            long hits = element.getHitCount();
            bean.add(cls, key.toString(), hits, element.getCreationTime(), element.getLastAccessTime());
        }
        return bean;
    }
}
