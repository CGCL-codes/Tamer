package com.aptana.ide.internal.core.resources;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class MarkerAttributeMap implements Map {

    /**
	 * elements
	 */
    protected Object[] elements = null;

    /**
	 * count
	 */
    protected int count = 0;

    /**
	 * DEFAULT_SIZE
	 */
    protected static final int DEFAULT_SIZE = 16;

    /**
	 * GROW_SIZE
	 */
    protected static final int GROW_SIZE = 10;

    /**
	 * Creates a new marker attribute map of default size
	 */
    public MarkerAttributeMap() {
        super();
    }

    /**
	 * Creates a new marker attribute map.
	 * 
	 * @param initialCapacity
	 *            The initial number of elements that will fit in the map.
	 */
    public MarkerAttributeMap(int initialCapacity) {
        elements = new Object[Math.max(initialCapacity * 2, 0)];
    }

    /**
	 * Creates a new marker attribute map of default size
	 * 
	 * @param map
	 *            The entries in the given map will be added to the new map.
	 */
    public MarkerAttributeMap(Map map) {
        this(map.size());
        putAll(map);
    }

    /**
	 * @see java.util.Map#clear()
	 */
    public void clear() {
        elements = null;
        count = 0;
    }

    /**
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
    public boolean containsKey(Object key) {
        key = ((String) key).intern();
        if (elements == null || count == 0) {
            return false;
        }
        for (int i = 0; i < elements.length; i = i + 2) {
            if (elements[i] == key) {
                return true;
            }
        }
        return false;
    }

    /**
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
    public boolean containsValue(Object value) {
        if (elements == null || count == 0) {
            return false;
        }
        for (int i = 1; i < elements.length; i = i + 2) {
            if (elements[i] != null && elements[i].equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * @see java.util.Map#entrySet()
	 * 
	 * This implementation does not conform properly to the
	 * specification in the Map interface. The returned collection will not
	 * be bound to this map and will not remain in sync with this map.
	 */
    public Set entrySet() {
        return toHashMap().entrySet();
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    public boolean equals(Object o) {
        if (!(o instanceof Map)) {
            return false;
        }
        Map other = (Map) o;
        if (count != other.size()) {
            return false;
        }
        if (!keySet().equals(other.keySet())) {
            return false;
        }
        for (int i = 0; i < elements.length; i = i + 2) {
            if (elements[i] != null && (!elements[i + 1].equals(other.get(elements[i])))) {
                return false;
            }
        }
        return true;
    }

    /**
	 * @see java.util.Map#get(java.lang.Object)
	 */
    public Object get(Object key) {
        key = ((String) key).intern();
        if (elements == null || count == 0) {
            return null;
        }
        for (int i = 0; i < elements.length; i = i + 2) {
            if (elements[i] == key) {
                return elements[i + 1];
            }
        }
        return null;
    }

    /**
	 * The capacity of the map has been exceeded, grow the array by GROW_SIZE to
	 * accomodate more entries.
	 */
    protected void grow() {
        Object[] expanded = new Object[elements.length + GROW_SIZE];
        System.arraycopy(elements, 0, expanded, 0, elements.length);
        elements = expanded;
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < elements.length; i = i + 2) {
            if (elements[i] != null) {
                hash += elements[i].hashCode();
            }
        }
        return hash;
    }

    /**
	 * @see java.util.Map#isEmpty()
	 */
    public boolean isEmpty() {
        return count == 0;
    }

    /**
	 * @see java.util.Map#keySet()
	 * 
	 * This implementation does not conform properly to the
	 * specification in the Map interface. The returned collection will not
	 * be bound to this map and will not remain in sync with this map.
	 */
    public Set keySet() {
        Set result = new HashSet(size());
        for (int i = 0; i < elements.length; i = i + 2) {
            if (elements[i] != null) {
                result.add(elements[i]);
            }
        }
        return result;
    }

    /**
	 * put
	 *
	 * @param key
	 * @param value
	 * @return Object
	 */
    public Object put(Object key, Object value) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (value == null) {
            return remove(key);
        }
        key = ((String) key).intern();
        if (elements == null) {
            elements = new Object[DEFAULT_SIZE];
        }
        if (count == 0) {
            elements[0] = key;
            elements[1] = value;
            count++;
            return null;
        }
        for (int i = 0; i < elements.length; i = i + 2) {
            if (elements[i] == key) {
                Object oldValue = elements[i + 1];
                elements[i + 1] = value;
                return oldValue;
            }
        }
        if (elements.length <= (count * 2)) {
            grow();
        }
        for (int i = 0; i < elements.length; i = i + 2) {
            if (elements[i] == null) {
                elements[i] = key;
                elements[i + 1] = value;
                count++;
                return null;
            }
        }
        return null;
    }

    /**
	 * putAll
	 *
	 * @param map
	 */
    public void putAll(Map map) {
        for (Iterator i = map.keySet().iterator(); i.hasNext(); ) {
            Object key = i.next();
            Object value = map.get(key);
            put(key, value);
        }
    }

    /**
	 * @see java.util.Map#remove(java.lang.Object)
	 */
    public Object remove(Object key) {
        key = ((String) key).intern();
        if (elements == null || count == 0) {
            return null;
        }
        for (int i = 0; i < elements.length; i = i + 2) {
            if (elements[i] == key) {
                elements[i] = null;
                Object result = elements[i + 1];
                elements[i + 1] = null;
                count--;
                return result;
            }
        }
        return null;
    }

    /**
	 * @see java.util.Map#size()
	 */
    public int size() {
        return count;
    }

    /**
	 * Creates a new hash map with the same contents as this map.
	 */
    private HashMap toHashMap() {
        HashMap result = new HashMap(size());
        for (int i = 0; i < elements.length; i = i + 2) {
            if (elements[i] != null) {
                result.put(elements[i], elements[i + 1]);
            }
        }
        return result;
    }

    /**
	 * @see java.util.Map#values()
	 * 
	 * This implementation does not conform properly to the
	 * specification in the Map interface. The returned collection will not
	 * be bound to this map and will not remain in sync with this map.
	 */
    public Collection values() {
        Set result = new HashSet(size());
        for (int i = 1; i < elements.length; i = i + 2) {
            if (elements[i] != null) {
                result.add(elements[i]);
            }
        }
        return result;
    }
}
