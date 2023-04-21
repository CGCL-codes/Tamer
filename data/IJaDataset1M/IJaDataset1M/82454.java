package com.quickwcm.utils;

import java.util.ArrayList;

public class ArrayUtils {

    public static <T> T[] append(T[] array, T value) {
        ArrayList<T> tmp = new ArrayList<T>();
        for (T t : array) {
            tmp.add(t);
        }
        tmp.add(value);
        return tmp.toArray(array);
    }

    public static <T> T[] append(T[] array, T[] values) {
        ArrayList<T> tmp = new ArrayList<T>();
        for (T t : array) {
            tmp.add(t);
        }
        for (T t : values) {
            tmp.add(t);
        }
        return tmp.toArray(array);
    }
}
