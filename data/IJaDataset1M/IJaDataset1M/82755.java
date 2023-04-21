package org.personalsmartspace.sre.slm.impl;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.Vector;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.onm.api.pss3p.ICallbackListener;
import org.personalsmartspace.onm.api.pss3p.ServiceMessage;
import org.personalsmartspace.onm.api.pss3p.XMLConverter;
import org.personalsmartspace.spm.identity.api.platform.DigitalPersonalIdentifier;
import org.personalsmartspace.spm.identity.api.platform.MalformedDigitialPersonalIdentifierException;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;
import org.personalsmartspace.sre.api.pss3p.PssConstants;
import org.personalsmartspace.sre.api.pss3p.PssServiceIdentifier;
import org.personalsmartspace.sre.api.pss3p.callback.IProxyFactory;
import org.personalsmartspace.sre.ems.api.pss3p.IEventMgr;
import org.personalsmartspace.sre.ems.api.pss3p.PSSEventTypes;
import org.personalsmartspace.sre.ems.api.pss3p.PeerEventTypes;
import org.personalsmartspace.sre.slm.api.platform.*;
import org.personalsmartspace.sre.slm.api.pss3p.callback.IService;
import org.personalsmartspace.sre.slm.api.pss3p.ServiceState;
import org.personalsmartspace.sre.slm.api.pss3p.ISlm3P;
import org.personalsmartspace.sre.slm.api.pss3p.callback.IGui;

/**
 * The main SLM class.
 *
 * @author Mitja Vardjan
 */
public class ServiceLifecycleManager implements IServiceLifecycleManager, ISlm2Slm, ISlm3P {

    private PSSLog log = new PSSLog(this);

    private final DeviceRegistry devices;

    /**
     * List of all deployed 3rd party services.
     */
    protected static DeploymentRegistry services;

    private DeploymentManager deploymentManager;

    private LoadBalancer loadBalancer;

    private BundleContext bundleContext;

    private FwServiceLeader fwServiceLeader;

    /**
     * SLM constructor
     * @param osgiBundleContext
     */
    public ServiceLifecycleManager(BundleContext osgiBundleContext) {
        Osgi.initialize(osgiBundleContext);
        this.bundleContext = osgiBundleContext;
        this.devices = new DeviceRegistry(osgiBundleContext);
        this.loadBalancer = new LoadBalancer(devices);
        this.deploymentManager = new DeploymentManager(osgiBundleContext);
        services = new DeploymentRegistry(osgiBundleContext, devices);
        try {
            services.recover();
            log.info("Number of all 3P services: " + services.getAllServiceIds(true).length);
            log.info("Number of all local 3P services: " + services.getServicesOnLocalDevice().length);
        } catch (SLMException ex) {
            log.error("Deployment Registry initialization error", ex);
        }
        fwServiceLeader = new FwServiceLeader(devices);
        osgiBundleContext.addBundleListener(new OsgiListenerBundlesChange(services, this));
        try {
            String filter = "(" + Constants.OBJECTCLASS + "=" + IService.class.getName() + ")";
            OsgiListenerServicesChange listener = new OsgiListenerServicesChange(services, this);
            osgiBundleContext.addServiceListener(listener, filter);
        } catch (InvalidSyntaxException e) {
            log.error("Ipojo/IService filter is not valid: " + e.getMessage());
        }
        registerEmsListeners();
        setServiceIdsOnLocalDevice();
    }

    protected final void registerEmsListeners() {
        IEventMgr ems;
        String[] eventTypesPeer = { PeerEventTypes.PEER_JOINED_EVENT, PeerEventTypes.PEER_LEFT_EVENT };
        String[] eventTypesFault = { PeerEventTypes.SERVICE_FAULT_EVENT };
        try {
            ems = OtherFwComponents.getEms();
            log.debug("Found EMS in OSGi");
            ems.registerListener(new EventListenerPeerChange(this, services, fwServiceLeader, devices), eventTypesPeer, null);
            ems.registerListener(new EventListenerServiceFault(services), eventTypesFault, null);
            ems.registerListener(new EventListenerServiceLyfecycle(services, this), new String[] { PSSEventTypes.SERVICE_LIFECYCLE_EVENT }, null);
        } catch (SLMException ex) {
            log.warn("Could not find EMS in local OSGi. The relevant actions " + "will be done after EMS starts. If EMS is found later, " + "this is nothing to worry about.", ex);
        }
    }

    /**
     * Add a new device to the device registry. If the device is already added,
     * then nothing is done, the device is not added again.
	 *
     * @param peerId JXTA peer ID of the device that can be passed to {@link
     * DeviceProfile#DeviceProfile(double[], org.personalsmartspace.sre.slm.impl.Peripheral[]) }
     * @param profileParams Profile parameters of the device
	 */
    @Override
    public void addRemoteDevice(String peerId, double[] profileParams) {
        if (devices.exists(peerId)) {
            log.info("Device " + peerId + " has rejoined.");
            return;
        }
        Device device;
        DeviceProfile profile = new DeviceProfile(profileParams, null);
        try {
            device = new Device(peerId, profile, DeviceState.ON, DeviceType.OTHER);
        } catch (SLMException ex) {
            log.warn("Could not create Device instance", ex);
            return;
        }
        devices.add(device);
    }

