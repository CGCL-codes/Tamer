package net.assimilator.tools.deploymentdirectory.mocks;

import net.assimilator.core.*;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;

/**
 * Description:
 * User: Larry Mitchell
 * Date: Sep 18, 2007
 * Time: 9:00:54 PM
 * Version: $id$
 */
public class MockOperationalStringManager implements OperationalStringManager {

    private MockOperationalString opString;

    public MockOperationalStringManager() {
        opString = new MockOperationalString();
    }

    /**
     * Get the OperationalString the OperationalStringManager is managing
     *
     * @return The OperationalString
     * @throws java.rmi.RemoteException If communication errors occur
     */
    public OperationalString getOperationalString() throws RemoteException {
        return opString;
    }

    /**
     * Whether the OperationalManager is the active managing
     * OperationalStringManager for the OperationalString. The managing
     * OperationalStringManager will actively respond to scenarios where
     * service's contained within this OperationalString need to be allocated,
     * updated, relocated, removed or added. If the OperationalStringManager is
     * not the managing OperationalStringManager, it will observe and record
     * OperationalString transitions but not act on them.
     *
     * @return True if managing, false otherwise
     */
    public boolean isManaging() throws RemoteException {
        return false;
    }

    /**
     * Set the OperationalStringManager managing status based on the active
     * parameter
     *
     * @param active If true, the OperationalStringManager is the active
     *               managing OperationalStringManager for the OperationalString. The managing
     *               OperationalStringManager will actively respond to scenarios where
     *               service's contained within this OperationalString need to be allocated,
     *               updated, relocated, removed or added. If the OperationalStringManager is
     *               not the managing OperationalStringManager, it will observe and record
     *               OperationalString transitions but not act on them.
     */
    public void setManaging(boolean active) throws RemoteException {
    }

    /**
     * Get the deployment Date history
     *
     * @return An array of Date objects documenting the date & time the
     *         OperationalString has been deployed. If has never been deployed (has been
     *         scheduled) then a zero-length array will be returned.
     */
    public Date[] getDeploymentDates() throws RemoteException {
        return new Date[0];
    }

    /**
     * Update the OperationalString that the OperationalStringManager is managing.
     * This involves updating ServiceElement instances, which may include the additon
     * and or removal of ServiceElements in the OperationalString
     * <p/>
     * If the input OperationalString includes nested OperationalStrings, and the
     * nested OperationalString is not deployed, the nested OperationalString will
     * be deployed
     * <p/>
     * If the nested OperationalString has been deployed and is referenced by only
     * the OperationalString being updated, the nested OperationalString will be
     * updated as well
     *
     * @param opstring The OperationalString to update
     * @return If there are errors updating the OperationalString a Map will
     *         be returned with name value pairs associating the service and
     *         corresponding exception attempting to update the OperationalString
     * @throws net.assimilator.core.OperationalStringException
     *          If there are problems updating the
     *          the OperationalString
     */
    public Map updateOperationalString(OperationalString opstring) throws OperationalStringException, RemoteException {
        return null;
    }

    /**
     * This method returns the ServiceElement object for a requested service
     * based on a service's proxy
     *
     * @param proxy The proxy for the Service
     * @return The matching ServiceElement
     * @throws net.assimilator.core.OperationalStringException
     *                                  If the service's proxy is unknown
     * @throws java.rmi.RemoteException If communication errors occur
     */
    public ServiceElement getServiceElement(Object proxy) throws OperationalStringException, RemoteException {
        return null;
    }

    /**
     * This method returns the ServiceElement object for a requested service
     * based on the array of interface (or proxy) classes the service implements
     * as an array of String objects and an optional service name. The
     * OperationalStringManager will locate the ServiceElement by matching the
     * interface (or proxy) names provided and the name (if not null) of the
     * supplied OperationalString.
     *
     * @param interfaces Array of interface (or proxy) classes the service
     *                   implements as an array of String objects
     * @param name       The name of the Service
     * @throws net.assimilator.core.OperationalStringException
     *                                  If the service described does not
     *                                  exist
     * @throws java.rmi.RemoteException If communication errors occur
     */
    public ServiceElement getServiceElement(String[] interfaces, String name) throws OperationalStringException, RemoteException {
        return null;
    }

    /**
     * This method will add a ServiceElement to an OperationalString. Based on
     * the attributes of the ServiceElement, ServiceBean instances will be
     * provisioned to available compute resources based on the capability of the
     * compute resource to meet the operational criteria criteria of the
     * ServiceElement
     *
     * @param sElem The ServiceElement to add
     * @throws net.assimilator.core.OperationalStringException
     *                                  If the service described by the
     *                                  ServiceElement does not exist or the OperationalStringManager is not the
     *                                  managing OperationalStringManager for the OperationalString
     * @throws net.assimilator.core.OperationalStringException
     *                                  If the service described by the
     *                                  ServiceElement does not exist, or there are problems adding the
     *                                  ServiceElement
     * @throws java.rmi.RemoteException If communication errors occur
     */
    public void addServiceElement(ServiceElement sElem) throws OperationalStringException, RemoteException {
    }

