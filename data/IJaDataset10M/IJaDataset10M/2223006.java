package com.volantis.synergetics.cache;

/**
 * A ManagedCache is an extension of a LimitedCache that has a timeout
 * facility. In other words a ManagedCache is a combination of a LimitedCache
 * and a RefreshingCache. This type of cache is the slowest of the Volantis
 * internal cache classes.
 *
 * @deprecated Use {@link com.volantis.cache.Cache} instead.
 */
public class ManagedCache extends LimitedCache {

    /**
     * Volantis copyright object.
     */
    private static String mark = "(c) Volantis Systems Ltd 2001. ";

    /**
     * Construct a new ManagedCache that stores objects as SoftReferences
     *
     * @param maxEntries the maximum number of elements allowed in this cache
     * @param strategy   the strategy for removing elements from the cache once
     *                   the maximum limit has been reached
     * @param timeout    the the value for the timeout property of this cache
     *                   in seconds.
     */
    public ManagedCache(int maxEntries, CacheStrategy strategy, int timeout) {
        super(maxEntries, strategy);
        setTimeout(timeout);
    }

    /**
     * Construct a new ManagedCache.
     *
     * @param factory    the factory used to create the ReadThroughFutureResult
     *                   obejcts for use in this cache.
     * @param maxEntries the maximum number of elements allowed in this cache
     * @param strategy   the strategy for removing elements from the cache once
     *                   the maximum limit has been reached
     * @param timeout    the the value for the timeout property of this cache
     *                   in seconds.
     */
    public ManagedCache(FutureResultFactory factory, int maxEntries, CacheStrategy strategy, int timeout) {
        super(factory, maxEntries, strategy);
        setTimeout(timeout);
    }
}
