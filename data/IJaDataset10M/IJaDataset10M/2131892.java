package org.slasoi.llms.stubs;

import org.apache.log4j.Logger;
import org.slasoi.llms.interfaces.IConfigureMonitoring;
import org.slasoi.llms.manager.Manager;
import org.slasoi.llms.manager.MonitoringProvisioningException;
import org.slasoi.llms.manager.MonitoringRequest;

public class ConfigureMonitoringStub implements IConfigureMonitoring {

    private static Logger log = Logger.getLogger(ConfigureMonitoringStub.class);

    Manager manager;

    private ConfigureMonitoringStub(Manager manager) {
        super();
        this.manager = manager;
        System.out.println("---------------------------- ConfigureMonitoringStub initialized -------------------------------");
        log.debug("ConfigureMonitoringStub initialized.");
    }

    /**
     * Provisioned monitoring mechanisms/capabilites. It TODO
     *
     * @param request The monitoring request.
     * @return requestID
     */
    public Object provision(MonitoringRequest request) throws MonitoringProvisioningException {
        return manager.provision(request);
    }

    /**
     * Provisioned monitoring mechanisms/capabilites from XML request.
     *
     * @param requestXML The monitoring request in XML format (see MonitoringProvisioning.xml example).
     * @return requestID
     */
    public Object provision(String requestXML) throws MonitoringProvisioningException {
        return manager.provision(MonitoringRequest.createFromXML(requestXML));
    }

    public Object reprovision(Object requestID, MonitoringRequest newRequest) throws MonitoringProvisioningException {
        return manager.reprovision(requestID, newRequest);
    }

    public void free(Object requestID) throws MonitoringProvisioningException {
        manager.free(requestID);
    }
}
