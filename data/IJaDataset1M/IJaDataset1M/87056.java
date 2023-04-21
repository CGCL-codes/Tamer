package org.slasoi.ss.v11.poc.impl.negotiation;

import java.util.Collection;
import org.slasoi.infrastructure.servicemanager.types.ProvisionRequestType;
import org.slasoi.infrastructure.servicemanager.types.ReservationResponseType;
import org.slasoi.monitoring.common.configuration.MonitoringSystemConfiguration;
import org.slasoi.monitoring.common.features.ComponentMonitoringFeatures;
import org.slasoi.ss.v11.poc.impl.datastructure.Request;

/**
 * The implementation of interface <code>ServiceManagerHandler</code>
 * 
 * @see org.slasoi.isslam.poc.servicesmanager.ServiceManagerHandler
 * @author Kuan Lu
 */
public class ServiceManagerHandlerImpl {

    public static ServiceManagerHandlerImpl instance;

    /**
     * Gets the instance of infrastructure service manager handler.
     */
    public static ServiceManagerHandlerImpl getInstance() {
        if (ServiceManagerHandlerImpl.instance == null) {
            ServiceManagerHandlerImpl.instance = new ServiceManagerHandlerImpl();
            return ServiceManagerHandlerImpl.instance;
        } else return ServiceManagerHandlerImpl.instance;
    }

    public ComponentMonitoringFeatures[] getMonitoringFeature() {
        return null;
    }

    public ProvisionRequestType generateProvisionRequestType(Collection<Request> requestList, MonitoringSystemConfiguration monitoringConfig) {
        return new ProvisionRequestType();
    }

    /**
     * Queries the resource capability of service manager.
     */
    public ProvisionRequestType query(ProvisionRequestType temp) {
        return new ProvisionRequestType();
    }

    /**
     * Reserves the resources from infrastructure service manager.
     */
    public ReservationResponseType reserve(ProvisionRequestType temp) {
        return new ReservationResponseType();
    }
}
