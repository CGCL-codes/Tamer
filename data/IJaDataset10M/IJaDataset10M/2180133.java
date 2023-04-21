package org.jmage.cache;

import java.util.Properties;

/**
 * Cache
 */
public interface Cache {

    /**
     * Initialize the cache using Properties
     *
     * @param properties
     */
    public void init(Properties properties) throws CacheException;

    /**
     * Pages an object into the cache, affecting it's cache ranking.
     *
     * @param key    the key
     * @param object the object
     */
    public void pageIn(Object key, Object object) throws CacheException;

    /**
     * Pages an object out of the cache, affecting it's cache ranking.
     *
     * @param key the object's cache key
     * @return the object
     */
    public Object pageOut(Object key) throws CacheException;

    /**
     * Removes an object from the cache
     *
     * @param key the object's cache key
     */
    public void remove(Object key) throws CacheException;

    /**
     * Destroy the cache
     */
    public void destroy() throws CacheException;
}
