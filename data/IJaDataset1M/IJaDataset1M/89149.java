package net.assimilator.tools.deploymentdirectory.mocks;

import net.assimilator.monitor.ProvisionMonitorAdmin;
import net.assimilator.monitor.PeerInfo;
import net.assimilator.core.*;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.entry.Entry;
import net.jini.core.discovery.LookupLocator;
import java.rmi.RemoteException;
import java.util.Map;
import java.net.URL;

/**
 * Description:
 * User: Larry Mitchell
 * Date: Sep 18, 2007
 * Time: 2:07:06 PM
 * Version: $id$
 */
public class MockProvisionMonitorAdmin implements ProvisionMonitorAdmin {

    private MockOperationalStringManager opStringManager;

    public MockProvisionMonitorAdmin(MockProvisionMonitor mockProvisionMonitor, Object exporter) {
        opStringManager = new MockOperationalStringManager();
    }

    /**
     * Get the ProvisionMonitor instances that are being backed up.
     *
     * @return Array of PeerInfo objects
     * @throws java.rmi.RemoteException if the remote call fails.
     */
    public PeerInfo[] getBackupInfo() throws RemoteException {
        return new PeerInfo[0];
    }

    /**
     * Returns an array of instances of ServiceRegistrar, each corresponding to a
     * lookup service with which the service is currently registered (joined)
     */
    public ServiceRegistrar[] getJoinSet() throws RemoteException {
        return new ServiceRegistrar[0];
    }

    /**
     * Get the ServiceElement for the ServiceBean
     *
     * @return The ServiceElement
     */
    public ServiceElement getServiceElement() throws RemoteException {
        return null;
    }

    /**
     * Set the ServiceElement for the ServiceBean
     *
     * @param sElem The ServiceElement
     */
    public void setServiceElement(ServiceElement sElem) throws RemoteException {
    }

    /**
     * How long the service has been running
     */
    public long getUpTime() throws RemoteException {
        return 0;
    }

    /**
     * The start method provides informs a ServiceBean to make itself ready to
     * accept inbound communications, returning an Object which can be used to
     * communicate with the ServiceBean. If the ServiceBean has started itself,
     * subsequent invocations of this method will not re-start the ServiceBean,
     * but return the Object created during the initial start
     *
     * @return An Object that can be used to communicate to the
     *         ServiceBean
     * @throws net.assimilator.core.JSBControlException
     *                                  If any errors occur
     * @throws java.rmi.RemoteException If any communications errors occur
     */
    public Object start() throws JSBControlException, RemoteException {
        return null;
    }

    /**
     * The stop method informs the ServiceBean to unexport itself from any
     * underlying distributed Object communication mechanisms making it
     * incapable of accepting inbound communications. This call should be used
     * with great care as the ServiceBean will not be able to respond to <b>any
     * </b> remote invocations.
     *
     * @param force If true, unexports the ServiceBean even if there are
     *              pending or in-progress calls; if false, only unexports the ServiceBean if
     *              there are no pending or in-progress calls
     * @throws net.assimilator.core.JSBControlException
     *                                  If any errors occur
     * @throws java.rmi.RemoteException If any communications errors occur
     */
    public void stop(boolean force) throws JSBControlException, RemoteException {
    }

    /**
     * The advertise method informs a ServiceBean to advertise itself on the
     * network providing access to all clients. The ServiceBean must be ready to
     * accept incoming communications (has been started). If the ServiceBean has
     * advertised itself, subsequent invocations of this method will not
     * re-advertise the ServiceBean
     *
     * @throws net.assimilator.core.JSBControlException
     *                                  If any errors occur
     * @throws java.rmi.RemoteException If any communications errors occur
     */
    public void advertise() throws JSBControlException, RemoteException {
    }

