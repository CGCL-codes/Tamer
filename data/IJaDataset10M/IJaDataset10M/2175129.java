package org.opennms.netmgt.threshd;

import java.util.List;
import java.util.Map;
import org.opennms.netmgt.collectd.CollectionAttribute;
import org.opennms.netmgt.collectd.CollectionResource;
import org.opennms.netmgt.model.RrdRepository;
import org.opennms.netmgt.xml.event.Event;

/**
 * @author <a href="mailto:agalue@opennms.org">Alejandro Galue</a>
 *
 */
public class CollectorThresholdingSet extends ThresholdingSet {

    public CollectorThresholdingSet(int nodeId, String hostAddress, String serviceName, RrdRepository repository, long interval) {
        super(nodeId, hostAddress, serviceName, repository, interval);
    }

    public boolean hasThresholds(CollectionAttribute attribute) {
        CollectionResource resource = attribute.getResource();
        return hasThresholds(resource.getResourceTypeName(), attribute.getName());
    }

    public List<Event> applyThresholds(CollectionResource resource, Map<String, CollectionAttribute> attributesMap) {
        CollectionResourceWrapper resourceWrapper = new CollectionResourceWrapper(m_interval, m_nodeId, m_hostAddress, m_serviceName, m_repository, resource, attributesMap);
        return applyThresholds(resourceWrapper, attributesMap);
    }

    @Override
    protected boolean passedThresholdFilters(CollectionResourceWrapper resource, ThresholdEntity thresholdEntity) {
        if (resource.isAnInterfaceResource() && !resource.isValidInterfaceResource()) {
            log().info("passedThresholdFilters: Could not get data interface information for '" + resource.getIfLabel() + "' or this interface has an invalid ifIndex.  Not evaluating threshold.");
            return false;
        }
        return super.passedThresholdFilters(resource, thresholdEntity);
    }
}
