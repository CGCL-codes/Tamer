package com.volantis.shared.metadata.impl;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of a {@link Map} which restricts objects put into it to be of a
 * particular type which is declared during the construction of this {@link Map}.
 */
public class TypedMap extends AbstractMap implements Serializable {

    /**
     * The Serial Version UID.
     */
    static final long serialVersionUID = 4967363497817901425L;

    private static final Class allowableKeyClass = String.class;

    /**
     * The map which this object wraps.
     */
    private final Map map;

    /**
     * The allowable <code>Class</code> of objects which can be put into this
     * <code>Set</code>.
     */
    private Class allowableValueClass;

    /**
     * The name of the allowable value class (for persistence only)
     */
    private final String allowableValueClassName;

    /**
     * Indicates whether null values are allowed.
     */
    private final boolean allowNullValue;

    /**
     * Constructor which takes a <code>Map</code> and wraps it to restrict the keys and
     * values of this map to be of the specified type.
     * @param map The <code>Map</code> which this class wraps.
     * @param allowableValueClass The allowable <code>Class</code> of objects which can be
     *        used as values in this <code>Map</code>.
     */
    public TypedMap(Map map, Class allowableValueClass, boolean allowNullValue) {
        this.map = map;
        this.allowableValueClass = allowableValueClass;
        this.allowableValueClassName = allowableValueClass.getName();
        this.allowNullValue = allowNullValue;
    }

    /**
     * This method should be the only mechanism used to access the
     * allowableKeyClass.
     *
     * @return the allowable key class
     */
    private Class getAllowableKeyClass() {
        return allowableKeyClass;
    }

