package com.siberhus.commons.lang;

import org.apache.commons.lang.StringUtils;

public class NamingConvention {

    public static String firstTitleCase(String value) {
        StringBuilder newValue = new StringBuilder(value);
        newValue.setCharAt(0, Character.toTitleCase(newValue.charAt(0)));
        return newValue.toString();
    }

    public static String firstUpperCase(String value) {
        StringBuilder newValue = new StringBuilder(value);
        newValue.setCharAt(0, Character.toUpperCase(newValue.charAt(0)));
        return newValue.toString();
    }

    public static String firstLowerCase(String value) {
        StringBuilder newValue = new StringBuilder(value);
        newValue.setCharAt(0, Character.toLowerCase(newValue.charAt(0)));
        return newValue.toString();
    }

    public static String db2java(String value) {
        if (StringUtils.indexOf(value, "_") == -1) {
            return StringUtils.lowerCase(value);
        }
        char[] chs = value.toCharArray();
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < chs.length; i++) {
            if (chs[i] == '_') {
                i++;
                name.append(Character.toUpperCase(chs[i]));
                continue;
            }
            name.append(Character.toLowerCase(chs[i]));
        }
        return name.toString();
    }

    public static void main(String[] args) {
        System.out.println(firstUpperCase(db2java("how_do_you_do")));
    }
}
