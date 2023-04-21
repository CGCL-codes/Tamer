package com.volantis.mcs.cache;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * This Factory provide access via reflection to implementations of @link
 * CacheStore and @link CacheManager objects for building @link Cache
 * implemetations.
 */
public abstract class CacheFactory {

    /**
     * The default single instance of this factory
     */
    private static CacheFactory defaultInstance;

    /**
     * Instantiate the default instance using reflection to prevent
     * dependencies between this and the implementation class.
     */
    static {
        try {
            ClassLoader loader = CacheFactory.class.getClassLoader();
            Class implClass = loader.loadClass("com.volantis.mcs.cache.impl.DefaultCacheFactory");
            defaultInstance = (CacheFactory) implClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new UndeclaredThrowableException(e);
        } catch (InstantiationException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Get the single instance of the DefaultCacheFactory.
     *
     * @return The default instance of the DefaultCacheFactory;
     */
    public static CacheFactory getDefaultInstance() {
        return defaultInstance;
    }

    /**
     * Creates a default implementation of CacheStore
     * @param timeToLive The amount of time an entry can remain in cache before
     * it is considered to be out of date.
     * @return The Default @link CacheStore
     */
    public abstract CacheStore createDefaultCacheStore(long timeToLive);

    /**
     * Creates an implementation of CacheManager that flushes expired
     * CacheIdentities from the cache automatically.
     *
     * @return The an implementation of @link CacheManager that runs in the 
     * background.
     */
    public abstract CacheManager createBackgroundCacheManager();
}
