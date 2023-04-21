package spiffy.core.util;

import java.util.HashMap;
import java.util.Set;

/**
 * A 3-dimensional hashmap is a HashMap that enables you to refer to values via three keys rather than one. The
 * underlying implementation is simply a HashMap containing HashMap containing a HashMap, each of which maps to values.
 * 
 * @author Kasper B. Graversen
 */
public class ThreeDHashMap<K1, K2, K3, V> {

    private final HashMap<K1, HashMap<K2, HashMap<K3, V>>> map = new HashMap<K1, HashMap<K2, HashMap<K3, V>>>();

    /**
 * Existence check of a value (or <tt>null</tt>) mapped to the keys.
 * 
 * @param firstKey
 *            first key
 * @param secondKey
 *            second key
 * @return true when an element (or <tt>null</tt>) has been stored with the keys
 */
    public boolean containsKey(final K1 firstKey, final K2 secondKey) {
        final HashMap<K2, HashMap<K3, V>> innerMap1 = map.get(firstKey);
        if (innerMap1 == null) {
            return false;
        }
        return innerMap1.containsKey(secondKey);
    }

    /**
 * Existence check of a value (or <tt>null</tt>) mapped to the keys.
 * 
 * @param firstKey
 *            first key
 * @param secondKey
 *            second key
 * @param thirdKey
 *            third key
 * @return true when an element (or <tt>null</tt>) has been stored with the keys
 */
    public boolean containsKey(final K1 firstKey, final K2 secondKey, final K3 thirdKey) {
        final HashMap<K2, HashMap<K3, V>> innerMap1 = map.get(firstKey);
        if (innerMap1 == null) {
            return false;
        }
        final HashMap<K3, V> innerMap2 = innerMap1.get(secondKey);
        if (innerMap2 == null) {
            return false;
        }
        return innerMap2.containsKey(thirdKey);
    }

    /**
 * Fetch the outermost Hashmap .
 * 
 * @param firstKey
 *            first key
 * @return the the innermost hashmap
 */
    public HashMap<K2, HashMap<K3, V>> get(final K1 firstKey) {
        return map.get(firstKey);
    }

    /**
 * Fetch the outermost Hashmap as a TwoDHashMap .
 * 
 * @param firstKey
 *            first key
 * @return the the innermost hashmap
 */
    public TwoDHashMap<K2, K3, V> getAs2d(final K1 firstKey) {
        return new TwoDHashMap<K2, K3, V>(map.get(firstKey));
    }

    /**
 * Fetch the innermost Hashmap .
 * 
 * @param firstKey
 *            first key
 * @param secondKey
 *            second key
 * @return the the innermost hashmap
 */
    public HashMap<K3, V> get(final K1 firstKey, final K2 secondKey) {
        final HashMap<K2, HashMap<K3, V>> innerMap1 = map.get(firstKey);
        if (innerMap1 == null) {
            return null;
        }
        return innerMap1.get(secondKey);
    }

    /**
 * Fetch a value from the Hashmap .
 * 
 * @param firstKey
 *            first key
 * @param secondKey
 *            second key
 * @param thirdKey
 *            third key
 * @return the element or null.
 */
    public V get(final K1 firstKey, final K2 secondKey, final K3 thirdKey) {
        final HashMap<K2, HashMap<K3, V>> innerMap1 = map.get(firstKey);
        if (innerMap1 == null) {
            return null;
        }
        final HashMap<K3, V> innerMap2 = innerMap1.get(secondKey);
        if (innerMap2 == null) {
            return null;
        }
        return innerMap2.get(thirdKey);
    }

    /**
 * Insert a value
 * 
 * @param firstKey
 *            first key
 * @param secondKey
 *            second key
 * @param thirdKey
 *            third key
 * @param value
 *            the value to be inserted. <tt>null</tt> may be inserted as well.
 * @return null or the value the insert is replacing.
 */
    public Object set(final K1 firstKey, final K2 secondKey, final K3 thirdKey, final V value) {
        HashMap<K2, HashMap<K3, V>> innerMap1 = map.get(firstKey);
        if (innerMap1 == null) {
            innerMap1 = new HashMap<K2, HashMap<K3, V>>();
            map.put(firstKey, innerMap1);
        }
        HashMap<K3, V> innerMap2 = innerMap1.get(secondKey);
        if (innerMap2 == null) {
            innerMap2 = new HashMap<K3, V>();
            innerMap1.put(secondKey, innerMap2);
        }
        return innerMap2.put(thirdKey, value);
    }

    /**
 * Returns the number of key-value mappings in this map for the first key.
 * 
 * @return Returns the number of key-value mappings in this map for the first key.
 */
    public int size() {
        return map.size();
    }

    /**
 * Returns the number of key-value mappings in this map for the second key.
 * 
 * @return Returns the number of key-value mappings in this map for the second key.
 */
    public int size(final K1 firstKey) {
        final HashMap<K2, HashMap<K3, V>> innerMap = map.get(firstKey);
        if (innerMap == null) {
            return 0;
        }
        return innerMap.size();
    }

    /**
 * Returns the number of key-value mappings in this map for the third key.
 * 
 * @return Returns the number of key-value mappings in this map for the third key.
 */
    public int size(final K1 firstKey, final K2 secondKey) {
        final HashMap<K2, HashMap<K3, V>> innerMap1 = map.get(firstKey);
        if (innerMap1 == null) {
            return 0;
        }
        final HashMap<K3, V> innerMap2 = innerMap1.get(secondKey);
        if (innerMap2 == null) {
            return 0;
        }
        return innerMap2.size();
    }

    /**
 * Returns a set of the keys of the outermost map.
 */
    public Set<K1> keySet() {
        return map.keySet();
    }
}
