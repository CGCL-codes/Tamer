package org.opennms.netmgt.poller.monitors;

import java.util.Map;
import org.opennms.netmgt.model.PollStatus;
import org.opennms.netmgt.passive.PassiveStatusKeeper;
import org.opennms.netmgt.poller.Distributable;
import org.opennms.netmgt.poller.DistributionContext;
import org.opennms.netmgt.poller.MonitoredService;
import org.opennms.netmgt.poller.ServiceMonitor;

@Distributable(DistributionContext.DAEMON)
public class PassiveServiceMonitor implements ServiceMonitor {

    public void initialize(Map<String, Object> parameters) {
        return;
    }

    public void release() {
        return;
    }

    public void initialize(MonitoredService svc) {
        return;
    }

    public void release(MonitoredService svc) {
        return;
    }

    public PollStatus poll(MonitoredService svc, Map<String, Object> parameters) {
        PollStatus status = PassiveStatusKeeper.getInstance().getStatus(svc.getNodeLabel(), svc.getIpAddr(), svc.getSvcName());
        return status;
    }
}