    private void deploy(IServiceIdentifier serviceId, URI uri, byte[] jar, ICallbackListener callback) {
        long bundleId;
        Device localDevice = devices.getLocal();
        Service service = services.getService(serviceId);
        String msg;
        if (jar == null) {
            msg = "Code to deploy as a 3rd party service is null.";
            log.warn(msg);
            Rpc.handleError(msg, callback);
            return;
        } else if (localDevice == null) {
            msg = "Cannot deploy service on local device, which is null.";
            log.error(msg);
            Rpc.handleError(msg, callback);
            return;
        }
        try {
            PerformanceMetricsEvaluation.start(41, "ServiceLifecycleManagement", "ServiceLifecycleManagement.DeployLocal", "ServiceLifecycleManagement.DeployLocal.ServiceSize=" + jar.length);
            bundleId = deploymentManager.deployToLocal(serviceId, uri.toString(), jar);
            PerformanceMetricsEvaluation.stop(41, "ServiceLifecycleManagement", "ServiceLifecycleManagement.DeployLocal", "ServiceLifecycleManagement.DeployLocal.ServiceSize=" + jar.length);
        } catch (SLMException ex) {
            Rpc.handleError(ex.getMessage() + ". Cause: " + ex.getCause(), callback);
            return;
        }
        if (bundleId < 0) {
            Rpc.handleError("deployToLocal() failed: " + bundleId, callback);
            return;
        }
        service.bundleId = bundleId;
        services.setPeer(serviceId, localDevice.peerId);
        localDevice.addService(service);
        for (Device dev : devices.listAll()) {
            if (!DeviceRegistry.getLocalId().equals(dev.peerId)) {
                services.syncRemoteRegistry(dev.peerId);
            }
        }
        msg = "Service " + serviceId + " deployed successfully to " + localDevice.peerId;
        log.info(msg);
        Rpc.handleCallbackStr(msg, callback);
        service.okToSetStateToDeployed = true;
        setIdToServiceAndSetState(service);
    }

    @Override
    public void deploy(String id, URI uri, Properties deploymentInfo, byte[] jar, ICallbackListener callback) throws IOException {
        log.info("deploy: " + id + ", JAR size = " + jar != null ? jar.length : "null");
        IServiceIdentifier serviceId = new PssServiceIdentifier(id);
        services.add(serviceId, uri, new DeviceProfile(deploymentInfo));
        deploy(serviceId, uri, jar, callback);
    }

    protected void setIdToServiceAndSetState(Service s) {
        IServiceIdentifier serviceId = s.id;
        try {
            if (!s.idSetToService) {
                getService(serviceId).setID(serviceId);
                s.idSetToService = true;
                log.info("ID set to service " + serviceId);
            }
            if (!s.stateDeployedHasOccured && s.okToSetStateToDeployed) {
                s.setState(ServiceState.Deployed);
                s.okToSetStateToDeployed = false;
                s.stateDeployedHasOccured = true;
            }
            if (s.stateDeployedHasOccured) {
                s.setState(ServiceState.Available);
            }
            log.info("Deployment of service " + serviceId + " finalized.");
        } catch (SLMException ex) {
            log.debug("Could not set ID of just installed service " + serviceId + " . The service is expected to register IService later " + "e.g. through IPOJO or DS and its ID should be set then.", ex);
        }
    }

    @Override
    public IService getService(IServiceIdentifier serviceId) throws SLMException {
        Bundle bundle;
        ServiceReference[] sref;
        Object serviceObj;
        if (!services.isOnLocalDevice(serviceId)) {
            throw new SLMException("Service \"" + serviceId + "\" is not installed on local device.");
        }
        bundle = getOsgiBundle(serviceId);
        if (bundle == null) {
            throw new SLMException("Could not get bundle for service " + serviceId);
        }
        sref = bundle.getRegisteredServices();
        if (sref == null || sref.length < 1) {
            throw new SLMException("Bundle " + bundle.getBundleId() + " has no OSGi services registered");
        }
        log.debug("Bundle " + bundle.getBundleId() + " (service " + serviceId + ") has " + sref.length + " services registered");
        for (int i = 0; i < sref.length; i++) {
            serviceObj = this.bundleContext.getService(sref[i]);
            if (serviceObj == null) {
                log.warn("BundleContext.getService(sref[" + i + "]) returned null!" + " sref[" + i + "] = " + sref[i]);
                throw new SLMException("BundleContext.getService(" + sref + ") returned null!");
            }
            if (IService.class.isAssignableFrom(serviceObj.getClass())) {
                log.debug("Service Reference [" + i + "] is assignable to IService");
                return (IService) this.bundleContext.getService(sref[i]);
            } else {
                log.debug("Service Reference [" + i + "] is not assignable to IService");
            }
        }
        throw new SLMException("Service " + serviceId + " (bundle " + bundle.getBundleId() + ") did not implement and register interface " + IService.class.getName());
    }

