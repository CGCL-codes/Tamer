package org.judo.util;

public class JavaNameConversion {

    public static String getUnderscoreNameFromClass(String name) {
        String newName = "";
        char chars[] = name.toCharArray();
        if (Character.isUpperCase(chars[0])) chars[0] = Character.toLowerCase(chars[0]);
        for (char ch : chars) {
            if (Character.isUpperCase(ch)) newName += "_";
            newName += ch;
        }
        newName = newName.toLowerCase();
        return newName;
    }

    public static String getUnderscoreName(String name) {
        String newName = "";
        char chars[] = name.toCharArray();
        for (char ch : chars) {
            if (Character.isUpperCase(ch)) newName += "_";
            newName += ch;
        }
        newName = newName.toLowerCase();
        return newName;
    }

    public static String getUnderscoreTableName(String name) {
        String newName = "";
        char chars[] = name.toCharArray();
        if (Character.isLowerCase(chars[0])) chars[0] = Character.toUpperCase(chars[0]);
        for (char ch : chars) {
            if (Character.isUpperCase(ch)) newName += "_";
            newName += ch;
        }
        return newName;
    }

    public static String getMemberName(String name) {
        String javaName = null;
        name = name.toLowerCase();
        String components[] = name.split("_");
        boolean first = true;
        for (int i = 0; i < components.length; i++) {
            if (first) {
                first = false;
                javaName = components[i];
            } else {
                if (components[i].length() == 1) javaName += components[i].toUpperCase(); else {
                    String camelPart = components[i].substring(1);
                    javaName += Character.toUpperCase(components[i].charAt(0)) + camelPart;
                }
            }
        }
        return javaName;
    }

    public static String getClassName(String name) {
        String javaName = "";
        name = name.toLowerCase();
        String components[] = name.split("_");
        for (int i = 0; i < components.length; i++) {
            if (components[i].length() == 1) javaName += components[i].toUpperCase(); else {
                String camelPart = components[i].substring(1);
                javaName += Character.toUpperCase(components[i].charAt(0)) + camelPart;
            }
        }
        return javaName;
    }
}
