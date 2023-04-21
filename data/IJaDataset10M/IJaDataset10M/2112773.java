package ca.spaz.util;

import java.util.*;

/**
 * A caching map implementation that provides a maximum number of stored objects.
 * @author Chris Rose
 */
public class CacheMap implements Map {

    private Map backingMap;

    private LinkedList orderStack;

    private int maxCacheSize;

    public CacheMap(int size) {
        this.backingMap = new HashMap();
        this.orderStack = new LinkedList();
        this.maxCacheSize = size;
    }

    public int size() {
        return backingMap.size();
    }

    public boolean isEmpty() {
        return backingMap.isEmpty();
    }

    public boolean containsKey(Object key) {
        return backingMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return backingMap.containsValue(value);
    }

    public Object get(Object key) {
        assert (orderStack.size() == backingMap.size());
        return backingMap.get(key);
    }

    public Object put(Object arg0, Object arg1) {
        assert (orderStack.size() == backingMap.size());
        if (!orderStack.contains(arg0)) {
            if (orderStack.size() >= maxCacheSize) {
                prepQueue();
            }
            orderStack.addLast(arg0);
            backingMap.put(arg0, arg1);
        } else {
            assert (backingMap.containsKey(arg0));
        }
        assert (orderStack.size() == backingMap.size());
        assert (backingMap.size() <= maxCacheSize);
        return null;
    }

    private void prepQueue() {
        while (orderStack.size() >= maxCacheSize) {
            Object key = orderStack.removeFirst();
            Object res = backingMap.remove(key);
            assert (res != null);
        }
    }

    public Object remove(Object key) {
        assert (orderStack.size() == backingMap.size());
        orderStack.remove(key);
        Object ret = backingMap.remove(key);
        assert (orderStack.size() == backingMap.size());
        return ret;
    }

    public void putAll(Map arg0) {
        assert (orderStack.size() == backingMap.size());
        for (Iterator iter = arg0.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            put(entry.getKey(), entry.getValue());
        }
        assert (orderStack.size() == backingMap.size());
    }

    public void clear() {
        orderStack.clear();
        backingMap.clear();
    }

    public Set keySet() {
        return backingMap.keySet();
    }

    public Collection values() {
        return backingMap.values();
    }

    public Set entrySet() {
        return backingMap.entrySet();
    }

    public boolean equals(Object obj) {
        return backingMap.equals(obj);
    }

    public int hashCode() {
        return backingMap.hashCode();
    }

    public String toString() {
        return backingMap.toString();
    }
}
