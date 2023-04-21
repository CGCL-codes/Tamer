package com.avaje.ebean.server.cache;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.cache.ServerCache;
import com.avaje.ebean.cache.ServerCacheFactory;
import com.avaje.ebean.cache.ServerCacheOptions;

/**
 * Default implementation of ServerCacheFactory.
 */
public class DefaultServerCacheFactory implements ServerCacheFactory {

    private EbeanServer ebeanServer;

    public void init(EbeanServer ebeanServer) {
        this.ebeanServer = ebeanServer;
    }

    public ServerCache createCache(Class<?> beanType, ServerCacheOptions cacheOptions) {
        ServerCache cache = new DefaultServerCache("Bean:" + beanType, cacheOptions);
        cache.init(ebeanServer);
        return cache;
    }
}
