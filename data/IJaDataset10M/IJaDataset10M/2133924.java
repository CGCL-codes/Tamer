package com.wuming.lifehelper.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.apache.log4j.Logger;

public class GenericsUtils {

    private static final Logger log = Logger.getLogger(GenericsUtils.class);

    private GenericsUtils() {
    }

    /**
     * Through reflection , get generic parameters's type of declared parent
     * class when class is defined
     * 
     * @param clazz
     *            The class to introspect
     * @return the first generic declaration, or <code>Object.class</code> if
     *         cannot be determined
     */
    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenricType(Class clazz) {
        return getSuperClassGenericType(clazz, 0);
    }

    /**
     * Through reflection , get generic parameters's type of declared parent
     * class when class is defined
     * 
     * @param clazz
     *            clazz The class to introspect
     * @param index
     *            the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or <code>Object.class</code> if
     *         cannot be determined
     */
    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenericType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            log.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            log.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            log.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }
        return (Class) params[index];
    }
}