    /**
     * This method will modify the ServiceElement attributes of a ServiceElement
     * in the OperationalStringManager. The OperationalStringManager will locate
     * the ServiceElement, and apply the new attributes
     *
     * @param sElem The ServiceElement to update
     * @throws net.assimilator.core.OperationalStringException
     *                                  If the service described by the
     *                                  ServiceElement does not exist, , or there are problems updating the
     *                                  ServiceElement
     * @throws java.rmi.RemoteException If communication errors occur
     */
    public void updateServiceElement(ServiceElement sElem) throws OperationalStringException, RemoteException {
    }

    /**
     * This method will remove a ServiceElement from an OperationalString and
     * optionally terminate all service instances that have been provisioned
     * from the ServiceElement description
     *
     * @param sElem   The ServiceElement to remove
     * @param destroy If true, destroy all services upon removal, otherwise
     *                just remove
     * @throws net.assimilator.core.OperationalStringException
     *                                  If the ServiceElement is null or not
     *                                  being managed by the OperationalStringManager
     * @throws java.rmi.RemoteException If communication errors occur
     */
    public void removeServiceElement(ServiceElement sElem, boolean destroy) throws OperationalStringException, RemoteException {
    }

    /**
     * Get the ServiceBeanInstance objects for a ServiceElement
     *
     * @param sElem The ServiceElement
     * @throws net.assimilator.core.OperationalStringException
     *                                  If the ServiceElement is unknown to the
     *                                  OperationalStringManager
     * @throws java.rmi.RemoteException If communication errors occur
     */
    public ServiceBeanInstance[] getServiceBeanInstances(ServiceElement sElem) throws OperationalStringException, RemoteException {
        return new ServiceBeanInstance[0];
    }

    /**
     * Relocate (move) a ServiceBean instance to another compute resource. If the
     * relocation request cannot be accomplished (no available compute resources to
     * move to), and the {@link net.assimilator.core.ServiceProvisionListener} parameter
     * is not null, the {@link net.assimilator.core.ServiceProvisionListener} will be
     * notified of the result. Additionally if the relocate request cannot be carried
     * out, the request will not be submitted for future processing.
     *
     * @param instance    The ServiceBeanInstance to relocate
     * @param listener    If not null, the ServiceProvisionListener will be
     *                    notified on the result of the attempt to relocate the service
     *                    instance.
     * @param hostAddress The address of the compute resource to relocate to. If
     *                    this parameter is null, the OperationalStringManager will determine a
     *                    suitable compute resource
     * @throws net.assimilator.core.OperationalStringException
     *                                  If the ServiceElement is not being
     *                                  managed by the OperationalStringManager or the service requesting relocation
     *                                  is not a {@link net.assimilator.core.ServiceElement#DYNAMIC} service
     * @throws java.rmi.RemoteException If communication errors occur
     */
    public void relocate(ServiceBeanInstance instance, ServiceProvisionListener listener, String hostAddress) throws OperationalStringException, RemoteException {
    }

    /**
     * Increment (increase) the number of instances by one. This will cause the
     * provisioning of a ServiceBean to an available compute resource which meets
     * the operational criteria specified by the ServiceElement.
     * <p/>
     * If the increment request cannot be accomplished (no available compute
     * resources that meet operational requirements of the service), and the
     * {@link net.assimilator.core.ServiceProvisionListener} parameter is not null,
     * the {@link net.assimilator.core.ServiceProvisionListener} will be notified of
     * the result. Additionally if the increment request cannot be carried
     * out, the request will be submitted for future processing
     *
     * @param sElem     The ServiceElement instance to increment. This parameter
     *                  is used to match a ServiceElement being managed by the
     *                  OperationalStringManager
     * @param permanent If the increment request should be considered permanent. If
     *                  set to false, the number of service instances may vary over time
     * @param listener  If not null, the ServiceProvisionListener will be
     *                  notified on the result of the attempt to increment the amount of service
     *                  instances.
     * @throws net.assimilator.core.OperationalStringException
     *                                  If the ServiceElement is not being
     *                                  managed by the OperationalStringManager (or the OperationalStringManager
     *                                  is not the managing OperationalStringManager for the OperationalString)
     * @throws java.rmi.RemoteException If communication errors occur
     */
    public void increment(ServiceElement sElem, boolean permanent, ServiceProvisionListener listener) throws OperationalStringException, RemoteException {
    }

