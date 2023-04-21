package prisms.util;

import java.util.Iterator;
import java.util.Map;

/**
 * An ArrayMap is a very inefficient map type that is more robust in dealing with changes to its
 * keys than other maps. HashMaps and TreeMaps may "lose" the reference to a value if the key to
 * that value changes in a way that causes it to map or compare differently. ArrayMap has no such
 * limitations, but it pays for this feature in its speed with large data sets. Searching and
 * insertion are both O(n). Another bonus feature is that ArrayMap relies only on the equals
 * method--it requires no contract with the hashCode() and no comparators.
 * 
 * @param <K> The key type for this map
 * @param <V> The value type for this map
 */
public class ArrayMap<K, V> implements Map<K, V> {

    private Object[] theKeys;

    private Object[] theValues;

    /** Creates an ArrayMap */
    public ArrayMap() {
        theKeys = new Object[0];
        theValues = new Object[0];
    }

    public int size() {
        return theKeys.length;
    }

    public boolean isEmpty() {
        return theKeys.length == 0;
    }

    public V get(Object key) {
        for (int i = 0; i < theKeys.length; i++) if (equal(key, theKeys[i])) return (V) theValues[i];
        return null;
    }

    private static boolean equal(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    public boolean containsKey(Object key) {
        for (int i = 0; i < theKeys.length; i++) if (equal(key, theKeys[i])) return true;
        return false;
    }

    public boolean containsValue(Object value) {
        for (int i = 0; i < theValues.length; i++) if (equal(value, theValues[i])) return true;
        return false;
    }

    public V put(K key, V value) {
        for (int i = 0; i < theKeys.length; i++) {
            if (equal(key, theKeys[i])) {
                V old = (V) theValues[i];
                theKeys[i] = key;
                theValues[i] = value;
                return old;
            }
        }
        Object[] newKeys = ArrayUtils.add(theKeys, key);
        Object[] newVals = ArrayUtils.add(theValues, value);
        theKeys = newKeys;
        theValues = newVals;
        return null;
    }

    public void putAll(Map<? extends K, ? extends V> t) {
        for (Map.Entry<? extends K, ? extends V> entry : t.entrySet()) put(entry.getKey(), entry.getValue());
    }

    public V remove(Object key) {
        for (int i = 0; i < theKeys.length; i++) {
            if (equal(theKeys[i], key)) {
                V old = (V) theValues[i];
                Object[] newKeys = ArrayUtils.remove(theKeys, i);
                Object[] newVals = ArrayUtils.remove(theValues, i);
                theKeys = newKeys;
                theValues = newVals;
                return old;
            }
        }
        return null;
    }

    public void clear() {
        theKeys = new Object[0];
        theValues = new Object[0];
    }

    public java.util.Set<K> keySet() {
        final Object[] iterKeys = theKeys;
        return new java.util.AbstractSet<K>() {

            @Override
            public int size() {
                return iterKeys.length;
            }

            @Override
            public Iterator<K> iterator() {
                return new Iterator<K>() {

                    private int index = 0;

                    public boolean hasNext() {
                        return index < iterKeys.length;
                    }

                    public K next() {
                        if (index >= iterKeys.length) throw new java.util.NoSuchElementException();
                        K ret = (K) iterKeys[index];
                        index++;
                        return ret;
                    }

                    public void remove() {
                        ArrayMap.this.remove(iterKeys[index - 1]);
                    }
                };
            }
        };
    }

    public java.util.Collection<V> values() {
        final Object[] iterKeys = theKeys;
        final Object[] iterVals = theValues;
        return new java.util.AbstractSet<V>() {

            @Override
            public int size() {
                return iterVals.length;
            }

            @Override
            public Iterator<V> iterator() {
                return new Iterator<V>() {

                    private int index = 0;

                    public boolean hasNext() {
                        return index < iterVals.length;
                    }

                    public V next() {
                        if (index >= iterVals.length) throw new java.util.NoSuchElementException();
                        V ret = (V) iterVals[index];
                        index++;
                        return ret;
                    }

                    public void remove() {
                        ArrayMap.this.remove(iterKeys[index - 1]);
                    }
                };
            }
        };
    }

    public java.util.Set<Map.Entry<K, V>> entrySet() {
        final Object[] iterKeys = theKeys;
        final Object[] iterVals = theValues;
        return new java.util.AbstractSet<Map.Entry<K, V>>() {

            @Override
            public int size() {
                return iterVals.length;
            }

            @Override
            public Iterator<Map.Entry<K, V>> iterator() {
                return new Iterator<Map.Entry<K, V>>() {

                    private int index = 0;

                    public boolean hasNext() {
                        return index < iterVals.length;
                    }

                    public Map.Entry<K, V> next() {
                        if (index >= iterKeys.length) throw new java.util.NoSuchElementException();
                        final K entryKey = (K) iterKeys[index];
                        final V[] entryVal = (V[]) new Object[] { iterVals[index] };
                        Map.Entry<K, V> ret = new Map.Entry<K, V>() {

                            public K getKey() {
                                return entryKey;
                            }

                            public V getValue() {
                                return entryVal[0];
                            }

                            public V setValue(V value) {
                                ArrayMap.this.put(entryKey, value);
                                V ret2 = entryVal[0];
                                entryVal[0] = value;
                                return ret2;
                            }
                        };
                        index++;
                        return ret;
                    }

                    public void remove() {
                        ArrayMap.this.remove(iterKeys[index - 1]);
                    }
                };
            }
        };
    }
}
