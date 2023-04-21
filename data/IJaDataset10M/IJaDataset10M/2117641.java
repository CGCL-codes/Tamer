package com.informa.utils;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static String replaceNull(final String text) {
        if (text != null) {
            return text;
        }
        return "";
    }

    public static String replaceUnderScoresWithSpaces(String text) {
        return text.replace("_", " ");
    }

    public static String arrayToCommaDelimitedString(double[] values) {
        StringBuilder commaDelimitedString = new StringBuilder("");
        for (int i = 0; i < values.length; i++) {
            commaDelimitedString.append(values[i]);
            if (i + 1 < values.length) {
                commaDelimitedString.append(",");
            }
        }
        return commaDelimitedString.toString();
    }

    public static Set<String> replaceAll(Set<String> values, String before, String after) {
        Set<String> replacedValues = new LinkedHashSet<String>();
        for (String value : values) {
            String replacedValue = value.replaceAll(before, after);
            replacedValues.add(replacedValue);
        }
        return replacedValues;
    }

    public static String asUtf8(String s, String sourceEncoding) {
        String utf = null;
        try {
            utf = new String(s.getBytes(sourceEncoding), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return utf;
    }

    public static String match(String text, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(text);
        matcher.find();
        return matcher.group();
    }

    public static String match(String text, String regex, int group) {
        Matcher matcher = Pattern.compile(regex).matcher(text);
        matcher.find();
        return matcher.group(group);
    }

    public static String firstWords(String text, int wordCount) {
        return match(text, "(\\pP*\\s*\\w+){0," + wordCount + "}").trim();
    }

    public static String commaSeparated(List<String> strings) {
        return org.springframework.util.StringUtils.collectionToDelimitedString(strings, ", ");
    }
}
