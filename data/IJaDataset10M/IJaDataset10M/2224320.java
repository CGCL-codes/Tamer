package org.jawa.util;

import java.io.InputStream;
import java.net.URL;

/**
 * A utility class to assist with loading classes or resources by name. Many application servers use
 * custom classloaders, which will break uses of:
 * <pre>
 *    Class.forName(className);
 * </pre>
 *
 * This utility attempts to load the class or resource using a number of different mechanisms to
 * work around this problem.
 *
 * @author JAWA
 */
public class ClassUtils {

    private static ClassUtils instance = new ClassUtils();

    /**
     * Loads the class with the specified name.
     *
     * @param className the name of the class
     * @return the resulting <code>Class</code> object
     * @throws ClassNotFoundException if the class was not found
     */
    public static Class forName(String className) throws ClassNotFoundException {
        return instance.loadClass(className);
    }

    /**
     * Loads the given resource as a stream.
     *
     * @param name the name of the resource that exists in the classpath.
     * @return the resource as an input stream or <tt>null</tt> if the resource was not found.
     */
    public static InputStream getResourceAsStream(String name) {
        return instance.loadResource(name);
    }

    /**
     * 获取指定名称的URL
     * @param name <tt>指定的 String型 名称</tt>
     * @return <tt>返回URL对象</tt>
     */
    public static URL getResourceURL(String name) {
        return instance.loadResourceURL(name);
    }

    /**
     * 构造函数
     */
    private ClassUtils() {
    }

    public Class loadClass(String className) throws ClassNotFoundException {
        Class theClass = null;
        try {
            theClass = Class.forName(className);
        } catch (ClassNotFoundException e1) {
            try {
                theClass = Thread.currentThread().getContextClassLoader().loadClass(className);
            } catch (ClassNotFoundException e2) {
                theClass = getClass().getClassLoader().loadClass(className);
            }
        }
        return theClass;
    }

    private InputStream loadResource(String name) {
        InputStream in = getClass().getResourceAsStream(name);
        if (in == null) {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
            if (in == null) {
                in = getClass().getClassLoader().getResourceAsStream(name);
            }
        }
        return in;
    }

    private URL loadResourceURL(String name) {
        URL in = null;
        if (in == null) {
            in = Thread.currentThread().getContextClassLoader().getResource(name);
            if (in == null) {
                in = getClass().getClassLoader().getResource(name);
            }
        }
        return in;
    }
}
