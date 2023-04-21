package org.actioncenters.core.spring;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Keeps the application contexts so that we onle create one instance per config file.
 * 
 * @author dougk
 */
public class ApplicationContextHelper {

    /**
     * The constructor should never be called.
     */
    protected ApplicationContextHelper() {
        throw new UnsupportedOperationException();
    }

    /** A map of the spring application contexts. */
    private static Map<String, ApplicationContext> applicationContexts = new HashMap<String, ApplicationContext>();

    /**
     * Gets the application contexts.
     * 
     * @return the applicationContexts
     */
    private static Map<String, ApplicationContext> getApplicationContexts() {
        return applicationContexts;
    }

    /**
     * Gets the application context for the provided config file.
     * 
     * @param configFile
     *            Name of the xml config file.
     * 
     * @return The Application Context.
     */
    public static ApplicationContext getApplicationContext(String configFile) {
        ApplicationContext returnValue = getApplicationContexts().get(configFile);
        if (returnValue == null) {
            returnValue = setNewContext(configFile);
        }
        return returnValue;
    }

    /**
     * Sets a new context.
     * 
     * @param configFile
     *            The name of the xml config file.
     * 
     * @return The application context
     */
    private static synchronized ApplicationContext setNewContext(String configFile) {
        ApplicationContext returnValue = getApplicationContexts().get(configFile);
        if (returnValue == null) {
            returnValue = new ClassPathXmlApplicationContext(configFile);
            if (returnValue != null) {
                getApplicationContexts().put(configFile, returnValue);
            }
        }
        return returnValue;
    }
}