    private Bundle getOsgiBundle(IServiceIdentifier serviceId) {
        long bundleId;
        Bundle bundle;
        bundleId = services.getOsgiBundleId(serviceId);
        bundle = this.bundleContext.getBundle(bundleId);
        if (bundle == null) {
            log.warn("Could not get OSGi bundle for service \"" + serviceId + "\"");
            return null;
        }
        return bundle;
    }

    @Override
    public ServiceState getServiceState(IServiceIdentifier serviceId) {
        Service s = services.getService(serviceId);
        if (s == null) {
            return null;
        } else {
            return s.getState();
        }
    }

    @Override
    public IServiceIdentifier install(URI downloadServiceUri, Properties deploymentInfo, String dpi, String peerId, ICallbackListener callback) {
        log.info("Service URI = " + downloadServiceUri + ", DPI for service = " + dpi + ", peer ID = " + peerId);
        String msg;
        DeviceProfile serviceProfile = new DeviceProfile(deploymentInfo);
        IServiceIdentifier serviceId;
        byte[] jar;
        if (DeviceRegistry.getLocalId() == null) {
            msg = "setPeerId(String localPeerId) has not been called yet!";
            log.warn(msg);
            callback.handleErrorMessage(msg);
            return null;
        }
        jar = FileManagement.getFile(downloadServiceUri);
        if (jar == null) {
            msg = "Could not get service file from \"" + downloadServiceUri + "\"";
            log.warn(msg);
            callback.handleErrorMessage(msg);
            return null;
        }
        if (peerId == null) try {
            PerformanceMetricsEvaluation.start(40, "ServiceLifecycleManagement", "ServiceLifecycleManagement.SelectDevice", "ServiceLifecycleManagement.SelectDevice.NumberDevices=" + devices.listAll().length);
            peerId = this.loadBalancer.match(serviceProfile);
            PerformanceMetricsEvaluation.stop(40, "ServiceLifecycleManagement", "ServiceLifecycleManagement.SelectDevice", "ServiceLifecycleManagement.SelectDevice.NumberDevices=" + devices.listAll().length);
            if (peerId == null) {
                msg = "No device meets the requirements of this service. " + "The service can still be forced to get installed to " + "an explicitly selected peer.";
                log.warn(msg);
                callback.handleErrorMessage(msg);
                return null;
            }
        } catch (SLMException e) {
            log.error("Unable to do performance evaluation - " + e);
        }
        serviceId = register(serviceProfile, dpi, downloadServiceUri);
        log.info("Will deploy service " + serviceId + " from " + DeviceRegistry.getLocalId() + " to " + peerId);
        if (DeviceRegistry.getLocalId().equals(peerId)) {
            deploy(serviceId, downloadServiceUri, jar, callback);
        } else {
            deploymentManager.deployToRemote(serviceId, downloadServiceUri, deploymentInfo, jar, peerId, callback);
        }
        return serviceId;
    }

    @Override
    public IServiceIdentifier install(URI downloadServiceUri, Properties deploymentInfo, String dpi, String peerId) {
        return install(downloadServiceUri, deploymentInfo, dpi, peerId, new RpcListenerSuccessFail("install(" + downloadServiceUri + ")"));
    }

    public Device[] listDevices() {
        return devices.listAll();
    }

    @Override
    public IServiceIdentifier[] listServices(boolean alsoRemote) {
        return services.getAllServiceIds(alsoRemote);
    }

    /**
     * Register new PSS service and generate a new unique PSS service ID.
     *
     * @param serviceProfile Device requirements of this service
     * @param dpi Digital Personal Identifier that owns this service
     * @param downloadUri URI where the service JAR can be downloaded from
     * @return ServiceID Generated PSS service ID
     */
    private IServiceIdentifier register(DeviceProfile serviceProfile, String dpi, URI downloadUri) {
        IServiceIdentifier serviceID = createServiceId(dpi);
        services.add(serviceID, downloadUri, serviceProfile);
        services.setState(serviceID, ServiceState.Registered);
        return serviceID;
    }

    /**
     * This method is public only for JUnit test.
     * Creates new service ID, different from any registered IDs.
     *
     * @param dpi DPI to be included in service ID.
     * @return A unique and random service ID
     */
    public IServiceIdentifier createServiceId(String dpi) {
        PssServiceIdentifier id;
        Random rand = new Random();
        long n;
        do {
            n = rand.nextLong();
            if (n < 0) {
                n = 1 - n;
            }
            id = new PssServiceIdentifier(String.format("%019d", n), dpi);
        } while (services.exists(id) == true);
        return id;
    }

    /**
	 * Remove given device from device registry.
     *
	 * @param peerId ID of the device to remove.
	 */
    public void removeDevice(String peerId) {
        devices.remove(peerId);
    }

