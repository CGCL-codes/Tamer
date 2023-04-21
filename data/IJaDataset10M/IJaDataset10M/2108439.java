package org.flow.framework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {

    public static String getGetterName(Field f) {
        return getGetterName(f.getName());
    }

    public static String getGetterName(String fieldName) {
        String getterName = "get";
        getterName += fieldName.substring(0, 1).toUpperCase();
        getterName += fieldName.substring(1);
        return getterName;
    }

    public static String getSetterName(Field f) {
        return getSetterName(f.getName());
    }

    public static String getSetterName(String fieldName) {
        String getterName = "set";
        getterName += fieldName.substring(0, 1).toUpperCase();
        getterName += fieldName.substring(1);
        return getterName;
    }

    public static void inject(Field field, Object subject, Object obj) throws InvocationTargetException, IllegalAccessException {
        Class clazz = field.getDeclaringClass();
        try {
            Method setter = clazz.getDeclaredMethod(getSetterName(field), new Class[] { field.getType() });
            setter.invoke(subject, new Object[] { obj });
        } catch (NoSuchMethodException e) {
            field.setAccessible(true);
            field.set(subject, obj);
        }
    }
}
