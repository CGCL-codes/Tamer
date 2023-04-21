package org.soda.dpws.registry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ws.policy.Policy;
import org.apache.ws.policy.util.PolicyFactory;
import org.apache.ws.policy.util.PolicyWriter;
import org.jdom.Element;
import org.soda.dpws.DPWSException;
import org.soda.dpws.addressing.EndpointReference;
import org.soda.dpws.addressing.UserEndpointReference;
import org.soda.dpws.addressing.WSAConstants;
import org.soda.dpws.exchange.MessageSerializer;
import org.soda.dpws.handler.AbstractHandlerSupport;
import org.soda.dpws.internal.DPWSContextImpl;
import org.soda.dpws.invocation.Call;
import org.soda.dpws.metadata.MetadataConstants;
import org.soda.dpws.registry.eventing.EventingWSDLInfoFactory;
import org.soda.dpws.registry.eventing.WSEConstants;
import org.soda.dpws.server.ServicePort;
import org.soda.dpws.service.Binding;
import org.soda.dpws.util.LocalizedString;
import org.soda.dpws.util.xml.PartBuilder;
import org.soda.dpws.wsdl.OperationInfo;
import org.soda.dpws.wsdl.PortTypeInfo;
import org.soda.dpws.wsdl.WSDLInfo;
import eu.more.core.internal.proxy.Ifmappingtable;
import eu.more.core.internal.proxy.Mappedservice;

/**
 * A DPWS hosted service.
 *
 * It is created automatically when a device is instantiated from a
 * {@link DeviceModel}. It should not be used by the toolkit's user except for
 * adding it to a device. A service's sole job is to process xml messages and
 * pass it to the applicative implementation.
 */
public class ServiceEndpoint extends AbstractHandlerSupport {

    private ServiceClass serviceClass;

    private String id = null;

    private Map<String, Object> actionToImplementor = new HashMap<String, Object>();

    protected List<QName> supportedTypes = new ArrayList<QName>();

    protected Map<String, OperationInfo> invokationMap = new HashMap<String, OperationInfo>();

    protected Map<String, OperationInfo> operationMap = new HashMap<String, OperationInfo>();

    protected Map<String, ServicePort> physicalBindings = new HashMap<String, ServicePort>();

    private Device hostDevice = null;

    private MessageSerializer faultSerializer;

    protected List<PortTypeInfo> portTypeInfos = new ArrayList<PortTypeInfo>();

    private QName name;

    private Map<String, EventSubscription> eventSubscriptions = new HashMap<String, EventSubscription>();

    private boolean eventSource = false;

    private Log log = LogFactory.getLog(ServiceEndpoint.class);

    private Ifmappingtable ifmappingpe = null;

    /**
	 * Encapsulated Method for setting Interface Mapping Table
	 * @param ifmappingpe
	 */
    public void setIfmappingpe(Ifmappingtable ifmappingpe) {
        this.ifmappingpe = ifmappingpe;
    }

    /**
	 * Encapsulated Method returning the Interface Mapping Table
	 * @return
	 */
    public Ifmappingtable getIfmappingpe() {
        return ifmappingpe;
    }

    /**
   * Default constructor.
   */
    ServiceEndpoint(String id) {
        setId(id);
    }

    /**
   * Method closing the Service.
   *
   * @throws DPWSException
   */
    public void shutdown() throws DPWSException {
        closeAllSubscriptions(WSEConstants.SOURCESHUTTINGDOWN_URI, new LocalizedString("Shut Down", Locale.US));
    }

    private void closeAllSubscriptions(String status, LocalizedString reason) {
        synchronized (eventSubscriptions) {
            if (!eventSource || eventSubscriptions.isEmpty()) return;
            Iterator<String> it = eventSubscriptions.keySet().iterator();
            while (it.hasNext()) {
                String uuid = it.next();
                EventSubscription eventSubscription = eventSubscriptions.get(uuid);
                try {
                    sendSubscriptionEnd(eventSubscription, status, reason);
                } catch (DPWSException e) {
                }
            }
            eventSubscriptions.clear();
        }
    }