    @Override
    public void startUserSession(IServiceIdentifier serviceId, String sessionId, IDigitalPersonalIdentifier consumerDpi, ICallbackListener callback) {
        log.info("Service = " + serviceId + ", session = " + sessionId + ", DPI = " + consumerDpi.toString());
        RpcListenerGetProxy getProxyListener = new RpcListenerGetProxy(this, bundleContext, serviceId, sessionId, consumerDpi, callback);
        getProxy(serviceId, getProxyListener);
    }

    @Override
    public void startUserSessionOnService(String serviceIdStr, String sessionId, String consumerDpiStr, ICallbackListener callback) {
        IServiceIdentifier serviceId = new PssServiceIdentifier(serviceIdStr);
        IDigitalPersonalIdentifier consumerDpi;
        String peerId = services.getDevice(serviceId);
        Service s = services.getService(serviceId);
        IService osgiService;
        log.info("Service = " + serviceIdStr + ", DPI = " + consumerDpiStr);
        DigitalPersonalIdentifier dpi2 = new DigitalPersonalIdentifier();
        if (serviceIdStr == null || consumerDpiStr == null) {
            Rpc.handleError(OtherFwComponents.errorStringForSM(serviceId, "serviceId and dpi must not be null"), callback);
            return;
        }
        if (services.isOnLocalDevice(serviceId)) {
            IDigitalPersonalIdentifier publicDpi = OtherFwComponents.getPublicDpi();
            if (publicDpi == null) {
                callback.handleErrorMessage(OtherFwComponents.errorStringForSM(serviceId, "Could not get public DPI."));
                return;
            }
            try {
                osgiService = getService(serviceId);
                consumerDpi = DigitalPersonalIdentifier.fromString(consumerDpiStr);
                if (osgiService.startUserSession(sessionId, consumerDpi, publicDpi)) {
                    s.addSession(new UserSession(sessionId, consumerDpi));
                    OtherFwComponents.postEventToLocal(PeerEventTypes.START_USERSESSION_EVENT, "", new UserSession(sessionId, consumerDpi, serviceId));
                    log.info("Session " + sessionId + " started on service " + serviceIdStr + " for DPI " + consumerDpiStr);
                    Rpc.handleCallbackObj(OtherFwComponents.successStringForSM(serviceId), callback);
                } else {
                    Rpc.handleError(OtherFwComponents.errorStringForSM(serviceId, "Service refused to start a new session"), callback);
                }
            } catch (SLMException ex) {
                log.warn(ex);
                Rpc.handleError(OtherFwComponents.errorStringForSM(serviceId, ex.getMessage()), callback);
            } catch (MalformedDigitialPersonalIdentifierException ex) {
                log.warn(ex);
                Rpc.handleError(OtherFwComponents.errorStringForSM(serviceId, ex.getMessage()), callback);
            }
        } else {
            ServiceMessage message = new ServiceMessage(ISlm2Slm.class.getName(), ISlm2Slm.class.getName(), peerId != null ? peerId : serviceId.getOperatorId(), peerId == null, "startUserSessionOnService", true, new Object[] { serviceIdStr, sessionId, consumerDpiStr }, new String[] { String.class.getName(), String.class.getName(), String.class.getName() });
            Rpc.invoke(message, callback);
            log.debug("Initiated remote startUserSessionOnService for service " + serviceIdStr);
        }
    }

    @Override
    public void stopUserSession(IServiceIdentifier serviceId, String sessionId, IDigitalPersonalIdentifier consumerDpi, ICallbackListener callback) {
        log.info("Service = " + serviceId + ", session = " + sessionId + ", DPI = " + consumerDpi.toString());
        RpcListenerStopUserSessionOnService stopUserSessionOnServiceListener = new RpcListenerStopUserSessionOnService(bundleContext, this, serviceId, sessionId, consumerDpi, callback);
        stopUserSessionOnService(serviceId.toUriString(), sessionId, consumerDpi.toUriString(), stopUserSessionOnServiceListener);
    }

