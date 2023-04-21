package gnu.trove.decorator;

import gnu.trove.TIntFloatHashMap;
import gnu.trove.TIntFloatIterator;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;

/**
 * Wrapper class to make a TIntFloatHashMap conform to the <tt>java.util.Map</tt> API.
 * This class simply decorates an underlying TIntFloatHashMap and translates the Object-based
 * APIs into their Trove primitive analogs.
 * <p/>
 * <p/>
 * Note that wrapping and unwrapping primitive values is extremely inefficient.  If
 * possible, users of this class should override the appropriate methods in this class
 * and use a table of canonical values.
 * </p>
 * <p/>
 * Created: Mon Sep 23 22:07:40 PDT 2002
 *
 * @author Eric D. Friedman
 * @author Rob Eden
 */
public class TIntFloatHashMapDecorator extends AbstractMap<Integer, Float> implements Map<Integer, Float>, Cloneable {

    /** the wrapped primitive map */
    protected TIntFloatHashMap _map;

    /**
     * Creates a wrapper that decorates the specified primitive map.
     */
    public TIntFloatHashMapDecorator(TIntFloatHashMap map) {
        super();
        this._map = map;
    }

    /**
     * Returns a reference to the map wrapped by this decorator.
     */
    public TIntFloatHashMap getMap() {
        return _map;
    }

    /**
     * Clones the underlying trove collection and returns the clone wrapped in a new
     * decorator instance.  This is a shallow clone except where primitives are
     * concerned.
     *
     * @return a copy of the receiver
     */
    public TIntFloatHashMapDecorator clone() {
        try {
            TIntFloatHashMapDecorator copy = (TIntFloatHashMapDecorator) super.clone();
            copy._map = (TIntFloatHashMap) _map.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    /**
     * Inserts a key/value pair into the map.
     *
     * @param key   an <code>Object</code> value
     * @param value an <code>Object</code> value
     * @return the previous value associated with <tt>key</tt>,
     *         or Float(0) if none was found.
     */
    public Float put(Integer key, Float value) {
        return wrapValue(_map.put(unwrapKey(key), unwrapValue(value)));
    }

    /**
     * Compares this map with another map for equality of their stored
     * entries.
     *
     * @param other an <code>Object</code> value
     * @return true if the maps are identical
     */
    public boolean equals(Object other) {
        if (_map.equals(other)) {
            return true;
        } else if (other instanceof Map) {
            Map that = (Map) other;
            if (that.size() != _map.size()) {
                return false;
            } else {
                Iterator it = that.entrySet().iterator();
                for (int i = that.size(); i-- > 0; ) {
                    Map.Entry e = (Map.Entry) it.next();
                    Object key = e.getKey();
                    Object val = e.getValue();
                    if (key instanceof Integer && val instanceof Float) {
                        int k = unwrapKey(key);
                        float v = unwrapValue(val);
                        if (_map.containsKey(k) && v == _map.get(k)) {
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Retrieves the value for <tt>key</tt>
     *
     * @param key an <code>Object</code> value
     * @return the value of <tt>key</tt> or null if no such mapping exists.
     */
    public Float get(Integer key) {
        int k = unwrapKey(key);
        float v = _map.get(k);
        if (v == 0) {
            return _map.containsKey(k) ? wrapValue(v) : null;
        } else {
            return wrapValue(v);
        }
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
     * @return the removed value, or Float(0) if it was not found in the map
     */
    public Float remove(Integer key) {
        return wrapValue(_map.remove(unwrapKey(key)));
    }

    /**
     * Returns a Set view on the entries of the map.
     *
     * @return a <code>Set</code> value
     */
    public Set<Map.Entry<Integer, Float>> entrySet() {
        return new AbstractSet<Map.Entry<Integer, Float>>() {

            public int size() {
                return _map.size();
            }

            public boolean isEmpty() {
                return TIntFloatHashMapDecorator.this.isEmpty();
            }

            public boolean contains(Object o) {
                if (o instanceof Map.Entry) {
                    Object k = ((Map.Entry) o).getKey();
                    Object v = ((Map.Entry) o).getValue();
                    return TIntFloatHashMapDecorator.this.containsKey(k) && TIntFloatHashMapDecorator.this.get(k).equals(v);
                } else {
                    return false;
                }
            }

            public Iterator<Map.Entry<Integer, Float>> iterator() {
                return new Iterator<Map.Entry<Integer, Float>>() {

                    private final TIntFloatIterator it = _map.iterator();

                    public Map.Entry<Integer, Float> next() {
                        it.advance();
                        final Integer key = wrapKey(it.key());
                        final Float v = wrapValue(it.value());
                        return new Map.Entry<Integer, Float>() {

                            private Float val = v;

                            public boolean equals(Object o) {
                                return o instanceof Map.Entry && ((Map.Entry) o).getKey().equals(key) && ((Map.Entry) o).getValue().equals(val);
                            }

                            public Integer getKey() {
                                return key;
                            }

                            public Float getValue() {
                                return val;
                            }

                            public int hashCode() {
                                return key.hashCode() + val.hashCode();
                            }

                            public Float setValue(Float value) {
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

            public boolean add(Float o) {
                throw new UnsupportedOperationException();
            }

            public boolean remove(Object o) {
                throw new UnsupportedOperationException();
            }

            public boolean addAll(Collection<? extends Map.Entry<Integer, Float>> c) {
                throw new UnsupportedOperationException();
            }

            public boolean retainAll(Collection<?> c) {
                throw new UnsupportedOperationException();
            }

            public boolean removeAll(Collection<?> c) {
                throw new UnsupportedOperationException();
            }

            public void clear() {
                TIntFloatHashMapDecorator.this.clear();
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
        return _map.containsValue(unwrapValue(val));
    }

    /**
     * Checks for the present of <tt>key</tt> in the keys of the map.
     *
     * @param key an <code>Object</code> value
     * @return a <code>boolean</code> value
     */
    public boolean containsKey(Object key) {
        return _map.containsKey(unwrapKey(key));
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
    public void putAll(Map<? extends Integer, ? extends Float> map) {
        Iterator<? extends Entry<? extends Integer, ? extends Float>> it = map.entrySet().iterator();
        for (int i = map.size(); i-- > 0; ) {
            Entry<? extends Integer, ? extends Float> e = it.next();
            this.put(e.getKey(), e.getValue());
        }
    }

    /**
     * Wraps a key
     *
     * @param k key in the underlying map
     * @return an Object representation of the key
     */
    protected Integer wrapKey(int k) {
        return new Integer(k);
    }

    /**
     * Unwraps a key
     *
     * @param key wrapped key
     * @return an unwrapped representation of the key
     */
    protected int unwrapKey(Object key) {
        return ((Integer) key).intValue();
    }

    /**
     * Wraps a value
     *
     * @param k value in the underlying map
     * @return an Object representation of the value
     */
    protected Float wrapValue(float k) {
        return new Float(k);
    }

    /**
     * Unwraps a value
     *
     * @param value wrapped value
     * @return an unwrapped representation of the value
     */
    protected float unwrapValue(Object value) {
        return ((Float) value).floatValue();
    }
}
