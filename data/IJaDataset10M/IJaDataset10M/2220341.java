package mockit.multicore;

import java.lang.reflect.*;

final class Utilities {

    static <T> T invoke(Object owner, String methodName) {
        Method m;
        try {
            m = owner.getClass().getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        m.setAccessible(true);
        try {
            return (T) m.invoke(owner);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    static void setField(Object owner, String fieldName, Object newValue) {
        setField(owner.getClass(), owner, fieldName, newValue);
    }

    static void setField(Class<?> ownerClass, Object owner, String fieldName, Object newValue) {
        Field f;
        try {
            f = ownerClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        f.setAccessible(true);
        try {
            f.set(owner, newValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
