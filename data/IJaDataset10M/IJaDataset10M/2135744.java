package ar.com.oddie.persistence.cache;

import java.util.HashMap;

public class CacheManager {

    private static HashMap<String, Cache<?, ?>> context;

    static {
        context = new HashMap<String, Cache<?, ?>>();
    }

    @SuppressWarnings("unchecked")
    public static <K, T> Cache<K, T> getCache(String cacheName) {
        if (!context.containsKey(cacheName)) {
            context.put(cacheName, new Cache<K, T>(cacheName));
        }
        return (Cache<K, T>) context.get(cacheName);
    }

    public static void clear() {
        context.clear();
    }
}
