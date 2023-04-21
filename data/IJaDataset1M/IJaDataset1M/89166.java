package com.dyuproject.protostuff.parser;

import java.util.LinkedHashMap;

/**
 * Annotation for messages, enums, services, rpc, fields
 *
 * @author David Yu
 * @created Dec 30, 2010
 */
public class Annotation implements HasName {

    final String name;

    final LinkedHashMap<String, Object> refs = new LinkedHashMap<String, Object>();

    final LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();

    public Annotation(String name) {
        this.name = name;
    }

    public LinkedHashMap<String, Object> getParams() {
        return params;
    }

    void put(String key, Object value) {
        if (params.put(key, value) != null) throw new IllegalStateException("Duplicate annotation key: " + key);
    }

    void putRef(String key, Object value) {
        put(key, value);
        refs.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key) {
        return (T) params.get(key);
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return new StringBuilder().append("Annotation|").append(name).append('|').append(params).toString();
    }
}
