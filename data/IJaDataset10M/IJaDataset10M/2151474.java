package org.codespin.silkworm.xml;

import java.util.*;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 * The ElementMapAdapter class is an adapter class that adapts a
 * <code>org.jdom.Element</code> to the <code>java.util.Map</code> interface.
 *
 * @author <a href="mailto:gjritter@codespin.org">Greg Ritter</a>
 * @version $Revision: 1.2 $
 */
public class ElementMapAdapter implements Map {

    /**
     * Creates a new instance of the
     * <code>org.codespin.xml.ElementMapAdapter</code> class, adapting the
     * specified <code>org.jdom.Element</code> to the
     * <code>java.util.Map</code> interface.
     *
     * @param element a <code>org.jdom.Element</code> value to be adapted to
     * the <code>java.util.Map</code> class
     */
    public ElementMapAdapter(Element element) {
        this.element = element;
        size = calculateSize();
    }

    /**
     * Calculate the size of this map and return it.
     *
     * @return an <code>int</code> value indicating the size of this map
     */
    private int calculateSize() {
        return keySet().size();
    }

    /**
     * Return the size of this map.
     *
     * @return a <code>int</code> value indicating the size of this map
     */
    public int size() {
        return size;
    }

    /**
     * Return <code>true</code> if this map has no entries; <code>false</code>
     * otherwise.
     *
     * @return a <code>boolean</code> value - <code>true</code> if this map
     * has no entries; <code>false</code> otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Return <code>true</code> if this map contains the specified key;
     * <code>false</code> otherwise. The keys of both attributes and elements
     * contained within the <code>org.jdom.Element</code> being adapted by
     * this adapter are their names.
     *
     * @param key an <code>Object</code> value that is the key to search for
     * in this map
     * @return a <code>boolean</code> value - <code>true</code> if this map
     * contains ths specified key; <code>false</code> otherwise
     */
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    /**
     * Return <code>true</code> if this map contains the specified value;
     * <code>false</code> otherwise. The values of attributes contained within
     * the <code>org.jdom.Element</code> being adapted are their names; the
     * values of contained elements are the elements themselves.
     *
     * @param value an <code>Object</code> value that is the value to search
     * for in this map
     * @return a <code>boolean</code> value - <code>true</code> if this map
     * contains the specified value; <code>false</code> otherwise
     */
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    /**
     * Returns the value associated with the specified key in this map, or
     * null if no such key exists. This method will first look for attributes
     * that have the specified key as their name; if no attribute is found, it
     * will return all children with the specified key as their name.
     *
     * @param key an <code>Object</code> value that is the key
     * @return an <code>Object</code> that is mapped to the specified key
     */
    public Object get(Object key) {
        Object returnVal = null;
        if (key instanceof String) {
            returnVal = element.getAttributeValue((String) key);
            if (returnVal == null) {
                List children = element.getChildren((String) key);
                List adapterList = new ArrayList();
                for (int i = 0; i < children.size(); i++) {
                    adapterList.add(new ElementMapAdapter((Element) children.get(i)));
                }
                if (adapterList.size() > 0) {
                    returnVal = adapterList;
                }
            }
        }
        return returnVal;
    }

    /**
     * Add an attribute with name equal to the specified key, and value equal
     * to the specified value.
     *
     * @param key an <code>Object</code> value that is the key
     * @param value an <code>Object</code> value that is the value to be
     * mapped to the key
     * @return an <code>Object</code> value that is the previous value mapped
     * to the specified key, or <code>null</code> if no previous value was
     * mapped to the key
     */
    public Object put(Object key, Object value) {
        Object returnVal = get(key);
        element.setAttribute((String) key, (String) value);
        return returnVal;
    }

    /**
     * Unsupported by this implementation of Map.
     *
     * @param key an <code>Object</code> value that is the key to be removed
     * @return the <code>Object</code> value removed
     */
    public Object remove(Object key) {
        throw new UnsupportedOperationException("The remove method is not supported by the org.codespin.xml.ElementMapAdapter class.");
    }

