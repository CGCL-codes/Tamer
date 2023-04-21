package com.earnware.james.core;

public interface KeyStrategy {

    public String build(Object o);

    public Object parse(String key);
}
