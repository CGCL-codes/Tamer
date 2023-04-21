package opennlp.tools.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Provides fixed size, pre-allocated, least recently used replacement cache. 
 */
public class Cache implements Map {

    /** The element in the linked list which was most recently used. **/
    private DoubleLinkedListElement first;

    /** The element in the linked list which was least recently used. **/
    private DoubleLinkedListElement last;

    /** Temporary holder of the key of the least-recently-used element. */
    private Object lastKey;

    /** Temporary value used in swap. */
    private ObjectWrapper temp;

    /** Holds the object wrappers which the keys are mapped to. */
    private ObjectWrapper[] wrappers;

    /** Map which stores the keys and values of the cache. */
    private Map map;

    /** The size of the cache. */
    private int size;

    /**
   * Creates a new cache of the specified size.
   * @param size The size of the cache.
   */
    public Cache(int size) {
        map = new HashMap(size);
        wrappers = new ObjectWrapper[size];
        this.size = size;
        Object o = new Object();
        first = new DoubleLinkedListElement(null, null, o);
        map.put(o, new ObjectWrapper(null, first));
        wrappers[0] = new ObjectWrapper(null, first);
        DoubleLinkedListElement e = first;
        for (int i = 1; i < size; i++) {
            o = new Object();
            e = new DoubleLinkedListElement(e, null, o);
            wrappers[i] = new ObjectWrapper(null, e);
            map.put(o, wrappers[i]);
            e.prev.next = e;
        }
        last = e;
    }

    public void clear() {
        map.clear();
        DoubleLinkedListElement e = first;
        for (int oi = 0; oi < size; oi++) {
            wrappers[oi].object = null;
            Object o = new Object();
            map.put(o, wrappers[oi]);
            e.object = o;
            e = e.next;
        }
    }

    public Object put(Object key, Object value) {
        ObjectWrapper o = (ObjectWrapper) map.get(key);
        if (o != null) {
            DoubleLinkedListElement e = o.listItem;
            if (e != first) {
                e.prev.next = e.next;
                if (e.next != null) {
                    e.next.prev = e.prev;
                } else {
                    last = e.prev;
                }
                e.next = first;
                first.prev = e;
                e.prev = null;
                first = e;
            }
            return o.object;
        }
        lastKey = last.object;
        last.object = key;
        last.next = first;
        first.prev = last;
        first = last;
        last = last.prev;
        first.prev = null;
        last.next = null;
        temp = (ObjectWrapper) map.remove(lastKey);
        temp.object = value;
        temp.listItem = first;
        map.put(key, temp);
        return null;
    }

    public Object get(Object key) {
        ObjectWrapper o = (ObjectWrapper) map.get(key);
        if (o != null) {
            DoubleLinkedListElement e = o.listItem;
            if (e != first) {
                e.prev.next = e.next;
                if (e.next != null) {
                    e.next.prev = e.prev;
                } else {
                    last = e.prev;
                }
                e.next = first;
                first.prev = e;
                e.prev = null;
                first = e;
            }
            return o.object;
        } else {
            return null;
        }
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public Set entrySet() {
        return map.entrySet();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Set keySet() {
        return map.keySet();
    }

    public void putAll(Map t) {
        map.putAll(t);
    }

    public Object remove(Object key) {
        return map.remove(key);
    }

    public int size() {
        return map.size();
    }

    public Collection values() {
        return map.values();
    }
}

class ObjectWrapper {

    public Object object;

    public DoubleLinkedListElement listItem;

    public ObjectWrapper(Object o, DoubleLinkedListElement li) {
        object = o;
        listItem = li;
    }

    public Object getObject() {
        return object;
    }

    public DoubleLinkedListElement getListItem() {
        return listItem;
    }

    public void setObject(Object o) {
        object = o;
    }

    public void setListItem(DoubleLinkedListElement li) {
        listItem = li;
    }

    public boolean eqauls(Object o) {
        return object.equals(o);
    }
}

class DoubleLinkedListElement {

    public DoubleLinkedListElement prev;

    public DoubleLinkedListElement next;

    public Object object;

    public DoubleLinkedListElement(DoubleLinkedListElement p, DoubleLinkedListElement n, Object o) {
        prev = p;
        next = n;
        object = o;
        if (p != null) {
            p.next = this;
        }
        if (n != null) {
            n.prev = this;
        }
    }
}

class DoubleLinkedList {

    DoubleLinkedListElement first;

    DoubleLinkedListElement last;

    DoubleLinkedListElement current;

    public DoubleLinkedList() {
        first = null;
        last = null;
        current = null;
    }

    public void addFirst(Object o) {
        first = new DoubleLinkedListElement(null, first, o);
        if (current.next == null) {
            last = current;
        }
    }

    public void addLast(Object o) {
        last = new DoubleLinkedListElement(last, null, o);
        if (current.prev == null) {
            first = current;
        }
    }

    public void insert(Object o) {
        if (current == null) {
            current = new DoubleLinkedListElement(null, null, o);
        } else {
            current = new DoubleLinkedListElement(current.prev, current, o);
        }
        if (current.prev == null) {
            first = current;
        }
        if (current.next == null) {
            last = current;
        }
    }

    public DoubleLinkedListElement getFirst() {
        current = first;
        return first;
    }

    public DoubleLinkedListElement getLast() {
        current = last;
        return last;
    }

    public DoubleLinkedListElement getCurrent() {
        return current;
    }

    public DoubleLinkedListElement next() {
        if (current.next != null) {
            current = current.next;
        }
        return current;
    }

    public DoubleLinkedListElement prev() {
        if (current.prev != null) {
            current = current.prev;
        }
        return current;
    }

    public String toString() {
        DoubleLinkedListElement e = first;
        String s = "[" + e.object.toString();
        e = e.next;
        while (e != null) {
            s = s + ", " + e.object.toString();
            e = e.next;
        }
        s = s + "]";
        return s;
    }
}
