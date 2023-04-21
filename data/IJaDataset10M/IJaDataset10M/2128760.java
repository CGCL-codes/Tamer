package com.spoledge.audao.db.dao.gae;

import com.spoledge.audao.db.dao.DtoCache;
import com.spoledge.audao.db.dao.DtoCacheFactory;

/**
 * This is a DtoCacheFactory which uses MemcacheService caches.
 */
public class MemcacheDtoCacheFactoryImpl<K, V> implements DtoCacheFactory<K, V> {

    private String name;

    /**
     * Creates a new factory.
     */
    public MemcacheDtoCacheFactoryImpl(String name) {
        this.name = name;
    }

    /**
     * Creates a cache with no expiration policy.
     */
    public DtoCache<K, V> createDtoCache(int maxSize) {
        return new MemcacheDtoCacheImpl<K, V>(name);
    }

    /**
     * Creates a cache with expiration policy.
     */
    public DtoCache<K, V> createDtoCache(long expireMillis, int maxSize) {
        return new ExpiringMemcacheDtoCacheImpl<K, V>(expireMillis, name);
    }
}
