package org.gzigzag.util;

import java.util.*;

public class SecondaryHashMap extends AbstractFlatHashMap {

    public Object get(Object key) {
        int hash = key.hashCode();
        int firstHash = hash % size;
        if (firstHash < 0) firstHash = -firstHash;
        Object curKey = keys[firstHash];
        if (curKey == null) return null;
        if (curKey.equals(key)) return values[firstHash];
        do {
            firstHash++;
            firstHash %= size;
            curKey = keys[firstHash];
            if (curKey == null) return null;
            if (curKey.equals(key)) return values[firstHash];
        } while (true);
    }

    void extend() {
        resize(size * 3 + 1, size);
    }

    public Object put(Object key, Object value) {
        if (value == null) remove(key);
        int hash = key.hashCode();
        int firstHash = hash % size;
        if (firstHash < 0) firstHash = -firstHash;
        Object curKey = keys[firstHash];
        if (curKey == null) {
            keys[firstHash] = key;
            values[firstHash] = value;
            if (entries++ > entryLimit) extend();
            return null;
        }
        if (curKey.equals(key)) {
            Object prev = values[firstHash];
            values[firstHash] = value;
            return prev;
        }
        do {
            firstHash++;
            firstHash %= size;
            curKey = keys[firstHash];
            if (curKey == null) {
                keys[firstHash] = key;
                values[firstHash] = value;
                if (entries++ > entryLimit) extend();
                return null;
            }
            if (curKey.equals(key)) {
                Object prev = values[firstHash];
                values[firstHash] = value;
                return prev;
            }
        } while (true);
    }

    public Object remove(Object key) {
        return null;
    }
}
