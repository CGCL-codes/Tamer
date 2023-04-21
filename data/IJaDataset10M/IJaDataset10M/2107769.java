package net.sf.jpasecurity.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Arne Limburg
 * @see ListMap
 */
public class ListHashMap<K, V> extends AbstractCollectionHashMap<K, List<V>, V> implements ListMap<K, V> {

    /**
     * {@inheritDoc}
     */
    public ListHashMap() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public ListHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * {@inheritDoc}
     */
    public ListHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * {@inheritDoc}
     */
    public ListHashMap(Map<? extends K, ? extends List<V>> map) {
        super(map);
    }

    /**
     * {@inheritDoc}
     */
    public V get(K key, int index) {
        List<V> list = get(key);
        if (list == null) {
            throw new NoSuchElementException("No list for key " + key);
        }
        return list.get(index);
    }

    /**
     * {@inheritDoc}
     */
    public int indexOf(K key, V value) {
        List<V> list = get(key);
        if (list == null) {
            return -1;
        }
        return list.indexOf(value);
    }

    /**
     * {@inheritDoc}
     */
    protected List<V> createCollection() {
        return new ArrayList<V>();
    }
}
