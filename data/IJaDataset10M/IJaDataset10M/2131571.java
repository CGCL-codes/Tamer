package com.googlecode.agscrum.model.util;

import java.lang.reflect.Field;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

public class ReflectionUtil {

    @SuppressWarnings("unchecked")
    public static String getFieldNameID(Class clazz) {
        if (clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    try {
                        return field.getName();
                    } catch (Exception e) {
                        return null;
                    }
                }
            }
        }
        if (clazz.getSuperclass() != Object.class) {
            return getFieldNameID(clazz.getSuperclass());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Object getValueField(Class clazz, Object object, String fieldName) {
        if (clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(fieldName)) {
                    field.setAccessible(true);
                    try {
                        return field.get(object);
                    } catch (Exception e) {
                        return null;
                    }
                }
            }
        }
        if (clazz.getSuperclass() != Object.class) {
            return getValueField(clazz.getSuperclass(), object, fieldName);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static String getQueryString(String namedQuery, Class class1) {
        try {
            NamedQueries namedQueries = (NamedQueries) class1.getAnnotation(NamedQueries.class);
            NamedQuery[] naQueries = namedQueries.value();
            String queryString = null;
            for (NamedQuery namedQuery2 : naQueries) {
                if (namedQuery2.name().equals(namedQuery)) {
                    queryString = namedQuery2.query();
                    break;
                }
            }
            return queryString;
        } catch (Exception e) {
            throw new IllegalArgumentException("NamedQuery not found:" + class1.getCanonicalName());
        }
    }

    @SuppressWarnings("unchecked")
    public static Object getValueID(Class clazz, Object object) {
        if (clazz != Object.class) {
            Field[] campos = clazz.getDeclaredFields();
            for (Field field : campos) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    try {
                        return field.get(object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (clazz.getSuperclass() != Object.class) {
            return getValueID(clazz.getSuperclass(), object);
        }
        return null;
    }
}