    /**
   * Remove an endpoint
   *
   * Reverse to {@link #addEndpoint(ServicePort endpoint) addEndpoint}
   *
   * @category UNDEPLOYMENT
   * @param endpoint
   */
    public void removeEndpoint(ServicePort endpoint) {
        physicalBindings.remove(endpoint.getAddress());
    }

    private void sendSubscriptionEnd(EventSubscription eventSubscription, String status, LocalizedString reason) throws DPWSException {
        if (eventSubscription.getEndTo() == null) return;
        Element elt = PartBuilder.createSubscriptionEndBody(this, eventSubscription, status, reason);
        EndpointReference epr = eventSubscription.getEndTo();
        String action = WSEConstants.SUBSCRIPTION_END_ACTION;
        Call call = new Call(new UserEndpointReference(epr));
        call.send(action, elt);
    }

    /**
   * Binds a service with a specific model and implementor. The constructors
   * compares the interfaces implemented by the implementor and those that can
   * be found in through the model. It fills a list of supported types and a map
   * relating action names and methods to be invoked.
   *
   * @param serviceClass
   *          the model for this service.
   * @param implementor
   *          the object implementing this service.
   * @param doAddToSupportedTypes
   * @param registerServiceClass
   * @throws DPWSException
   */
    @SuppressWarnings("unchecked")
    public void bind(ServiceClass serviceClass, Object implementor, boolean doAddToSupportedTypes, boolean registerServiceClass) throws DPWSException {
        if (registerServiceClass) this.serviceClass = serviceClass;
        if (serviceClass == null) return;
        List portTypeInfos = serviceClass.getPortTypeInfos();
        if (portTypeInfos == null) return;
        if (implementor != null) {
            if (implementor.getClass().getSuperclass().equals(eu.more.core.internal.MOREService.class)) {
                eu.more.core.internal.MOREService moresrv = (eu.more.core.internal.MOREService) implementor;
                if (moresrv.isproxy) {
                    for (int i = 0; i < portTypeInfos.size(); i++) {
                        PortTypeInfo pti = (PortTypeInfo) portTypeInfos.get(i);
                        String interName = pti.getServerInterface().getName();
                        for (int j = 0; j < portTypeInfos.size(); j++) {
                            PortTypeInfo portTypeInfo = (PortTypeInfo) portTypeInfos.get(j);
                            Class serverInterface = portTypeInfo.getServerInterface();
                            if (fillInformation(serverInterface, interName, portTypeInfo, implementor, false, doAddToSupportedTypes)) break;
                            Class handlerInterface = portTypeInfo.getHandlerInterface();
                            if (fillInformation(handlerInterface, interName, portTypeInfo, implementor, true, doAddToSupportedTypes)) break;
                        }
                    }
                }
            }
            Class[] inters = implementor.getClass().getInterfaces();
            for (int i = 0; i < inters.length; i++) {
                String interName = inters[i].getName();
                for (int j = 0; j < portTypeInfos.size(); j++) {
                    PortTypeInfo portTypeInfo = (PortTypeInfo) portTypeInfos.get(j);
                    Class serverInterface = portTypeInfo.getServerInterface();
                    if (fillInformation(serverInterface, interName, portTypeInfo, implementor, false, doAddToSupportedTypes)) break;
                    Class handlerInterface = portTypeInfo.getHandlerInterface();
                    if (fillInformation(handlerInterface, interName, portTypeInfo, implementor, true, doAddToSupportedTypes)) break;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private boolean fillInformation(Class testClass, String interName, PortTypeInfo portTypeInfo, Object implementor, boolean isEventHandler, boolean doAddToSupportedTypes) throws DPWSException {
        if (testClass != null && (testClass.getName().equals(interName) || interName.equals("java.lang.reflect.InvocationHandler"))) {
            if (!eventSource && !isEventHandler && portTypeInfo.isEventSource()) {
                eventSource = true;
                synchronized (eventSubscriptions) {
                    eventSubscriptions.clear();
                }
                WSDLInfo wsdlInfo = EventingWSDLInfoFactory.getWSDLInfo("");
                Iterator it = wsdlInfo.getPortTypeInfos().iterator();
                while (it.hasNext()) {
                    PortTypeInfo pti = (PortTypeInfo) it.next();
                    Iterator itPti = pti.getOperations().iterator();
                    while (itPti.hasNext()) {
                        OperationInfo aoi = (OperationInfo) itPti.next();
                        invokationMap.put(aoi.getInAction(), aoi);
                    }
                }
            }
            if (doAddToSupportedTypes) this.supportedTypes.add(portTypeInfo.getName());
            Collection operations = portTypeInfo.getOperations();
            for (Iterator it = operations.iterator(); it.hasNext(); ) {
                OperationInfo aoi = (OperationInfo) it.next();
                if (aoi.isEvent() == isEventHandler) {
                    invokationMap.put(isEventHandler ? aoi.getOutAction() : aoi.getInAction(), aoi);
                    actionToImplementor.put(isEventHandler ? aoi.getOutAction() : aoi.getInAction(), implementor);
                    operationMap.put(aoi.getName(), aoi);
                }
            }
            portTypeInfos.add(portTypeInfo);
            return true;
        }
        return false;
    }

    /**
   * Returns the supported types of the service
   *
   * @return the types supported by this service.
   */
    public List<QName> getSupportedTypes() {
        return Collections.unmodifiableList(supportedTypes);
    }

    /**
   * Returns the service class of the service.
   *
   * @return the service class that created the service.
   */
    public ServiceClass getServiceClass() {
        return serviceClass;
    }

    /**
   * Returns the id of the service.
   *
   * @return the <code>URI</code> representing the DPWS id of the service.
   */
    public String getId() {
        return id;
    }

    /**
   * Sets the DPWS id of the service.
   *
   * @param id
   *          the <code>URI</code> representing the DPWS id of the service.
   */
    public void setId(String id) {
        this.id = id;
        if (id != null) setName(new QName(this.id));
    }

    /**
   * Returns a collection of endpoints assciated to the service.
   *
   * @return the service associated endpoint collection.
   */
    public Collection<ServicePort> getPhysicalBindings() {
        return Collections.unmodifiableCollection(physicalBindings.values());
    }

    /**
   * Method binding an endpoint to the service.
   *
   * @param endpoint
   *          the endpoint that was bound to the service.
   */
    public void addEndpoint(ServicePort endpoint) {
        physicalBindings.put(endpoint.getAddress(), endpoint);
    }

    /**
   *
   */
    public void removeAllEndpoints() {
        physicalBindings.clear();
    }

    public Object[] invoke(DPWSContextImpl context, OperationInfo context_aoi, Object body) throws DPWSException {
        String actionURI = (context_aoi.isEvent()) ? context_aoi.getOutAction() : context_aoi.getInAction();
        OperationInfo aoi = invokationMap.get(actionURI);
        if (aoi == null) throw new DPWSException("Could not invoke action : " + actionURI + " .");
        Method method = aoi.getMethod();
        if (method == null) throw new DPWSException("No method define for action : " + actionURI + " .");
        Class<?>[] classes = method.getParameterTypes();
        if (classes == null) throw new DPWSException("Method with incorrect parameters for action : " + actionURI + " .");
        int len = classes.length;
        Object[] params = new Object[classes.length];
        params[0] = context;
        Object localImplementor = actionToImplementor.get(actionURI);
        if (body == null) {
        } else if (body instanceof List) {
            List<?> bodyList = (List<?>) body;
            if (!(localImplementor instanceof InvocationHandler) && len != 1 + bodyList.size()) throw new DPWSException("Incorrect parameter number for action : " + actionURI + " .");
            for (int i = 0; i < bodyList.size(); i++) params[i + 1] = bodyList.get(i);
        } else {
            params[1] = new Object[] { body };
        }
        try {
            Class<?> retClass = method.getReturnType();
            if (localImplementor instanceof InvocationHandler) {
                InvocationHandler handler = (InvocationHandler) localImplementor;
                Object obj = handler.invoke(null, method, params);
                return new Object[] { obj };
            }
            if (retClass.isArray()) {
                return (Object[]) method.invoke(localImplementor, params);
            }
            Object obj = null;
            if ((getIfmappingpe() != null) && (getIfmappingpe().serviceMap.containsKey(actionURI))) {
                Mappedservice ms = getIfmappingpe().serviceMap.get(actionURI);
                if (ms.invparams.length == 0) obj = ms.invmethode.invoke(getIfmappingpe().invokerinstanz); else {
                    Object[] invokingparameters = new Object[params.length - 1];
                    for (int i = 0; i < params.length - 1; i++) invokingparameters[0] = params[i + 1];
                    obj = ms.invmethode.invoke(getIfmappingpe().invokerinstanz, invokingparameters);
                }
            } else {
                obj = method.invoke(localImplementor, params);
            }
            return new Object[] { obj };
        } catch (IllegalArgumentException e) {
            throw new DPWSException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new DPWSException(e.getMessage());
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof DPWSException) throw (DPWSException) e.getTargetException();
            throw new DPWSException(e.getTargetException());
        } catch (Throwable e) {
            log.error("While invoking InvocationHandler", e);
            throw new DPWSException(e.getMessage());
        }
    }

    /**
   * @param context
   * @param action
   * @param bindingTransport
   * @return the associated {@link Binding}
   */
    public Binding getBinding(DPWSContextImpl context, String action, String bindingTransport) {
        if (action == null) return null;
        OperationInfo opInfo = invokationMap.get(action);
        if (opInfo == null) return null;
        PortTypeInfo pti = opInfo.getPortTypeInfo();
        if (pti == null) return null;
        return pti.getBinding(bindingTransport);
    }

    /**
   * Method accessing to the addressing operation information by the action
   * name.
   *
   * @param actionName
   *          the name of the action whose information must be retrieved.
   * @return the addressing information if any.
   */
    public OperationInfo getAddressingOperationInfoByInAction(String actionName) {
        return invokationMap.get(actionName);
    }

    /**
   * Method accessing the service parent device if any.
   *
   * @return the <code>Device</code> hosting the service if any.
   */
    public Device getHostDevice() {
        return hostDevice;
    }

    /**
   * @param hostDevice
   */
    public void setHostDevice(Device hostDevice) {
        this.hostDevice = hostDevice;
    }

    /**
   * Accessor.
   *
   * @return the {@link Registry} associated to the service host.
   */
    public Registry getRegistry() {
        return hostDevice.getRegistry();
    }

    Collection<Element> buildMetadataSectionElements() {
        Collection<Element> coll = new ArrayList<Element>();
        Element metaElt = new Element(MetadataConstants.METADATASECTION_LOCALNAME, MetadataConstants.METADATAEXCHANGE_PREFIX, MetadataConstants.METADATAEXCHANGE_NAMESPACE);
        metaElt.setAttribute(MetadataConstants.DIALECT_ATTNAME, MetadataConstants.RELATIONSHIP_DIALECT);
        Element tmpElt = new Element(MetadataConstants.RELATIONSHIP_LOCALNAME, MetadataConstants.DEVPROF_PREFIX, MetadataConstants.DEVPROF_NAMESPACE);
        tmpElt.setAttribute(MetadataConstants.TYPE_ATTNAME, MetadataConstants.DEVPROFHOST_NAMESPACE);
        Element hostElt = null;
        if (hostDevice != null) {
            hostElt = hostDevice.buildHostElement(false);
            if (hostElt != null) tmpElt.addContent(hostElt);
        }
        hostElt = buildHostElement(true);
        if (hostElt != null) tmpElt.addContent(hostElt);
        metaElt.addContent(tmpElt);
        coll.add(metaElt);
        buildWSDLMetadataSectionElement(coll);
        return coll;
    }

    protected void buildWSDLMetadataSectionElement(Collection<Element> coll) {
        if (serviceClass != null) {
            List<WSDLInfo> webServices = serviceClass.getWebServices();
            for (Iterator<WSDLInfo> it = webServices.iterator(); it.hasNext(); ) {
                WSDLInfo wsdlInfo = it.next();
                if (!wsdlInfo.getLocations().isEmpty()) {
                    for (int i = 0; i < wsdlInfo.getLocations().size(); i++) {
                        String strLocation = wsdlInfo.getLocations().get(i);
                        if (strLocation.length() == 0) continue;
                        Element metaElt = new Element(MetadataConstants.METADATASECTION_LOCALNAME, MetadataConstants.METADATAEXCHANGE_PREFIX, MetadataConstants.METADATAEXCHANGE_NAMESPACE);
                        metaElt.setAttribute(MetadataConstants.DIALECT_ATTNAME, MetadataConstants.WSDL_DIALECT);
                        Element tmpElt = null;
                        tmpElt = new Element(MetadataConstants.LOCATION_LOCALNAME, MetadataConstants.METADATAEXCHANGE_PREFIX, MetadataConstants.METADATAEXCHANGE_NAMESPACE);
                        tmpElt.addContent(strLocation);
                        metaElt.addContent(tmpElt);
                        coll.add(metaElt);
                    }
                }
            }
        }
    }

    protected Element buildHostElement(boolean isHosted) {
        Element retElt = new Element(isHosted ? MetadataConstants.HOSTED_LOCALNAME : MetadataConstants.HOST_LOCALNAME, MetadataConstants.DEVPROF_PREFIX, MetadataConstants.DEVPROF_NAMESPACE);
        Collection<ServicePort> coll = physicalBindings.values();
        for (Iterator<ServicePort> it = coll.iterator(); it.hasNext(); ) {
            ServicePort endpoint = it.next();
            Element eprElt = new Element(WSAConstants.ENDPOINTREFERENCE_LOCALNAME, WSAConstants.WSA_PREFIX, WSAConstants.WSA_NAMESPACE_200408);
            PartBuilder.buildSimpleEPRContent(eprElt, endpoint.getFullAddress());
            List<Policy> policies = endpoint.getPolicies();
            if (policies != null && !policies.isEmpty()) {
                PolicyWriter writer = PolicyFactory.getPolicyWriter(PolicyFactory.DOM_POLICY_WRITER);
                for (int i = 0; i < policies.size(); i++) {
                    Policy policy = policies.get(i);
                    Element policyElt = writer.writePolicy(policy);
                    eprElt.addContent(policyElt);
                }
            }
            retElt.addContent(eprElt);
        }
        Element tmpElt = null;
        if (!supportedTypes.isEmpty()) {
            StringBuffer buf = new StringBuffer();
            tmpElt = new Element(MetadataConstants.TYPES_LOCALNAME, MetadataConstants.DEVPROF_PREFIX, MetadataConstants.DEVPROF_NAMESPACE);
            PartBuilder.addSupportedTypes(buf, supportedTypes, tmpElt);
            tmpElt.addContent(buf.toString());
            retElt.addContent(tmpElt);
        }
        tmpElt = new Element(MetadataConstants.SERVICEID_LOCALNAME, MetadataConstants.DEVPROF_PREFIX, MetadataConstants.DEVPROF_NAMESPACE);
        tmpElt.addContent(id);
        retElt.addContent(tmpElt);
        return retElt;
    }

    /**
   * Accessor.
   *
   * @return the service fault serializer.
   */
    public MessageSerializer getFaultSerializer() {
        return faultSerializer;
    }

    /**
   * Sets the service fault serializer.
   *
   * @param faultSerializer
   *          the new fault serializer.
   */
    public void setFaultSerializer(MessageSerializer faultSerializer) {
        this.faultSerializer = faultSerializer;
    }

    /**
   * Returns the qualified name of the service descriptor.
   *
   * @return the qualified name.
   */
    public QName getName() {
        return name;
    }

    /**
   * Sets the qualified name of the service descriptor.
   *
   * @param name
   *          the new qualified name.
   */
    public void setName(QName name) {
        this.name = name;
    }

    /**
   * Returns the name of this endpoint. This method simply returns the local
   * part of the qualified name of the name of the service.
   *
   * @return the service name.
   */
    public String getSimpleName() {
        return getName().getLocalPart();
    }

    /**
   * Returns the namespace of this endpoint. This method simply returns the
   * namespace part of the qualified name of the name of the service.
   *
   * @return the service namespace.
   */
    public String getTargetNamespace() {
        return getName().getNamespaceURI();
    }

    public List<EndpointReference> getSubcribers(String action) {
        synchronized (eventSubscriptions) {
            if (!eventSource || eventSubscriptions.isEmpty()) return null;
            List<EndpointReference> retList = new ArrayList<EndpointReference>();
            Iterator<String> it = eventSubscriptions.keySet().iterator();
            while (it.hasNext()) {
                String uuid = it.next();
                EventSubscription eventSubscription = eventSubscriptions.get(uuid);
                if (eventSubscription.getExpiration() < System.currentTimeMillis()) {
                    System.out.println("Removing expired subscription with UUID " + uuid);
                    eventSubscriptions.remove(uuid);
                    continue;
                }
                if (eventSubscription.hasAction(action)) {
                    retList.add(eventSubscription.getNotifyTo());
                }
            }
            if (retList.isEmpty()) return null;
            return retList;
        }
    }

    /**
   * Accessor.
   *
   * @param uuid
   *          the uuid of the event subscription
   * @return the subscription if it exists.
   */
    EventSubscription getEventSubscription(String uuid) {
        synchronized (eventSubscriptions) {
            return eventSubscriptions.get(uuid);
        }
    }

    public Map<String, EventSubscription> getEventSubscriptions() {
        if (eventSubscriptions != null) return Collections.unmodifiableMap(eventSubscriptions);
        return null;
    }

    /**
   * Method adding an event subscription.
   *
   * @param evtSubscr
   *          the event subscription to be added.
   */
    boolean addEventSubscription(EventSubscription evtSubscr) {
        synchronized (eventSubscriptions) {
            if (eventSubscriptions != null) {
                eventSubscriptions.put(evtSubscr.getUuid(), evtSubscr);
                return true;
            }
            return false;
        }
    }

    public void removeEventSubscription(String uuid) {
        synchronized (eventSubscriptions) {
            eventSubscriptions.remove(uuid);
        }
    }

    /**
   * Accessor.
   *
   * @return the collection of all operations supported by the service.
   */
    public Collection<OperationInfo> getOperations() {
        return Collections.unmodifiableCollection(operationMap.values());
    }

    /**
   * Accessor.
   *
   * @param opName
   *          the name of the operation searched.
   * @return the operation info corresponding to the name.
   */
    public OperationInfo getOperation(String opName) {
        return operationMap.get(opName);
    }

    /**
   * Accessor.
   *
   * @param action
   *          the action whose {@link PortTypeInfo} is searched.
   * @return the {@link PortTypeInfo} holding the specified action.
   */
    public PortTypeInfo getPortTypeInfo(String action) {
        return invokationMap.get(action).getPortTypeInfo();
    }

    /**
   * Tells if at least one port type has several bindings.
   *
   * @return true if at least one port type has several bindings.
   */
    public boolean hasServiceUniqueBindings() {
        for (int i = 0; i < portTypeInfos.size(); i++) {
            PortTypeInfo pti = portTypeInfos.get(i);
            if (pti.hasMultipleBindings()) return false;
        }
        return true;
    }

    /**
   * Tells whether the service endpoint sends events.
   *
   * @return a boolean indicating the property.
   */
    public boolean isEventSource() {
        return eventSource;
    }
}
