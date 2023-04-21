package com.reserveamerica.elastica.cluster.factoryimpl.nodelocator;

import com.reserveamerica.elastica.cluster.AbstractFactoryImpl;
import com.reserveamerica.elastica.cluster.ClusterConfigException;
import com.reserveamerica.elastica.cluster.NodeLocator;

/**
 * This node locator factory is responsible for creating DBNodeLocator objects.
 * 
 * @author BStasyszyn
 */
public class FileNodeLocatorFactory extends AbstractFactoryImpl<NodeLocator> {

    private static final String DIRECTORY_ATTRIBUTE = "directory";

    @Override
    public NodeLocator create(String ownerId, Object... args) throws ClusterConfigException {
        return new FileNodeLocator(id, ownerId, getAttribute(DIRECTORY_ATTRIBUTE));
    }
}
