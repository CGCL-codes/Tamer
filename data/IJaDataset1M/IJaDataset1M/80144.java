package com.onede4.cache;

import java.util.List;

public interface Cache {

    /**
	 * Get an item from the cache, nontransactionally
	 * @param key
	 * @return the cached object or <tt>null</tt>
	 * @throws CacheException
	 */
    public Object get(Object key) throws CacheException;

    /**
	 * Add an item to the cache, nontransactionally, with
	 * failfast semantics
	 * @param key
	 * @param value
	 * @throws CacheException
	 */
    public void put(Object key, Object value) throws CacheException;

    /**
	 * Add an item to the cache
	 * @param key
	 * @param value
	 * @throws CacheException
	 */
    public void update(Object key, Object value) throws CacheException;

    /**
	 * Get all the keys of the cache
	 * @return
	 * @throws CacheException
	 */
    public List keys() throws CacheException;

    /**
	 * Remove an item from the cache
	 */
    public void remove(Object key) throws CacheException;

    /**
	 * Clear the cache
	 */
    public void clear() throws CacheException;

    /**
	 * Clean up
	 */
    public void destroy() throws CacheException;
}
