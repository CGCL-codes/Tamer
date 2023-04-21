package net.sf.erm.service.monitoring;

import net.sf.erm.service.netmap.Device;

/**
 * @author lorban
 */
public interface MonitoringServiceProbe {

    Class[] requiredTasksSupport();

    void executeOn(DataCollector collector, Device device) throws DataCollectorException;
}