    @Override
    public void stopUserSessionOnService(String serviceIdStr, String sessionId, String consumerDpiStr, ICallbackListener callback) {
        log.info("Service = " + serviceIdStr + ", DPI = " + consumerDpiStr);
        IServiceIdentifier serviceId = new PssServiceIdentifier(serviceIdStr);
        IDigitalPersonalIdentifier consumerDpi;
        String peerId = services.getDevice(serviceId);
        Service s = services.getService(serviceId);
        IService osgiService;
        if (serviceIdStr == null || consumerDpiStr == null) {
            Rpc.handleError(OtherFwComponents.errorStringForSM(serviceId, "serviceId and dpi must not be null"), callback);
            return;
        }
        if (services.isOnLocalDevice(serviceId)) {
            IDigitalPersonalIdentifier publicDpi = OtherFwComponents.getPublicDpi();
            if (publicDpi == null) {
                callback.handleErrorMessage("Could not get public DPI.");
                return;
            }
            try {
                osgiService = getService(serviceId);
                consumerDpi = DigitalPersonalIdentifier.fromString(consumerDpiStr);
                if (osgiService.stopUserSession(sessionId, consumerDpi, publicDpi)) {
                    s.removeSession(sessionId);
                    OtherFwComponents.postEventToLocal(PeerEventTypes.STOP_USERSESSION_EVENT, "", new UserSession(sessionId, consumerDpi, serviceId));
                    log.info("Session " + sessionId + " stopped on service " + serviceIdStr + " for DPI " + consumerDpiStr);
                    Rpc.handleCallbackObj(OtherFwComponents.successStringForSM(serviceId), callback);
                } else {
                    Rpc.handleError(OtherFwComponents.errorStringForSM(serviceId, "Service refused to stop session"), callback);
                }
            } catch (SLMException ex) {
                log.warn(ex);
                Rpc.handleError(OtherFwComponents.errorStringForSM(serviceId, ex.getMessage()), callback);
            } catch (MalformedDigitialPersonalIdentifierException ex) {
                log.warn(ex);
                Rpc.handleError(OtherFwComponents.errorStringForSM(serviceId, ex.getMessage()), callback);
            }
        } else {
            ServiceMessage message = new ServiceMessage(ISlm2Slm.class.getName(), ISlm2Slm.class.getName(), peerId != null ? peerId : serviceId.getOperatorId(), peerId == null, "stopUserSessionOnService", true, new Object[] { serviceIdStr, sessionId, consumerDpiStr }, new String[] { String.class.getName(), String.class.getName(), String.class.getName() });
            Rpc.invoke(message, callback);
            log.debug("Initiated remote stopUserSessionOnService for service " + serviceIdStr);
        }
    }

    @Override
    public int uninstall(IServiceIdentifier serviceId) {
        RpcListenerSuccessFail callback = new RpcListenerSuccessFail("uninstall(" + serviceId + ")");
        uninstall(serviceId, callback);
        if (callback.hasBeenInvoked()) {
            if (callback.isSuccessful()) {
                return 0;
            } else {
                return -1;
            }
        } else {
            return -2;
        }
    }

    @Override
    public void uninstall(IServiceIdentifier serviceId, ICallbackListener callback) {
        String deviceId = services.getDevice(serviceId);
        Bundle bundle;
        String msg;
        if (!services.exists(serviceId)) {
            msg = "Service " + serviceId + " is not registered.";
            log.warn(msg);
            Rpc.handleError(msg, callback);
            return;
        }
        if (deviceId == null) {
            services.remove(serviceId, true);
            msg = "Service " + serviceId + " not installed in this PSS, removed from registry.";
            log.info(msg);
            Rpc.handleCallbackStr(msg, callback);
            return;
        } else if (deviceId.equals(DeviceRegistry.getLocalId())) {
            bundle = getOsgiBundle(serviceId);
            log.info("About to uninstall OSGi bundle \"" + bundle.getSymbolicName() + "\" (Bundle ID = " + bundle.getBundleId() + ", " + "service ID = \"" + serviceId + "\")");
            try {
                bundle.uninstall();
            } catch (BundleException ex) {
                msg = "Could not uninstall OSGi bundle \"" + bundle.getSymbolicName() + "\" (ID=" + bundle.getBundleId() + "). " + ex.getMessage();
                log.warn(msg);
                Rpc.handleError(msg, callback);
                return;
            }
            services.remove(serviceId, true);
            Rpc.handleCallbackStr("Success", callback);
            return;
        } else {
            ServiceMessage message = new ServiceMessage(IServiceLifecycleManager.class.getName(), IServiceLifecycleManager.class.getName(), deviceId, false, "uninstall", true, XMLConverter.objectsToXml(new Object[] { serviceId }), new String[] { IServiceIdentifier.class.getName() });
            message.setEmbeddedParaTypes(new String[] { serviceId.getClass().getName() });
            Rpc.invoke(message, callback);
        }
    }

    @Override
    public ServiceReference getServiceReference(IServiceIdentifier serviceId) {
        Bundle bundle;
        ServiceReference[] sref;
        Object serviceObj;
        if (!services.isOnLocalDevice(serviceId)) {
            log.warn("Service \"" + serviceId + "\" is not installed on local device.");
            return null;
        }
        bundle = getOsgiBundle(serviceId);
        if (bundle == null) {
            log.warn("Could not get bundle for service " + serviceId);
            return null;
        }
        sref = bundle.getRegisteredServices();
        if (sref == null || sref.length < 1) {
            log.warn("Bundle " + bundle.getBundleId() + " has no OSGi services registered");
            return null;
        }
        log.debug("Bundle " + bundle.getBundleId() + " (service " + serviceId + ") has " + sref.length + " services registered");
        for (int i = 0; i < sref.length; i++) {
            serviceObj = this.bundleContext.getService(sref[i]);
            if (serviceObj == null) {
                log.warn("BundleContext.getService(sref[" + i + "]) returned null!" + " sref[" + i + "] = " + sref[i]);
            } else if (IService.class.isAssignableFrom(serviceObj.getClass())) {
                log.debug("Service Reference [" + i + "] is assignable to IService");
                return sref[i];
            } else {
                log.debug("Service Reference [" + i + "] is not assignable to IService");
            }
        }
        log.warn("Service " + serviceId + " (bundle " + bundle.getBundleId() + ") did not implement and register interface " + IService.class.getName());
        return null;
    }