    /**
     * The unadvertise method informs the ServiceBean to cancel all
     * advertisements (registrations, etc...) it has made on the network. The
     * ServiceBean must still be available to accept incoming communications. If
     * the ServiceBean has not advertised itself, this method has no defined
     * behavior
     *
     * @throws net.assimilator.core.JSBControlException
     *                                  If any errors occur
     * @throws java.rmi.RemoteException If any communications errors occur
     */
    public void unadvertise() throws JSBControlException, RemoteException {
    }

    /**
     * Get the current attribute sets for the service.
     *
     * @return the current attribute sets for the service
     * @throws java.rmi.RemoteException
     */
    public Entry[] getLookupAttributes() throws RemoteException {
        return new Entry[0];
    }

    /**
     * Add attribute sets for the service.  The resulting set will be used
     * for all future joins.  The attribute sets are also added to all
     * currently-joined lookup services.
     *
     * @param attrSets the attribute sets to add
     * @throws java.rmi.RemoteException
     */
    public void addLookupAttributes(Entry[] attrSets) throws RemoteException {
    }

    /**
     * Modify the current attribute sets, using the same semantics as
     * ServiceRegistration.modifyAttributes.  The resulting set will be used
     * for all future joins.  The same modifications are also made to all
     * currently-joined lookup services.
     *
     * @param attrSetTemplates the templates for matching attribute sets
     * @param attrSets         the modifications to make to matching sets
     * @throws java.rmi.RemoteException
     * @see net.jini.core.lookup.ServiceRegistration#modifyAttributes
     */
    public void modifyLookupAttributes(Entry[] attrSetTemplates, Entry[] attrSets) throws RemoteException {
    }

    /**
     * Get the list of groups to join.  An empty array means the service
     * joins no groups (as opposed to "all" groups).
     *
     * @return an array of groups to join. An empty array means the service
     *         joins no groups (as opposed to "all" groups).
     * @throws java.rmi.RemoteException
     * @see #setLookupGroups
     */
    public String[] getLookupGroups() throws RemoteException {
        return new String[0];
    }

    /**
     * Add new groups to the set to join.  Lookup services in the new
     * groups will be discovered and joined.
     *
     * @param groups groups to join
     * @throws java.rmi.RemoteException
     * @see #removeLookupGroups
     */
    public void addLookupGroups(String[] groups) throws RemoteException {
    }

    /**
     * Remove groups from the set to join.  Leases are cancelled at lookup
     * services that are not members of any of the remaining groups.
     *
     * @param groups groups to leave
     * @throws java.rmi.RemoteException
     * @see #addLookupGroups
     */
    public void removeLookupGroups(String[] groups) throws RemoteException {
    }

    /**
     * Replace the list of groups to join with a new list.  Leases are
     * cancelled at lookup services that are not members of any of the
     * new groups.  Lookup services in the new groups will be discovered
     * and joined.
     *
     * @param groups groups to join
     * @throws java.rmi.RemoteException
     * @see #getLookupGroups
     */
    public void setLookupGroups(String[] groups) throws RemoteException {
    }

    /**
     * Get the list of locators of specific lookup services to join.
     *
     * @return the list of locators of specific lookup services to join
     * @throws java.rmi.RemoteException
     * @see #setLookupLocators
     */
    public LookupLocator[] getLookupLocators() throws RemoteException {
        return new LookupLocator[0];
    }

    /**
     * Add locators for specific new lookup services to join.  The new
     * lookup services will be discovered and joined.
     *
     * @param locators locators of specific lookup services to join
     * @throws java.rmi.RemoteException
     * @see #removeLookupLocators
     */
    public void addLookupLocators(LookupLocator[] locators) throws RemoteException {
    }

    /**
     * Remove locators for specific lookup services from the set to join.
     * Any leases held at the lookup services are cancelled.
     *
     * @param locators locators of specific lookup services to leave
     * @throws java.rmi.RemoteException
     * @see #addLookupLocators
     */
    public void removeLookupLocators(LookupLocator[] locators) throws RemoteException {
    }

