package net.ko.inheritance;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import net.ko.kobject.KObject;
import net.ko.utils.Converter;

@SuppressWarnings({ "rawtypes" })
public class KReflectObject {

    public static Object kinvoke(Method method, Object o, Object[] paramsValues) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class[] types = method.getParameterTypes();
        Object[] params = new Object[paramsValues.length];
        for (int i = 0; i < types.length; i++) {
            Object param = (Object) Converter.convert((String) paramsValues[i], types[i]);
            params[i] = param;
        }
        return method.invoke(o, params);
    }

    public static Object kinvoke(String methodName, Object o, Object[] paramsValues) {
        return kinvoke(methodName, o, o.getClass(), paramsValues);
    }

    public static Object kinvoke(String methodName, Object o, Class clazz, Object[] paramsValues) {
        boolean find = false;
        Object ret = new String("");
        int i = 0;
        Method[] methods = clazz.getMethods();
        while (i < methods.length && !find) {
            Method m = methods[i];
            find = (m.getName().equalsIgnoreCase(methodName)) && m.getParameterTypes().length == paramsValues.length;
            if (find) {
                try {
                    ret = kinvoke(m, o, paramsValues);
                    break;
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            i++;
        }
        if (!find) {
            Class sClazz = clazz.getSuperclass();
            if (sClazz != null) ret = kinvoke(methodName, o, clazz.getSuperclass(), paramsValues);
        }
        return ret;
    }

    public static Object kinvoke(String methodName, String className, Object[] paramsValues) throws ClassNotFoundException {
        Class clazz = Class.forName(className);
        return kinvoke(methodName, clazz, clazz, paramsValues);
    }

    public static Object kinvoke(String methodName, Class clazz, Object[] paramsValues) {
        return kinvoke(methodName, (Object) clazz, paramsValues);
    }

    public Object invoke(String methodName, Object[] paramsValues) {
        return invoke(methodName, this.getClass(), paramsValues);
    }

    public Object invoke(String methodName, Class clazz, Object[] paramsValues) {
        return kinvoke(methodName, this, clazz, paramsValues);
    }

    public Object invoke(Method method, Object[] paramsValues) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return kinvoke(method, this, paramsValues);
    }

    public void setAttributes(Map<String, Object> map) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry pairs = (Map.Entry) iterator.next();
            try {
                __setAttribute((String) pairs.getKey(), pairs.getValue());
            } catch (Exception e) {
            }
        }
    }

    public void __setAttribute(String attribute, Object mixValue) throws SecurityException, IllegalAccessException {
        __setAttribute(this, attribute, mixValue);
    }

    public static void __setAttribute(Object o, String attribute, Object mixValue) throws SecurityException, IllegalAccessException {
        Field f = null;
        try {
            try {
                f = getField(o.getClass(), attribute);
                f.setAccessible(true);
                f.set(o, mixValue);
            } catch (NoSuchFieldException nsfE) {
                kinvoke(attribute, o, new Object[] { mixValue });
            }
        } catch (IllegalArgumentException iaE) {
            f.set(o, Converter.convert((String) mixValue, f.getType()));
        }
    }

    private static Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }

    public static ArrayList<String> getFieldNames(Class clazz) throws IllegalArgumentException, IllegalAccessException {
        ArrayList<String> result = new ArrayList<String>();
        for (Field field : clazz.getDeclaredFields()) {
            if (!Modifier.isTransient(field.getModifiers()) || !field.getName().startsWith("_")) {
                result.add(field.getName());
            }
        }
        if (clazz.getSuperclass() != null && !clazz.getSuperclass().getSimpleName().startsWith("KObject")) {
            result.addAll(getFieldNames(clazz.getSuperclass()));
        }
        return result;
    }
}