    @Override
    public IServiceIdentifier getServiceId(long bundleId) {
        Service s = services.getService(bundleId);
        return (s == null) ? null : s.id;
    }

    @Override
    public String getPeer(IServiceIdentifier serviceId) {
        Service s = services.getService(serviceId);
        return (s == null) ? null : s.peer;
    }

    @Override
    public void setPeerId(String localPeerId) {
        if (localPeerId == null) {
            throw new IllegalArgumentException("Local peer ID must not be null.");
        }
        devices.setLocalDeviceId(localPeerId);
        services.setLocalPeerId(localPeerId);
    }

    /**
     * Set service IDs to services running on the local device.
     */
    private void setServiceIdsOnLocalDevice() {
        Service[] localServices = services.getServicesOnLocalDevice();
        IService osgiService;
        for (Service s : localServices) {
            try {
                log.info("Will set ID to service " + s.id);
                osgiService = getService(s.id);
                osgiService.setID(s.id);
                s.idSetToService = true;
            } catch (SLMException ex) {
                log.warn("Could not tell service " + s.id + " its ID. " + "Reason:" + ex.getMessage() + ". If the service registers IService later and its ID" + " is set then, this warning is harmless.");
                s.idSetToService = false;
            }
        }
    }

    /**
     * Cleanup at shutdown.
     */
    protected void shutdown() throws SLMException {
        services.persist();
    }

    @Override
    public Object getProxy(IServiceIdentifier serviceId) {
        Service service = services.getService(serviceId);
        boolean serviceInThisPss = (service != null && service.isInThisPss);
        if (services.isOnLocalDevice(serviceId)) {
            log.info("Service " + serviceId + " is on local peer, no proxy needed.");
            try {
                return getService(serviceId);
            } catch (SLMException ex) {
                log.warn("Could not get local service " + serviceId, ex);
                return null;
            }
        } else if (service != null && service.proxyBundleId > 0) {
            Object proxy = new Osgi(null, "(&(" + PssConstants.LOCAL_SERVICE_ID + "=" + serviceId.toUriString() + "))").get();
            if (proxy != null) {
                log.info("Returning existing and OSGi-registered proxy for " + serviceId);
                return proxy;
            }
            Osgi osgi = new Osgi(IProxyFactory.class.getName());
            ServiceReference[] srefFactory = osgi.getServiceReferences();
            if (srefFactory == null) {
                log.warn("No proxy factory found in OSGi.");
                return null;
            }
            for (ServiceReference srFactory : srefFactory) {
                if (srFactory.getBundle().getBundleId() == service.proxyBundleId) {
                    IProxyFactory factory = (IProxyFactory) bundleContext.getService(srFactory);
                    if (serviceInThisPss) {
                        log.info("Returning proxy for service " + serviceId + " on peer " + service.peer);
                        return factory.getProxy(serviceId, service.peer);
                    } else {
                        log.info("Returning proxy for service " + serviceId);
                        return factory.getProxy(serviceId);
                    }
                }
            }
            log.warn("No proxy found in OSGi, although it should be already installed.");
            return null;
        } else {
            log.warn("No service nor proxy for " + serviceId + " on this peer.");
            return null;
        }
    }

    @Override
    public void getProxy(IServiceIdentifier serviceId, ICallbackListener callback) {
        Service service;
        ServiceMessage message;
        Object existingProxy = getProxy(serviceId);
        String msg;
        log.info("Will try to get proxy or service object for " + serviceId);
        if (!services.exists(serviceId)) {
            services.addServiceFromOtherPss(serviceId);
        }
        service = services.getService(serviceId);
        if (callback == null) {
            log.warn("Callback to get proxy for service " + serviceId + " is null.");
            return;
        }
        if (existingProxy != null) {
            log.debug("Will return existing proxy or service " + serviceId);
            callback.handleCallbackObject(existingProxy);
            return;
        } else if (services.isOnLocalDevice(serviceId)) {
            msg = "Service " + serviceId + " should be on local device, but is not found";
            log.error(msg);
            callback.handleErrorMessage(msg);
        } else {
            message = new ServiceMessage(IServiceLifecycleManager.class.getName(), ISlm2Slm.class.getName(), service.isInThisPss ? service.peer : serviceId.getOperatorId(), !service.isInThisPss, "getProxyJar", true, new Object[] { serviceId.toUriString() }, new String[] { String.class.getName() });
            Rpc.invoke(message, new RpcListenerGetProxyJar(deploymentManager, serviceId.toString() + "_proxy", service, this, callback));
            log.debug("Initiated downloading (from " + message.getDestinationID() + ") and installing of proxy for service " + serviceId);
        }
    }

