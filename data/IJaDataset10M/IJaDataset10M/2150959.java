package com.techfort.tfal.utils;

public class StringUtils {

    public static String capitalize(String str) {
        String start = "" + str.charAt(0);
        return start.toUpperCase() + str.substring(1);
    }
}
