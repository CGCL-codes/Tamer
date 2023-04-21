package com.siberhus.datacleaner;

import java.util.regex.Pattern;

public class SpecialCharCleaner {

    public static final Pattern SPEICIAL_CHAR_PATTERN = Pattern.compile("[\\x80-\\xFF]");

    public static final Pattern SPEICIAL_CHARS_PATTERN = Pattern.compile("[\\x80-\\xFF]+");

    public static String removeAll(String data) {
        if (data == null) return null;
        return SPEICIAL_CHARS_PATTERN.matcher(data).replaceAll("");
    }
}
