package org.junit.remote.internal.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author  Steven Stallion
 * @version $Revision: 12 $
 */
public class Classes {

    public static Class forName(String name) {
        assert name != null;
        try {
            return Class.forName(name);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void invokeNoArgStaticMethods(Collection<Method> methods) throws IllegalAccessException, InvocationTargetException {
        invokeNoArgInstanceMethods(methods, null);
    }

    public static void invokeNoArgInstanceMethods(Collection<Method> methods, Object instance) throws IllegalAccessException, InvocationTargetException {
        assert methods != null;
        for (Method method : methods) {
            invokeNoArgMethod(method, instance);
        }
    }

    public static void invokeNoArgMethod(Method method, Object instance) throws IllegalAccessException, InvocationTargetException {
        method.invoke(instance);
    }

    public static boolean overrides(Method m1, Method m2) {
        assert m1 != null;
        assert m2 != null;
        return m1.getName().equals(m2.getName()) && m1.getReturnType().isAssignableFrom(m2.getReturnType()) && Arrays.isEqual(m1.getParameterTypes(), m2.getParameterTypes());
    }

    private Classes() {
    }
}
