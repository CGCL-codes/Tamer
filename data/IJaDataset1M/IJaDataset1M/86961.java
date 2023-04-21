package org.openbbs.util;

/**
 * @author stefan
 */
public class ClassnameHelper {

    public static String nameWithoutPackage(Class<?> clazz) {
        if (clazz == null) return "null";
        int lastDotIndex = clazz.getName().lastIndexOf('.');
        if (lastDotIndex == -1) return clazz.getName();
        return clazz.getName().substring(lastDotIndex + 1);
    }
}
