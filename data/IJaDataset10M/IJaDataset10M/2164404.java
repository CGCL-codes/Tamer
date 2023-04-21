package org.apache.commons.logging.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * Concrete subclass of {@link LogFactory}.
 * </p>
 * 
 * <p>
 * This factory generates instances of {@link AndroidLogger}. It will remember previously created instances for the same name, and will return them on
 * repeated requests to the <code>getInstance()</code> method.
 * </p>
 * 
 * <p>
 * This implementation ignores any configured attributes.
 * </p>
 * 
 * @author Roy Clarkson
 */
public class AndroidLoggerFactory extends LogFactory {

    /**
     * The {@link org.apache.commons.logging.Log}instances that have already been created, keyed by logger name.
     */
    private final Map<String, Log> instances;

    /**
     * The name (<code>org.apache.commons.logging.Log</code>) of the system property identifying our {@link Log} implementation class.
     */
    public static final String LOG_PROPERTY = "org.apache.commons.logging.androidlogger";

    /**
     * Configuration attributes.
     */
    protected Map<String, Object> attributes = new HashMap<String, Object>();

    /**
     * Public no-arguments constructor required by the lookup mechanism.
     */
    public AndroidLoggerFactory() {
        this.instances = new HashMap<String, Log>();
    }

    /**
     * Return the configuration attribute with the specified name (if any), or <code>null</code> if there is no such attribute.
     * 
     * @param name
     *            Name of the attribute to return
     */
    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    /**
     * Return an array containing the names of all currently defined configuration attributes. If there are no such attributes, a zero length array is
     * returned.
     */
    @Override
    public String[] getAttributeNames() {
        List<String> names = new ArrayList<String>();
        Set<String> keys = attributes.keySet();
        names.addAll(keys);
        return names.toArray(new String[attributes.size()]);
    }

    /**
     * Convenience method to derive a name from the specified class and call <code>getInstance(String)</code> with it.
     * 
     * @param clazz
     *            Class for which a suitable Log name will be derived
     * 
     * @exception LogConfigurationException
     *                if a suitable <code>Log</code> instance cannot be returned
     */
    @Override
    public Log getInstance(Class clazz) throws LogConfigurationException {
        return (getInstance(clazz.getName()));
    }

    /**
     * <p>
     * Construct (if necessary) and return a <code>Log</code> instance, using the factory's current set of configuration attributes.
     * </p>
     * 
     * @param name
     *            Logical name of the <code>Log</code> instance to be returned (the meaning of this name is only known to the underlying logging
     *            implementation that is being wrapped)
     * 
     * @exception LogConfigurationException
     *                if a suitable <code>Log</code> instance cannot be returned
     */
    @Override
    public Log getInstance(String name) throws LogConfigurationException {
        Log log = null;
        synchronized (instances) {
            log = instances.get(name);
            if (log == null) {
                log = new AndroidLogger(name);
                instances.put(name, log);
            }
        }
        return log;
    }

    /**
     * Release any internal references to previously created {@link org.apache.commons.logging.Log}instances returned by this factory. This is useful
     * in environments like servlet containers, which implement application reloading by throwing away a ClassLoader. Dangling references to objects
     * in that class loader would prevent garbage collection.
     */
    @Override
    public void release() {
        System.out.println("WARN: The method " + AndroidLoggerFactory.class + "#release() was invoked.");
        System.out.flush();
    }

    /**
     * Remove any configuration attribute associated with the specified name. If there is no such attribute, no action is taken.
     * 
     * @param name
     *            Name of the attribute to remove
     */
    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    /**
     * Set the configuration attribute with the specified name. Calling this with a <code>null</code> value is equivalent to calling
     * <code>removeAttribute(name)</code>.
     * 
     * @param name
     *            Name of the attribute to set
     * @param value
     *            Value of the attribute to set, or <code>null</code> to remove any setting for this attribute
     */
    @Override
    public void setAttribute(String name, Object value) {
        if (value == null) {
            attributes.remove(name);
        } else {
            attributes.put(name, value);
        }
    }
}
