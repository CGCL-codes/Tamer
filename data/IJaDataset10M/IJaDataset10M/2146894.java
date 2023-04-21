package glaceo.utils.cache;

import java.util.Collections;
import java.util.Hashtable;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Very simple {@link Hashtable} based cache. It is only intended to be used for "light"
 * non-memory intensive caching. If scaling or other features like disk support is
 * required a powerful solution like ehcache should be used. When the cache is full, the
 * least recently used object is discarded. The usage timestamp is reset when an object is
 * accessed by using put or get operations.
 *
 * @version $Id$
 * @author jjanke
 */
public final class GSimpleCache<T> {

    private SortedMap<GSimpleCacheKey, T> d_mapCachedObjects;

    private int d_nMaxSize = 1;

    /**
   * Constructs the cache and sets the maximum capacity. When this capacity is reached,
   * the least recently used object will be discarded from the cache before inserting a
   * new one.
   *
   * @param nMaxSize the maximum capacity of this cache (must be greater than 1)
   */
    public GSimpleCache(int nMaxSize) {
        if (nMaxSize < 1) throw new IllegalArgumentException("Cache size must be at least 1.");
        d_nMaxSize = nMaxSize;
    }

    /**
   * @return The maximum capacity of this cache.
   */
    public int getMaxSize() {
        return d_nMaxSize;
    }

    /**
   * Adds a new element to the cache.
   *
   * @param strKey the key identifying the cached object
   * @param object the object to be cached
   */
    public synchronized void put(String strKey, T object) {
        GSimpleCacheKey key = new GSimpleCacheKey(strKey);
        if (d_mapCachedObjects == null) d_mapCachedObjects = Collections.synchronizedSortedMap(new TreeMap<GSimpleCacheKey, T>()); else if (d_mapCachedObjects.size() == d_nMaxSize) d_mapCachedObjects.remove(d_mapCachedObjects.firstKey());
        d_mapCachedObjects.put(key, object);
    }

    /**
   * Removes an object from the cache.
   *
   * @param strKey the key of the object to be removed.
   * @return the removed object or <code>null</code>
   */
    public synchronized T remove(String strKey) {
        if (d_mapCachedObjects == null) return null;
        return d_mapCachedObjects.remove(new GSimpleCacheKey(strKey));
    }

    /**
   * Retrieves an object from the cache.
   *
   * @param strKey the key of the object to be retrieved
   * @return the matching cached object or <code>null</code> if there is no object
   *         corresponding to the given key
   */
    public synchronized T get(String strKey) {
        if (d_mapCachedObjects == null) return null;
        return d_mapCachedObjects.get(new GSimpleCacheKey(strKey));
    }
}
