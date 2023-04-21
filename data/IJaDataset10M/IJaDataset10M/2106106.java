package com.jalarbee.core.cache.memcached;

public class CachedObject {

    private String cachedObjectName;

    private CachedObject(String cachedObjectName) {
        this.cachedObjectName = cachedObjectName.replace(' ', '_');
    }

    public String toString() {
        return cachedObjectName;
    }
}
