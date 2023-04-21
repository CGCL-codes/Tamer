package javax.xml.transform;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * <p>Implements pluggable Datatypes.</p>
 * 
 * <p>This class is duplicated for each JAXP subpackage so keep it in
 * sync.  It is package private for secure class loading.</p>
 *
 * @author Santiago.PericasGeertsen@sun.com
 */
class FactoryFinder {

    /**
     * Internal debug flag.
     */
    private static boolean debug = false;

    /**
     * Cache for properties in java.home/lib/jaxp.properties
     */
    static Properties cacheProps = new Properties();

    /**
     * Flag indicating if properties from java.home/lib/jaxp.properties 
     * have been cached.
     */
    static boolean firstTime = true;

    /**
     * Security support class use to check access control before
     * getting certain system resources.
     */
    static SecuritySupport ss = new SecuritySupport();

    static {
        try {
            String val = ss.getSystemProperty("jaxp.debug");
            debug = val != null && !"false".equals(val);
        } catch (SecurityException se) {
            debug = false;
        }
    }

    private static void dPrint(String msg) {
        if (debug) {
            System.err.println("JAXP: " + msg);
        }
    }

    /**
     * Attempt to load a class using the class loader supplied. If that fails
     * and fall back is enabled, the current (i.e. bootstrap) class loader is
     * tried. 
     * 
     * If the class loader supplied is <code>null</code>, first try using the
     * context class loader followed by the current (i.e. bootstrap) class
     * loader. 
     */
    private static Class getProviderClass(String className, ClassLoader cl, boolean doFallback) throws ClassNotFoundException {
        try {
            if (cl == null) {
                cl = ss.getContextClassLoader();
                if (cl == null) {
                    throw new ClassNotFoundException();
                } else {
                    return cl.loadClass(className);
                }
            } else {
                return cl.loadClass(className);
            }
        } catch (ClassNotFoundException e1) {
            if (doFallback) {
                return Class.forName(className, true, FactoryFinder.class.getClassLoader());
            } else {
                throw e1;
            }
        }
    }

    /**
     * Create an instance of a class. Delegates to method 
     * <code>getProviderClass()</code> in order to load the class.
     *
     * @param className Name of the concrete class corresponding to the
     * service provider
     *
     * @param cl ClassLoader to use to load the class, null means to use
     * the bootstrap ClassLoader
     *
     * @param doFallback True if the current ClassLoader should be tried as
     * a fallback if the class is not found using cl
     */
    static Object newInstance(String className, ClassLoader cl, boolean doFallback) throws ConfigurationError {
        try {
            Class providerClass = getProviderClass(className, cl, doFallback);
            Object instance = providerClass.newInstance();
            if (debug) {
                dPrint("created new instance of " + providerClass + " using ClassLoader: " + cl);
            }
            return instance;
        } catch (ClassNotFoundException x) {
            throw new ConfigurationError("Provider " + className + " not found", x);
        } catch (Exception x) {
            throw new ConfigurationError("Provider " + className + " could not be instantiated: " + x, x);
        }
    }

    /**
     * Finds the implementation Class object in the specified order.  Main
     * entry point.
     * @return Class object of factory, never null
     *
     * @param factoryId             Name of the factory to find, same as
     *                              a property name
     * @param fallbackClassName     Implementation class name, if nothing else
     *                              is found.  Use null to mean no fallback.
     *
     * Package private so this code can be shared.
     */
    static Object find(String factoryId, String fallbackClassName) throws ConfigurationError {
        dPrint("find factoryId =" + factoryId);
        try {
            String systemProp = ss.getSystemProperty(factoryId);
            if (systemProp != null) {
                dPrint("found system property, value=" + systemProp);
                return newInstance(systemProp, null, true);
            }
        } catch (SecurityException se) {
            if (debug) se.printStackTrace();
        }
        try {
            String factoryClassName = null;
            if (firstTime) {
                synchronized (cacheProps) {
                    if (firstTime) {
                        String configFile = ss.getSystemProperty("java.home") + File.separator + "lib" + File.separator + "jaxp.properties";
                        File f = new File(configFile);
                        firstTime = false;
                        if (ss.doesFileExist(f)) {
                            dPrint("Read properties file " + f);
                            cacheProps.load(ss.getFileInputStream(f));
                        }
                    }
                }
            }
            factoryClassName = cacheProps.getProperty(factoryId);
            if (factoryClassName != null) {
                dPrint("found in $java.home/jaxp.properties, value=" + factoryClassName);
                return newInstance(factoryClassName, null, true);
            }
        } catch (Exception ex) {
            if (debug) ex.printStackTrace();
        }
        Object provider = findJarServiceProvider(factoryId);
        if (provider != null) {
            return provider;
        }
        if (fallbackClassName == null) {
            throw new ConfigurationError("Provider for " + factoryId + " cannot be found", null);
        }
        dPrint("loaded from fallback value: " + fallbackClassName);
        return newInstance(fallbackClassName, null, true);
    }

    private static Object findJarServiceProvider(String factoryId) throws ConfigurationError {
        String serviceId = "META-INF/services/" + factoryId;
        InputStream is = null;
        ClassLoader cl = ss.getContextClassLoader();
        if (cl != null) {
            is = ss.getResourceAsStream(cl, serviceId);
            if (is == null) {
                cl = FactoryFinder.class.getClassLoader();
                is = ss.getResourceAsStream(cl, serviceId);
            }
        } else {
            cl = FactoryFinder.class.getClassLoader();
            is = ss.getResourceAsStream(cl, serviceId);
        }
        if (is == null) {
            return null;
        }
        if (debug) {
            dPrint("found jar resource=" + serviceId + " using ClassLoader: " + cl);
        }
        BufferedReader rd;
        try {
            rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            rd = new BufferedReader(new InputStreamReader(is));
        }
        String factoryClassName = null;
        try {
            factoryClassName = rd.readLine();
            rd.close();
        } catch (IOException x) {
            return null;
        }
        if (factoryClassName != null && !"".equals(factoryClassName)) {
            dPrint("found in resource, value=" + factoryClassName);
            return newInstance(factoryClassName, cl, false);
        }
        return null;
    }

    static class ConfigurationError extends Error {

        private Exception exception;

        /**
         * Construct a new instance with the specified detail string and
         * exception.
         */
        ConfigurationError(String msg, Exception x) {
            super(msg);
            this.exception = x;
        }

        Exception getException() {
            return exception;
        }
    }
}
