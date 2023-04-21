package com.reserveamerica.elastica.cluster.factoryimpl.nodelocator;

import com.reserveamerica.elastica.cluster.AbstractNodeLocator;
import com.reserveamerica.elastica.cluster.ClusterConfigException;
import com.reserveamerica.elastica.cluster.NodeInfo;

/**
 * This node locator is used in place of the actual node locator whose factory class
 * could not be found. This allows the cluster configuration to be instantiated even though all classes 
 * in the configuration could not be resolved. An exception is thrown if this node locator is invoked.
 * 
 * @author bstasyszyn
 */
public class ShellNodeLocator extends AbstractNodeLocator {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ShellNodeLocator.class);

    private final String factoryClass;

    public ShellNodeLocator(String id, String ownerId, String factoryClass) {
        super(id, ownerId);
        this.factoryClass = factoryClass;
    }

    public NodeInfo locate(String nodeId) throws ClusterConfigException {
        log.error("locate - Unable to invoke node locator [" + id + "] for [" + ownerId + "] since the factory class [" + factoryClass + "] could not be found.");
        throw new RuntimeException("Unable to invoke node locator [" + id + "] for [" + ownerId + "] since the factory class [" + factoryClass + "] could not be found.");
    }
}
