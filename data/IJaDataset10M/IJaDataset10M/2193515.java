package net.assimilator.monitor;

import com.sun.jini.config.Config;
import com.sun.jini.constants.ThrowableConstants;
import com.sun.jini.landlord.FixedLeasePeriodPolicy;
import com.sun.jini.landlord.LeasePeriodPolicy;
import com.sun.jini.landlord.LeasedResource;
import net.assimilator.core.JSBInstantiationException;
import net.assimilator.core.ServiceBeanInstance;
import net.assimilator.core.ServiceElement;
import net.assimilator.core.provision.ServiceBeanInstantiator;
import net.assimilator.core.provision.ServiceProvisionEvent;
import net.assimilator.event.EventHandler;
import net.assimilator.jsb.ServiceElementUtil;
import net.assimilator.qos.ResourceCapability;
import net.assimilator.resources.resource.PoolableThread;
import net.assimilator.resources.resource.ThreadPool;
import net.assimilator.resources.servicecore.LandlordLessor;
import net.assimilator.resources.servicecore.LeaseListenerAdapter;
import net.assimilator.resources.servicecore.ServiceResource;
import net.assimilator.watch.StopWatch;
import net.jini.config.Configuration;
import net.jini.core.event.EventRegistration;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lease.Lease;
import net.jini.core.lease.LeaseDeniedException;
import net.jini.core.lease.UnknownLeaseException;
import net.jini.security.BasicProxyPreparer;
import net.jini.security.ProxyPreparer;
import java.rmi.MarshalledObject;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import static java.text.MessageFormat.format;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ServiceProvisioner is responsible for managing the leases of
 * <code>ServiceBeanInstantiator</code> objects and performing the actual provisioning of
 * <code>ServiceElement</code> objects to available <code>ServiceBeanInstantiator</code>
 * instances based on the capability to fulfill the Quality of Service specification
 * of the <code>ServiceElement</code>.
 *
 * @author Dennis Reedy
 * @version $Id: ServiceProvisioner.java 277 2007-09-13 03:59:34Z khartig $
 */
public class ServiceProvisioner {

    /**
     * Indicates a Provision failure.
     */
    static final int PROVISION_FAILURE = 1 << 0;

    /**
     * Indicates a JSB cannot be provisioned.
     */
    static final int UNINSTANTIABLE_JSB = 1 << 1;

    /**
     * The Landlord which will manage leases for ServiceInstantiation resource.
     */
    LandlordLessor landlord;

    /**
     * ProvisionEvent sequence number.
     */
    int sequenceNumber = 0;

    /**
     * Event source
     */
    Object eventSource;

    /**
     * EventHandler to fire ProvisionFailureEvent notifications.
     */
    EventHandler failureHandler;

    /**
     * ThreadPool for provision processing.
     */
    ThreadPool provisioningPool;

    /**
     * Default number of minimum Threads to have in the ThreadPool.
     */
    static final int DEFAULT_MIN_THREADS = 3;

    /**
     * Default number of maximum Threads to have in the ThreadPool.
     */
    static final int DEFAULT_MAX_THREADS = 10;

    /**
     * ThreadPool for provision failure event processing.
     */
    ThreadPool provisionFailurePool;

    /**
     * Collection of in-process provision attempts.
     */
    List<ServiceElement> inProcess = Collections.synchronizedList(new ArrayList<ServiceElement>());

    /**
     * StopWatch to measure provision time
     */
    StopWatch watch;

    /**
     * Manages pending provision dispatch requests for provision types of auto
     */
    PendingManager pendingMgr = new PendingManager();

    /**
     * Manages provision dispatch requests for provision types of station
     */
    FixedServiceManager fixedServiceManager = new FixedServiceManager();

    /**
     * Manages the selection of ServiceResource objects for provisioning requests
     */
    ServiceResourceSelector selector;

    /**
     * ProxyPreparer for ServiceInstantiator proxies
     */
    ProxyPreparer instantiatorPreparer;

    /**
     * Logger instance
     */
    static final Logger logger = ProvisionMonitorImpl.logger;

    /**
     * Logger for updates from Cybernode instances
     */
    static final Logger cybernodeLogger = Logger.getLogger(ProvisionMonitorImpl.LOGGER + ".cybernode");

