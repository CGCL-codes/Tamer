package org.openmim.infrastructure.scheduler;

import java.util.*;

public class SortedList extends TreeMap {

    public Object put(Object key, Object value) {
        Object o = get(key);
        List values;
        if (o != null) values = (List) o; else {
            values = new LinkedList();
            super.put(key, values);
        }
        values.add(value);
        return value;
    }

    public Object removeFirst(Object key) {
        if (key == null) return null;
        Object o = get(key);
        if (o == null) {
            remove(key);
            return null;
        }
        LinkedList values = (LinkedList) o;
        if (values.size() == 0) {
            remove(key);
            return null;
        }
        o = values.removeFirst();
        if (values.size() == 0) remove(key);
        return o;
    }

    public Object remove(Object key, Object value) {
        if (key == null) throw new NullPointerException();
        Object o = get(key);
        if (o == null) {
            remove(key);
            return null;
        }
        List values = (List) o;
        if (values.size() == 0) {
            remove(key);
            return null;
        }
        if (values.remove(value)) {
            if (values.size() == 0) remove(key);
            return value;
        } else return null;
    }
}
