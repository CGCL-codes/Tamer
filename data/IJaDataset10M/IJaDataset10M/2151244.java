package onepoint.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A thread safe implementation of LRU cache, based on <code>LinkedHashMap</code>.
 * This cache has a fixed maximum number of elements (<code>maxCacheSize</code>).
 * If the cache is full and another entry is added, the LRU (least recently used) entry is dropped.
 *
 * @author ovidiu.lupas
 */
public final class XCache {

    private final float hashTableLoadFactor = 0.75f;

    private Map cache;

    private int maxCacheSize;

    /**
    * Creates a default LRU cache. The maximul number of entries kept is 500.
    */
    public XCache() {
        this(500);
    }

    /**
    * Creates a new LRU cache.
    *
    * @param cacheSize the maximum number of entries that will be kept in this cache.
    */
    public XCache(int cacheSize) {
        this.maxCacheSize = cacheSize;
        int hashTableCapacity = (int) Math.ceil(cacheSize / hashTableLoadFactor) + 1;
        cache = Collections.synchronizedMap(new LinkedHashMap(hashTableCapacity, hashTableLoadFactor, true) {

            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > XCache.this.maxCacheSize;
            }
        });
    }

    /**
    * Sets up the maximul number of entries for this cache .
    *
    * @param cacheSize <code>int</code> the maximum number of entries that will be kept in this cache.
    */
    public synchronized void setMaxCacheSize(int cacheSize) {
        maxCacheSize = cacheSize;
    }

    /**
    * Retrives the maximul number of entries for this cache .
    *
    * @return <code>int</code> the maximum number of entries that will be kept in this cache.
    */
    public synchronized int getMaxCacheSize() {
        return maxCacheSize;
    }

    /**
    * Retrieves an entry from the cache.The retrieved entry becomes the MRU (most recently used) entry.
    *
    * @param key <code>Object</code> the key whose associated value is to be returned.
    * @return <code>Object</code> the value associated to this key, or null if no value with this key exists in the cache.
    */
    public synchronized Object get(Object key) {
        return cache.get(key);
    }

    /**
    * Adds an entry to this cache.If the cache is full, the LRU (least recently used) entry is dropped.
    *
    * @param key   <code>Object</code> the key with which the specified value is to be associated.
    * @param value <code>Object</code> value to be associated with the specified key.
    */
    public synchronized void put(Object key, Object value) {
        cache.put(key, value);
    }

    /**
    * Clears the cache.
    */
    public synchronized void clear() {
        cache.clear();
    }

    /**
    * Returns the number of used entries in the cache.
    *
    * @return <code>int</code> the number of entries currently in the cache.
    */
    public synchronized int usedEntries() {
        return cache.size();
    }

    /**
    * Returns a <code>Collection</code> that contains a copy of all cache entries.
    *
    * @return a <code>Collection</code> with a copy of the cache content.
    */
    public synchronized Collection getAll() {
        return new ArrayList(cache.entrySet());
    }
}
