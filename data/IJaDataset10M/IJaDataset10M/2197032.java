package org.jtools.util;

public class StringUtils {

    public static boolean isJavaClassname(String x) {
        if (x == null) return false;
        int offs = 0;
        for (int pos = x.indexOf('.', offs); pos >= 0; pos = x.indexOf('.', offs)) {
            if (pos == offs) return false;
            if (!isJavaIdentifier(x.substring(offs, pos))) return false;
            offs = pos + 1;
        }
        for (int pos = x.indexOf('$', offs); pos >= 0; pos = x.indexOf('$', offs)) {
            if (pos == offs) return false;
            if (!isJavaIdentifier(x.substring(offs, pos))) return false;
            offs = pos + 1;
        }
        return isJavaIdentifier(x.substring(offs));
    }

    public static boolean isJavaIdentifier(String x) {
        if (x == null) return false;
        char[] c = x.toCharArray();
        if (c.length == 0) return false;
        if (!Character.isJavaIdentifierStart(c[0])) return false;
        if (c[0] == '$') return false;
        for (int i = 0; i < c.length; i++) {
            if (!Character.isJavaIdentifierPart(c[i])) return false;
            if (c[i] == '.') return false;
            if (c[i] == '$') return false;
        }
        return true;
    }

    public static String literal(String x) {
        if (x == null) return "null";
        return "\"" + x + "\"";
    }

    public static String replace(String x, String oldString, String newString) {
        return replace(x, oldString, newString, false, false);
    }

    public static String replace(String x, String oldString, String newString, boolean ignoreend) {
        return replace(x, oldString, newString, ignoreend, false);
    }

    public static String replace(String x, String oldString, String newString, boolean ignoreend, boolean trailingonly) {
        if (ignoreend) while (x.endsWith(oldString)) x = x.substring(0, x.length() - oldString.length());
        boolean ready = false;
        int vfirst = 0;
        int p = x.indexOf(oldString);
        while ((!ready) && (p >= 0)) {
            if (trailingonly && (p > vfirst)) ready = true; else {
                String s;
                s = x.substring(0, p) + newString + x.substring(p + oldString.length());
                x = s;
                vfirst = p + newString.length();
                p = x.indexOf(oldString, vfirst);
            }
        }
        return x;
    }

    public static String subPackage(String parent, String child) {
        if ("".equals(parent)) return child;
        return parent + "." + child;
    }

    public static String trimmed(String x) {
        if (x == null) return null;
        return x.trim();
    }

    public static String firstUpperCase(String x) {
        if (x == null) return null;
        if (x.length() == 0) return "";
        if (x.length() == 1) return x.toUpperCase();
        StringBuilder y = new StringBuilder(x);
        y.setCharAt(0, Character.toUpperCase(y.charAt(0)));
        return y.toString();
    }

    public static String firstLowerCase(String x) {
        if (x == null) return null;
        if (x.length() == 0) return "";
        if (x.length() == 1) return x.toLowerCase();
        StringBuilder y = new StringBuilder(x);
        y.setCharAt(0, Character.toLowerCase(y.charAt(0)));
        return y.toString();
    }
}
