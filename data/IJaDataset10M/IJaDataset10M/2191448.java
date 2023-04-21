package org.opennms.netmgt.collectd;

import java.util.List;
import org.opennms.netmgt.config.collector.CollectionResource;
import org.opennms.netmgt.config.datacollection.Parameter;

/**
 * PersistAllSelectorStrategy (default implementation of the PersistenceSelectorStrategy interface).
 * 
 * @author <a href="mail:agalue@opennms.org">Alejandro Galue</a>
 */
public class PersistAllSelectorStrategy implements PersistenceSelectorStrategy {

    @Override
    public boolean shouldPersist(CollectionResource resource) {
        return true;
    }

    @Override
    public void setParameters(List<Parameter> parameterCollection) {
    }
}
