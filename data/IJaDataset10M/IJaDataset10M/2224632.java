package org.colllib.datastruct;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.colllib.factories.Factory;
import org.colllib.transformer.Transformer;

/**
 * The auto init map takes the tedious work out of dynamic map creation, by
 * always supplying a value for each key: if a key has not been put in the
 * map so far, a get on the map will create a new value automatically for that
 * key, place it in the map, and return it. The creation of the value uses an
 * underlying factory or a transformation of the key.
 * <p>
 * Example: collect values by a key (say pair.x)
 * <p>
 * Classic:
 * <pre>
 * HashMap&lt;Integer, HashSet&lt;Integer>> m = new HashMap&lt;Integer, HashSet&lt;Integer>>();
 * for(Pair&lt;Integer, Integer> p : someCollection) {
 *   if(m.get(p.x) == null)
 *     m.put(p.x, new HashSet&lt;Integer>());
 *   m.get(p.x).add(p.y);
 * }
 * </pre>
 * AutoInit:
 * <pre>
 * AutoInitMap&lt;Integer, HashSet&lt;Integer>> m = new AutoInitMap&lt;Integer, HashSet&lt;Integer>>(FactoryCollection.cloneFactory(new HashSet&lt;Integer>()));
 * for(Pair&lt;Integer, Integer> p : someCollection)
 *   m.get(p.x).add(p.y);
 * </pre>
 * 
 * This file is part of CollLib released under the terms of the LGPL V3.0.
 * See the file licenses/lgpl-3.0.txt for details.
 * 
 * @author marc.jackisch
 */
public class AutoInitMap<K, V> implements Map<K, V>, Serializable {

    private final Map<K, V> backingMap;

    private final Factory<V> valueFactory;

    private final Transformer<K, V> valueTransformer;

    public AutoInitMap(Map<K, V> backingMap, Factory<V> valueFactory) {
        this.backingMap = backingMap;
        this.valueFactory = valueFactory;
        this.valueTransformer = null;
    }

    public AutoInitMap(Map<K, V> backingMap, Transformer<K, V> valueTransformer) {
        this.backingMap = backingMap;
        this.valueFactory = null;
        this.valueTransformer = valueTransformer;
    }

    public AutoInitMap(Factory<V> valueFactory) {
        this(new HashMap<K, V>(), valueFactory);
    }

    public AutoInitMap(Transformer<K, V> valueTransformer) {
        this(new HashMap<K, V>(), valueTransformer);
    }

    public Map<K, V> getBackingMap() {
        return backingMap;
    }

    @Override
    public int size() {
        return backingMap.size();
    }

    @Override
    public boolean isEmpty() {
        return backingMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return backingMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return backingMap.containsValue(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        if (!backingMap.containsKey(key)) {
            if (valueFactory != null) backingMap.put((K) key, valueFactory.create()); else backingMap.put((K) key, valueTransformer.transform((K) key));
        }
        return backingMap.get(key);
    }

    @Override
    public V put(K key, V value) {
        return backingMap.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return backingMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        backingMap.putAll(m);
    }

    @Override
    public void clear() {
        backingMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return backingMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return backingMap.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return backingMap.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return backingMap.equals(o);
    }

    @Override
    public int hashCode() {
        return backingMap.hashCode();
    }
}
