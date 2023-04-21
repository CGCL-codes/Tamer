package org.jmage.util;

/**
 * ReflectionUtil
 */
public class ReflectionUtil {

    public Object instantiate(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class clazz = Class.forName(className);
        Object o = clazz.newInstance();
        return o;
    }
}
