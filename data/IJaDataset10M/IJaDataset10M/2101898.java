package org.jcvi.util;

import java.util.Comparator;
import java.util.Map;

/**
 * {@code MapValueComparator} is a {@link Comparator}
 * that allows sorting a Map's keys based on the values mapped
 * to the keys.
 * @author dkatzel
 *
 *
 */
public class MapValueComparator<K, V extends Comparable> implements Comparator<K> {

    private final Map<K, V> map;

    /**
     * @param map
     */
    public MapValueComparator(Map<K, V> map) {
        this.map = map;
    }

    /**
    * {@inheritDoc}
    */
    @SuppressWarnings("unchecked")
    @Override
    public int compare(K o1, K o2) {
        return map.get(o1).compareTo(map.get(o2));
    }
}