    /**
     * Create a ServiceProvisioner.
     *
     * @param config Configuration object used to set operational parameters
     * @throws Exception if errors are encountered using the Configuration object or
     *                   creating LandlordLessor
     */
    ServiceProvisioner(Configuration config) throws Exception {
        if (config == null) throw new NullPointerException("config is null");
        long ONE_MINUTE = 1000 * 60;
        long DEFAULT_LEASE_TIME = ONE_MINUTE * 5;
        long DEFAULT_MAX_LEASE_TIME = ONE_MINUTE * 60 * 24;
        int provisioningPoolMinThreads = Config.getIntEntry(config, ProvisionMonitorImpl.CONFIG_COMPONENT, "provisioningPoolMinThreads", DEFAULT_MIN_THREADS, 1, 30);
        int provisioningPoolMaxThreads = Config.getIntEntry(config, ProvisionMonitorImpl.CONFIG_COMPONENT, "provisioningPoolMaxThreads", DEFAULT_MAX_THREADS, provisioningPoolMinThreads + 1, 500);
        if (logger.isLoggable(Level.FINEST)) logger.log(Level.FINEST, "MinThreads={0}, MaxThreads={1}", new Object[] { provisioningPoolMinThreads, provisioningPoolMaxThreads });
        LeasePeriodPolicy provisionerLeasePolicy = (LeasePeriodPolicy) Config.getNonNullEntry(config, ProvisionMonitorImpl.CONFIG_COMPONENT, "provisionerLeasePeriodPolicy", LeasePeriodPolicy.class, new FixedLeasePeriodPolicy(DEFAULT_MAX_LEASE_TIME, DEFAULT_LEASE_TIME));
        instantiatorPreparer = (ProxyPreparer) config.getEntry(ProvisionMonitorImpl.CONFIG_COMPONENT, "instantiatorPreparer", ProxyPreparer.class, new BasicProxyPreparer());
        provisioningPool = new ThreadPool("ProvisionPool:" + System.currentTimeMillis(), provisioningPoolMinThreads, provisioningPoolMaxThreads);
        provisionFailurePool = new ThreadPool("ProvisionFailurePool:" + System.currentTimeMillis(), provisioningPoolMinThreads, provisioningPoolMaxThreads);
        landlord = new LandlordLessor(config, provisionerLeasePolicy);
        landlord.addLeaseListener(new LeaseMonitor());
        selector = (ServiceResourceSelector) config.getEntry(ProvisionMonitorImpl.CONFIG_COMPONENT, "serviceResourceSelector", ServiceResourceSelector.class, new RoundRobinSelector());
        selector.setLandlordLessor(landlord);
        if (logger.isLoggable(Level.FINEST)) logger.log(Level.FINEST, "ServiceResourceSelector : " + selector.getClass().getName());
    }

    /**
     * Clean up all resources.
     */
    void terminate() {
        landlord.stop(true);
        provisioningPool.destroy();
        provisionFailurePool.destroy();
    }

    /**
     * @return the ServiceResourceSelector
     */
    ServiceResourceSelector getServiceResourceSelector() {
        return (selector);
    }

    /**
     * @return The PendingManager
     */
    PendingManager getPendingManager() {
        return (pendingMgr);
    }

    /**
     * @return The FixedServiceManager
     */
    FixedServiceManager getFixedServiceManager() {
        return (fixedServiceManager);
    }

    /**
     * Set the event source which will be used as the source of ProvisionFailureEvent
     * notifications.
     *
     * @param eventSource Event source object.
     */
    void setEventSource(Object eventSource) {
        this.eventSource = eventSource;
    }

