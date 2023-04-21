package junit.extensions;

import java.lang.reflect.Field;

public final class FieldAccessor {

    public static Object get(Object object, String fieldName) {
        if (object == null) throw new RuntimeException("Object passed to FieldAccessor.get is null");
        Class objectClass = object.getClass();
        Exception exception = null;
        while (objectClass != null) {
            try {
                Field field = objectClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(object);
            } catch (Exception ex) {
                exception = ex;
                objectClass = objectClass.getSuperclass();
            }
        }
        exception.printStackTrace();
        throw new RuntimeException("Failed field access: get");
    }

    public static Object getStatic(Class classObject, String fieldName) {
        Exception exception = null;
        while (classObject != null) {
            try {
                Field field = classObject.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(classObject);
            } catch (Exception ex) {
                exception = ex;
                classObject = classObject.getSuperclass();
            }
        }
        exception.printStackTrace();
        throw new RuntimeException("Failed static field access: getStatic");
    }

    public static void set(Object object, String fieldName, Object value) {
        if (object == null) throw new RuntimeException("Object passed to FieldAccessor.set is null");
        Class objectClass = object.getClass();
        Exception exception = null;
        while (objectClass != null) {
            try {
                Field field = objectClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(object, value);
                return;
            } catch (Exception ex) {
                exception = ex;
                objectClass = objectClass.getSuperclass();
            }
        }
        throw new RuntimeException("Failed field access: set", exception);
    }

    public static void setStatic(Class objectClass, String fieldName, Object value) {
        if (objectClass == null) throw new RuntimeException("Object passed to FieldAccessor.setStatic is null");
        Exception exception = null;
        while (objectClass != null) {
            try {
                Field field = objectClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(objectClass, value);
                return;
            } catch (Exception ex) {
                exception = ex;
                objectClass = objectClass.getSuperclass();
            }
        }
        exception.printStackTrace();
        throw new RuntimeException("Failed field access: setStatic");
    }
}
