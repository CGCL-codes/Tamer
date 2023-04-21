package org.artofsolving.jodconverter;

import java.lang.reflect.Field;

public class ReflectionUtils {

    private ReflectionUtils() {
        throw new AssertionError("utility class must not be instantiated");
    }

    public static Object getPrivateField(Object instance, String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        return getPrivateField(instance.getClass(), instance, fieldName);
    }

    public static Object getPrivateField(Class<?> type, Object instance, String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = type.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }
}
