package net.sf.kerner.commons.collection.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import net.sf.kerner.commons.collection.MapFactory;

public class LinkedHashMapFactory<K, V> implements MapFactory<K, V> {

    public Map<K, V> create() {
        return new LinkedHashMap<K, V>();
    }

    public Map<K, V> create(Map<? extends K, ? extends V> elements) {
        return new LinkedHashMap<K, V>(elements);
    }
}
