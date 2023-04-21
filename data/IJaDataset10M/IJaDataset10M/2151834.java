package org.opennms.netmgt.collectd;

import java.util.List;
import org.opennms.netmgt.config.collector.CollectionResource;
import org.opennms.netmgt.config.datacollection.Parameter;

/**
 * PersistenceSelectorStrategy
 * 
 * @author <a href="mail:agalue@opennms.org">Alejandro Galue</a>
 */
public interface PersistenceSelectorStrategy {

    public boolean shouldPersist(CollectionResource resource);

    public void setParameters(List<Parameter> parameterCollection);
}
