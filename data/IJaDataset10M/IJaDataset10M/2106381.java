package com.volantis.cache.group;

import com.volantis.cache.CacheEntry;
import com.volantis.cache.Cache;
import com.volantis.cache.provider.CacheableObjectProvider;
import com.volantis.cache.provider.ProviderResult;
import com.volantis.shared.system.SystemClock;

public class TestCacheableObjectProvider implements CacheableObjectProvider {

    public ProviderResult retrieve(SystemClock clock, Object key, CacheEntry entry) {
        String value = "value for (" + entry.getKey() + ")";
        return new ProviderResult(value, selectGroup(entry), true, null);
    }

    private Group selectGroup(CacheEntry entry) {
        Cache cache = entry.getCache();
        String key = (String) entry.getKey();
        int index = key.indexOf('.');
        if (index == -1) {
            throw new IllegalArgumentException("Invalid key '" + key + "'");
        }
        String group = key.substring(0, index);
        Group root = cache.getRootGroup();
        return root.getGroup(group);
    }
}
