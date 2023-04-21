package org.exist.util;

import java.lang.reflect.Method;
import javax.xml.parsers.SAXParserFactory;
import org.apache.log4j.Logger;

/**
 *  Helper class for creating an instance of javax.xml.parsers.SAXParserFactory
 * 
 * @author dizzzz@exist-db.org
 */
public class ExistSAXParserFactory {

    private static final Logger LOG = Logger.getLogger(ExistSAXParserFactory.class);

    public static final String systemProperty = "org.exist.SAXParserFactory";

    /**
     *  Get SAXParserFactory instance specified by factory class name.
     *
     * @param className Full class name of factory
     *
     * @return A Sax parser factory or NULL when not available.
     */
    public static SAXParserFactory getSAXParserFactory(String className) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (Exception ex) {
            LOG.debug(className + ": " + ex.getMessage(), ex);
            return null;
        }
        Method method = null;
        try {
            method = clazz.getMethod("newInstance", (Class[]) null);
        } catch (Exception ex) {
            LOG.debug("Method " + className + ".newInstance not found.", ex);
            return null;
        }
        Object result = null;
        try {
            result = method.invoke(null, (Object[]) null);
        } catch (Exception ex) {
            LOG.debug("Could not invoke method " + className + ".newInstance.", ex);
            return null;
        }
        if (!(result instanceof SAXParserFactory)) {
            LOG.debug("Could not create instance of SAXParserFactory: " + result.toString());
            return null;
        }
        return (SAXParserFactory) result;
    }

    /**
     *  Get instance of a SAXParserFactory. Return factory specified by
     * system property org.exist.SAXParserFactory (if available) otherwise
     * return system default.
     *
     * @return A sax parser factory.
     */
    public static SAXParserFactory getSAXParserFactory() {
        SAXParserFactory factory = null;
        String config = System.getProperty(systemProperty);
        if (config != null) {
            factory = getSAXParserFactory(config);
        }
        if (factory == null) {
            factory = SAXParserFactory.newInstance();
        }
        return factory;
    }
}
