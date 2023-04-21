package net.assimilator.monitor;

import net.assimilator.core.OperationalString;
import net.assimilator.core.ServiceBeanInstance;
import net.assimilator.core.ServiceElement;
import net.assimilator.event.RemoteServiceEvent;
import java.io.Serializable;

/**
 * This class is used to communicate state changes on OperationalString and
 * ServiceElements contained within OperationalString objects that are being
 * monitored by the ProvisionMonitor. Interested event consumers register for event
 * notification(s) and are notified as state changes occur relative to
 * OperationalString and ServiceElements within OperationalString objects.
 *
 * @version $Id: ProvisionMonitorEvent.java 138 2007-04-24 02:01:44Z khartig $
 */
public class ProvisionMonitorEvent extends RemoteServiceEvent implements Serializable {

    /**
     * Unique Event ID
     */
    public static final long ID = 2764185076071141340L;

    /**
     * Indicates that this event has been created and sent as a result of updating
     * a ServiceElement in an OperationalString
     */
    public static final int SERVICE_ELEMENT_UPDATED = 1;

    /**
     * Indicates that this event has been created and sent as a result of
     * incrementing the number of ServiceBean instances in an OperationalString
     */
    public static final int SERVICE_BEAN_INCREMENTED = 2;

    /**
     * Indicates that this event has been created and sent as a result of
     * incrementing the number of ServiceBean instances in an OperationalString
     */
    public static final int SERVICE_BEAN_DECREMENTED = 3;

    /**
     * Indicates that this event has been created and sent as a result of adding
     * a service to an OperationalString
     */
    public static final int SERVICE_ELEMENT_ADDED = 4;

    /**
     * Indicates that this event has been created and sent as a result of removing
     * a service from an OperationalString
     */
    public static final int SERVICE_ELEMENT_REMOVED = 5;

    /**
     * Indicates that this event has been created and sent as a result of deploying
     * an OperationalString
     */
    public static final int OPSTRING_DEPLOYED = 6;

    /**
     * Indicates that this event has been created and sent as a result of
     * undeploying an OperationalString
     */
    public static final int OPSTRING_UNDEPLOYED = 7;

    /**
     * Indicates that this event has been created and sent as a result of
     * updating an OperationalString
     */
    public static final int OPSTRING_UPDATED = 8;

    /**
     * Indicates that this event has been created and sent as a result of updating
     * a ServiceBeanInstance
     */
    public static final int SERVICE_BEAN_INSTANCE_UPDATED = 9;

    /**
     * Indicates that this event has been created and sent as a result of submitting
     * a redeployment request
     */
    public static final int REDEPLOY_REQUEST = 10;

    /**
     * Indicates that this event has been created and sent as a result of a
     * succesfull service provisioning
     */
    public static final int SERVICE_PROVISIONED = 11;

    /**
     * Indicates that this event has been created and sent as a result of a
     * service failure
     */
    public static final int SERVICE_FAILED = 12;

    /**
     * Indicates that this event has been created and sent as a result of
     * changing the primary OperationalStringManager
     */
    public static final int OPSTRING_MGR_CHANGED = 13;

    /**
     * The action for the event
     */
    private int action = 0;

    /**
     * The OperationalString name
     */
    private String opStringName;

    /**
     * The ServiceElement. May be null
     */
    private ServiceElement sElem;

    /**
     * The OperationalString. May be null
     */
    private OperationalString opString;

    /**
     * The Redeployment arguments. May be null
     */
    private Object[] redeploymentParms;

    /**
     * The ServiceBeanInstance. May be null
     */
    private ServiceBeanInstance instance;

    private static final long serialVersionUID = -5450189256974730306L;

    /**
     * Create a ProvisionMonitorEvent for a ServiceElement add, remove or change
     * notification.
     *
     * @param source The source (originator) of the event.
     * @param action The type of ProvisionMonitorEvent.
     * @param sElem  The ServiceElement that changed.
     */
    public ProvisionMonitorEvent(Object source, int action, ServiceElement sElem) {
        super(source);
        if (sElem == null) throw new NullPointerException("sElem is null");
        opStringName = sElem.getOperationalStringName();
        this.action = action;
        this.sElem = sElem;
    }

    /**
     * Create a ProvisionMonitorEvent for an OperationalString deployment or
     * undeployment, update or OperationaStringManager change.
     *
     * @param source   The source (originator) of the event.
     * @param opString The OperationalString undeployed.
     * @param action   defines the action to take.
     */
    public ProvisionMonitorEvent(Object source, int action, OperationalString opString) {
        super(source);
        if (opString == null) throw new NullPointerException("opString is null");
        this.action = action;
        this.opString = opString;
        opStringName = opString.getName();
    }

