package com.webtair.dump4j.core;

public interface Dumper {

    void dump(Object obj);

    void dump(Object obj, int depth);
}
