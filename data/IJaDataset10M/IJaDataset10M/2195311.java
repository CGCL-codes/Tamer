package com.agimatec.commons.util;

/**
 * <p>Title: Agimatec GmbH</p>
 * <p>Description: Reflection util for class access.</p>
 * <p>Copyright (c) 2007</p>
 * <p>Company: Agimatec GmbH </p>
 *
 * @author Roman Stumm
 */
public class ClassUtils {

    /**
     * @return contextclassloader or the caller classLoader
     */
    public static ClassLoader getClassLoader() {
        final ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return cl == null ? ClassUtils.class.getClassLoader() : cl;
    }

    /**
     * @param className
     * @return a Class
     * @throws ClassNotFoundException
     * @see Class#forName(String)
     */
    public static Class forName(String className) throws ClassNotFoundException {
        return Class.forName(className, true, getClassLoader());
    }
}
