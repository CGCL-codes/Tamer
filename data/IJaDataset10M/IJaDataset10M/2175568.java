package com.astrientlabs.cache;

public class Cache {

    public static Cache instance;

    private Object[] model;

    private Object[] keys;

    public Cache(int size) {
        model = new Object[size];
        keys = new Object[size];
    }

    public int slots() {
        return model.length;
    }

    public void put(Object key, Object value) {
        int pos = Math.abs(key.hashCode() % model.length);
        keys[pos] = key;
        model[pos] = value;
    }

    public Object get(Object key) {
        int pos = Math.abs(key.hashCode() % model.length);
        if (key.equals(keys[pos])) {
            return model[pos];
        }
        return null;
    }

    public int size() {
        int count = 0;
        for (int i = 0; i < model.length; i++) {
            if (model[i] != null) count++;
        }
        return count;
    }

    public void clear() {
        for (int i = 0; i < model.length; i++) {
            model[i] = null;
            keys[i] = null;
        }
    }
}
