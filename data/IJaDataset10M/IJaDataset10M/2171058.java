package sun.reflect.misc;

import java.lang.reflect.Modifier;
import sun.reflect.Reflection;

public final class ReflectUtil {

    private ReflectUtil() {
    }

    public static Class forName(String name) throws ClassNotFoundException {
        checkPackageAccess(name);
        return Class.forName(name);
    }

    public static Object newInstance(Class cls) throws InstantiationException, IllegalAccessException {
        checkPackageAccess(cls);
        return cls.newInstance();
    }

    public static void ensureMemberAccess(Class currentClass, Class memberClass, Object target, int modifiers) throws IllegalAccessException {
        if (target == null && Modifier.isProtected(modifiers)) {
            int mods = modifiers;
            mods = mods & (~Modifier.PROTECTED);
            mods = mods | Modifier.PUBLIC;
            Reflection.ensureMemberAccess(currentClass, memberClass, target, mods);
            try {
                mods = mods & (~Modifier.PUBLIC);
                Reflection.ensureMemberAccess(currentClass, memberClass, target, mods);
                return;
            } catch (IllegalAccessException e) {
                if (isSubclassOf(currentClass, memberClass)) {
                    return;
                } else {
                    throw e;
                }
            }
        } else {
            Reflection.ensureMemberAccess(currentClass, memberClass, target, modifiers);
        }
    }

    private static boolean isSubclassOf(Class queryClass, Class ofClass) {
        while (queryClass != null) {
            if (queryClass == ofClass) {
                return true;
            }
            queryClass = queryClass.getSuperclass();
        }
        return false;
    }

    public static void checkPackageAccess(Class clazz) {
        checkPackageAccess(clazz.getName());
    }

    public static void checkPackageAccess(String name) {
        SecurityManager s = System.getSecurityManager();
        if (s != null) {
            String cname = name.replace('/', '.');
            if (cname.startsWith("[")) {
                int b = cname.lastIndexOf('[') + 2;
                if (b > 1 && b < cname.length()) {
                    cname = cname.substring(b);
                }
            }
            int i = cname.lastIndexOf('.');
            if (i != -1) {
                s.checkPackageAccess(cname.substring(0, i));
            }
        }
    }

    public static boolean isPackageAccessible(Class clazz) {
        try {
            checkPackageAccess(clazz);
        } catch (SecurityException e) {
            return false;
        }
        return true;
    }
}
