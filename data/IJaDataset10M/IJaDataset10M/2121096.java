package net.sourceforge.freejava.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class Pair<K, V> implements Entry<K, V>, Serializable {

    private static final long serialVersionUID = 1L;

    public K first;

    public V second;

    public Pair() {
    }

    public Pair(K first) {
        this.first = first;
    }

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K getFirst() {
        return first;
    }

    public void setFirst(K first) {
        this.first = first;
    }

    public V getSecond() {
        return second;
    }

    public void setSecond(V second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        if (!(object instanceof Pair<?, ?>)) return false;
        Pair<?, ?> objectPair = (Pair<?, ?>) object;
        if (first == null) {
            if (objectPair.first != null) return false;
        } else if (!first.equals(objectPair.first)) return false;
        if (second == null) {
            if (objectPair.second != null) return false;
        } else if (!second.equals(objectPair.second)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        if (first != null) hash += first.hashCode();
        if (second != null) hash += second.hashCode();
        return hash;
    }

    public void clear() {
        first = null;
    }

    public boolean containsKey(Object key) {
        if (key == null) throw new NullPointerException("null key");
        return key.equals(first);
    }

    public boolean containsValue(Object value) {
        if (first == null) return false;
        if (second == null) return second == value;
        return second.equals(value);
    }

    public Set<java.util.Map.Entry<K, V>> entrySet() {
        Set<java.util.Map.Entry<K, V>> set = new HashSet<java.util.Map.Entry<K, V>>();
        if (first != null) set.add(this);
        return set;
    }

    public V get(Object key) {
        if (key == null) throw new NullPointerException("null key");
        if (key.equals(first)) return second;
        return null;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public Set<K> keySet() {
        Set<K> s = new HashSet<K>();
        if (first != null) s.add(first);
        return s;
    }

    public V put(K key, V value) {
        if (key == null) throw new NullPointerException("null key");
        first = key;
        second = value;
        return null;
    }

    public void putAll(Map<? extends K, ? extends V> t) {
        Iterator<? extends K> it = t.keySet().iterator();
        if (it.hasNext()) {
            first = it.next();
            second = t.get(first);
        }
    }

    public V remove(Object key) {
        if (key == null) throw new NullPointerException("Null key");
        if (key.equals(first)) {
            first = null;
            return second;
        }
        return null;
    }

    public int size() {
        if (first == null) return 0;
        return 1;
    }

    public Collection<V> values() {
        List<V> list = new ArrayList<V>();
        if (first != null) list.add(second);
        return list;
    }

    public K getKey() {
        return first;
    }

    public V getValue() {
        return second;
    }

    public V setValue(V value) {
        V old = second;
        second = value;
        return old;
    }
}