    @Override
    public void getProxyJar(String serviceId, ICallbackListener listener) {
        PssServiceIdentifier id;
        Service s;
        byte[] jar;
        ServiceMessage message;
        log.debug("getProxyJar(" + serviceId + ")");
        if (listener == null) {
            log.warn("Callback to get proxy JAR for service " + serviceId + " is null.");
            return;
        }
        id = new PssServiceIdentifier(serviceId);
        s = services.getService(id);
        if (s == null) {
            listener.handleErrorMessage("Service " + serviceId + " is not installed in this PSS.");
        } else if (services.isOnLocalDevice(id)) {
            jar = s.getProxyJar(bundleContext);
            if (jar != null) {
                listener.handleCallbackObject(jar);
            } else {
                listener.handleErrorMessage("Service " + serviceId + " has no proxy");
            }
        } else {
            message = new ServiceMessage(IServiceLifecycleManager.class.getName(), ISlm2Slm.class.getName(), s.peer, false, "getProxyJar", true, new Object[] { serviceId }, new String[] { String.class.getName() });
            Rpc.invoke(message, listener);
            log.debug("Initiated downloading of proxy for service " + serviceId + " from " + s.peer);
        }
    }

    @Override
    public void getGuiJar(String serviceId, ICallbackListener listener) {
        PssServiceIdentifier id;
        Service s;
        byte[] jar;
        ServiceMessage message;
        log.debug("getGuiJar(" + serviceId + ")");
        if (listener == null) {
            log.warn("Callback to get GUI JAR for service " + serviceId + " is null.");
            return;
        }
        id = new PssServiceIdentifier(serviceId);
        s = services.getService(id);
        if (s == null) {
            listener.handleErrorMessage("Service " + serviceId + " is not registered on this peer.");
        } else if (!s.isInThisPss) {
            listener.handleErrorMessage("Service " + serviceId + " found, but it is not installed in this PSS.");
        } else if (services.isOnLocalDevice(id)) {
            jar = s.getGuiJar(bundleContext);
            if (jar != null) {
                listener.handleCallbackObject(jar);
            } else {
                listener.handleCallbackString("GUI JAR not found.");
            }
        } else {
            message = new ServiceMessage(IServiceLifecycleManager.class.getName(), ISlm2Slm.class.getName(), s.peer, false, "getGuiJar", true, new Object[] { serviceId }, new String[] { String.class.getName() });
            Rpc.invoke(message, listener);
            log.debug("Initiated downloading of GUI for service " + serviceId + " from " + s.peer);
        }
    }

    @Override
    public void showGui(String serviceId, String peerId, String state, ICallbackListener callback) {
        IServiceIdentifier id = new PssServiceIdentifier(serviceId);
        Service service;
        ServiceMessage message;
        String localPeerId = DeviceRegistry.getLocalId();
        RpcListenerGetGuiJar getGuiJarListener;
        log.info("Will show GUI for service " + serviceId + " at " + (peerId == null ? "local peer" : ("peer " + peerId)) + " and set its state to " + state);
        if (!services.exists(id)) {
            services.addServiceFromOtherPss(id);
        }
        service = services.getService(id);
        getGuiJarListener = new RpcListenerGetGuiJar(deploymentManager, serviceId + "_GUI", service, state, bundleContext, callback);
        if (peerId == null || localPeerId.equals(peerId)) {
            log.debug("Service bundle ID = " + service.bundleId + ". Proxy bundle ID = " + service.proxyBundleId);
            if (!service.isProxyInstalledOrServiceLocal()) {
                log.warn("No proxy nor service is deployed locally. Will deploy proxy now.");
                getProxy(id, new RpcListenerDeployProxy(getGuiJarListener, service, state));
            }
            if (service.guiBundleId > 0) {
                log.debug("GUI bundle is already installed. Will set it up.");
                getGuiJarListener.setupGuiAtLocalPeer(state);
                return;
            } else if (localPeerId.equals(service.peer)) {
                log.debug("GUI bundle not yet installed, will try to get its JAR locally.");
                byte[] guiJar = service.getGuiJar(bundleContext);
                if (guiJar != null) {
                    getGuiJarListener.deploy(guiJar);
                } else {
                    log.debug("Service " + serviceId + " has no GUI");
                    Rpc.handleCallbackStr("Service " + serviceId + " has no GUI", callback);
                }
            } else {
                log.debug("Will download GUI bundle and install it.");
                message = new ServiceMessage(IServiceLifecycleManager.class.getName(), ISlm2Slm.class.getName(), service.isInThisPss ? service.peer : id.getOperatorId(), !service.isInThisPss, "getGuiJar", true, new Object[] { serviceId }, new String[] { String.class.getName() });
                Rpc.invoke(message, getGuiJarListener);
                log.debug("Initiated downloading (from " + message.getDestinationID() + ") and installing of GUI for service " + id);
                return;
            }
        } else {
            message = new ServiceMessage(IServiceLifecycleManager.class.getName(), IServiceLifecycleManager.class.getName(), peerId, false, "showGui", true, new Object[] { serviceId, peerId, state }, new String[] { String.class.getName(), String.class.getName(), String.class.getName() });
            Rpc.invoke(message, callback);
            log.debug("Initiated showing of GUI for service " + serviceId + " at peer " + peerId);
        }
    }

