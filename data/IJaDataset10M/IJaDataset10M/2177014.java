package org.sucri.floxs;

import org.apache.commons.collections15.MultiMap;
import org.sucri.floxs.model.Accesor;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Wen Yu
 * Date: Jul 15, 2007
 * Time: 10:53:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class Tools {

    public static boolean good(String s) {
        return s != null && s.length() > 0;
    }

    public static <T> boolean good(Collection<T> s) {
        return s != null && s.size() > 0;
    }

    public static <T> boolean good(List<T> s) {
        return s != null && s.size() > 0;
    }

    public static <K, T> boolean good(MultiMap<K, T> s) {
        return s != null && s.size() > 0;
    }

    public static <K, T> boolean good(Map<K, T> s) {
        return s != null && s.size() > 0;
    }

    public static <T> boolean good(T[] s) {
        return s != null && s.length > 0;
    }

    public static String spacer(String s, int repeat) {
        if (repeat > 0) for (int i = 0; i < repeat; i++) s += s;
        return s;
    }

    public static <T> String toString(T[] s, String delim) {
        String made = null;
        if (good(s)) {
            for (T t : s) made = extend(made, t.toString(), delim);
        }
        return made;
    }

    public static String extend(String root, String tag, String delimiter) {
        if (tag == null || delimiter == null) return root;
        if (!good(root)) root = tag; else root += (delimiter != null ? delimiter + tag : tag);
        return root;
    }

    public static <T> T front(Collection<T> s) {
        if (good(s)) for (T ss : s) return ss;
        return null;
    }

    public static <T> T front(List<T> s) {
        if (good(s)) return s.get(0);
        return null;
    }

    public static boolean isNull(String s) {
        return (s == null || s.equals(Accesor.NULL));
    }

    public static boolean isNumber(Object s) {
        if (s == null) return false;
        Class c = s.getClass();
        return (c.equals(int.class) || c.equals(Integer.class) || c.equals(long.class) || c.equals(Long.class) || c.equals(float.class) || c.equals(Float.class) || c.equals(double.class) || c.equals(Double.class));
    }

    public static boolean isBoolean(Object s) {
        if (s == null) return false;
        Class c = s.getClass();
        return (c.equals(boolean.class) || c.equals(Boolean.class));
    }
}