    /**
     * Set the ProvisionFailureHandler for sending ProvisionFailureEvent objects.
     *
     * @param failureHandler failure handler to set.
     */
    void setProvisionFailureHandler(EventHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    /**
     * Set the StopWatch to measure provision times.
     *
     * @param watch the stop watch object.
     */
    void setWatch(StopWatch watch) {
        this.watch = watch;
    }

    /**
     * Registers a RemoteEventListener that will be notified in a round robin
     * fashion to instantiate a ServiceBean as defined by the ServiceElement object
     * that will be contained within the ServiceProvisionEvent.
     *
     * @param instantiator       The listener
     * @param handback           A handback object
     * @param resourceCapability The capabilities of the ServiceInstantiator
     * @param serviceLimit       The maximum number of services the
     *                           ServiceBeanInstantiator has been configured to instantiate
     * @param duration           Requested lease duration
     * @return EventRegistration
     * @throws java.rmi.RemoteException when remote calls fail.
     * @throws net.jini.core.lease.LeaseDeniedException
     *                                  when new lease creation fails.
     */
    EventRegistration register(ServiceBeanInstantiator instantiator, MarshalledObject handback, ResourceCapability resourceCapability, int serviceLimit, long duration) throws LeaseDeniedException, RemoteException {
        instantiator = (ServiceBeanInstantiator) instantiatorPreparer.prepareProxy(instantiator);
        InstantiatorResource resource = new InstantiatorResource(instantiator, handback, resourceCapability, serviceLimit);
        try {
            resource.setServiceRecords();
        } catch (Throwable t) {
            cybernodeLogger.log(Level.WARNING, "Registering a Cybernode", t);
            throw new LeaseDeniedException("Getting ServiceRecords");
        }
        ServiceResource serviceResource = new ServiceResource(resource);
        Lease lease = landlord.newLease(serviceResource, duration);
        EventRegistration registration = new EventRegistration(ServiceProvisionEvent.ID, eventSource, lease, sequenceNumber);
        if (cybernodeLogger.isLoggable(Level.FINE)) {
            int instantiatorCount = landlord.total();
            cybernodeLogger.log(Level.FINE, "Registered new ServiceBeanInstantiator @ {0}\n" + "ServiceBeanInstantiator count [{1}]", new Object[] { resourceCapability.getAddress(), instantiatorCount });
        }
        fixedServiceManager.process(serviceResource);
        pendingMgr.process();
        return registration;
    }

    /**
     * Get the corresponding InstantiatorResource by looping through all
     * known ServiceResource instances. If not found or the lease is not valid
     * throw an UnknownLeaseException.
     *
     * @param resource            The ServiceBeanInstantiator
     * @param updatedCapabilities Updated ResourceCapability
     * @param serviceLimit        The maximum number of services the
     *                            ServiceBeanInstantiator has been configured to instantiate.
     * @throws net.jini.core.lease.UnknownLeaseException
     *                                  if no service resources are found of the service lease is invaild.
     * @throws java.rmi.RemoteException if remote calls fail.
     */
    void handleFeedback(ServiceBeanInstantiator resource, ResourceCapability updatedCapabilities, int serviceLimit) throws UnknownLeaseException, RemoteException {
        resource = (ServiceBeanInstantiator) instantiatorPreparer.prepareProxy(resource);
        ServiceResource[] svcResources = selector.getServiceResources();
        if (svcResources.length == 0) {
            logger.fine("Throwing UnknownLeaseException from ServiceProvisioner.");
            throw new UnknownLeaseException("Empty Collection, no leases");
        }
        for (ServiceResource svcResource : svcResources) {
            InstantiatorResource ir = (InstantiatorResource) svcResource.getResource();
            if (cybernodeLogger.isLoggable(Level.FINEST)) cybernodeLogger.log(Level.FINEST, "Update from [{0}:{1}] " + "updatedCapabilities: {2}, " + "serviceLimit {3}", new Object[] { ir.getHostAddress(), resource.toString(), updatedCapabilities, serviceLimit });
            logger.log(Level.FINEST, "Update from [{0}:{1}] " + "updatedCapabilities: {2}, " + "serviceLimit {3}", new Object[] { ir.getHostAddress(), resource.toString(), updatedCapabilities, serviceLimit });
            if (ir.getInstantiator().equals(resource)) {
                if (!landlord.ensure(svcResource)) throw new UnknownLeaseException("No matching Lease found");
                ir.setResourceCapability(updatedCapabilities);
                ir.setServiceLimit(serviceLimit);
                try {
                    ir.setServiceRecords();
                } catch (Throwable t) {
                    logger.log(Level.WARNING, "Getting ServiceRecords", t);
                    cybernodeLogger.log(Level.WARNING, "Getting ServiceRecords", t);
                }
                fixedServiceManager.process(svcResource);
                pendingMgr.process();
                break;
            }
        }
    }

    /**
     * Get a ServiceBeanInstantiator that meets the operational requirements of a
     * ServiceElement.
     *
     * @param request The ProvisionRequest
     * @return A ServiceResource that contains an InstantiatorResource
     *         which meets the operational criteria of the ServiceElement.
     */
    ServiceResource acquireServiceResource(ProvisionRequest request) {
        ServiceResource resource = null;
        synchronized (selector) {
            try {
                if (request.requestedHostAddress != null) {
                    resource = selector.getServiceResource(request.sElem, request.requestedHostAddress, true);
                } else if (request.excludeHostAddress != null) {
                    resource = selector.getServiceResource(request.sElem, request.excludeHostAddress, false);
                } else {
                    resource = selector.getServiceResource(request.sElem);
                }
                if (resource != null) {
                    InstantiatorResource ir = (InstantiatorResource) resource.getResource();
                    ir.incrementProvisionCounter(request.sElem);
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Getting ServiceResource", e);
            }
        }
        return resource;
    }

    /**
     * Dispatch a provision request. This method is used to provision ServiceElement
     * object that have a provision type of DYNAMIC
     *
     * @param request The ProvisionRequest
     */
    void dispatch(ProvisionRequest request) {
        ServiceResource resource = acquireServiceResource(request);
        dispatch(request, resource, 0);
    }

    /**
     * Provision a pending ServiceElement with a provision type of DYNAMIC with an
     * index into the Collection of pending ServiceElement instances managed by the
     * PendingManager. If a ServiceResource cannot be found, put the ServiceElement
     * under the management of the PendingManager and fire a ProvisionFailureEvent
     *
     * @param request  The ProvisionRequest
     * @param resource A ServiceResource that contains an InstantiatorResource
     *                 which meets the operational requirements of the ServiceElement
     * @param index    Index of the ServiceElement in the pending collection
     */
    private void dispatch(ProvisionRequest request, ServiceResource resource, long index) {
        try {
            if (resource != null) {
                inProcess.add(request.sElem);
                PoolableThread thread = (PoolableThread) provisioningPool.get();
                thread.execute(new ProvisionTask(request, resource, index, pendingMgr));
            } else {
                String action = (request.type == ProvisionRequest.PROVISION ? "provision" : "relocate");
                String failureReason = "A Cybernode could not be obtained to " + action + " [" + request.sElem.getName() + "]";
                if (request.type == ProvisionRequest.RELOCATE) {
                    if (request.requestedHostAddress != null) failureReason = failureReason + " from compute resource " + request.excludeHostAddress + " to " + request.requestedHostAddress; else failureReason = failureReason + " from compute resource " + request.excludeHostAddress;
                }
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, failureReason);
                }
                if (request.svcProvisionListener != null) {
                    try {
                        request.svcProvisionListener.failed(request.sElem, true);
                    } catch (NoSuchObjectException e) {
                        logger.log(Level.WARNING, "ServiceBeanInstantiatorListener failure " + "notification did not succeeed, " + "[java.rmi.NoSuchObjectException:" + e.getLocalizedMessage() + "], remove " + "ServiceBeanInstantiatorListener " + "[" + request.svcProvisionListener + "]");
                        request.svcProvisionListener = null;
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "ServiceBeanInstantiatorListener notification", e);
                    }
                }
                if (request.type == ProvisionRequest.PROVISION) {
                    pendingMgr.addProvisionRequest(request, index);
                    if (logger.isLoggable(Level.FINER)) logger.log(Level.FINER, "Wrote [" + request.sElem.getName() + "] " + "to " + pendingMgr.getType());
                    if (logger.isLoggable(Level.FINEST)) pendingMgr.dumpCollection();
                }
                processProvisionFailure(new ProvisionFailureEvent(eventSource, request.sElem, failureReason, null));
            }
        } catch (Throwable t) {
            logger.log(Level.WARNING, "Dispatching ProvisionRequest", t);
            processProvisionFailure(new ProvisionFailureEvent(eventSource, request.sElem, t.getClass().getName() + ":" + t.getLocalizedMessage(), t));
        }
    }

    /**
     * The ProvisionTask is created to process a provision dispatch request
     */
    class ProvisionTask implements Runnable {

        ProvisionRequest request;

        ServiceResource svcResource;

        PendingServiceElementManager pendingMgr;

        long index;

        StopWatch stopWatch;

        ServiceBeanInstance jsbInstance = null;

        Throwable thrown = null;

        String failureReason = null;

        /**
         * Create a ProvisionTask
         *
         * @param request     The ProvisionRequest
         * @param svcResource The selected ServiceResource to provision to
         */
        ProvisionTask(ProvisionRequest request, ServiceResource svcResource) {
            this(request, svcResource, 0, null);
        }

        /**
         * Create a ProvisionTask
         *
         * @param request     The ProvisionRequest
         * @param svcResource The selected ServiceResource to provision to
         * @param index       The index of the ServiceElement in the
         *                    PendingServiceElementManager
         * @param pendingMgr  The PendingServiceElementManager
         */
        ProvisionTask(ProvisionRequest request, ServiceResource svcResource, long index, PendingServiceElementManager pendingMgr) {
            this.request = request;
            this.svcResource = svcResource;
            this.index = index;
            this.pendingMgr = pendingMgr;
        }

        public void run() {
            try {
                jsbInstance = null;
                thrown = null;
                stopWatch = new StopWatch(watch.getWatchDataSource(), watch.getId());
                int result = doProvision(request, svcResource);
                if ((result & PROVISION_FAILURE) != 0) {
                    boolean resubmitted = true;
                    if (logger.isLoggable(Level.FINE)) logger.log(Level.FINE, "Provision attempt failed for " + "[" + request.sElem.getName() + "]");
                    if ((result & UNINSTANTIABLE_JSB) != 0) {
                        resubmitted = false;
                        if (logger.isLoggable(Level.FINE)) logger.log(Level.FINE, "Service [" + request.sElem.getName() + "] " + "is un-instantiable, do not resubmit");
                    } else {
                        if (pendingMgr != null) {
                            if (request.type == ProvisionRequest.PROVISION) {
                                pendingMgr.addProvisionRequest(request, index);
                                if (logger.isLoggable(Level.FINE)) logger.log(Level.FINE, "Re-submitted [" + request.sElem.getName() + "] " + "to " + pendingMgr.getType());
                            }
                        }
                    }
                    if (thrown != null || result != 0) {
                        processProvisionFailure(new ProvisionFailureEvent(eventSource, request.sElem, failureReason, thrown));
                    }
                    if (request.svcProvisionListener != null) {
                        try {
                            request.svcProvisionListener.failed(request.sElem, resubmitted);
                        } catch (Exception e) {
                            logger.log(Level.WARNING, "Notifying ServiceProvisionListeners on failure", e);
                        }
                    }
                } else {
                    if (jsbInstance == null) {
                        if (logger.isLoggable(Level.FINER)) {
                            String addr = ((InstantiatorResource) svcResource.getResource()).getHostAddress();
                            logger.log(Level.FINER, "Cybernode at [" + addr + "] did not allocate [" + request.sElem.getName() + "], " + "service limit assumed to have been met");
                        }
                        return;
                    }
                    request.listener.serviceProvisioned(jsbInstance, (InstantiatorResource) svcResource.getResource());
                    if (request.svcProvisionListener != null) {
                        try {
                            request.svcProvisionListener.succeeded(jsbInstance);
                        } catch (Exception e) {
                            logger.log(Level.WARNING, "Notifying ServiceProvisionListeners on success", e);
                        }
                    }
                }
            } finally {
                inProcess.remove(request.sElem);
            }
        }

        int doProvision(ProvisionRequest request, ServiceResource serviceResource) {
            int result = 0;
            InstantiatorResource ir = (InstantiatorResource) serviceResource.getResource();
            try {
                try {
                    ServiceProvisionEvent event = new ServiceProvisionEvent(eventSource, request.opStringMgr, request.sElem);
                    event.setSequenceNumber(sequenceNumber);
                    event.setHandback(ir.getHandback());
                    int numProvisionRetries = 2;
                    for (int i = 0; i < numProvisionRetries; i++) {
                        if (logger.isLoggable(Level.FINER)) {
                            String retry = (i == 0 ? "" : ", retry (" + i + ") ");
                            logger.log(Level.FINER, "Allocating " + retry + "[" + request.sElem.getName() + "] ...");
                        }
                        stopWatch.startTiming();
                        jsbInstance = ir.getInstantiator().instantiate(event);
                        if (jsbInstance != null) {
                            ir.addServiceElementInstance(request.sElem, jsbInstance.getServiceBeanID());
                            sequenceNumber++;
                            if (logger.isLoggable(Level.FINER)) logger.log(Level.FINER, "Allocated [" + request.sElem.getName() + "]");
                            if (logger.isLoggable(Level.FINEST)) {
                                logger.log(Level.FINEST, "{0} ServiceBeanInstance {1}, " + "Annotation {2}", new Object[] { request.sElem.getName(), jsbInstance, java.rmi.server.RMIClassLoader.getClassAnnotation(jsbInstance.getService().getClass()) });
                            }
                            break;
                        } else {
                            if (logger.isLoggable(Level.FINER)) logger.log(Level.FINER, "Cybernode at " + "[" + ir.getHostAddress() + "] did not " + "allocate [" + request.sElem.getName() + "], " + "retry ...");
                            long retryWait = 1000;
                            try {
                                Thread.sleep(retryWait);
                            } catch (InterruptedException ie) {
                                if (logger.isLoggable(Level.FINEST)) logger.log(Level.FINEST, "Interrupted while sleeping " + "[" + retryWait + "] millis" + "for provision retry", ie);
                            }
                        }
                    }
                } catch (UnknownEventException e) {
                    result = PROVISION_FAILURE;
                    failureReason = e.getLocalizedMessage();
                    logger.severe(failureReason);
                    thrown = e;
                    selector.dropServiceResource(serviceResource);
                } catch (RemoteException e) {
                    result = PROVISION_FAILURE;
                    failureReason = e.getLocalizedMessage();
                    logger.log(Level.WARNING, "Provisioning [" + request.sElem.getName() + "] " + "to [" + ir.getHostAddress() + "]", e);
                    thrown = e;
                    final int category = ThrowableConstants.retryable(e);
                    if (category == ThrowableConstants.BAD_INVOCATION || category == ThrowableConstants.BAD_OBJECT || (e.detail != null && (e.detail instanceof java.net.ConnectException))) {
                        if (logger.isLoggable(Level.FINER)) logger.log(Level.FINER, "Drop Cybernode {0} from collection", new Object[] { ir.getInstantiator() });
                        selector.dropServiceResource(serviceResource);
                    }
                } catch (JSBInstantiationException e) {
                    if (e.isUninstantiable()) result = PROVISION_FAILURE | UNINSTANTIABLE_JSB; else result = PROVISION_FAILURE;
                    failureReason = e.getLocalizedMessage();
                    thrown = e;
                    Throwable t = e.getCause();
                    if (t != null) {
                        Throwable nested = t.getCause();
                        failureReason = (nested == null ? t.getLocalizedMessage() : nested.getLocalizedMessage());
                    }
                    if (logger.isLoggable(Level.FINEST)) {
                        logger.finest("JSBInstantiationException: " + failureReason + "\n" + "Resource in process counter " + "[" + ir.getInProcessCounter() + "], " + "for ServiceElement " + "[" + request.sElem.getName() + "] " + "[" + ir.getInProcessCounter(request.sElem) + "] " + "Number of instances [" + ir.getServiceElementInstances(request.sElem) + "]");
                    } else {
                        logger.log(Level.WARNING, "JSBInstantiationException: " + failureReason);
                    }
                } catch (Throwable t) {
                    logger.log(Level.WARNING, "Allocating ServiceBean", t);
                    result = PROVISION_FAILURE | UNINSTANTIABLE_JSB;
                    failureReason = t.getLocalizedMessage();
                    if (logger.isLoggable(Level.FINE)) logger.log(Level.FINE, failureReason);
                    thrown = t;
                }
            } finally {
                ir.decrementProvisionCounter(request.sElem);
                stopWatch.stopTiming();
            }
            return (result);
        }
    }

    /**
     * This class is used to manage the provisioning of pending ServiceElement
     * objects that have a ServiceProvisionManagement type of DYNAMIC.
     */
    class PendingManager extends PendingServiceElementManager {

        /**
         * Create a PendingManager
         */
        PendingManager() {
            super("Dynamic-Service Manager");
        }

        /**
         * Override parent's getCount to include the number of in-process elements
         * in addition to the number of pending ServiceElement instances.
         *
         * @param sElem the service element being checked.
         */
        int getCount(ServiceElement sElem) {
            int count = super.getCount(sElem);
            Object[] array = inProcess.toArray();
            for (Object anArray : array) {
                ServiceElement s = (ServiceElement) anArray;
                if (s.equals(sElem)) count++;
            }
            return (count);
        }

        /**
         * Process the pending collection.
         */
        void process() {
            int pendingSize = getSize();
            if (logger.isLoggable(Level.FINE) && pendingSize > 0) {
                dumpCollection();
            }
            if (pendingSize == 0) return;
            try {
                Key[] keys;
                synchronized (collection) {
                    Set keySet = collection.keySet();
                    keys = (Key[]) keySet.toArray(new Key[keySet.size()]);
                }
                for (Key key : keys) {
                    ProvisionRequest request = null;
                    ServiceResource resource = null;
                    synchronized (collection) {
                        request = (ProvisionRequest) collection.get(key);
                        if (request != null && request.sElem != null) {
                            resource = acquireServiceResource(request);
                            if (resource != null) {
                                synchronized (collection) {
                                    collection.remove(key);
                                }
                            }
                        }
                    }
                    if (resource == null) continue;
                    try {
                        dispatch(request, resource, key.index);
                    } catch (Exception e) {
                        if (logger.isLoggable(Level.FINEST)) logger.log(Level.FINEST, "Dispatching Pending Collection Element", e);
                    }
                    Thread.sleep(500);
                }
            } catch (Throwable t) {
                logger.log(Level.WARNING, "Processing Pending Collection", t);
            }
        }
    }

    /**
     * This class is used to manage the provisioning of ServiceElement objects that
     * have a ServiceProvisionManagement type of FIXED.
     */
    class FixedServiceManager extends PendingServiceElementManager {

        Map<ProvisionRequest, String> instanceMap = Collections.synchronizedMap(new HashMap<ProvisionRequest, String>());

        /**
         * Create a FixedServiceManager
         */
        FixedServiceManager() {
            super("Fixed-Service Manager");
        }

        /**
         * Process the entire Fixed collection over all known ServiceResource
         * instances.
         */
        void process() {
            ServiceResource[] resources = selector.getServiceResources();
            for (ServiceResource resource : resources) process(resource);
        }

        /**
         * Used to deploy a specific ProvisionRequest to all ServiceResource
         * instances which support the requirements of the ServiceElement
         *
         * @param request The ProvisionRequest to deploy
         */
        void deploy(ProvisionRequest request) {
            try {
                if (logger.isLoggable(Level.FINER)) logger.log(Level.FINER, "Deploy [" + request.sElem.getName() + "]");
                ServiceResource[] resources = selector.getServiceResources(request.sElem);
                if (resources.length > 0) {
                    ArrayList list = new ArrayList();
                    for (ServiceResource resource : resources) {
                        doDeploy(resource, request, list);
                    }
                    if (list.size() > 0) taskJoiner(list);
                }
            } catch (Throwable t) {
                logger.log(Level.WARNING, "FixedServiceManager deployNew", t);
            }
        }

        /**
         * Used to deploy a ServiceElement to a known host address.
         *
         * @param hostAddress The host address of the machine
         * @param request     The ProvisionRequest
         */
        void deployTo(String hostAddress, ProvisionRequest request) {
            boolean reallocateInstance = false;
            try {
                ServiceResource[] resources = selector.getServiceResources(hostAddress, true);
                if (resources.length > 0) {
                    if (logger.isLoggable(Level.FINER)) logger.log(Level.FINER, "Deploy to [" + resources.length + "] " + "Cybernodes at [" + hostAddress + "]");
                    ArrayList<PoolableThread> list = new ArrayList<PoolableThread>();
                    for (ServiceResource resource : resources) {
                        InstantiatorResource ir = (InstantiatorResource) resource.getResource();
                        if (ir.canProvision(request.sElem)) {
                            int numAllowed = getNumAllowed(resource, request);
                            if (numAllowed > 0) {
                                PoolableThread thread = (PoolableThread) provisioningPool.get();
                                ir.incrementProvisionCounter(request.sElem);
                                list.add(thread);
                                thread.execute(new ProvisionTask(request, resource));
                            }
                        } else {
                            reallocateInstance = true;
                        }
                    }
                    if (list.size() > 0) taskJoiner(list);
                } else {
                    reallocateInstance = true;
                }
                if (reallocateInstance) {
                    if (logger.isLoggable(Level.FINER)) logger.log(Level.FINER, "Could not deploy service " + "[" + request.sElem.getName() + "] " + "to host address [" + hostAddress + "]");
                    if (logger.isLoggable(Level.FINEST)) logger.log(Level.FINEST, "Write ProvisionRequest to instanceMap, " + "instanceID : " + request.sElem.getServiceBeanConfig().getInstanceID());
                    instanceMap.put(request, hostAddress);
                }
            } catch (Throwable t) {
                logger.log(Level.WARNING, "Deploying ServiceElement to a known host address", t);
            }
        }

        /**
         * Process the fixed services collection with an input ServiceResource.
         *
         * @param resource the service resource to check.
         */
        void process(ServiceResource resource) {
            if (resource == null) throw new NullPointerException("ServiceResource is null");
            InstantiatorResource ir = (InstantiatorResource) resource.getResource();
            try {
                int fixedServiceSize = getSize();
                if (fixedServiceSize == 0) return;
                if (logger.isLoggable(Level.FINEST)) {
                    dumpCollection();
                }
                ArrayList<ProvisionRequest> processed = new ArrayList<ProvisionRequest>();
                Set keys = instanceMap.keySet();
                for (Object key : keys) {
                    ProvisionRequest request = (ProvisionRequest) key;
                    String hostAddress = instanceMap.get(request);
                    if (hostAddress.equals(ir.getHostAddress()) && ir.canProvision(request.sElem)) {
                        ArrayList list = new ArrayList();
                        int numAllocated = doDeploy(resource, request, list, false);
                        if (numAllocated > 0) processed.add(request);
                        if (list.size() > 0) taskJoiner(list);
                    }
                }
                synchronized (collection) {
                    ArrayList list = new ArrayList();
                    Set keys2 = collection.keySet();
                    for (Object aKeys2 : keys2) {
                        ProvisionRequest request = (ProvisionRequest) collection.get(aKeys2);
                        boolean skip = false;
                        for (Object aProcessed : processed) {
                            ProvisionRequest processedReq = (ProvisionRequest) aProcessed;
                            if (processedReq.sElem.equals(request.sElem)) {
                                skip = true;
                                break;
                            }
                        }
                        if (skip) continue;
                        if (ir.canProvision(request.sElem)) doDeploy(resource, request, list);
                    }
                    if (list.size() > 0) taskJoiner(list);
                }
                for (Object aProcessed1 : processed) {
                    instanceMap.remove(aProcessed1);
                }
            } catch (Throwable t) {
                logger.log(Level.WARNING, "Processing FixedService Collection", t);
            } finally {
                ir.setDynamicEnabledOn();
            }
        }

        /**
         * Do the deployment for a fixed service.
         *
         * @param resource The ServiceResource
         * @param request  The ProvisionRequest
         * @param list     List that will be filled in with PoolableThread instances
         *                 running ProvisionTasks
         * @return int value indicating
         * @throws Exception if the service deployment fails.
         */
        int doDeploy(ServiceResource resource, ProvisionRequest request, List list) throws Exception {
            return (doDeploy(resource, request, list, true));
        }

        /**
         * Do the deployment for a fixed service
         *
         * @param resource         The ServiceResource
         * @param req              The ProvisionRequest
         * @param list             List that will be filled in with PoolableThread instances
         *                         running ProvisionTasks
         * @param changeInstanceID If true, increment the instanceID
         * @return int value indicating
         * @throws Exception if the service deployment fails.
         */
        int doDeploy(ServiceResource resource, ProvisionRequest req, List list, boolean changeInstanceID) throws Exception {
            int numAllowed = getNumAllowed(resource, req);
            ProvisionRequest request = ProvisionRequest.copy(req);
            if (numAllowed > 0) {
                Long currentID = request.sElem.getServiceBeanConfig().getInstanceID();
                long id = 0;
                if (currentID != null) {
                    id = currentID;
                }
                if (changeInstanceID || currentID == null) {
                    id = request.instanceIDMgr.getNextInstanceID();
                }
                for (int i = 0; i < numAllowed; i++) {
                    request.sElem = ServiceElementUtil.prepareInstanceID(request.sElem, true, id++);
                    if (logger.isLoggable(Level.FINEST)) logger.log(Level.FINEST, "[" + request.sElem.getName() + "] " + "instanceID : " + request.sElem.getServiceBeanConfig().getInstanceID());
                    inProcess.add(request.sElem);
                    PoolableThread thread = (PoolableThread) provisioningPool.get();
                    InstantiatorResource ir = (InstantiatorResource) resource.getResource();
                    ir.incrementProvisionCounter(request.sElem);
                    list.add(thread);
                    thread.execute(new ProvisionTask(request, resource));
                }
            }
            return (numAllowed);
        }

        /**
         * Determine how many services can be allocated based on how many
         * are already on the resource.
         *
         * @param resource the resource object to check.
         * @param request  object containing information about the service to provision.
         * @return the number of allowed services.
         */
        int getNumAllowed(ServiceResource resource, ProvisionRequest request) {
            InstantiatorResource ir = (InstantiatorResource) resource.getResource();
            int planned = request.sElem.getPlanned();
            int actual = ir.getServiceElementInstances(request.sElem);
            return (planned - actual);
        }

        /**
         * Wait until all ProvisionTask threads are complete.
         *
         * @param list The list of tasks to be joined.
         */
        void taskJoiner(ArrayList list) {
            if (logger.isLoggable(Level.FINEST)) logger.log(Level.FINEST, "Wait until all ProvisionTask threads are complete ...");
            for (Object task : list) {
                try {
                    ((PoolableThread) task).joinResource();
                } catch (InterruptedException ie) {
                    logger.log(Level.WARNING, "PoolableThread join interruption", ie);
                }
            }
            if (logger.isLoggable(Level.FINEST)) logger.log(Level.FINEST, "ProvisionTask threads join complete");
        }
    }

    /**
     * Helper method to obtain a PoolableThread and create a
     * ProvisionFailureEventTask to send a ProvisionFailureEvent.
     *
     * @param event the event information sent in the remote event object.
     */
    void processProvisionFailure(ProvisionFailureEvent event) {
        try {
            PoolableThread thread = (PoolableThread) provisionFailurePool.get();
            thread.execute(new ProvisionFailureEventTask(event));
        } catch (Exception e) {
            logger.log(Level.WARNING, "Creating a ProvisionFailureEventTask", e);
        }
    }

    /**
     * This class is used as by a <code>PoolableThread</code> to notify registered
     * event consumers of a ProvisionFailureEvent.
     */
    class ProvisionFailureEventTask implements Runnable {

        ProvisionFailureEvent event;

        ProvisionFailureEventTask(ProvisionFailureEvent event) {
            this.event = event;
        }

        public void run() {
            try {
                failureHandler.fire(event);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Exception notifying ProvisionFailureEvent consumers", e);
            }
        }
    }

    /**
     * Monitors ServiceBeanInstantiator leases being removed.
     */
    class LeaseMonitor extends LeaseListenerAdapter {

        public void removed(LeasedResource resource) {
            InstantiatorResource ir = (InstantiatorResource) ((ServiceResource) resource).getResource();
            int instantiatorCount = landlord.total();
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, format("ServiceBeanInstantiator @ {0} removed, ServiceBeanInstantiator count now [{1}]"), new Object[] { ir.getResourceCapability().getAddress(), instantiatorCount });
            }
        }
    }
}
