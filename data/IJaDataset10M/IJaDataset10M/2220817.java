package org.baatar.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author bahadir
 */
public class ReflectionUtil {

    private ReflectionUtil() {
    }

    private static ArrayList<Field> getFieldList(@SuppressWarnings("rawtypes") Class c) {
        ArrayList<Field> al = new ArrayList<Field>();
        produceFieldList(c, al);
        return al;
    }

    private static void produceFieldList(@SuppressWarnings("rawtypes") Class c, ArrayList<Field> al) {
        @SuppressWarnings("rawtypes") Class sc = c.getSuperclass();
        if (sc != Object.class) produceFieldList(sc, al);
        Field[] fl = c.getDeclaredFields();
        AccessibleObject.setAccessible(fl, true);
        al.addAll(Arrays.asList(fl));
    }

    private static int getFieldCount(@SuppressWarnings("rawtypes") Class c, int transfer) {
        @SuppressWarnings("rawtypes") Class sc = c.getSuperclass();
        if (sc != Object.class) transfer += getFieldCount(sc, transfer);
        return transfer + c.getDeclaredFields().length;
    }

    private static Object getFieldValue(Object o, Field f) {
        try {
            Object fo = f.get(o);
            if (fo == null) return null;
            return f.get(o);
        } catch (IllegalAccessException ex) {
            return ex.toString();
        }
    }

    private static boolean setFieldValue(Object o, Field f, Object value) {
        try {
            f.set(o, value);
            return true;
        } catch (IllegalAccessException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public static synchronized String getObjectContent(Object o) {
        ArrayList<Field> al = getFieldList(o.getClass());
        StringBuilder sb = new StringBuilder();
        @SuppressWarnings("rawtypes") Iterator i = al.iterator();
        while (i.hasNext()) {
            Field f = (Field) i.next();
            sb.append("\n").append(f.getName()).append("=").append(getFieldValue(o, f).toString());
        }
        return sb.toString();
    }

    public static synchronized int getClassFieldCount(@SuppressWarnings("rawtypes") Class c) {
        return getFieldCount(c, 0);
    }

    public static synchronized String getFieldName(@SuppressWarnings("rawtypes") Class c, int fldIndex) {
        ArrayList<Field> al = getFieldList(c);
        return al.get(fldIndex).getName();
    }

    public static synchronized Object getFieldValue(Object o, int fldIndex) {
        ArrayList<Field> al = getFieldList(o.getClass());
        return getFieldValue(o, al.get(fldIndex));
    }

    public static synchronized boolean setFieldValue(Object o, int fldIndex, Object value) {
        ArrayList<Field> al = getFieldList(o.getClass());
        return setFieldValue(o, al.get(fldIndex), value);
    }
}
