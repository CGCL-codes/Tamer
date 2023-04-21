package org.juddi.util;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The idea for most of this code was taken from the Apache
 * (Jakarta) Log4j project: http://jakarta.apache.org/log4j
 * 
 *  - Steve
 * 
 * @author Ceki G&uuml;lc&uuml;
 * @author anou_mana;
*/
public class Loader {

    private static Log log = LogFactory.getLog(Loader.class);

    /**
   * @param resource
   * @return InputStream to the named resource
   */
    public static InputStream getResourceAsStream(String resource) {
        ClassLoader classLoader = null;
        InputStream stream = null;
        try {
            classLoader = getContextClassLoader();
            if (classLoader != null) {
                log.debug("Trying to find [" + resource + "] using context classloader " + classLoader + ".");
                stream = classLoader.getResourceAsStream(resource);
                if (stream != null) {
                    return stream;
                }
            }
            classLoader = Loader.class.getClassLoader();
            if (classLoader != null) {
                log.debug("Trying to find [" + resource + "] using " + classLoader + " class loader.");
                stream = classLoader.getResourceAsStream(resource);
                if (stream != null) {
                    return stream;
                }
            }
        } catch (Throwable th) {
            log.warn("Exception thrown from Loader.getResource(\"" + resource + "\").", th);
        }
        log.debug("Trying to find [" + resource + "] using ClassLoader.getSystemResource().");
        return ClassLoader.getSystemResourceAsStream(resource);
    }

    /**
   * @param resource name
   * @return URL to the named resource
   */
    public static URL getResource(String resource) {
        ClassLoader classLoader = null;
        URL url = null;
        try {
            classLoader = getContextClassLoader();
            if (classLoader != null) {
                log.debug("Trying to find [" + resource + "] using context classloader " + classLoader + ".");
                url = classLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
            classLoader = Loader.class.getClassLoader();
            if (classLoader != null) {
                log.debug("Trying to find [" + resource + "] using " + classLoader + " class loader.");
                url = classLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
        } catch (Throwable th) {
            log.warn("Exception thrown from Loader.getResource(\"" + resource + "\").", th);
        }
        log.debug("Trying to find [" + resource + "] using ClassLoader.getSystemResource().");
        return ClassLoader.getSystemResource(resource);
    }

    /**
   * Get the Thread Context Class Loader which is only available 
   * in JDK 1.2 and later.     
   * @return null if running under a JDK that's earlier than 1.2
   **/
    private static ClassLoader getContextClassLoader() throws IllegalAccessException, InvocationTargetException {
        Method method = null;
        try {
            method = Thread.class.getMethod("getContextClassLoader", null);
        } catch (NoSuchMethodException e) {
            return null;
        }
        return (ClassLoader) method.invoke(Thread.currentThread(), null);
    }

    public static Class getClassForName(String name) throws ClassNotFoundException, NoClassDefFoundError {
        Class clazz = null;
        try {
            log.info("Using the Context ClassLoader");
            ClassLoader ccl = Thread.currentThread().getContextClassLoader();
            clazz = Class.forName(name, true, ccl);
        } catch (Exception e) {
            log.warn("Failed to load the class " + name + " with context class loader " + e);
        }
        if (null == clazz) {
            ClassLoader scl = ClassLoader.getSystemClassLoader();
            try {
                log.info("Using the System ClassLoader");
                clazz = Class.forName(name, true, scl);
            } catch (Exception e) {
                log.warn("Failed to load the class " + name + " with system class loader " + e);
            }
        }
        return clazz;
    }
}
