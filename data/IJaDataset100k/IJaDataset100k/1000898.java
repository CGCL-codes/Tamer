package com.googlecode.javatips4u.effectivejava.eliminate;

public class CacheKey {

    private int id;

    private String key = null;

    public CacheKey(int id, String key) {
        this.setId(id);
        this.setKey(key);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
