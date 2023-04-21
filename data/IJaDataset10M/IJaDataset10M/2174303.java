package it.freax.fpm.util;

import java.util.Map.Entry;

public class MapEntry<K, V> implements Entry<K, V> {

    private K key;

    private V value;

    public MapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public MapEntry(Entry<? extends K, ? extends V> entry) {
        this.key = entry.getKey();
        this.value = entry.getValue();
    }

    @Override
    public K getKey() {
        return this.key;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public V setValue(V value) {
        this.value = value;
        return this.value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        boolean ret = false;
        if (obj instanceof MapEntry<?, ?>) {
            MapEntry<?, ?> en = (MapEntry<?, ?>) obj;
            ret = en.getKey().equals(key) && en.getValue().equals(value);
        } else if (obj instanceof Entry<?, ?>) {
            MapEntry<?, ?> en = new MapEntry<K, V>((Entry<K, V>) obj);
            ret = en.getKey().equals(key) && en.getValue().equals(value);
        } else {
            ret = super.equals(obj);
        }
        return ret;
    }

    @Override
    public String toString() {
        return key.toString().concat("=").concat(value.toString());
    }
}
