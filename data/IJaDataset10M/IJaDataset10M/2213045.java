package com.grt192.event;

import java.util.Hashtable;

/**
 *
 * @author anand
 */
public class GlobalEvent {

    public static final int DEFAULT = 0;

    private int id;

    private String key;

    private Hashtable globals;

    public GlobalEvent(int id, String key, Hashtable globals) {
        this.id = id;
        this.key = key;
        this.globals = globals;
    }

    public Hashtable getGlobals() {
        return globals;
    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }
}
