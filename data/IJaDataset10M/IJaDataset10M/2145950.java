package quicklunch.e1.goodies.utils;

public abstract class ClassUtils {

    public static boolean instanceOf(Class clazz, Object obj) {
        if (obj == null || clazz == null) {
            return false;
        }
        return clazz.isInstance(obj);
    }
}
