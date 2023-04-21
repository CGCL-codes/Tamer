package awilkins.util;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

/**
 * WeakIdentityHashMap
 *
 * @author Antony
 */
public class WeakIdentityHashMap<K, V> extends ProtectedMap<K, V> implements Map<K, V> {

    /**
   * @see awilkins.util.ProtectedMap#newMap()
   */
    protected Map newMap() {
        return new IdentityHashMap();
    }

    /**
   * @see java.util.Map#clear()
   */
    public void clear() {
        super.clear();
    }

    /**
   * @see java.util.Map#containsKey(java.lang.Object)
   */
    public boolean containsKey(Object key) {
        return super.containsKey(key);
    }

    /**
   * @see java.util.Map#containsValue(java.lang.Object)
   */
    public boolean containsValue(Object value) {
        return super.containsValue(value);
    }

    /**
   * @see java.util.Map#entrySet()
   */
    public Set entrySet() {
        return super.entrySet();
    }

    /**
   * @see java.util.Map#isEmpty()
   */
    public boolean isEmpty() {
        return super.isEmpty();
    }

    /**
   * @see java.util.Map#keySet()
   */
    public Set keySet() {
        return super.keySet();
    }

    /**
   * @see java.util.Map#putAll(java.util.Map)
   */
    public void putAll(Map t) {
        super.putAll(t);
    }

    /**
   * @see java.util.Map#remove(Object)
   */
    public V remove(Object thingId) {
        return (V) super.remove(thingId);
    }

    /**
   * @see java.util.Map#size()
   */
    public int size() {
        return super.size();
    }

    /**
   * @see java.util.Map#get(java.lang.Object)
   */
    public V get(Object thingId) {
        return (V) super.get(thingId);
    }

    /**
   * @see java.util.Map#put(java.lang.Object, java.lang.Object)
   */
    public V put(K thingId, V thingMapping) {
        return (V) super.put(thingId, thingMapping);
    }

    /**
   * @see java.util.Map#values()
   */
    public Collection values() {
        return super.values();
    }
}
