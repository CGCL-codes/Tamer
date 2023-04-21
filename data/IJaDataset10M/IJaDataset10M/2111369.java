package org.tanso.fountain.core.nodeagent;

import org.tanso.fountain.core.component.container.ComponentContainer;
import org.tanso.fountain.core.component.container.PlatformServiceContainer;

/**
 * Used by the component containers, to access kernel APIs. <br />
 * Generally, each container will have a different connector instance
 * 
 * @author Haiping Huang
 */
public interface KernelConnector {

    /**
	 * Get a the platform services' container.
	 * 
	 * @return The platform services' container.
	 */
    PlatformServiceContainer getPlatformServiceContainer();

    /**
	 * Get the components' container.
	 * 
	 * @return The component container.
	 */
    ComponentContainer getComponentContainer();

    /**
	 * Get the id of the local node
	 * 
	 * @return The nodeId
	 */
    String getLocalNodeId();

    /**
	 * Get the local ip address
	 * 
	 * @return The ip address in string
	 */
    String getLocalIPAddress();

    /**
	 * Get a new kernel connector instance.
	 * 
	 * @return A new kernel connector instance.
	 */
    KernelConnector createNewKernelConnector();

    /**
	 * Get the repository path for the components
	 * 
	 * @return repository path for the components
	 */
    String getComponentRepositoryPath();

    /**
	 * Get the frameworks classloader
	 * 
	 * @return the framework classloader.
	 */
    ClassLoader getFrameworkClassLoader();

    /**
	 * Request the framework to shut down.
	 * 
	 * @param requestor
	 *            The requestor's object intance.
	 * @return true if the request will be performed later. else false
	 */
    boolean requestShutDown(Object requestor);
}
