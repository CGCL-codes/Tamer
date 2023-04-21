package overlaysim.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.math.BigInteger;
import overlaysim.protocol.overlay.GuidTools;

public class LRUCache {

    private HashMap<Object, Object> pool;

    private LinkedList<Object> list;

    private int capacity;

    public LRUCache(int size) {
        pool = new HashMap<Object, Object>();
        list = new LinkedList<Object>();
        this.capacity = size;
    }

    public synchronized Object get(Object key) {
        Object value = pool.get(key);
        if (value != null) {
            list.remove(key);
            list.addFirst(key);
        }
        return value;
    }

    public synchronized void put(Object key, Object value) {
        Object obj = null;
        if (pool.size() >= capacity) {
            obj = list.removeLast();
            pool.remove(obj);
        }
        obj = pool.get(key);
        if (!value.equals(obj)) {
            pool.remove(key);
        }
        pool.put(key, value);
        list.remove(key);
        list.addFirst(key);
    }

    public synchronized Object remove(Object key) {
        Object value = pool.get(key);
        list.remove(key);
        return value;
    }

    public synchronized void clear() {
        pool.clear();
        list.clear();
    }

    public synchronized String toString() {
        StringBuffer result = new StringBuffer();
        result.append("(list=[");
        for (Iterator<Object> i = list.iterator(); i.hasNext(); ) {
            BigInteger guid = (BigInteger) i.next();
            result.append(GuidTools.guid_to_string_40(guid));
            if (i.hasNext()) result.append(", ");
        }
        result.append("] ");
        result.append("pool" + pool + ")");
        return result.toString();
    }
}
