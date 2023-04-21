package net.comensus.tools.reflection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.commons.lang.ClassUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import net.comensus.tools.io.FastByteArrayOutputStream;

public class ReflectionTools {

    private ReflectionTools() {
    }

    @SuppressWarnings("unchecked")
    public static List<Class> getClassHierarchy(final Class clazz) {
        if (clazz == null) {
            return Collections.emptyList();
        }
        List<Class> result = new LinkedList<Class>();
        result.add(clazz);
        result.addAll(ClassUtils.getAllSuperclasses(clazz));
        return result;
    }

    public static Map<String, Field> getFieldsMap(final Class<?> clazz) {
        Map<String, Field> map = new HashMap<String, Field>();
        for (final Class<?> c : getClassHierarchy(clazz)) {
            List<Field> fields = Arrays.asList(c.getDeclaredFields());
            for (Field field : fields) {
                map.put(field.getName(), field);
            }
        }
        return map;
    }

    public static Set<Field> getAnnotatedFields(final Class clazz, final Class<? extends Annotation> annotation) {
        Map<String, Field> map = getFieldsMap(clazz);
        Set<Field> result = new HashSet<Field>();
        for (Field field : map.values()) {
            if (field.isAnnotationPresent(annotation)) {
                result.add(field);
            }
        }
        return result;
    }

    public static void setFieldValue(Object object, String fieldName, Object value) {
        Field field = getFieldByName(object.getClass(), fieldName);
        setFieldValue(object, field, value);
    }

    public static void setFieldValue(Object object, Field field, Object value) {
        boolean accessible = field.isAccessible();
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            field.setAccessible(accessible);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object object, String fieldName) {
        Field field = getFieldByName(object.getClass(), fieldName);
        boolean accessible = field.isAccessible();
        try {
            field.setAccessible(true);
            return (T) field.get(object);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException();
        } finally {
            field.setAccessible(accessible);
        }
    }

    private static Field getFieldByName(Class clazz, String fieldName) {
        Map<String, Field> fieldsMap = getFieldsMap(clazz);
        if (!fieldsMap.containsKey(fieldName)) {
            throw new IllegalArgumentException("  no such filed named  " + fieldName + " was found! ");
        }
        return fieldsMap.get(fieldName);
    }

    public static boolean isPrimitive(Class type) {
        return primitiveTypeFor(type) != null;
    }

    public static Class primitiveTypeFor(Class wrapper) {
        if (wrapper == Boolean.class) {
            return Boolean.TYPE;
        }
        if (wrapper == Byte.class) {
            return Byte.TYPE;
        }
        if (wrapper == Character.class) {
            return Character.TYPE;
        }
        if (wrapper == Short.class) {
            return Short.TYPE;
        }
        if (wrapper == Integer.class) {
            return Integer.TYPE;
        }
        if (wrapper == Long.class) {
            return Long.TYPE;
        }
        if (wrapper == Float.class) {
            return Float.TYPE;
        }
        if (wrapper == Double.class) {
            return Double.TYPE;
        }
        if (wrapper == Void.class) {
            return Void.TYPE;
        }
        return null;
    }

    public static Object copy(Object orig) {
        Object obj = null;
        try {
            FastByteArrayOutputStream fbos = new FastByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(fbos);
            out.writeObject(orig);
            out.flush();
            out.close();
            ObjectInputStream in = new ObjectInputStream(fbos.getInputStream());
            obj = in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }
}
