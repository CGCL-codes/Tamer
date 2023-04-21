package com.langerra.server.channel.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;
import com.langerra.server.channel.ChannelServiceFactory;
import com.langerra.server.channel.NamedCounter;
import com.langerra.shared.channel.Channel;
import com.langerra.shared.channel.ChannelService;
import com.langerra.shared.channel.ChannelServicePool;

public class AppEngineChannelServiceImpl implements ChannelService {

    static final Logger LOG = Logger.getLogger(AppEngineChannelServiceImpl.class.getName());

    public AppEngineChannelServiceImpl() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Serializable> ChannelServicePool<T> getServicePool() {
        LOG.fine("Creating channel service pool");
        return new ChannelServicePoolImpl<T>((ChannelImpl<T>) getChannel("__pool__", true));
    }

    @Override
    public void deleteChannel(String channelName) {
        LOG.fine("Deleting channel: " + channelName);
        final MemcacheService memCache = MemcacheServiceFactory.getMemcacheService(ChannelServiceFactory.getNamespace(channelName));
        memCache.clearAll();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Serializable> Channel<T> getChannel(String channelName, boolean persistent) {
        LOG.fine("Creating channel: " + channelName);
        final String namespace = ChannelServiceFactory.getNamespace(channelName);
        final MemcacheService service = MemcacheServiceFactory.getMemcacheService(namespace);
        final NamedCounter rOffset = new AppEngineCounter(service, "R");
        final NamedCounter wOffset = new AppEngineCounter(service, "W");
        final Map props = new HashMap();
        props.put(GCacheFactory.EXPIRATION_DELTA, 10 * 3600);
        props.put(GCacheFactory.MEMCACHE_SERVICE, namespace);
        Cache cache = null;
        final CacheManager manager = CacheManager.getInstance();
        while (cache == null) {
            try {
                manager.registerCache(namespace, manager.getCacheFactory().createCache(props));
            } catch (CacheException e) {
                e.printStackTrace();
            }
            cache = manager.getCache(namespace);
        }
        return new ChannelImpl<T>(namespace, cache, rOffset, wOffset, persistent);
    }
}
