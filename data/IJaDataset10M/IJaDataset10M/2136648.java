package com.jiexplorer.rcp.util;

public class UniqueID {

    static long current = System.currentTimeMillis();

    public static synchronized String get() {
        return String.valueOf(current++);
    }
}
