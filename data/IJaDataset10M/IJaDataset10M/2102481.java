package net.assimilator.core.provision;

import net.assimilator.core.OperationalStringManager;
import net.assimilator.core.ServiceElement;
import net.jini.core.event.RemoteEvent;
import java.io.Serializable;
import java.rmi.MarshalledObject;

/**
 * This event class is sent by a {@link net.assimilator.core.provision.ProvisionManager}
 * after detecting that a specific service requires provisioning. The recipient of
 * this event must attempt to instantiate the service described by the
 * {@link net.assimilator.core.ServiceElement} object.
 */
public class ServiceProvisionEvent extends RemoteEvent implements Serializable {

    private static final long serialVersionUID = -3431066399604114066L;

    /**
     * Unique Event ID
     */
    public static final long ID = -5934673946890420068L;

    /**
     * The OperationalStringManager
     */
    private OperationalStringManager opStringManager;

    /**
     * The ServiceElement
     */
    private ServiceElement svcElement;

    /**
     * Create a ServiceProvisionEvent
     *
     * @param source The event source
     */
    public ServiceProvisionEvent(Object source) {
        super(source, ID, 0, null);
    }

    /**
     * Instantiate a ServiceProvisionEvent
     *
     * @param source          The event source
     * @param opStringManager The OperationalStringMonitor
     * @param svcElement      The ServiceElement
     */
    public ServiceProvisionEvent(Object source, OperationalStringManager opStringManager, ServiceElement svcElement) {
        super(source, ID, 0, null);
        this.opStringManager = opStringManager;
        this.svcElement = svcElement;
    }

    /**
     * Set the ServiceElement object
     *
     * @param svcElement The ServiceElement
     */
    public void setServiceElement(ServiceElement svcElement) {
        this.svcElement = svcElement;
    }

    /**
     * Get the ServiceElement object
     *
     * @return ServiceElement The ServiceElement
     */
    public ServiceElement getServiceElement() {
        return (svcElement);
    }

    /**
     * Set the OperationalStringManager
     *
     * @param opStringManager The OperationalStringManager
     */
    public void setOperationalStringManager(OperationalStringManager opStringManager) {
        this.opStringManager = opStringManager;
    }

    /**
     * Get the OperationalStringManager
     *
     * @return OperationalStringManager The OperationalStringManager
     */
    public OperationalStringManager getOperationalStringManager() {
        return (opStringManager);
    }

    /**
     * Set the sequence number
     *
     * @param seqNum The sequence number
     */
    public void setSequenceNumber(long seqNum) {
        super.seqNum = seqNum;
    }

    /**
     * Set the handback object
     *
     * @param handback The handback object
     */
    public void setHandback(MarshalledObject handback) {
        super.handback = handback;
    }
}
