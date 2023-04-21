package org.sodeja.generator.impl;

public class NamingUtils {

    public static String firstUpper(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String firstLower(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public static String camelCaseToUnderscores(String name) {
        StringBuilder builder = new StringBuilder(name);
        builder.setCharAt(0, Character.toLowerCase(builder.charAt(0)));
        for (int i = 1; i < builder.length(); i++) {
            char ch = builder.charAt(i);
            if (!Character.isUpperCase(ch)) {
                continue;
            }
            builder.setCharAt(i, Character.toLowerCase(ch));
            builder.insert(i, '_');
            i++;
        }
        return builder.toString();
    }

    public static String getTableName(String clazzName) {
        return String.format("t_%s", camelCaseToUnderscores(clazzName));
    }
}
