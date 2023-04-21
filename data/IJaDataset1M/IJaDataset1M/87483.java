package com.openorm.execution;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {

    public static boolean fieldExists(Object obj, String fieldName) {
        return findField(obj, fieldName) != null;
    }

    public static Field findField(Object obj, String fieldName) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        Field[] fields = obj.getClass().getFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    public static boolean methodExists(Object obj, String methodName, Class<?>... parameterTypes) {
        return findMethod(obj, methodName, parameterTypes) != null;
    }

    public static Method findMethod(Object obj, String methodName, Class<?>... parameterTypes) {
        Method[] declaredMethods = obj.getClass().getDeclaredMethods();
        for (Method method : declaredMethods) {
            method.setAccessible(true);
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        Method[] methods = obj.getClass().getMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}
