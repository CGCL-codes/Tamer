package com.dukesoftware.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Instantiator {

    private static Class<?>[] createClassArray(Object... ctorParams) {
        final Class<?>[] ctorParamTypes = new Class[ctorParams.length];
        int i = 0;
        for (Object o : ctorParams) {
            ctorParamTypes[i++] = (o == null) ? null : o.getClass();
        }
        return ctorParamTypes;
    }

    public static <T> T newInstance(Class<T> cl, Object... ctorParams) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<?> ctor = findConstructor(cl, ctorParams);
        return ((Constructor<T>) ctor).newInstance(ctorParams);
    }

    public static <T> Constructor<?> findConstructor(Class<T> cl, Object... ctorParams) throws NoSuchMethodException {
        final Class<?>[] ctorParamTypes = createClassArray(ctorParams);
        Constructor<?> ctor = null;
        for (final Constructor<?> testCtor : cl.getConstructors()) {
            if (isAvailable(testCtor, ctorParams, ctorParamTypes) && (ctor == null || compare(testCtor, ctor, ctorParamTypes) > 0)) {
                ctor = testCtor;
            }
        }
        if (ctor == null) {
            throw new NoSuchMethodException("No Constructor");
        }
        return ctor;
    }

    private static final int compare(Constructor<?> c1, Constructor<?> c2, Class<?>[] ctorParamTypes) {
        Class<?>[] p1 = c1.getParameterTypes();
        Class<?>[] p2 = c2.getParameterTypes();
        int cc1 = 0, cc2 = 0;
        for (int i = 0; i < p1.length; i++) {
            if (p1[i].equals(p2[i])) {
                continue;
            }
            if (p1[i].isPrimitive()) {
                p1[i] = Primitives.convert(p1[i]);
            }
            if (p2[i].isPrimitive()) {
                p2[i] = Primitives.convert(p2[i]);
            }
            if (p1[i].equals(ctorParamTypes[i])) {
                cc1++;
                continue;
            } else if (p2[i].equals(ctorParamTypes[i])) {
                cc2++;
                continue;
            } else {
                try {
                    p1[i].asSubclass(p2[i]);
                    return 1;
                } catch (ClassCastException e) {
                    try {
                        p2[i].asSubclass(p1[i]);
                        return -1;
                    } catch (ClassCastException e2) {
                        throw new IllegalArgumentException("ambigous arguments");
                    }
                }
            }
        }
        return cc1 - cc2;
    }

    private static boolean isAvailable(Constructor<?> c, Object[] ctorParams, Class<?>[] ctorParamTypes) {
        final Class<?>[] definedParamterTypes = c.getParameterTypes();
        if (definedParamterTypes.length != ctorParamTypes.length) return false;
        for (int i = 0; i < definedParamterTypes.length; i++) {
            if (definedParamterTypes[i].isPrimitive()) {
                if (Primitives.isCompatible(definedParamterTypes[i], ctorParamTypes[i])) {
                    continue;
                } else {
                    return false;
                }
            } else {
                if (ctorParams[i] == null) {
                    continue;
                }
                if (!definedParamterTypes[i].isInstance(ctorParams[i])) {
                    return false;
                }
            }
        }
        return true;
    }
}
