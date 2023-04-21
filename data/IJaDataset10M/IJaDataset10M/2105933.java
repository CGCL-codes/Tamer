package org.mobicents.slee.container.deployment.jboss.action;

import javax.slee.ServiceID;
import javax.slee.management.ServiceState;
import org.mobicents.slee.container.management.ServiceManagement;

/**
 * Deployment action to activate a service. Note: If a active service with same
 * name and vendor is found, then the action will instead use the management
 * operation that deactivates and activates a service in one step, providing
 * smooth service upgrade.
 * 
 * @author martins
 * 
 */
public class ActivateServiceAction extends ServiceManagementAction {

    public ActivateServiceAction(ServiceID serviceID, ServiceManagement serviceManagement) {
        super(serviceID, serviceManagement);
    }

    @Override
    public void invoke() throws Exception {
        ServiceID oldVersion = null;
        for (ServiceID activeService : getServiceManagement().getServices(ServiceState.ACTIVE)) {
            if (activeService.getName().equals(getServiceID().getName()) && activeService.getVendor().equals(getServiceID().getVendor())) {
                oldVersion = activeService;
                break;
            }
        }
        if (oldVersion == null) {
            getServiceManagement().activate(getServiceID());
        } else {
            getServiceManagement().deactivateAndActivate(oldVersion, getServiceID());
        }
    }
}
