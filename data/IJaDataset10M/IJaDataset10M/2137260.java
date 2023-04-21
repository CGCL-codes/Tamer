package simpleorm.simpleweb.ute;

import java.util.Map;
import java.util.Set;
import java.util.Collection;

/**
 * Easy way to make a map out of a get() method.
 * Need to do this because in .jsps ${foo[bar]} is the only way to
 * pass a parameter to a get method, provided the get method is
 * disguized as a map.
 */
public abstract class WDummyMap implements Map {

    public int size() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map t) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public Set keySet() {
        throw new UnsupportedOperationException();
    }

    public Collection values() {
        throw new UnsupportedOperationException();
    }

    public Set entrySet() {
        throw new UnsupportedOperationException();
    }
}