    /**
     * Decrement (decrease the number of) and remove a specific ServiceBean
     * instance from the OperationalString.
     *
     * @param instance The ServiceBeanInstance
     * @param mandate  If set to true, and the decrement invocation is equal to
     *                 the number of service to maintain, the OperationalStringManager will decrement
     *                 the number of service to maintain. If set to false and the number of service
     *                 instances running greater then the number of services to maintain, the
     *                 OperationalStringManager will decrement the number of services to maintain.
     *                 If the number of active service instances are equal to the number of service
     *                 instances to maintain and the value is false, the decrement will not be allowed.
     * @param destroy  If true, destroy the ServiceBean upon removal, otherwise
     *                 just remove
     * @throws net.assimilator.core.OperationalStringException
     *                                  If the ServiceElement is not being
     *                                  managed by the OperationalStringManager
     * @throws java.rmi.RemoteException If communication errors occur
     */
    public void decrement(ServiceBeanInstance instance, boolean mandate, boolean destroy) throws OperationalStringException, RemoteException {
    }

    /**
     * Get the number of pending service provision requests. This method will only
     * take action if the ServiceElement provision type is
     * ServiceElement.DYNAMIC. Any other provision type will be ignored
     *
     * @param sElem The ServiceElement instance to query. This parameter
     *              is used to match a ServiceElement being managed by the
     *              OperationalStringManager
     * @return The number of pending requests. If the ServiceElement
     *         provision type is not ServiceElement.DYNAMIC OperationalStringManager is not
     *         the managing OperationalStringManager, -1 will be returned
     * @throws java.rmi.RemoteException If communication errors occur
     */
    public int getPendingCount(ServiceElement sElem) throws RemoteException {
        return 0;
    }

    /**
     * Trim (remove) service provision requests which are pending allocation. This
     * method will only take action if the ServiceElement provision type is
     * ServiceElement.DYNAMIC. Any other provision type will be ignored
     *
     * @param sElem  The ServiceElement instance to trim. This parameter
     *               is used to match a ServiceElement being managed by the
     *               OperationalStringManager
     * @param trimUp The number of pending requests to trim. The number of
     *               pending requests to trim will be determined as follows :
     *               <ul>
     *               <li>If the trimUp value is -1, then trim the all pending requests
     *               <li>If the trimUp value is not -1, the number of pending requests to trim will
     *               be calaculated as follows:
     *               <br>
     *               <code>Math.min((planned-actual), trimUp)</code>
     *               <br>
     *               Where planned is the
     *               property obtained from the managed ServiceElement, actual is the number of
     *               active (deployed) services, and trimUp is the input value.
     *               </ul>
     * @return The number of pending requests that were trimmed. The value will be
     *         the new managed ServiceElement planned property. If the ServiceElement
     *         provision type is not ServiceElement.DYNAMIC, -1 will be returned
     * @throws net.assimilator.core.OperationalStringException
     *                                  If the ServiceElement is not being
     *                                  managed by the OperationalStringManager
     * @throws java.rmi.RemoteException If communication errors occur
     */
    public int trim(ServiceElement sElem, int trimUp) throws OperationalStringException, RemoteException {
        return 0;
    }

    /**
     * Update a ServiceBeanInstance
     *
     * @param instance The ServiceBeanInstance
     * @throws net.assimilator.core.OperationalStringException
     *                                  If the ServiceElement is not being
     *                                  managed by the OperationalStringManager
     * @throws java.rmi.RemoteException If communication errors occur
     */
    public void updateServiceBeanInstance(ServiceBeanInstance instance) throws OperationalStringException, RemoteException {
    }

    /**
     * Redeploy an OperationalString, ServiceElement or ServiceBeanInstance. This
     * method will terminate then reallocate services based on the following criteria:
     * <p/>
     * <ul>
     * <li>If both the ServiceElement and ServiceBeanInstance parameters are null, the
     * OperationalString will be redeployed. All services in the OperationalString
     * will be terminated then redeployed
     * <li>If the ServiceElement parmater is not null, this method will terminate
     * then reallocate all deployed services identified by the ServiceElement
     * <li>If the ServiceBeanInstance is not null and the ServiceElement is not null,
     * this method will terminate and then reallocate the service identified by the
     * ServiceBeanInstance
     * </ul>
     *
     * @param sElem    If not null, the ServiceElement to redeploy
     * @param instance If not null, and the sElem param is not null, the
     *                 ServiceBeanInstance to redeploy
     * @param clean    If set to true, the service will be allocated using the
     *                 ServiceElement configuration, not the ServiceBeanInstance configuration
     * @param delay    The amount of time (in milliseconds) to wait until the
     *                 redeployment is performed. A value > 0 will result in scheduling the
     *                 redeployment
     * @param listener If not null, the ServiceProvisionListener will be
     *                 notified as each service is redeployed
     * @throws net.assimilator.core.OperationalStringException
     *                                  If there are errors redeploying
     * @throws java.rmi.RemoteException If communication errors occur
     */
    public void redeploy(ServiceElement sElem, ServiceBeanInstance instance, boolean clean, long delay, ServiceProvisionListener listener) throws OperationalStringException, RemoteException {
    }
}