    /**
     * This method should be the only mechanism used to access the
     * allowableValueClass.
     *
     * @return the allowable value class
     */
    private Class getAllowableValueClass() {
        synchronized (this) {
            if (null == allowableValueClass) {
                try {
                    allowableValueClass = Class.forName(allowableValueClassName);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return allowableValueClass;
    }

    /**
     * Returns a set view of the mappings contained in this map.  Each element
     * in this set is a a sub class of Map.Entry.  The set is backed by the map, so changes
     * to the map are reflected in the set, and vice-versa.  (If the map is
     * modified while an iteration over the set is in progress, the results of
     * the iteration are undefined.)  The set supports element removal, which
     * removes the corresponding entry from the map, via the
     * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt>,
     * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not support
     * the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * The Map.Entry which is returned is wrapped to ensure that no forbidden values can
     * be assigned to entries in the entry set via {@link Map.Entry#setValue}. Attempting
     * to do so will throw an IllegalArgumentException.
     *
     * @return a set view of the mappings contained in this map.
     */
    public Set entrySet() {
        Set entrySet = new EntrySet(map.entrySet());
        return entrySet;
    }

    /**
     * Associates the specified value with the specified key in this map
     * (optional operation).  If the map previously contained a mapping for
     * this key, the old value is replaced by the specified value.  (A map
     * <tt>m</tt> is said to contain a mapping for a key <tt>k</tt> if and only
     * if {@link #containsKey(Object) m.containsKey(k)} would return
     * <tt>true</tt>.))
     *
     * @param key key with which the specified value is to be associated.
     * @param value value to be associated with the specified key.
     * @return previous value associated with specified key, or <tt>null</tt>
     *	       if there was no mapping for key.  A <tt>null</tt> return can
     *	       also indicate that the map previously associated <tt>null</tt>
     *	       with the specified key, if the implementation supports
     *	       <tt>null</tt> values.
     *
     * @throws UnsupportedOperationException if the <tt>put</tt> operation is
     *	          not supported by this map.
     * @throws ClassCastException if the class of the specified key or value
     * 	          prevents it from being stored in this map.
     * @throws IllegalArgumentException if some aspect of this key or value
     *	          prevents it from being stored in this map.
     * @throws NullPointerException this map does not permit <tt>null</tt>
     *            keys or values, and the specified key or value is
     *            <tt>null</tt>.
     * @throws IllegalArgumentException if the provided key is not of the allowed type.
     * @throws IllegalArgumentException if the provided value is not of the allowed type.
     */
    public Object put(Object key, Object value) {
        return map.put(checkKey(key), checkValue(value));
    }

    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the
     * specified value.  More formally, returns <tt>true</tt> if and only if
     * this map contains at least one mapping to a value <tt>v</tt> such that
     * <tt>(value==null ? v==null : value.equals(v))</tt>.  This operation
     * will probably require time linear in the map size for most
     * implementations of the <tt>Map</tt> interface.
     *
     * @param value value whose presence in this map is to be tested.
     * @return <tt>true</tt> if this map maps one or more keys to the
     *         specified value.
     * @throws ClassCastException if the value is of an inappropriate type for
     * 		  this map (optional).
     * @throws NullPointerException if the value is <tt>null</tt> and this map
     *            does not not permit <tt>null</tt> values (optional).
     * @throws IllegalArgumentException if the provided value is not of the allowed type.
     */
    public boolean containsValue(Object value) {
        return map.containsValue(checkValue(value));
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified
     * key.  More formally, returns <tt>true</tt> if and only if
     * this map contains at a mapping for a key <tt>k</tt> such that
     * <tt>(key==null ? k==null : key.equals(k))</tt>.  (There can be
     * at most one such mapping.)
     *
     * @param key key whose presence in this map is to be tested.
     * @return <tt>true</tt> if this map contains a mapping for the specified
     *         key.
     *
     * @throws ClassCastException if the key is of an inappropriate type for
     * 		  this map (optional).
     * @throws NullPointerException if the key is <tt>null</tt> and this map
     *            does not not permit <tt>null</tt> keys (optional).
     * @throws IllegalArgumentException if the provided key is not of the allowed type.
     */
    public boolean containsKey(Object key) {
        return map.containsKey(checkKey(key));
    }

    /**
     * Returns the value to which this map maps the specified key.  Returns
     * <tt>null</tt> if the map contains no mapping for this key.  A return
     * value of <tt>null</tt> does not <i>necessarily</i> indicate that the
     * map contains no mapping for the key; it's also possible that the map
     * explicitly maps the key to <tt>null</tt>.  The <tt>containsKey</tt>
     * operation may be used to distinguish these two cases.
     *
     * <p>More formally, if this map contains a mapping from a key
     * <tt>k</tt> to a value <tt>v</tt> such that <tt>(key==null ? k==null :
     * key.equals(k))</tt>, then this method returns <tt>v</tt>; otherwise
     * it returns <tt>null</tt>.  (There can be at most one such mapping.)
     *
     * @param key key whose associated value is to be returned.
     * @return the value to which this map maps the specified key, or
     *	       <tt>null</tt> if the map contains no mapping for this key.
     *
     * @throws ClassCastException if the key is of an inappropriate type for
     * 		  this map (optional).
     * @throws NullPointerException key is <tt>null</tt> and this map does not
     *		  not permit <tt>null</tt> keys (optional).
     * @throws IllegalArgumentException if the provided key is not of the allowed type.
     *
     * @see #containsKey(Object)
     */
    public Object get(Object key) {
        return map.get(checkKey(key));
    }

    /**
     * Removes the mapping for this key from this map if it is present
     * (optional operation).   More formally, if this map contains a mapping
     * from key <tt>k</tt> to value <tt>v</tt> such that
     * <code>(key==null ?  k==null : key.equals(k))</code>, that mapping
     * is removed.  (The map can contain at most one such mapping.)
     *
     * <p>Returns the value to which the map previously associated the key, or
     * <tt>null</tt> if the map contained no mapping for this key.  (A
     * <tt>null</tt> return can also indicate that the map previously
     * associated <tt>null</tt> with the specified key if the implementation
     * supports <tt>null</tt> values.)  The map will not contain a mapping for
     * the specified  key once the call returns.
     *
     * @param key key whose mapping is to be removed from the map.
     * @return previous value associated with specified key, or <tt>null</tt>
     *	       if there was no mapping for key.
     *
     * @throws ClassCastException if the key is of an inappropriate type for
     * 		  this map (optional).
     * @throws NullPointerException if the key is <tt>null</tt> and this map
     *            does not not permit <tt>null</tt> keys (optional).
     * @throws UnsupportedOperationException if the <tt>remove</tt> method is
     *         not supported by this map.
     * @throws IllegalArgumentException if the provided key is not of the allowed type.
     */
    public Object remove(Object key) {
        return map.remove(checkKey(key));
    }

    /**
     * Helper method which ensures that an object is of the allowable class.
     * @param key The object which is being tested.
     * @return the element if the specified object is allowed in this list.
     */
    protected Object checkKey(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("Null keys not allowed");
        } else if (!getAllowableKeyClass().isInstance(key)) {
            throw new IllegalArgumentException("key: " + key + " is Not " + getAllowableKeyClass().getName());
        }
        return key;
    }

    /**
     * Helper method which ensures that an object is of the allowable class.
     * @param value The object which is being tested.
     * @return the element if the specified object is allowed in this list.
     */
    protected Object checkValue(Object value) {
        if (value == null) {
            if (!allowNullValue) {
                throw new IllegalArgumentException("Null keys not allowed");
            }
        } else if (!getAllowableValueClass().isInstance(value)) {
            throw new IllegalArgumentException("value: " + value + " is Not " + getAllowableValueClass().getName());
        }
        return value;
    }

    /**
     * We use this so that we can ensure that the value being set is permitted.
     */
    private class TypedMapEntry implements Map.Entry {

        /**
         * The Map.Entry entry we are wrapping as a TypedMapEntry.
         */
        private Map.Entry mapEntry;

        /**
         * Constructor which wraps the provided Map.Entry as a TypedMapEntry.
         * @param mapEntry The Map.Entry to wrap.
         */
        public TypedMapEntry(Map.Entry mapEntry) {
            this.mapEntry = mapEntry;
        }

        public Object getKey() {
            return mapEntry.getKey();
        }

        public Object getValue() {
            return mapEntry.getValue();
        }

        /**
         * Override setValue to ensure that the value provided is of the accepted type.
         * @param value new value to be stored in this entry.
         * @return old value corresponding to the entry.
         */
        public Object setValue(Object value) {
            return mapEntry.setValue(checkValue(value));
        }
    }

    /**
     * Private inner class which which represents the EntrySet of this Map obtained by
     * {@link java.util.Map#entrySet()}, and ensures that the Map can not be
     * modified in an illegal way via this set.
     */
    private class EntrySet extends AbstractSet {

        /**
         * The backing set of this EntrySet
         */
        Set backingSet;

        /**
         * Constructor which takes a backing set and wraps it.
         * @param backingSet The backing set
         */
        EntrySet(Set backingSet) {
            this.backingSet = backingSet;
        }

        /**
         * This <code>Iterator</code> returned by this method ensures that returned
         * {@link Map.Entry} objects are wrapped as {@link TypedMapEntry}s. This ensures
         * that they can not be modified to contain values of a forbidden type.
         * @return a type safe iterator for this map's <code>EntrySet</code>.
         */
        public Iterator iterator() {
            return new Iterator() {

                Iterator i = backingSet.iterator();

                public boolean hasNext() {
                    return i.hasNext();
                }

                public Object next() {
                    return new TypedMapEntry((Map.Entry) i.next());
                }

                public void remove() {
                    i.remove();
                }
            };
        }

        public int size() {
            return backingSet.size();
        }

        public boolean remove(Object o) {
            return backingSet.remove(o);
        }

        public boolean contains(Object o) {
            return backingSet.contains(checkValue(o));
        }
    }
}