    @Override
    public void moveGui(String serviceId, String peerId, ICallbackListener callback) {
        IServiceIdentifier id = new PssServiceIdentifier(serviceId);
        Service service = services.getService(id);
        String msg;
        log.info("Will move GUI for service " + serviceId + " to peer " + peerId);
        if (service == null) {
            msg = "Service " + serviceId + " not found.";
            log.warn(msg);
            callback.handleErrorMessage(msg);
            return;
        }
        Vector<String> allPeersToHideGui = new Vector<String>();
        Device[] allDevices = devices.listAll();
        for (Device d : allDevices) {
            allPeersToHideGui.add(d.peerId);
        }
        showGui(serviceId, peerId, null, callback);
        RpcListenerMoveGui hideGuiCallback = new RpcListenerMoveGui(serviceId, peerId, this, callback);
        if (allPeersToHideGui.isEmpty()) {
            log.debug("No GUI previously shown, will show new GUI.");
            showGui(serviceId, peerId, null, callback);
        } else {
            for (String peerToHideGui : allPeersToHideGui) {
                if (!peerToHideGui.equals(peerId)) {
                    hideGui(serviceId, peerToHideGui, hideGuiCallback);
                }
            }
        }
    }

    @Override
    public void hideGui(String serviceId, String peerId, ICallbackListener callback) {
        IServiceIdentifier id = new PssServiceIdentifier(serviceId);
        Service service;
        ServiceMessage message;
        String localPeerId = DeviceRegistry.getLocalId();
        String msg;
        log.info("Will hide GUI for service " + serviceId + " at peer " + peerId != null ? peerId : "(local)");
        if (!services.exists(id)) {
            services.addServiceFromOtherPss(id);
        }
        service = services.getService(id);
        if (peerId == null || localPeerId.equals(peerId)) {
            if (service.guiBundleId < 0) {
                msg = "GUI for service " + serviceId + " is not installed on this peer.";
                log.warn(msg);
                callback.handleErrorMessage(msg);
                return;
            }
            log.debug("Will hide local GUI for service " + serviceId + " by stopping the GUI bundle, ID = " + service.guiBundleId);
            Bundle bundle = bundleContext.getBundle(service.guiBundleId);
            if (bundle == null) {
                msg = "Could not find bundle " + service.guiBundleId + " for service " + service.id;
                log.warn(msg);
                callback.handleErrorMessage(msg);
                return;
            }
            IGui gui = (IGui) new Osgi(IGui.class.getName()).get(service.guiBundleId);
            String guiState = null;
            if (gui != null) {
                guiState = gui.getState();
                log.debug("GUI state = " + guiState);
            } else {
                log.warn("Could not find OSGi service " + IGui.class.getName() + " in bundle " + service.bundleId);
            }
            try {
                bundle.stop();
                callback.handleCallbackObject(guiState);
            } catch (BundleException ex) {
                msg = "Could not hide GUI for " + service.id + " because bundle " + service.guiBundleId + " could not be stopped.";
                log.warn(msg, ex);
                callback.handleErrorMessage(msg);
            }
        } else {
            message = new ServiceMessage(IServiceLifecycleManager.class.getName(), ISlm2Slm.class.getName(), peerId, false, "hideGui", true, new Object[] { serviceId, peerId }, new String[] { String.class.getName(), String.class.getName() });
            Rpc.invoke(message, callback);
            log.debug("Initiated hiding of GUI for service " + serviceId + " at peer " + peerId);
        }
    }

    @Override
    public void setServiceLeader(String frameworkServiceId, String peerId) {
        fwServiceLeader.set(frameworkServiceId, peerId);
    }

    @Override
    public String getServiceLeader(String frameworkServiceId) {
        return fwServiceLeader.get(frameworkServiceId);
    }

    @Override
    public void syncFwServiceLeader(String leaderAsXml, String lastChangeAsXml) {
        fwServiceLeader.sync(leaderAsXml, lastChangeAsXml);
    }

    @Override
    public void syncDeploymentRegistry(Vector servicesRegisteredOnRemoteSlm) {
        services.mergeSync(servicesRegisteredOnRemoteSlm);
    }

    @Override
    public long getBundleId(IServiceIdentifier serviceId) {
        Service s = services.getService(serviceId);
        return s != null ? s.bundleId : -1;
    }

    @Override
    public long getProxyBundleId(IServiceIdentifier serviceId) {
        Service s = services.getService(serviceId);
        return s != null ? s.proxyBundleId : -1;
    }

    @Override
    public long getGuiBundleId(IServiceIdentifier serviceId) {
        Service s = services.getService(serviceId);
        return s != null ? s.guiBundleId : -1;
    }

    @Override
    public HashMap<String, UserSession> getSessions(IServiceIdentifier serviceId) {
        Service s = services.getService(serviceId);
        return s != null ? s.getSessions() : null;
    }

    @Override
    public boolean isInThisPSS(IServiceIdentifier serviceID) {
        Service s = services.getService(serviceID);
        if (s == null) {
            return false;
        }
        return s.isInThisPss;
    }
}
