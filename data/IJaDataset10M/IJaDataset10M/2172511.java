package org.conann.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import org.conann.exceptions.WebBeansSystemException;
import static org.conann.util.StringUtils.isEmpty;
import javax.webbeans.inwork.MissingTests;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@UtilityClass
public abstract class ReflectionUtil {

    @MissingTests
    public static Object invokeMethod(Method method, Object instance, Object... params) {
        try {
            return method.invoke(instance, params);
        } catch (IllegalAccessException e) {
            throw new WebBeansSystemException(e, "Could not invoke method %s.%s()", method.getDeclaringClass(), method.getName());
        } catch (InvocationTargetException e) {
            throw new WebBeansSystemException(e, "Could not invoke method %s.%s()", method.getDeclaringClass(), method.getName());
        }
    }

    /**
     Retrieves a declared method from a given type. The method may be private.
     Wraps a java.lang.reflection to avoid the checked exceptions mess.

     @param type       The class containing the method.
     @param methodName The method name.
     @return A method of the specified name in the specified class if such exists.
     Throws a {@link WebBeansSystemException} otherwise.
     */
    public static Method getMethod(Class<?> type, String methodName, Class<?>... parameterTypes) {
        checkNotNull(type);
        checkArgument(!isEmpty(methodName));
        try {
            return type.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new WebBeansSystemException(e, "%s : Couldn't find method %s", type, methodName);
        }
    }

    /**
     Retrieves a declared field from a given type. The field may be private.
     Wraps a java.lang.reflection to avoid the checked exceptions mess.

     @param type      The class containing the field.
     @param fieldName The field name.
     @return A field of the specified name in the specified class if such exists.
     Throws a {@link WebBeansSystemException} otherwise.
     */
    public static Field getField(Class<?> type, String fieldName) {
        checkNotNull(type);
        checkArgument(!isEmpty(fieldName));
        try {
            return type.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new WebBeansSystemException(e, "%s : Couldn't find field named %s", type, fieldName);
        }
    }

    public static Set<Method> getAllMethodsInHirarchy(Class<?> type) {
        checkNotNull(type);
        final Set<Method> allMethodsInHirarchy = new HashSet<Method>(32);
        Class<?> typeIterator = type;
        while (typeIterator != null && !typeIterator.equals(Object.class)) {
            allMethodsInHirarchy.addAll(Arrays.asList(typeIterator.getDeclaredMethods()));
            typeIterator = typeIterator.getSuperclass();
        }
        return allMethodsInHirarchy;
    }

    public static Set<Class<?>> getAllInterfacesInHirarchy(Class<?> type) {
        checkNotNull(type);
        return recursiveGetAllInterfacesInHirarchy(type, new HashSet<Class<?>>(4));
    }

    private static Set<Class<?>> recursiveGetAllInterfacesInHirarchy(Class<?> type, Set<Class<?>> interfaces) {
        interfaces.add(type);
        for (final Class<?> extendedInterface : type.getInterfaces()) {
            recursiveGetAllInterfacesInHirarchy(extendedInterface, interfaces);
        }
        return interfaces;
    }

    public static Class<?> resolveClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new WebBeansSystemException(e, "Could not resolve class %s", className);
        }
    }
}
