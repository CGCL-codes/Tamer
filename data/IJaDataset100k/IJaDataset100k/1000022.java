package com.restfb.types;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Reflection-related utility methods.
 * 
 * @author <a href="http://restfb.com">Mark Allen</a>
 * @since 1.5
 */
abstract class ReflectionUtils {

    /**
   * Prevents instantiation.
   */
    private ReflectionUtils() {
    }

    /**
   * Gets all accessor methods for the given {@code clazz}.
   * 
   * @param clazz
   *          The class for which accessors are extracted.
   * @return All accessor methods for the given {@code clazz}.
   */
    static List<Method> getAccessors(Class<?> clazz) {
        if (clazz == null) throw new IllegalArgumentException("The 'clazz' parameter cannot be null.");
        List<Method> methods = new ArrayList<Method>();
        for (Method method : clazz.getMethods()) {
            String methodName = method.getName();
            if (!"getClass".equals(methodName) && !"hashCode".equals(methodName) && method.getReturnType() != null && !Void.class.equals(method.getReturnType()) && method.getParameterTypes().length == 0 && ((methodName.startsWith("get") && methodName.length() > 3) || (methodName.startsWith("is") && methodName.length() > 2) || (methodName.startsWith("has") && methodName.length() > 3))) methods.add(method);
        }
        Collections.sort(methods, new Comparator<Method>() {

            /**
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
            @Override
            public int compare(Method method1, Method method2) {
                return method1.getName().compareTo(method2.getName());
            }
        });
        return Collections.unmodifiableList(methods);
    }

    /**
   * Reflection-based implementation of {@link Object#toString()}.
   * 
   * @param object
   *          The object to convert to a string representation.
   * @return A string representation of {@code object}.
   * @throws IllegalStateException
   *           If an error occurs while performing reflection operations.
   */
    static String toString(Object object) {
        StringBuilder buffer = new StringBuilder(object.getClass().getSimpleName());
        buffer.append("[");
        boolean first = true;
        for (Method method : getAccessors(object.getClass())) {
            if (first) first = false; else buffer.append(" ");
            try {
                String methodName = method.getName();
                int offset = methodName.startsWith("is") ? 2 : 3;
                methodName = methodName.substring(offset, offset + 1).toLowerCase() + methodName.substring(offset + 1);
                buffer.append(methodName);
                buffer.append("=");
                buffer.append(method.invoke(object));
            } catch (Exception e) {
                throw new IllegalStateException("Unable to reflectively invoke " + method + " on " + object.getClass(), e);
            }
        }
        buffer.append("]");
        return buffer.toString();
    }

    /**
   * Reflection-based implementation of {@link Object#hashCode()}.
   * 
   * @param object
   *          The object to hash.
   * @return A hashcode for {@code object}.
   * @throws IllegalStateException
   *           If an error occurs while performing reflection operations.
   */
    static int hashCode(Object object) {
        if (object == null) return 0;
        int hashCode = 17;
        for (Method method : getAccessors(object.getClass())) {
            try {
                Object result = method.invoke(object);
                if (result != null) hashCode = hashCode * 31 + result.hashCode();
            } catch (Exception e) {
                throw new IllegalStateException("Unable to reflectively invoke " + method + " on " + object, e);
            }
        }
        return hashCode;
    }

    /**
   * Reflection-based implementation of {@link Object#equals(Object)}.
   * 
   * @param object1
   *          One object to compare.
   * @param object2
   *          Another object to compare.
   * @return {@code true} if the objects are equal, {@code false} otherwise.
   * @throws IllegalStateException
   *           If an error occurs while performing reflection operations.
   */
    static boolean equals(Object object1, Object object2) {
        if (object1 == null && object2 == null) return true;
        if (!(object1 != null && object2 != null)) return false;
        if (!(object1.getClass().isInstance(object2) || object2.getClass().isInstance(object1))) return false;
        Set<Method> accessorMethodsIntersection = new HashSet<Method>(getAccessors(object1.getClass()));
        accessorMethodsIntersection.retainAll(getAccessors(object2.getClass()));
        for (Method method : accessorMethodsIntersection) {
            try {
                Object result1 = method.invoke(object1);
                Object result2 = method.invoke(object2);
                if (result1 == null && result2 == null) continue;
                if (!(result1 != null && result2 != null)) return false;
                if (!result1.equals(result2)) return false;
            } catch (Exception e) {
                throw new IllegalStateException("Unable to reflectively invoke " + method, e);
            }
        }
        return true;
    }
}
