package com.mycila.event.internal;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class WeakCache<K, V> {

    private static final Object NULL = new Object();

    private final WeakHashMap<K, WeakReference<V>> cache = new WeakHashMap<K, WeakReference<V>>();

    private final Provider<K, V> provider;

    public WeakCache(Provider<K, V> provider) {
        this.provider = provider;
    }

    public V get(K key) {
        WeakReference<V> ref = cache.get(key);
        V val = null;
        if (ref != null) val = ref.get();
        if (val == null) {
            val = provider.get(key);
            if (val == null) val = (V) NULL;
            cache.put(key, new WeakReference<V>(val));
        }
        return val == NULL ? null : val;
    }

    public static interface Provider<K, V> {

        V get(K key);
    }
}
