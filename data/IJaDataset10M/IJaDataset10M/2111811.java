package org.piuframework.context.jboss;

import org.jboss.system.ServiceMBean;

/**
 * Standard MBean for integration into JBoss
 *
 * @author Dirk Mascher
 */
public interface PiuFrameworkContextMBean extends ServiceMBean {

    public void setApplicationContextName(String jndiName);

    public String getApplicationContextName();

    public void setResourceLoaderClassName(String className);

    public String getResourceLoaderClassName();

    public void setRepositoryPath(String path);

    public String getRepositoryPath();

    /**
     * Reconfigures the ApplicationContext and binds the new context into JNDI
     * @throws Exception
     */
    public void reconfigure() throws Exception;

    /**
     * Changes the default flavor for the specified service.
     * Works only if the service is explicitly registered with the service registry.
     * @param serviceInterfaceName Fully qualified interface name of the service.
     * @param defaultFlavor Symbolic name of new default flavor
     * @throws Exception
     */
    public void changeServiceDefaultFlavor(String serviceInterfaceName, String defaultFlavor) throws Exception;

    /**
     * Returns a HTML representation of the Piu service registry.
     */
    public String listRegisteredServices() throws Exception;
}
