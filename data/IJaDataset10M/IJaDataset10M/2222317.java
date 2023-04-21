package com.google.gwt.emultest.benchmarks.java.util;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A {@link SortedMapBenchmark} for {@link TreeMap TreeMaps}.
 */
public class TreeMapBenchmark extends SortedMapBenchmark {

    @Override
    protected SortedMap<Integer, String> newSortedMap() {
        return new TreeMap<Integer, String>();
    }
}
