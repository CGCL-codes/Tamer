package com.flanderra;

import java.util.Map;

public abstract class Element {

    public Element() {
        super();
    }

    public Element(String name) {
        super();
        this.name = name;
    }

    protected long uid;

    protected String name;

    protected Map ctx;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public Map getCtx() {
        return ctx;
    }

    public abstract void render();
}
