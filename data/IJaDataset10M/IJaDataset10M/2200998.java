package org.apache.axis2.databinding.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

/** Converter */
public class Converter {

    public static Object convert(Object arg, Class destClass) {
        if (destClass == null) {
            return arg;
        }
        Class argHeldType = null;
        if (arg != null) {
            argHeldType = getHolderValueType(arg.getClass());
        }
        if (arg != null && argHeldType == null && destClass.isAssignableFrom(arg.getClass())) {
            return arg;
        }
        Object destValue = null;
        Class destHeldType = getHolderValueType(destClass);
        if (arg instanceof Calendar && destClass == Date.class) {
            return ((Calendar) arg).getTime();
        }
        if (arg instanceof Date && destClass == Calendar.class) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) arg);
            return calendar;
        }
        if (arg instanceof Calendar && destClass == java.sql.Date.class) {
            return new java.sql.Date(((Calendar) arg).getTime().getTime());
        }
        if (arg instanceof HashMap && destClass == Hashtable.class) {
            return new Hashtable((HashMap) arg);
        }
        if (arg != null && destClass.isArray() && !destClass.getComponentType().equals(Object.class) && destClass.getComponentType().isAssignableFrom(arg.getClass())) {
            Object array = Array.newInstance(destClass.getComponentType(), 1);
            Array.set(array, 0, arg);
            return array;
        }
        if (!(arg instanceof Collection || (arg != null && arg.getClass().isArray())) && ((destHeldType == null && argHeldType == null) || (destHeldType != null && argHeldType != null))) {
            return arg;
        }
        if (arg == null) {
            return arg;
        }
        int length = 0;
        if (arg.getClass().isArray()) {
            length = Array.getLength(arg);
        } else {
            length = ((Collection) arg).size();
        }
        if (destClass.isArray()) {
            if (destClass.getComponentType().isPrimitive()) {
                Object array = Array.newInstance(destClass.getComponentType(), length);
                if (arg.getClass().isArray()) {
                    for (int i = 0; i < length; i++) {
                        Array.set(array, i, Array.get(arg, i));
                    }
                } else {
                    int idx = 0;
                    for (Iterator i = ((Collection) arg).iterator(); i.hasNext(); ) {
                        Array.set(array, idx++, i.next());
                    }
                }
                destValue = array;
            } else {
                Object[] array;
                try {
                    array = (Object[]) Array.newInstance(destClass.getComponentType(), length);
                } catch (Exception e) {
                    return arg;
                }
                if (arg.getClass().isArray()) {
                    for (int i = 0; i < length; i++) {
                        array[i] = convert(Array.get(arg, i), destClass.getComponentType());
                    }
                } else {
                    int idx = 0;
                    for (Iterator i = ((Collection) arg).iterator(); i.hasNext(); ) {
                        array[idx++] = convert(i.next(), destClass.getComponentType());
                    }
                }
                destValue = array;
            }
        } else if (Collection.class.isAssignableFrom(destClass)) {
            Collection newList = null;
            try {
                if (destClass == Collection.class || destClass == java.util.List.class) {
                    newList = new ArrayList();
                } else if (destClass == Set.class) {
                    newList = new HashSet();
                } else {
                    newList = (Collection) destClass.newInstance();
                }
            } catch (Exception e) {
                return arg;
            }
            if (arg.getClass().isArray()) {
                for (int j = 0; j < length; j++) {
                    newList.add(Array.get(arg, j));
                }
            } else {
                for (Iterator j = ((Collection) arg).iterator(); j.hasNext(); ) {
                    newList.add(j.next());
                }
            }
            destValue = newList;
        } else {
            destValue = arg;
        }
        return destValue;
    }

    private static Class getHolderValueType(Class aClass) {
        return null;
    }
}
