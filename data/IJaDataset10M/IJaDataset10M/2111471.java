package com.p6spy.engine.common;

public abstract class P6Options {

    public P6Options() {
    }

    public void reload(P6SpyProperties properties) {
        P6LogQuery.logDebug(this.getClass().getName() + " reloading properties");
        properties.setClassValues(this.getClass());
    }
}
