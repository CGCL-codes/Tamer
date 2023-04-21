package com.avaje.ebean.server.cache;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.cache.ServerCache;
import com.avaje.ebean.cache.ServerCacheFactory;
import com.avaje.ebean.cache.ServerCacheManager;
import com.avaje.ebean.cache.ServerCacheOptions;

/**
 * Manages the bean and query caches. 
 */
public class DefaultServerCacheManager implements ServerCacheManager {

    private final DefaultCacheHolder beanCache;

    private final DefaultCacheHolder queryCache;

    private final ServerCacheFactory cacheFactory;

    /**
	 * Create with a cache factory and default cache options.
	 */
    public DefaultServerCacheManager(ServerCacheFactory cacheFactory, ServerCacheOptions defaultBeanOptions, ServerCacheOptions defaultQueryOptions) {
        this.cacheFactory = cacheFactory;
        this.beanCache = new DefaultCacheHolder(cacheFactory, defaultBeanOptions, true);
        this.queryCache = new DefaultCacheHolder(cacheFactory, defaultQueryOptions, false);
    }

    public void init(EbeanServer server) {
        cacheFactory.init(server);
    }

    /**
	 * Clear both the bean cache and the query cache for a 
	 * given bean type.
	 */
    public void clear(Class<?> beanType) {
        if (isBeanCaching(beanType)) {
            getBeanCache(beanType).clear();
        }
        if (isQueryCaching(beanType)) {
            getQueryCache(beanType).clear();
        }
    }

    public void clearAll() {
        beanCache.clearAll();
        queryCache.clearAll();
    }

    /**
	 * Return the query cache for a given bean type.
	 */
    public ServerCache getQueryCache(Class<?> beanType) {
        return queryCache.getCache(beanType);
    }

    /**
	 * Return the bean cache for a given bean type.
	 */
    public ServerCache getBeanCache(Class<?> beanType) {
        return beanCache.getCache(beanType);
    }

    /**
	 * Return true if there is an active cache for the given bean type.
	 */
    public boolean isBeanCaching(Class<?> beanType) {
        return beanCache.isCaching(beanType);
    }

    public boolean isQueryCaching(Class<?> beanType) {
        return queryCache.isCaching(beanType);
    }
}
