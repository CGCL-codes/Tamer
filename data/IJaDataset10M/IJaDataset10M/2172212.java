package org.opennms.web.svclayer.outage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsMonitoredService;
import org.opennms.netmgt.model.OnmsOutage;
import org.opennms.netmgt.model.OnmsServiceType;

/**
 * 
 * @author <a href="mailto:joed@opennms.org">Johan Edstrom</a>
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 */
public class OutageListBuilder {

    public List<Map<String, Object>> theTable(Collection<OnmsOutage> foundOutages) {
        List<Map<String, Object>> theTable = new ArrayList<Map<String, Object>>();
        for (OnmsOutage outage : foundOutages) {
            OnmsMonitoredService monitoredService = outage.getMonitoredService();
            OnmsServiceType serviceType = monitoredService.getServiceType();
            OnmsIpInterface ipInterface = monitoredService.getIpInterface();
            Map<String, Object> outagerow = new HashMap<String, Object>();
            outagerow.put("outage", outage);
            outagerow.put("outageid", outage.getId());
            outagerow.put("node", ipInterface.getNode().getLabel());
            outagerow.put("nodeid", monitoredService.getNodeId());
            outagerow.put("ipaddr", ipInterface.getIpAddress());
            outagerow.put("interfaceid", ipInterface.getId());
            outagerow.put("ifserviceid", monitoredService.getId());
            outagerow.put("service", serviceType.getName());
            outagerow.put("serviceid", serviceType.getId());
            outagerow.put("eventid", outage.getServiceLostEvent().getId());
            if (outage.getIfLostService() != null) {
                outagerow.put("iflostservice", outage.getIfLostService());
                outagerow.put("iflostservicelong", outage.getIfLostService().getTime());
            }
            if (outage.getIfRegainedService() != null) {
                outagerow.put("ifregainedservice", outage.getIfRegainedService());
                outagerow.put("ifregainedservicelong", outage.getIfRegainedService().getTime());
            }
            if (outage.getSuppressTime() != null) {
                outagerow.put("suppresstime", outage.getSuppressTime());
            }
            outagerow.put("suppressedby", outage.getSuppressedBy());
            theTable.add(outagerow);
        }
        return theTable;
    }
}
