package com.dyuproject.util.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Util for java reflections
 * 
 * @author David Yu
 * @created Mar 14, 2008
 */
public final class ReflectUtil {

    static final String GET = "get";

    static final String IS = "is";

    static final String SET = "set";

    static final String GET_INSTANCE = "getInstance";

    private static final Class<?>[] __emptyArg = new Class<?>[] {};

    private static final Object[] __getterArg = new Object[] {};

    /**
     * Gets the getters of a pojo as a map of {@link String} as key and 
     * {@link Method} as value.
     */
    public static Map<String, Method> getGetterMethods(Class<?> pojoClass) {
        HashMap<String, Method> methods = new HashMap<String, Method>();
        fillGetterMethods(pojoClass, methods);
        return methods;
    }

    private static void fillGetterMethods(Class<?> pojoClass, Map<String, Method> baseMap) {
        if (pojoClass.getSuperclass() != Object.class) fillGetterMethods(pojoClass.getSuperclass(), baseMap);
        Method[] methods = pojoClass.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            if (!Modifier.isStatic(m.getModifiers()) && m.getParameterTypes().length == 0 && m.getReturnType() != null && Modifier.isPublic(m.getModifiers())) {
                String name = m.getName();
                if (name.startsWith(IS)) baseMap.put(toProperty(IS.length(), name), m); else if (name.startsWith(GET)) baseMap.put(toProperty(GET.length(), name), m);
            }
        }
    }

    /**
     * Gets the setters of a pojo as a map of {@link String} as key and 
     * {@link Method} as value.
     */
    public static Map<String, Method> getSetterMethods(Class<?> pojoClass) {
        HashMap<String, Method> methods = new HashMap<String, Method>();
        fillSetterMethods(pojoClass, methods);
        return methods;
    }

    private static void fillSetterMethods(Class<?> pojoClass, Map<String, Method> baseMap) {
        if (pojoClass.getSuperclass() != Object.class) fillSetterMethods(pojoClass.getSuperclass(), baseMap);
        Method[] methods = pojoClass.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            if (!Modifier.isStatic(m.getModifiers()) && m.getParameterTypes().length == 1 && m.getName().startsWith(SET) && Modifier.isPublic(m.getModifiers())) {
                baseMap.put(toProperty(SET.length(), m.getName()), m);
            }
        }
    }

    /**
     * Converts a method name into a camel-case field name, starting from {@code start}.
     */
    public static String toProperty(int start, String methodName) {
        char[] prop = new char[methodName.length() - start];
        methodName.getChars(start, methodName.length(), prop, 0);
        int firstLetter = prop[0];
        prop[0] = (char) (firstLetter < 91 ? firstLetter + 32 : firstLetter);
        return new String(prop);
    }

    /**
     * Converts a method name into a camel-case field name, starting from {@code start}.
     */
    public static String toField(int start, String methodName) {
        return toProperty(start, methodName);
    }

    /**
     * Tries to invoke Foo.getInstance() if the method (public+static) getInstance() is there.
     */
    public static Object getInstance(Class<?> clazz) {
        Method m = null;
        try {
            m = clazz.getDeclaredMethod(GET_INSTANCE, __emptyArg);
        } catch (Exception e) {
            try {
                m = clazz.getDeclaredMethod("get" + clazz.getSimpleName(), __emptyArg);
            } catch (Exception e1) {
                return null;
            }
        }
        if (m != null && Modifier.isStatic(m.getModifiers()) && Modifier.isPublic(m.getModifiers())) {
            try {
                return m.invoke(null, __getterArg);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Tries to invoke Foo.getInstance() if the method (public+static) getInstance() is there; 
     * If not, it creates a new instance via reflection.
     */
    public static Object newInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        Object o = getInstance(clazz);
        return o == null ? clazz.newInstance() : o;
    }
}
