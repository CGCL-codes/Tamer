package de.javatt.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * The EqualMap is a Map, that identifies keys by their equality.
 * 
 * @author Matthias Kempa
 * 
 */
public class EqualMap implements Map {

    /**
     * This Vector contains the keys. 
     */
    private ArrayList keyList = new ArrayList();

    /**
     * This Vector contains the values.
     */
    private ArrayList valueList = new ArrayList();

    /**
     * Standard constructor. Initializes the EqualMap.
     */
    public EqualMap() {
        super();
    }

    public int size() {
        return valueList.size();
    }

    public synchronized void clear() {
        keyList.clear();
        valueList.clear();
    }

    public boolean isEmpty() {
        return valueList.isEmpty();
    }

    public boolean containsKey(Object key) {
        boolean returnValue = false;
        for (Iterator it = keyList.iterator(); it.hasNext(); ) {
            Object obj = it.next();
            if (obj.equals(key)) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    public boolean containsValue(Object value) {
        boolean returnValue = false;
        for (Iterator it = valueList.iterator(); it.hasNext(); ) {
            Object obj = it.next();
            if (obj.equals(value)) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    public Collection values() {
        return valueList;
    }

    public synchronized void putAll(Map map) {
        Set keySet = map.keySet();
        Set valueSet = map.entrySet();
        keyList.addAll(keySet);
        valueList.addAll(valueSet);
    }

    public Set entrySet() {
        return new HashSet(valueList);
    }

    public Set keySet() {
        return new HashSet(keyList);
    }

    public synchronized Object get(Object key) {
        Object returnValue = null;
        int i = 0;
        for (Iterator it = keyList.iterator(); it.hasNext(); ) {
            Object obj = it.next();
            if (obj.equals(key)) {
                returnValue = valueList.get(i);
            }
            i++;
        }
        return returnValue;
    }

    public synchronized Object remove(Object key) {
        int index = -1;
        Object removed = null;
        if (containsKey(key)) {
            index = keyList.indexOf(key);
        }
        if (index > -1) {
            keyList.remove(index);
            removed = valueList.remove(index);
        }
        return removed;
    }

    public synchronized Object put(Object arg0, Object arg1) {
        int keyIndex = -1;
        if (containsKey(arg0)) {
            keyIndex = keyList.indexOf(arg0);
            keyList.set(keyIndex, arg0);
            valueList.set(keyIndex, arg1);
        } else {
            keyList.add(0, arg0);
            valueList.add(0, arg1);
        }
        return arg1;
    }
}
