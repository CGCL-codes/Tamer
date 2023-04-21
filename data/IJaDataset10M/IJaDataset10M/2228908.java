package org.spark.util.cache.simple;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.spark.util.cache.Cache;
import org.spark.util.cache.CacheItem;
import org.spark.util.cache.CacheKey;
import org.spark.util.cache.CacheKeyUtil;
import org.springframework.beans.factory.BeanNameAware;

public class SimpleCache implements Cache, BeanNameAware {

    private static final long serialVersionUID = -7787352780825300317L;

    protected Map<Object, SimpleCacheItem> cache = new ConcurrentHashMap<Object, SimpleCacheItem>();

    private String cacheName;

    private boolean autoRefresh = false;

    private AtomicInteger cacheHits = new AtomicInteger(0);

    private AtomicInteger misses = new AtomicInteger(0);

    public boolean isAutoRefresh() {
        return autoRefresh;
    }

    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    public void setBeanName(String name) {
        if (this.cacheName == null) this.cacheName = name;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public void clear() {
        cache.clear();
    }

    public void flush() {
        Iterator<Map.Entry<Object, SimpleCacheItem>> iterObj = cache.entrySet().iterator();
        while (iterObj.hasNext()) {
            Map.Entry<Object, SimpleCacheItem> entry = iterObj.next();
            CacheItem item = (CacheItem) entry.getValue();
            if (item.expired()) iterObj.remove();
        }
    }

    public Object get(Object key) {
        return get(key, -1);
    }

    public Object get(Object key, long timeout) {
        cacheHits.incrementAndGet();
        Object cacheKey = getCacheKey(key);
        CacheItem item = (CacheItem) cache.get(cacheKey);
        if (item == null) {
            misses.incrementAndGet();
            return null;
        }
        if (!item.expired(timeout)) {
            if (autoRefresh) item.refresh();
            return item.getObject();
        } else {
            cache.remove(cacheKey);
            misses.incrementAndGet();
            return null;
        }
    }

    public void put(Object key, Object obj) {
        put(key, obj, -1);
    }

    public void put(Object key, Object obj, long timeout) {
        SimpleCacheItem item = new SimpleCacheItem(obj, timeout);
        cache.put(getCacheKey(key), item);
    }

    public void remove(Object key) {
        Object cacheKey = getCacheKey(key);
        cache.remove(cacheKey);
    }

    public Collection<SimpleCacheItem> getCacheItems() {
        return Collections.unmodifiableCollection(cache.values());
    }

    public Collection<Object> getCacheItemNames() {
        return Collections.unmodifiableCollection(cache.keySet());
    }

    public CacheItem getCacheItem(Object key) {
        return (CacheItem) cache.get(key);
    }

    protected final Object getCacheKey(Object originKeyObj) {
        if (originKeyObj instanceof CacheKey) return originKeyObj; else return CacheKeyUtil.generateKey(originKeyObj);
    }

    public String getStatistics() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("cacheHits=" + cacheHits.intValue()).append(",misses=" + misses.intValue()).append(",size=" + cache.size());
        return buffer.toString();
    }
}
