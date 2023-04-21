package be.abeel.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Two-dimensional HashMap. Each value has a pair of keys. The outer Map
 * consists of HashMaps keyed by the first key, and each corresponding HashMap
 * is keyed by the second key.
 */
public class HashMap2D<K, L, V> extends HashMap<K, Map<L, V>> {

    /**
     * 
     */
    private static final long serialVersionUID = 6145637193414943351L;

    /**
     * Constructs an empty LinkedHashMap.
     */
    public HashMap2D() {
        super();
    }

    /**
     * Puts a value into the HashMap2D with given keys. If the inner Map does
     * not exist, it is created.
     * 
     * @param key1
     *            The first key of the property
     * @param key2
     *            The second key
     * @param value
     *            The value of the property.
     */
    public void put(K key1, L key2, V value) {
        if (this.containsKey(key1)) {
            this.get(key1).put(key2, value);
        } else {
            Map<L, V> map = new LinkedHashMap<L, V>();
            map.put(key2, value);
            this.put(key1, map);
        }
    }

    /**
     * Gets the value of the property.
     * 
     * @param key1
     *            The outer key of the property
     * @param key2
     *            The inner key of the property.
     * @return The value of the property
     */
    public V get(K key1, L key2) {
        V value = null;
        if (this.containsKey(key1)) {
            value = this.get(key1).get(key2);
        }
        return value;
    }

    /**
     * Removes the property specified by the given keys.
     * 
     * @param key1
     *            Key to Map for removed value
     * @param key2
     *            Key to removed value
     * @return The removed value or null if the property doesn't exist.
     */
    public V remove(K key1, L key2) {
        V removedValue = null;
        Map<L, V> map = get(key1);
        if (map != null) {
            removedValue = map.remove(key2);
        }
        return removedValue;
    }

    public boolean containsKey(K key1, L key2) {
        return this.containsKey(key1) && this.get(key1).containsKey(key2);
    }

    /**
     * Converts the LinkedHashMap2 to a n by 2 array of Objects. Where n is the
     * number of entries in the outer Map. The array contains pairs of outer
     * keys and inner Maps.
     * 
     * @return Contents as array of Objects
     */
    public Object[][] toArray() {
        ArrayList<Object[]> contents = new ArrayList<Object[]>();
        for (K key1 : this.keySet()) {
            Map<L, V> map = this.get(key1);
            contents.add(new Object[] { key1, map });
        }
        return contents.toArray(new Object[this.size()][2]);
    }
}
