package uk.ac.ebi.pride.data.controller.access;

import uk.ac.ebi.pride.data.controller.DataAccessException;
import uk.ac.ebi.pride.data.controller.cache.Cache;
import uk.ac.ebi.pride.data.controller.cache.CacheBuilder;

/**
 * Interface for accessing the cache.
 * <p/>
 * User: rwang
 * Date: 06-Sep-2010
 * Time: 09:46:55
 */
public interface CacheAccess {

    /**
     * Get the cache.
     *
     * @return a reference to cache.
     */
    public Cache getCache();

    /**
     * Set a cache
     *
     * @param cache DataAccessCache
     */
    public void setCache(Cache cache);

    /**
     * Get a cache builder
     *
     * @return DataAccessCacheBuilder   cache builder
     */
    public CacheBuilder getCacheBuilder();

    /**
     * Set a cache builder
     *
     * @param builder Data access cache builder
     */
    public void setCacheBuilder(CacheBuilder builder);

    /**
     * Clear all the content in existing cache.
     */
    public void clearCache();

    /**
     * Populate the cache with content.
     *
     * @throws DataAccessException data access exception
     */
    public void populateCache() throws DataAccessException;
}
