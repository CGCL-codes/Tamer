package com.tomczarniecki.s3;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Maps {

    public static <K, V> Map<K, V> create() {
        return new HashMap<K, V>();
    }

    public static <K, V> SortedMap<K, V> createSorted() {
        return new TreeMap<K, V>();
    }
}
