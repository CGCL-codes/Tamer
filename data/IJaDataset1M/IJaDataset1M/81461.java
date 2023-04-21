package com.koylu.caffein.cache;

import java.util.List;

public interface CacheManager {

    public void initiate() throws Exception;

    public void shutdown();

    public void clear();

    public void clearCache(String cacheName);

    public void put(String cacheName, String key, Object value);

    public Object get(String cacheName, String key);

    public void remove(String cacheName, String key);

    public List<String> getCaches();

    public List<String> getCacheKeys(String cacheName);
}