    /**
     * Create a ProvisionMonitorEvent indicating a ServiceBeanInstance has been
     * updated.
     *
     * @param source       The source (originator) of the event.
     * @param opStringName The name of the OperationalString.
     * @param instance     The ServiceBeanInstance that changed.
     */
    public ProvisionMonitorEvent(Object source, String opStringName, ServiceBeanInstance instance) {
        super(source);
        if (opStringName == null) throw new NullPointerException("opStringName cannot be null");
        if (instance == null) throw new NullPointerException("instance is null");
        this.action = SERVICE_BEAN_INSTANCE_UPDATED;
        this.instance = instance;
        this.opStringName = opStringName;
    }

    /**
     * Create a ProvisionMonitorEvent indicating a service provision or failure
     * notification.
     *
     * @param source       The source (originator) of the event.
     * @param opStringName The name of the OperationalString.
     * @param sElem        The ServiceElement.
     * @param instance     The ServiceBeanInstance.
     * @param action       defines the action to take.
     */
    public ProvisionMonitorEvent(Object source, int action, String opStringName, ServiceElement sElem, ServiceBeanInstance instance) {
        super(source);
        if (opStringName == null) throw new NullPointerException("opStringName cannot be null");
        if (sElem == null) throw new NullPointerException("sElem is null");
        this.action = action;
        this.sElem = sElem;
        this.instance = instance;
        this.opStringName = opStringName;
    }

    /**
     * Create a ProvisionMonitorEvent indicating a redeployment request
     * has been submitted.
     *
     * @param source       The source (originator) of the event.
     * @param opStringName The name of the OperationalString.
     * @param sElem        The ServiceElement.
     * @param instance     The ServiceBeanInstance.
     * @param args         Parameters for a redeployment.
     */
    public ProvisionMonitorEvent(Object source, String opStringName, ServiceElement sElem, ServiceBeanInstance instance, Object[] args) {
        super(source);
        if (opStringName == null) throw new NullPointerException("opStringName cannot be null");
        if (args == null) throw new NullPointerException("redeploymentParms cannot be null");
        this.action = REDEPLOY_REQUEST;
        this.opStringName = opStringName;
        this.sElem = sElem;
        this.instance = instance;
        redeploymentParms = new Object[args.length];
        System.arraycopy(args, 0, redeploymentParms, 0, redeploymentParms.length);
    }

    /**
     * Get the action attribute.
     *
     * @return The action.
     */
    public int getAction() {
        return (action);
    }

    /**
     * Get the OperationalString name.
     *
     * @return The name of the OperationalString associated with this event.
     */
    public String getOperationalStringName() {
        return (opStringName);
    }

    /**
     * Get the ServiceElement attribute.
     *
     * @return The ServiceElement associated with this event. This property
     *         will be null if the action type is not SERVICE_ELEMENT_ADDED,
     *         SERVICE_ELEMENT_UPDATED, SERVICE_ELEMENT_REMOVED, SERVICE_PROVISIONED or
     *         SERVICE_FAILED. This property may be null if the action type is
     *         REDEPLOY_REQUEST.
     */
    public ServiceElement getServiceElement() {
        return (sElem);
    }

    /**
     * Get the OperationalString attribute.
     *
     * @return The OperationalString associated with this event. This property
     *         will be null if the action type is not OPSTRING_DEPLOYED or
     *         OPSTRING_UNDEPLOYED.
     */
    public OperationalString getOperationalString() {
        return (opString);
    }

    /**
     * Get the ServiceBeanInstance.
     *
     * @return The ServiceBeanInstance associated with this event. This property
     *         will be null if the action type is not SERVICE_BEAN_INSTANCE_UPDATED. This
     *         property may be null if the action type is REDEPLOY_REQUEST,
     *         SERVICE_PROVISIONED or SERVICE_FAILED.
     */
    public ServiceBeanInstance getServiceBeanInstance() {
        return (instance);
    }

    /**
     * Get the Redeployment parameters.
     *
     * @return The Redeployment parameters associated with this event. The
     *         Object array will have as it's content
     *         {Date.class, Boolean.class, ServiceProvisionListener.class}. This
     *         property will be null if the action type is not REDEPLOY_REQUEST.
     */
    public Object[] getRedeploymentParms() {
        return (redeploymentParms);
    }
}
