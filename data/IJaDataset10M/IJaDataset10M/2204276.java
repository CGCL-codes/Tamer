package org.vspirit.doveide.project.builder.property;

import java.util.TreeMap;

/**
 *
 * @author codekitten
 */
public class DovePropertyMap {

    public static final int NUM = 17;

    public static TreeMap<Integer, String> getMap() {
        TreeMap<Integer, String> map = new TreeMap<Integer, String>();
        map.put(0, " -d");
        map.put(1, " -ignore");
        map.put(2, " -quiet");
        map.put(3, " -o");
        map.put(4, " -unittest");
        map.put(5, " -v");
        map.put(6, " -w");
        map.put(7, " -inline");
        map.put(8, " -O");
        map.put(9, " -release");
        map.put(10, " -cov");
        map.put(11, " -debug");
        map.put(12, " -g");
        map.put(13, " -gc");
        map.put(14, " -profile");
        map.put(15, " -D");
        map.put(16, " -H");
        return map;
    }
}