    /**
     * Replace the list of locators of specific lookup services to join
     * with a new list.  Leases are cancelled at lookup services that were
     * in the old list but are not in the new list.  Any new lookup services
     * will be discovered and joined.
     *
     * @param locators locators of specific lookup services to join
     * @throws java.rmi.RemoteException
     * @see #getLookupLocators
     */
    public void setLookupLocators(LookupLocator[] locators) throws RemoteException {
    }

    /**
     * Destroy the service, if possible, including its persistent storage.
     * This method should (in effect) spawn a separate thread to do the
     * actual work asynchronously, and make a reasonable attempt to let
     * this remote call return successfully. As such, a successful return
     * from this method does not mean that the service has been destroyed.
     * Although the service should make a reasonable attempt to let this
     * remote call return successfully, the service must not wait
     * indefinitely for other (in-progress and subsequent) remote calls to
     * finish before proceeding to destroy itself. Once this method has been
     * called, the service can, but need not, reject all other (in-progress
     * and subsequent) remote calls to the service.
     */
    public void destroy() throws RemoteException {
    }

    /**
     * Deploy an OperationalString URL to the ProvisionMonitor. The
     * ProvisionMonitor will load the location and deploy the OperationalString
     * <p/>
     * If the OperationalString includes nested OperationalStrings, the nested
     * OperationalStrings will be deployed as well. If nested OperationalString
     * items are already deployed, they will not be re-deployed. If the
     * OperationalString specified by the input URL has already been deployed, or
     * is scheduled for deployment, then no part of that OperationalString (or nested
     * OperationalString instances) will be deployed
     *
     * @param opStringUrl The URL indicating the location of the OperationalString
     *                    to deploy
     * @return If there are errors loading part of the OperationalString the Map will
     *         be returned with name value pairs associating the service and corresponding
     *         exceptions
     * @throws java.rmi.RemoteException on failure to deploy.
     * @throws net.assimilator.core.OperationalStringException
     *                                  if the operational string can not be found.
     */
    public Map deploy(URL opStringUrl) throws OperationalStringException, RemoteException {
        return null;
    }

    /**
     * Deploy an OperationalString URL to the ProvisionMonitor. The
     * ProvisionMonitor will load the location and deploy the OperationalString
     * <p/>
     * If the OperationalString includes nested OperationalStrings, the nested
     * OperationalStrings will be deployed as well. If nested OperationalString
     * items are already deployed, they will not be re-deployed. If the
     * OperationalString specified by the input URL has already been deployed, or
     * is scheduled for deployment, then no part of that OperationalString (or nested
     * OperationalString instances) will be deployed
     *
     * @param opStringUrl The URL indicating the location of the OperationalString
     *                    to deploy.
     * @param listener    If not null, the ServiceProvisionListener will be
     *                    notified as each service is deployed.
     * @return If there are errors loading part of the OperationalString the Map will
     *         be returned with name value pairs associating the service and corresponding
     *         exceptions.
     * @throws java.rmi.RemoteException on failure to deploy.
     * @throws net.assimilator.core.OperationalStringException
     *                                  if the operational string can not be found.
     */
    public Map deploy(URL opStringUrl, ServiceProvisionListener listener) throws OperationalStringException, RemoteException {
        return null;
    }

    /**
     * Deploy an OperationalString to the ProvisionMonitor. The
     * ProvisionMonitor will deploy the contents specified by the
     * OperationalString object.
     * <p/>
     * f the OperationalString includes nested OperationalStrings, the nested
     * OperationalStrings will be deployed as well. If nested OperationalString
     * items are already deployed, they will not be re-deployed. If the
     * OperationalString specified by the input URL has already been deployed, or
     * is scheduled for deployment, then no part of that OperationalString (or nested
     * OperationalString instances) will be deployed.
     *
     * @param opstring The OperationalString to deploy.
     * @return If there are errors loading part of the OperationalString the Map will
     *         be returned with name value pairs associating the service and corresponding
     *         exceptions.
     * @throws java.rmi.RemoteException on failure to deploy.
     * @throws net.assimilator.core.OperationalStringException
     *                                  if the operational string can not be found.
     */
    public Map deploy(OperationalString opstring) throws OperationalStringException, RemoteException {
        return null;
    }

