package org.dbe.servent.filter;

import java.util.Properties;
import org.dbe.servent.core.ComponentManager;

/**
 * Filter context passed to the Fiter during the the init execution.
 * 
 * @author bob 
 */
public interface FilterContext {

    /**
     * Return the name of the filter
     * 
     * @return name of the filter
     */
    public String getName();

    /**
     * Return the value parameter of null if parameter is not found
     * 
     * @param key name of the parameter
     * @return value of the parameter
     */
    public String getParameter(String key);

    /**
     * Obtain a service provided by the servent network, local or remote.
     * @param interfaceClass The interface of the service to retrieve
     * @param smid SMID of the service to obtain
     * @return A class that represents the service and complies with the interface
     */
    public Object getService(Class interfaceClass, String smid);

    /**
     * Obtain the ComponentManager instance to get access to the components
     * @return the ComponentManager
     */
    public ComponentManager getComponentManager();

    public String getProperty(String string);

    public Properties getProperties();
}
