package com.spinn3r.flatmap;

import java.io.*;
import java.nio.*;
import java.util.*;
import java.nio.*;
import java.nio.channels.*;
import static com.spinn3r.flatmap.FlatMapWriter.*;

/**
 *
 */
public abstract class ReadOnlyMap<K, V> {

    public V put(K key, V value) {
        throw new RuntimeException("Read only map");
    }

    public V remove(K key) {
        throw new RuntimeException("Read only map");
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        throw new RuntimeException("Read only map");
    }

    public void clear() {
        throw new RuntimeException("Read only map");
    }
}
