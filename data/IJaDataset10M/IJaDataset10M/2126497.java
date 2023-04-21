package org.datanucleus.ide.idea;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

abstract class InternalReflectionHelper {

    public static Method getDeclaredMethod(final Object object, final String name, final Class<?>... paramTypes) throws NoSuchMethodException {
        Method method = null;
        Class<?> clazz = object.getClass();
        do {
            try {
                method = clazz.getDeclaredMethod(name, paramTypes);
            } catch (Exception ignored) {
            }
        } while (method == null && (clazz = clazz.getSuperclass()) != null);
        if (method == null) {
            throw new NoSuchMethodException();
        }
        return method;
    }

    public static Object invokeMethod(final Object object, final String methodName, final Class<?>[] paramTypes, final Object[] parameters) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        final Method method = getDeclaredMethod(object, methodName, paramTypes);
        method.setAccessible(true);
        return method.invoke(object, parameters);
    }
}
