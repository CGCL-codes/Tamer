package org.skunk.dav.client;

import java.util.*;

/**
 * A HashMap subclass in which keys, if they are strings,
 * are coerced into uppercase.
 */
public class HeaderMap extends HashMap {

    /**
   * Creates a new HeaderMap.
   */
    public HeaderMap() {
        super();
    }

    /**
   * Puts an entry in the HeaderMap table.
   * @param key The key for the entry.
   * @param value The value of the entry.
   * @return previous value associated with the key, or null if
   * there was no previous mapping. A null return can also be
   * returned if previously null was previously associated with the key.
   */
    public Object put(Object key, Object value) {
        if (key == null || value == null) return null;
        return super.put(getUpperCaseObject(key), value);
    }

    /**
   * Gets the Object to swhich the key is mapped.
   * @param key The key.
   * @return Object, or null if there is no mapping.
   */
    public Object get(Object key) {
        return super.get(getUpperCaseObject(key));
    }

    /**
   * Makes strings, and only strings, uppercase.
   * @param o The object to uppercase.
   * @return The uppercased object.
   */
    private Object getUpperCaseObject(Object o) {
        if (o instanceof String) {
            return ((String) o).toUpperCase().trim();
        }
        return o;
    }

    /**
   * Indicates whether there is a mapping for the specified key
   * in the table.
   * @param key The key.
   * @return true or false.
   */
    public boolean containsKey(Object key) {
        return super.containsKey(getUpperCaseObject(key));
    }

    /**
   * Removes an object from the table.
   * @param key The key cooresponding to the entry which should be removed.
   * @return the object to which the key mapped, or null if no such mapping
   * exists.
   */
    public Object remove(Object key) {
        return super.remove(getUpperCaseObject(key));
    }

    /**
   * Gets a string representation of the HeaderMap.
   * @return A pretty-printed representation of the HeaderMap.
   */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        Set daSet = keySet();
        for (Iterator it = daSet.iterator(); it.hasNext(); ) {
            Object key = it.next();
            sb.append(key);
            sb.append(": ");
            sb.append(get(key));
            sb.append(DAVConstants.CRLF);
        }
        return sb.toString();
    }

    /**
   * Gets a string representation of the value associated with the key
   * in the HeaderMap.
   * @param headerKey The key.
   * @return String representation of the value associated with the key.
   */
    public String getHeader(String headerKey) {
        if (containsKey(headerKey)) return get(headerKey).toString();
        return null;
    }

    /**
   * A convenience funtion to get a header value out of a HeaderMap.
   * @param headerKey The key.
   * @param headerMap The HeaderMap.
   * @return Header value, or null if the second argument was not a
   * HeaderMap or no such header exists.
   */
    public static String getHeader(String headerKey, Map headerMap) {
        if (headerMap instanceof HeaderMap) return ((HeaderMap) headerMap).getHeader(headerKey);
        return null;
    }
}
