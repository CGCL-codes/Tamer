package org.clouddreamwork.minisql;

import java.util.*;
import javax.cache.*;
import org.clouddreamwork.*;
import com.google.appengine.api.memcache.jsr107cache.*;

public class MiniCache {

    @SuppressWarnings("unchecked")
    public static void put(String name, String owner, Object content, int expirationTime) {
        Map<String, Integer> props = new HashMap<String, Integer>();
        props.put(GCacheFactory.EXPIRATION_DELTA, expirationTime);
        try {
            Cache cache = CacheManager.getInstance().getCacheFactory().createCache(props);
            cache.put(DataModel.generateKeyName(name, owner), content);
        } catch (CacheException e) {
            Utils.throwIfDevelopment(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static void put(String name, String owner, Object content) {
        try {
            Cache cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.EMPTY_MAP);
            cache.put(DataModel.generateKeyName(name, owner), content);
        } catch (CacheException e) {
            Utils.throwIfDevelopment(e);
        }
    }

    public static Object get(String name, String owner) {
        try {
            Cache cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.EMPTY_MAP);
            return cache.get(DataModel.generateKeyName(name, owner));
        } catch (CacheException e) {
            Utils.throwIfDevelopment(e);
            return null;
        }
    }

    public static boolean containsKey(String name, String owner) {
        try {
            Cache cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.EMPTY_MAP);
            return cache.containsKey(DataModel.generateKeyName(name, owner));
        } catch (CacheException e) {
            Utils.throwIfDevelopment(e);
            return false;
        }
    }

    public static void remove(String name, String owner) {
        try {
            Cache cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.EMPTY_MAP);
            Object obj = cache.get(DataModel.generateKeyName(name, owner));
            if (obj != null) cache.remove(obj);
        } catch (CacheException e) {
            Utils.throwIfDevelopment(e);
        }
    }

    public static CacheStatistics getCacheStatistics() {
        try {
            Cache cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.EMPTY_MAP);
            return cache.getCacheStatistics();
        } catch (CacheException e) {
            Utils.throwIfDevelopment(e);
            return null;
        }
    }

    public static int[] getStatisticData() {
        CacheStatistics stat = getCacheStatistics();
        int[] ans = { stat.getObjectCount(), stat.getCacheHits(), stat.getCacheMisses() };
        return ans;
    }
}
