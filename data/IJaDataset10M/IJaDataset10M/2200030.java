package org.slasoi.softwareservicemanager.impl;

import org.slasoi.models.scm.ServiceBuilder;
import org.slasoi.models.scm.ServiceInstance;
import org.slasoi.softwareservicemanager.management.IManageabilityAgentFacade;
import org.slasoi.softwareservicemanager.provisioning.ServiceState;

/**
 * This class host information about a service. That is the current state of the provisioning, its ManageabilityAgent
 * and service instance.
 * 
 */
public class ServiceInformation {

    /**
     * Current state of the service.
     */
    private ServiceState state;

    /**
     * ManageabilityAgent responsible for the service.
     */
    private IManageabilityAgentFacade manageabilityAgent;

    /**
     * Metadata describing the service instance.
     */
    private ServiceInstance serviceInstance;

    /**
     * Metadata describing the configuration & setup of the service.
     */
    private ServiceBuilder serviceBuilder;

    /**
     * Create a new information object for a given builder.
     * 
     * @param builder
     *            Builder used to create the service.
     */
    public ServiceInformation(final ServiceBuilder builder) {
        state = ServiceState.eBUILDER_CREATED;
        this.serviceBuilder = builder;
    }

    /**
     * Getter for the private state variable.
     * 
     * @return State variable.
     */
    public final ServiceState getState() {
        return state;
    }

    /**
     * Setter for the private state variable.
     * 
     * @param state
     *            New state.
     */
    public final void setState(final ServiceState state) {
        this.state = state;
    }

    /**
     * Getter for private ManageabilityAgent.
     * 
     * @return ManageabilityAgent.
     */
    public final IManageabilityAgentFacade getManageabilityAgent() {
        return manageabilityAgent;
    }

    /**
     * Setter for private ManageabilityAgent.
     * 
     * @param manageabilityAgent
     *            New manageabilityAgent.
     */
    public final void setManageabilityAgent(final IManageabilityAgentFacade manageabilityAgent) {
        this.manageabilityAgent = manageabilityAgent;
    }

    /**
     * Getter for private ServiceInstance.
     * 
     * @return ServiceInstance.
     */
    public final ServiceInstance getServiceInstance() {
        return serviceInstance;
    }

    /**
     * Setter for private ServiceInstance.
     * 
     * @param serviceInstance
     *            New ServiceInstance.
     */
    public final void setServiceInstance(final ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    /**
     * Getter for private ServiceBuilder.
     * 
     * @return ServiceBuilder
     */
    public final ServiceBuilder getServiceBuilder() {
        return serviceBuilder;
    }
}