    /**
     * Deploy an OperationalString to the ProvisionMonitor. The
     * ProvisionMonitor will deploy the contents specified by the
     * OperationalString object.
     * <p/>
     * f the OperationalString includes nested OperationalStrings, the nested
     * OperationalStrings will be deployed as well. If nested OperationalString
     * items are already deployed, they will not be re-deployed. If the
     * OperationalString specified by the input URL has already been deployed, or
     * is scheduled for deployment, then no part of that OperationalString (or nested
     * OperationalString instances) will be deployed.
     *
     * @param opstring The OperationalString to deploy.
     * @param listener If not null, the ServiceProvisionListener will be
     *                 notified as each service is deployed.
     * @return If there are errors loading part of the OperationalString the Map will
     *         be returned with name value pairs associating the service and corresponding
     *         exceptions.
     * @throws java.rmi.RemoteException on failure to deploy.
     * @throws net.assimilator.core.OperationalStringException
     *                                  if the operational string can not be found.
     */
    public Map deploy(OperationalString opstring, ServiceProvisionListener listener) throws OperationalStringException, RemoteException {
        return null;
    }

    /**
     * Undeploy and Remove an OperationalString deployed by the ProvisionMonitor.
     * The ProvisionMonitor will search for the OperationalString by it's name and
     * if found, remove the OperationalString and any nested OperationalStrings that
     * are included by the OperatinalString. As a result of undeploying the
     * OperationalString(s), all services that have a provision type of
     * ServiceProvisionManagement.DYNAMIC or ServiceProvisionManagement.FIXED will
     * be terminated. If any DeploymentRequest or RedeploymenRequests are pending
     * for the OperationalString(s) being undeployed, these requests will be
     * cancelled
     *
     * @param opStringName The name of the OperationalString to remove
     * @return Returns true if the OperationalString has been undeployed
     * @throws net.assimilator.core.OperationalStringException
     *                                  If the OperationalString does not exist
     * @throws java.rmi.RemoteException If communication errors occur
     */
    public boolean undeploy(String opStringName) throws OperationalStringException, RemoteException {
        return false;
    }

    /**
     * Determine if this ProvisionMonitor has deployed the
     * specified OperationalString as determined by the name.
     *
     * @param opStringName The name of the OperationalString
     * @return If found return true, otherwise false
     * @throws java.rmi.RemoteException if cant determine if the operational string has been deployed.
     */
    public boolean hasDeployed(String opStringName) throws RemoteException {
        return false;
    }

    /**
     * This method provides the capability to retrieve an array of
     * OperationalStringManager objects that are currently managing OperationalString
     * instances that have been deployed. If an OperationalString includes nested
     * OperationalStrings, those OperationalStringManager instances that are nested
     * will be returned as part of this array.
     *
     * @return Array of OperationalStringManager instances that are managing deployed
     *         OperationalString instances.
     * @throws java.rmi.RemoteException thrown on faliure to find operational string managers.
     */
    public OperationalStringManager[] getOperationalStringManagers() throws RemoteException {
        OperationalStringManager[] opStringManagers = new OperationalStringManager[1];
        opStringManagers[0] = opStringManager;
        return opStringManagers;
    }

    /**
     * This method provides the capability to retrieve an OperationalStringManager
     * for a specific OperationalString.
     *
     * @param name The name of the OperationalString
     * @return The OperationalStringManager instance that is managing a deployed
     *         OperationalString.
     * @throws java.rmi.RemoteException .
     * @throws net.assimilator.core.OperationalStringException
     *                                  thrown on faliure to find operational string manager.
     */
    public OperationalStringManager getOperationalStringManager(String name) throws OperationalStringException, RemoteException {
        return opStringManager;
    }
}
