package com.exjali.collections;

import java.util.Map;

/**
 * <p>Wrapper for a key and a value used by the <code>map()</code> methods of 
 * {@link CollectionShortcuts} and {@link UncheckedCollectionShortcuts} as
 * arguments to initialize actula maps.
 * </p>
 * 
 * @author Raphael Lemaire
 *
 * @param <K> The key type.
 * @param <V> The value type.
 * 
 * @see {@link CollectionShortcuts.map}
 * @see {@link CollectionShortcuts.roMap}
 * @see {@link UncheckedCollectionShortcuts.map}
 * @see {@link UncheckedCollectionShortcuts.roMap}
 */
public class Entry<K, V> implements Map.Entry<K, V> {

    private K key;

    private V value;

    public Entry(K key, V value) {
        super();
        this.key = key;
        this.value = value;
    }

    public static <K, V> Entry<K, V> entry(K key, V value) {
        return new Entry<K, V>(key, value);
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        return this.value = value;
    }
}
