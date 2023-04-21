package org.dynalang.mop.beans;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class OverloadedMethodUtilities {

    static final Class<Object> OBJECT_CLASS = Object.class;

    static Class<?> getMostSpecificCommonType(Class<?> c1, Class<?> c2) {
        if (c1 == c2) {
            return c1;
        }
        if (c2.isPrimitive()) {
            if (c2 == Byte.TYPE) c2 = Byte.class; else if (c2 == Short.TYPE) c2 = Short.class; else if (c2 == Character.TYPE) c2 = Character.class; else if (c2 == Integer.TYPE) c2 = Integer.class; else if (c2 == Float.TYPE) c2 = Float.class; else if (c2 == Long.TYPE) c2 = Long.class; else if (c2 == Double.TYPE) c2 = Double.class;
        }
        Set<Class<?>> a1 = getAssignables(c1, c2);
        Set<Class<?>> a2 = getAssignables(c2, c1);
        a1.retainAll(a2);
        if (a1.isEmpty()) {
            return Object.class;
        }
        List<Class<?>> max = new ArrayList<Class<?>>();
        outer: for (Class<?> clazz : a1) {
            for (Iterator<Class<?>> maxiter = max.iterator(); maxiter.hasNext(); ) {
                Class<?> maxClazz = maxiter.next();
                if (isMoreSpecific(maxClazz, clazz)) {
                    continue outer;
                }
                if (isMoreSpecific(clazz, maxClazz)) {
                    maxiter.remove();
                }
            }
            max.add(clazz);
        }
        if (max.size() > 1) {
            return OBJECT_CLASS;
        }
        return max.get(0);
    }

    /**
     * Determines whether a type represented by a class object is 
     * convertible to another type represented by a class object using a 
     * method invocation conversion, without matching object and primitive
     * types. This method is used to determine the more specific type when
     * comparing signatures of methods.
     * @return true if either formal type is assignable from actual type, 
     * or formal and actual are both primitive types and actual can be
     * subject to widening conversion to formal.
     */
    static boolean isMoreSpecific(Class<?> specific, Class<?> generic) {
        if (generic.isAssignableFrom(specific)) {
            return true;
        }
        if (generic.isPrimitive()) {
            if (generic == Short.TYPE && (specific == Byte.TYPE)) {
                return true;
            }
            if (generic == Integer.TYPE && (specific == Short.TYPE || specific == Byte.TYPE)) {
                return true;
            }
            if (generic == Long.TYPE && (specific == Integer.TYPE || specific == Short.TYPE || specific == Byte.TYPE)) {
                return true;
            }
            if (generic == Float.TYPE && (specific == Long.TYPE || specific == Integer.TYPE || specific == Short.TYPE || specific == Byte.TYPE)) {
                return true;
            }
            if (generic == Double.TYPE && (specific == Float.TYPE || specific == Long.TYPE || specific == Integer.TYPE || specific == Short.TYPE || specific == Byte.TYPE)) {
                return true;
            }
        }
        return false;
    }

    private static Set<Class<?>> getAssignables(Class<?> c1, Class<?> c2) {
        Set<Class<?>> s = new HashSet<Class<?>>();
        collectAssignables(c1, c2, s);
        return s;
    }

    private static void collectAssignables(Class<?> c1, Class<?> c2, Set<Class<?>> s) {
        if (c1.isAssignableFrom(c2)) {
            s.add(c1);
        }
        Class<?> sc = c1.getSuperclass();
        if (sc != null) {
            collectAssignables(sc, c2, s);
        }
        Class<?>[] itf = c1.getInterfaces();
        for (int i = 0; i < itf.length; ++i) {
            collectAssignables(itf[i], c2, s);
        }
    }
}