    /**
     * Add all of the key-value pairs in the specified map to the underlying
     * element as attributes.
     *
     * @param t a <code>Map</code> value containing key-value pairs to be
     * added
     */
    public void putAll(Map t) {
        Set keys = t.keySet();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            put(key, t.get(key));
        }
    }

    /**
     * Unsupported by this implementation of Map.
     *
     * @throws UnsupportedOperationException clear is not supported by this
     * map.
     */
    public void clear() {
        throw new UnsupportedOperationException("The clear method is not supported by the org.codespin.xml.ElementMapAdapter class.");
    }

    /**
     * Returns a set view of the keys contained in this map. Note that unlike
     * other implementations of map, removal of a key from the returned set
     * does not remove the key from the map.
     *
     * @return a <code>java.util.Set</code> view of the keys contained in this map.
     */
    public Set keySet() {
        Set keys = new HashSet();
        List attributes = element.getAttributes();
        List children = element.getChildren();
        for (int i = 0; i < attributes.size(); i++) {
            keys.add(((Attribute) attributes.get(i)).getName());
        }
        for (int i = 0; i < children.size(); i++) {
            keys.add(((Element) children.get(i)).getName());
        }
        return keys;
    }

    /**
     * Returns a collection view of the values contained in this map. Note
     * that unlike other implementations of map, removal of a value from the
     * returned collection does not affect the map.
     *
     * @return a <code>java.util.Collection</code> view of the values
     * contained in this map.
     */
    public Collection values() {
        Collection values = new ArrayList();
        List attributes = element.getAttributes();
        List children = element.getChildren();
        for (int i = 0; i < attributes.size(); i++) {
            values.add(((Attribute) attributes.get(i)).getValue());
        }
        for (int i = 0; i < children.size(); i++) {
            values.add((Element) children.get(i));
        }
        return values;
    }

    /**
     * Returns a set view of the mappings contained in this map. Note that
     * unlike other implementations of map, removal of a value from the
     * returned set does not affect the map.
     *
     * @return a set view of the mappings contained in this map.
     */
    public Set entrySet() {
        Set returnVal = new HashSet();
        Set keys = keySet();
        Iterator iterator = keySet().iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            returnVal.add(new Entry(key, get(key)));
        }
        return returnVal;
    }

    /**
     * Returns a string representation of the object. In general, the
     * <code>toString</code> method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The <code>toString</code> method for class <code>Object</code>
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `<code>@</code>', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return  a string representation of the object.
     */
    public String toString() {
        return "[ElementMapAdapter wrapping " + element + "]";
    }

    /**
     * An implementation of Map.Entry for this map class.
     */
    class Entry implements Map.Entry {

        /**
         * Creates a new instance of the
         * <code>org.codespin.xml.ElementMapAdapter.Entry</code> class.
         *
         * @param key an <code>Object</code> value that is the key
         * @param value an <code>Object</code> value that is the value
         */
        public Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Returns the key corresponding to this entry.
         *
         * @return the key corresponding to this entry.
         */
        public Object getKey() {
            return key;
        }

        /**
         * Returns the value corresponding to this entry.  If the mapping
         * has been removed from the backing map (by the iterator's
         * <tt>remove</tt> operation), the results of this call are undefined.
         *
         * @return the value corresponding to this entry.
         */
        public Object getValue() {
            return value;
        }

        /**
         * The setValue method is not supported by this class.
         *
         * @param value new value to be stored in this entry.
         * @return old value corresponding to the entry.
         *
         * @throws UnsupportedOperationException
         */
        public Object setValue(Object value) {
            throw new UnsupportedOperationException("The setValue method is not supported by the org.codespin.xml.ElementMapAdapter.Entry class.");
        }

        /**
         * Holds the key of this entry.
         */
        private Object key;

        /**
         * Holds the value of this entry.
         */
        private Object value;
    }

    /**
     * Holds the <code>org.jdom.Element</code> that is being adapted.
     */
    private Element element;

    /**
     * Holds the current size of the map. This is calculated when the map
     * is constructed, and again whenever a value is added to the map, to
     * avoid expensive recalculation each time <code>size()</code> is called.
     */
    private int size;
}
