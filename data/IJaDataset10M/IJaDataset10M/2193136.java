package com.thoughtworks.xstream.benchmark.cache.model;

public class One {

    private String one;

    public One(String one) {
        this.one = one;
    }

    public boolean equals(Object obj) {
        return one.equals(((One) obj).one);
    }

    public int hashCode() {
        return one.hashCode() >>> 1;
    }
}
