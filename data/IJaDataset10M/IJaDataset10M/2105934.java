package com.io_software.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/** a {@link Map} implementation that remembers the order of insertion, so that
    a key set iterator and an element set iterator can be requested that
    iterate in the insertion order.

    @author Axel Uhl
    @version $Id: ListMap.java,v 1.1 2001/02/20 12:13:41 aul Exp $
*/
public class ListMap extends HashMap {

    /** Constructs the list that keeps track of insertion order

        @param s the initial size
    */
    private void init(int s) {
        keyList = new ArrayList(s);
        valueList = new ArrayList(s);
    }

    /**
     * Constructs a new, empty map with the specified initial 
     * capacity and the specified load factor. 
     *
     * @param      initialCapacity   the initial capacity of the ListMap.
     * @param      loadFactor        the load factor of the ListMap
     * @throws     IllegalArgumentException  if the initial capacity is less
     *               than zero, or if the load factor is nonpositive.
     */
    public ListMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        init(initialCapacity);
    }

    /**
     * Constructs a new, empty map with the specified initial capacity
     * and default load factor, which is <tt>0.75</tt>.
     *
     * @param   initialCapacity   the initial capacity of the ListMap.
     * @throws    IllegalArgumentException if the initial capacity is less
     *              than zero.
     */
    public ListMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    /**
     * Constructs a new, empty map with a default capacity and load
     * factor, which is <tt>0.75</tt>.
     */
    public ListMap() {
        this(11, 0.75f);
    }

    /**
     * Constructs a new map with the same mappings as the given map.  The
     * map is created with a capacity of twice the number of mappings in
     * the given map or 11 (whichever is greater), and a default load factor,
     * which is <tt>0.75</tt>.
     *
     * @param t the map whose mappings are to be placed in this map.
     */
    public ListMap(Map t) {
        this(Math.max(2 * t.size(), 11), 0.75f);
        putAll(t);
    }

    /** adds the specified key/value pair to this map. If and entry with an
	equal key already exists, the value is replaced, and the key/value pair
	is removed from both key/value list and reappended at their end.

	@param k the key
	@param v the value
    */
    public Object put(Object key, Object value) {
        Object result = super.put(key, value);
        if (result != null) {
            keyList.remove(result);
            valueList.remove(value);
        }
        keyList.add(key);
        valueList.add(value);
        return result;
    }

    /** removes the key/value pair with the given key. This will also remove
	this pair from the insertion order lists

	@param key the key for which to remove key/value
	@return the key that was removed or <tt>null</tt> if nothing was
	removed
    */
    public Object remove(Object key) {
        if (containsKey(key)) {
            Object value = get(key);
            keyList.remove(key);
            valueList.remove(value);
        }
        return super.remove(key);
    }

    /** returns an iterator over the key set that iterates over the elements in
	insertion order
    */
    public Iterator keyIteratorInInsertionOrder() {
        return keyList.iterator();
    }

    /** returns an iterator over the key set that iterates over the elements in
	insertion order
    */
    public Iterator iteratorInInsertionOrder() {
        return valueList.iterator();
    }

    /** holds the additional {@link List} that keeps track of insertion order
	of the keys
    */
    private List keyList;

    /** holds the additional {@link List} that keeps track of insertion order
	of the values
    */
    private List valueList;
}
