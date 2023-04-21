package com.siberhus.commons.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Utility class that makes it easy to construct Collection literals, and also provides
 * a nicer syntax for creating array literals.
 *
 */
public class Literal {

    /** Returns an array containing all the elements supplied. */
    public static <T> T[] array(T... elements) {
        return elements;
    }

    /** Returns an array containing all the elements supplied. */
    public static boolean[] array(boolean... elements) {
        return elements;
    }

    /** Returns an array containing all the elements supplied. */
    public static byte[] array(byte... elements) {
        return elements;
    }

    /** Returns an array containing all the elements supplied. */
    public static char[] array(char... elements) {
        return elements;
    }

    /** Returns an array containing all the elements supplied. */
    public static short[] array(short... elements) {
        return elements;
    }

    /** Returns an array containing all the elements supplied. */
    public static int[] array(int... elements) {
        return elements;
    }

    /** Returns an array containing all the elements supplied. */
    public static long[] array(long... elements) {
        return elements;
    }

    /** Returns an array containing all the elements supplied. */
    public static float[] array(float... elements) {
        return elements;
    }

    /** Returns an array containing all the elements supplied. */
    public static double[] array(double... elements) {
        return elements;
    }

    /** Returns a new List instance containing the supplied elements. */
    public static <T> List<T> list(T... elements) {
        List<T> list = new ArrayList<T>();
        Collections.addAll(list, elements);
        return list;
    }

    /** Returns a new Set instance containing the supplied elements. */
    public static <T> Set<T> set(T... elements) {
        Set<T> set = new HashSet<T>();
        Collections.addAll(set, elements);
        return set;
    }

    /** Returns a new LinkedHashSet instance containing the supplied elements. */
    public static <T> LinkedHashSet<T> linkedSet(T... elements) {
        LinkedHashSet<T> set = new LinkedHashSet<T>();
        Collections.addAll(set, elements);
        return set;
    }

    /** Returns a new SortedSet instance containing the supplied elements. */
    public static <T extends Comparable<?>> SortedSet<T> sortedSet(T... elements) {
        SortedSet<T> set = new TreeSet<T>();
        Collections.addAll(set, elements);
        return set;
    }

    public static <K, V> HashMap<K, V> map(Object[][] kvs) {
        return (HashMap<K, V>) map(new HashMap<K, V>(), kvs);
    }

    public static <K, V> LinkedHashMap<K, V> linkedMap(Object[][] kvs) {
        return (LinkedHashMap<K, V>) map(new LinkedHashMap<K, V>(), kvs);
    }

    public static <K extends Comparable<?>, V> TreeMap<K, V> sortedMap(Object[][] kvs) {
        return (TreeMap<K, V>) map(new TreeMap<K, V>(), kvs);
    }

    public static <K, V> HashMap<K, V> map(K[] keys, V[] values) {
        return (HashMap<K, V>) map(new HashMap<K, V>(), keys, values);
    }

    public static <K, V> LinkedHashMap<K, V> linkedMap(K[] keys, V[] values) {
        return (LinkedHashMap<K, V>) map(new LinkedHashMap<K, V>(), keys, values);
    }

    public static <K extends Comparable<?>, V> TreeMap<K, V> sortedMap(K[] keys, V[] values) {
        return (TreeMap<K, V>) map(new TreeMap<K, V>(), keys, values);
    }

    @SuppressWarnings("unchecked")
    private static <K, V> Map<K, V> map(Map<K, V> map, Object[][] kvs) {
        if (kvs == null) return map;
        for (Object[] kv : kvs) {
            if (kv.length == 1) {
                map.put((K) kv[0], null);
            } else if (kv.length == 2) {
                map.put((K) kv[0], (V) kv[1]);
            }
        }
        return map;
    }

    private static <K, V> Map<K, V> map(Map<K, V> map, K[] keys, V[] values) {
        if (keys == null) return null;
        if (values == null) {
            for (int i = 0; i < keys.length; i++) {
                map.put(keys[i], null);
            }
        } else {
            for (int i = 0; i < keys.length; i++) {
                if (i < values.length) {
                    map.put(keys[i], values[i]);
                } else {
                    map.put(keys[i], null);
                }
            }
        }
        return map;
    }
}
