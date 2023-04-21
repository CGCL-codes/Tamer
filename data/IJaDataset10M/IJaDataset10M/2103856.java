package gnu.trove.decorator;

import gnu.trove.map.TCharObjectMap;
import gnu.trove.iterator.TCharObjectIterator;
import java.io.*;
import java.util.*;

/**
 * Wrapper class to make a TCharObjectMap conform to the <tt>java.util.Map</tt> API.
 * This class simply decorates an underlying TCharObjectMap and translates the Object-based
 * APIs into their Trove primitive analogs.
 * <p/>
 * Note that wrapping and unwrapping primitive values is extremely inefficient.  If
 * possible, users of this class should override the appropriate methods in this class
 * and use a table of canonical values.
 * <p/>
 * Created: Mon Sep 23 22:07:40 PDT 2002
 *
 * @author Eric D. Friedman
 * @author Robert D. Eden
 * @author Jeff Randall
 */
public class TCharObjectMapDecorator<V> extends AbstractMap<Character, V> implements Map<Character, V>, Externalizable, Cloneable {

    static final long serialVersionUID = 1L;

    /** the wrapped primitive map */
    protected TCharObjectMap<V> _map;

    /**
     * FOR EXTERNALIZATION ONLY!!
     */
    public TCharObjectMapDecorator() {
    }

    /**
     * Creates a wrapper that decorates the specified primitive map.
     *
     * @param map the <tt>TCharObjectMap</tt> to wrap.
     */
    public TCharObjectMapDecorator(TCharObjectMap<V> map) {
        super();
        this._map = map;
    }

    /**
     * Returns a reference to the map wrapped by this decorator.
     *
     * @return the wrapped <tt>TCharObjectMap</tt> instance.
     */
    public TCharObjectMap<V> getMap() {
        return _map;
    }

    /**
     * Inserts a key/value pair into the map.
     *
     * @param key   an <code>Character</code> value
     * @param value an <code>Object</code> value
     * @return the previous value associated with <tt>key</tt>,
     *         or <tt>null</tt> if none was found.
     */
    public V put(Character key, V value) {
        char k;
        if (key == null) {
            k = _map.getNoEntryKey();
        } else {
            k = unwrapKey(key);
        }
        return _map.put(k, value);
    }

    /**
     * Retrieves the value for <tt>key</tt>
     *
     * @param key an <code>Object</code> value
     * @return the value of <tt>key</tt> or null if no such mapping exists.
     */
    public V get(Object key) {
        char k;
        if (key != null) {
            if (key instanceof Character) {
                k = unwrapKey((Character) key);
            } else {
                return null;
            }
        } else {
            k = _map.getNoEntryKey();
        }
        return _map.get(k);
    }

    /**
     * Empties the map.
     */
    public void clear() {
        this._map.clear();
    }

    /**
     * Deletes a key/value pair from the map.
     *
     * @param key an <code>Object</code> value
     * @return the removed value, or Integer(0) if it was not found in the map
     */
    public V remove(Object key) {
        char k;
        if (key != null) {
            if (key instanceof Character) {
                k = unwrapKey((Character) key);
            } else {
                return null;
            }
        } else {
            k = _map.getNoEntryKey();
        }
        return _map.remove(k);
    }

    /**
     * Returns a Set view on the entries of the map.
     *
     * @return a <code>Set</code> value
     */
    public Set<Map.Entry<Character, V>> entrySet() {
        return new AbstractSet<Map.Entry<Character, V>>() {

            public int size() {
                return _map.size();
            }

            public boolean isEmpty() {
                return TCharObjectMapDecorator.this.isEmpty();
            }

            public boolean contains(Object o) {
                if (o instanceof Map.Entry) {
                    Object k = ((Map.Entry) o).getKey();
                    Object v = ((Map.Entry) o).getValue();
                    return TCharObjectMapDecorator.this.containsKey(k) && TCharObjectMapDecorator.this.get(k).equals(v);
                } else {
                    return false;
                }
            }

            public Iterator<Map.Entry<Character, V>> iterator() {
                return new Iterator<Map.Entry<Character, V>>() {

                    private final TCharObjectIterator<V> it = _map.iterator();

                    public Map.Entry<Character, V> next() {
                        it.advance();
                        final Character key = wrapKey(it.key());
                        final V v = it.value();
                        return new Map.Entry<Character, V>() {

                            private V val = v;

                            public boolean equals(Object o) {
                                return o instanceof Map.Entry && ((Map.Entry) o).getKey().equals(key) && ((Map.Entry) o).getValue().equals(val);
                            }

                            public Character getKey() {
                                return key;
                            }

                            public V getValue() {
                                return val;
                            }

                            public int hashCode() {
                                return key.hashCode() + val.hashCode();
                            }

                            public V setValue(V value) {
                                val = value;
                                return put(key, value);
                            }
                        };
                    }

                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    public void remove() {
                        it.remove();
                    }
                };
            }

            public boolean add(Map.Entry<Character, V> o) {
                throw new UnsupportedOperationException();
            }

            public boolean remove(Object o) {
                boolean modified = false;
                if (contains(o)) {
                    Character key = ((Map.Entry<Character, V>) o).getKey();
                    _map.remove(unwrapKey(key));
                    modified = true;
                }
                return modified;
            }

            public boolean addAll(Collection<? extends Map.Entry<Character, V>> c) {
                throw new UnsupportedOperationException();
            }

            public void clear() {
                TCharObjectMapDecorator.this.clear();
            }
        };
    }

    /**
     * Checks for the presence of <tt>val</tt> in the values of the map.
     *
     * @param val an <code>Object</code> value
     * @return a <code>boolean</code> value
     */
    public boolean containsValue(Object val) {
        return _map.containsValue(val);
    }

    /**
     * Checks for the present of <tt>key</tt> in the keys of the map.
     *
     * @param key an <code>Object</code> value
     * @return a <code>boolean</code> value
     */
    public boolean containsKey(Object key) {
        if (key == null) return _map.containsKey(_map.getNoEntryKey());
        return key instanceof Character && _map.containsKey(((Character) key).charValue());
    }

    /**
     * Returns the number of entries in the map.
     *
     * @return the map's size.
     */
    public int size() {
        return this._map.size();
    }

    /**
     * Indicates whether map has any entries.
     *
     * @return true if the map is empty
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Copies the key/value mappings in <tt>map</tt> into this map.
     * Note that this will be a <b>deep</b> copy, as storage is by
     * primitive value.
     *
     * @param map a <code>Map</code> value
     */
    public void putAll(Map<? extends Character, ? extends V> map) {
        Iterator<? extends Entry<? extends Character, ? extends V>> it = map.entrySet().iterator();
        for (int i = map.size(); i-- > 0; ) {
            Entry<? extends Character, ? extends V> e = it.next();
            this.put(e.getKey(), e.getValue());
        }
    }

    /**
     * Wraps a key
     *
     * @param k key in the underlying map
     * @return an Object representation of the key
     */
    protected Character wrapKey(char k) {
        return Character.valueOf(k);
    }

    /**
     * Unwraps a key
     *
     * @param key wrapped key
     * @return an unwrapped representation of the key
     */
    protected char unwrapKey(Character key) {
        return key.charValue();
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        _map = (TCharObjectMap<V>) in.readObject();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeObject(_map);
    }
}
