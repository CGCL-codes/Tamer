package com.cidero.util;

/**
 * Tagged message class. Originally created to associate a sequence number
 * with command messages so message responses can paired with message
 * requests
 */
public class TaggedMsg {

    int id;

    Object obj;

    public TaggedMsg(int id, Object obj) {
        this.id = id;
        this.obj = obj;
    }

    public int getId() {
        return id;
    }

    public Object getObject() {
        return obj;
    }
}
