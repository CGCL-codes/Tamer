package org.ws4d.java.service;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.ws4d.java.communication.DPWSException;
import org.ws4d.java.communication.ServerEngine;
import org.ws4d.java.communication.http.HTTPUser;
import org.ws4d.java.constants.SOAPConstants;
import org.ws4d.java.constants.WSAConstants;
import org.ws4d.java.constants.XOPConstants;
import org.ws4d.java.io.ResponseOutput;
import org.ws4d.java.logging.Log;
import org.ws4d.java.modules.attachment.IAttachmentData;
import org.ws4d.java.util.ArrayDictionary;
import org.ws4d.java.util.MIMEUtil;
import org.ws4d.java.util.Properties;
import org.ws4d.java.util.QuickSorterString;
import org.ws4d.java.xml.QualifiedName;
import org.ws4d.java.xml.QualifiedNameVector;
import org.ws4d.java.xml.XMLAttribute;
import org.ws4d.java.xml.XMLElement;
import org.ws4d.java.xml.XMLElementUtil;
import org.ws4d.java.xml.XMLSerialization;

/**
 * AbstractService provides the base structure for DPWS services and devices. It
 * stores the actions of a service as well as the port type and namespace
 * definitions. This class give every DPWS service the possibility to manage
 * port types, namespace names and actions.
 */
public abstract class AbstractService extends AbstractEndpoint {

    public static final String OBJECT_ID_DPWSSERVICE_PREFIX = "DPWSService:";

    public static final byte SERVICE_TYPE_HOSTING_SERVICE = 0;

    public static final byte SERVICE_TYPE_HOSTED_SERVICE = 1;

    public static final byte SERVICE_TYPE_REMOTE_DEVICE = 2;

    public static final byte SERVICE_TYPE_REMOTE_SERVICE = 3;

    /**
	 * The serviceId of this service.
	 */
    protected String serviceId;

    /**
	 * Maps a QualifiedName including a port type string to an Hashtable mapping
	 * an action name to the real action.
	 */
    private ArrayDictionary portType2ActionMap = new ArrayDictionary();

    /** Store the names of port types which consist of events. */
    protected Vector eventedPortTypes = new Vector();

    /** Type. */
    private byte serviceType;

    /** flag if service is added, that no more changes are allowed. */
    private boolean isReadOnly = false;

    /** User data for HTTP authentication. */
    protected HTTPUser user = null;

    protected String configName = null;

    /**
	 * Constructs an AbstractService with setting namespace, namespace prefix
	 * and porttype via a QualifiedName object. The configuration name is used
	 * to link devices and services to property parameters.
	 * 
	 * @param configName Configuration name to find associated configuration
	 *            parameters for service in properties. Must be unique for
	 *            devices and services in the properties file.
	 */
    public AbstractService(String configName) {
        init(configName);
    }

    /**
	 * Constructs an AbstractService with setting namespace, namespace prefix
	 * and porttype via a QualifiedName object.
	 * 
	 */
    public AbstractService() {
        init(null);
    }

    /**
	 * This method just sets the namespace, namespace prefix and porttype. The
	 * QualifedName object will be stored in a new Hashtable. The configuration
	 * name is used to link devices and services to property parameters.
	 * 
	 * @param configName Configuration name to find associated configuration
	 *            parameters for service in properties. Must be unique for
	 *            devices and services in the properties file.
	 */
    private void init(String configName) {
        if (configName == null) {
            String name = this.getClass().toString() + "§" + System.identityHashCode(this);
            setConfigName(name);
        } else {
            setConfigName(configName);
        }
        running = false;
    }

    public void setServiceType(byte type) {
        serviceType = type;
    }

    public int getServiceType() {
        return serviceType;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String name) {
        configName = name;
    }

    /**
	 * This method parses the SOAP envelope for wsa:Action parts, invokes the
	 * service action and creates the response SOAP envelope. At first it reads
	 * the SOAP header and try to extract the wsa:Action part. If wsa:Action
	 * part is not found, has the length 0 or is unknown, a SOAP fault is
	 * created and send back as response. The action is deserialized and invoked
	 * on the associated service. If the action is not one-way, the output is
	 * serialized and send as response.
	 * 
	 * @param envelope SOAP envelope.
	 * @param tmpAttachmentList attachments extracted from MIME.
	 * @param respOut output stream.
	 */
    public final void invokeActionInternal(XMLElement envelope, Hashtable tmpAttachmentList, ResponseOutput respOut) {
        XMLElement[] wsaHeaderFields = new XMLElement[3];
        wsaHeaderFields[0] = XMLElementUtil.getElementViaPath(envelope, "Header/Action", WSAConstants.WSA_NAMESPACE_NAME);
        String wsaAction = null;
        AbstractAction tmpAction = null;
        try {
            if (wsaHeaderFields[0] == null) {
                DPWSException exception = WSAConstants.WSA_EXCEPTION_MESSAGE_INFORMATION_HEADER_REQUIRED.copy();
                exception.setDetail(WSAConstants.WSA_NAMESPACE_NAME + "/" + WSAConstants.WSA_ELEM_ACTION);
                respOut.sendFault(exception);
                return;
            }
            wsaAction = XMLElementUtil.getAllInnerText(wsaHeaderFields[0], true);
            if (wsaAction.length() == 0) {
                DPWSException exception = WSAConstants.WSA_EXCEPTION_MESSAGE_INFORMATION_HEADER_REQUIRED.copy();
                exception.setDetail(WSAConstants.WSA_NAMESPACE_NAME + "/" + WSAConstants.WSA_ELEM_ACTION);
                respOut.sendFault(exception);
                return;
            }
            AbstractAction svcAction = null;
            Vector svcActions = new Vector();
            int cPa = 0;
            for (Enumeration enQns = portType2ActionMap.keys(); enQns.hasMoreElements(); ) {
                Hashtable actionMap = (Hashtable) portType2ActionMap.get((QualifiedName) enQns.nextElement());
                Enumeration signatures = actionMap.elements();
                while (signatures.hasMoreElements()) {
                    Hashtable signatureMap = (Hashtable) signatures.nextElement();
                    Enumeration actions = signatureMap.elements();
                    while (actions.hasMoreElements()) {
                        AbstractAction action = (AbstractAction) actions.nextElement();
                        if (wsaAction.equals(action.getInputActionName())) {
                            svcActions.addElement(action);
                            if (action.getType() == AbstractAction.ACTION_TYPE_IN_ONEWAY || action.getType() == AbstractAction.ACTION_TYPE_IN_OUT_REQUEST_RESPONSE) {
                                cPa += 2;
                            } else if (action.getType() == AbstractAction.ACTION_TYPE_OUT_NOTIFICATION || action.getType() == AbstractAction.ACTION_TYPE_OUT_IN_SOLICIT_RESPONSE) {
                                cPa += 1;
                            }
                        }
                    }
                }
            }
            XMLElement body = XMLElementUtil.getInnerElement(envelope, SOAPConstants.SOAP_ELEM_BODY);
            if (svcActions.size() > 0 && svcActions.size() == 1) {
                svcAction = (AbstractAction) svcActions.firstElement();
            } else if (svcActions.size() > 0 && svcActions.size() > 1) {
                for (int k = 0; k < svcActions.size(); k++) {
                    AbstractAction act = (AbstractAction) svcActions.elementAt(k);
                    try {
                        boolean match = XMLSerialization.isActionRequestSerializable(act, body);
                        if (match) {
                            svcAction = act;
                        }
                    } catch (DPWSException e) {
                        Log.printStackTrace(e);
                    }
                }
            }
            if (svcAction == null && svcActions.size() == 0) {
                DPWSException exception = WSAConstants.WSA_EXCEPTION_UNSUPPORTED_ACTION.copy();
                exception.addReason("de-DE", "Die " + wsaAction + " konnte beim Empfänger nicht verarbeitet werden");
                exception.addReason("en-US", "The " + wsaAction + " cannot be processed at the receiver");
                exception.setDetail(wsaAction);
                respOut.sendFault(exception);
                return;
            }
            tmpAction = svcAction;
            if (tmpAction.getType() == AbstractAction.ACTION_TYPE_IN_ONEWAY && Properties.getInstance().getGlobalBooleanProperty(Properties.PROP_ASYNC_ONEWAY_OPS)) respOut.sendResponse(null);
            try {
                XMLSerialization.deSerializeActionRequest(tmpAction, body);
                if (tmpAction.hasBinaryInputParameter()) {
                    Enumeration enu = tmpAction.getInputParameters().elements();
                    for (; enu.hasMoreElements(); ) {
                        Parameter param = (Parameter) enu.nextElement();
                        XMLElement paramxml = XMLElementUtil.getInnerElement(body, param.getElementName(), null);
                        String namespace = XMLElementUtil.getNamespaceName(paramxml);
                        if (param.hasBinaryType() && !param.isComplexType()) {
                            XMLElement paraminclude = XMLElementUtil.getInnerElement(paramxml, XOPConstants.XOP_ELEMENT_INCLUDE, XOPConstants.XOP_NAMESPACE_NAME);
                            XMLAttribute paramhref = XMLElementUtil.getElementAttribute(paraminclude, "href");
                            String contentIDReference = MIMEUtil.createReferenceFromCID(paramhref.getValue());
                            IAttachmentData data = (IAttachmentData) tmpAttachmentList.get(contentIDReference);
                            if (data != null) {
                                param.setAttachmentData(data);
                                param.setValue(contentIDReference);
                                Log.debug("Attachment [" + contentIDReference + "] assigned to Parameter [" + param.getName() + "]");
                            }
                        } else if (param.hasBinaryType() && param.isComplexType()) {
                            Vector keys = param.getBinaryTypeKeys();
                            for (int i = 0; i < keys.size(); i++) {
                                String key = (String) keys.elementAt(i);
                                XMLElement innerParam = XMLElementUtil.getElementViaPath(paramxml, key, namespace);
                                XMLElement paraminclude = XMLElementUtil.getInnerElement(innerParam, XOPConstants.XOP_ELEMENT_INCLUDE, XOPConstants.XOP_NAMESPACE_NAME);
                                XMLAttribute paramhref = XMLElementUtil.getElementAttribute(paraminclude, "href");
                                String contentIDReference = MIMEUtil.createReferenceFromCID(paramhref.getValue());
                                IAttachmentData data = (IAttachmentData) tmpAttachmentList.get(contentIDReference);
                                if (data != null) {
                                    PVI[] path = PVI.createPath(key);
                                    param.setAttachmentData(path, data);
                                    param.setValue(path, contentIDReference);
                                    Log.debug("Attachment [" + contentIDReference + "] assigned to Parameter [" + param.getName() + "]");
                                }
                            }
                        }
                    }
                }
                tmpAction.invoke();
            } catch (DPWSException e) {
                respOut.sendFault(e);
                return;
            }
            if (!(tmpAction.getType() == AbstractAction.ACTION_TYPE_IN_ONEWAY)) {
                XMLElementUtil.setInnerText(wsaHeaderFields[0], tmpAction.getOutputActionName());
                wsaHeaderFields[1] = XMLElementUtil.getElementViaPath(envelope, "Header/MessageID", WSAConstants.WSA_NAMESPACE_NAME);
                if (wsaHeaderFields[1] != null) XMLElementUtil.setElementName(wsaHeaderFields[1], "RelatesTo");
                wsaHeaderFields[2] = new XMLElement(WSAConstants.WSA_ELEM_TO, null, WSAConstants.WSA_NAMESPACE_NAME, WSAConstants.WSA_ROLE_ANONYMOUS);
                XMLElement newHeader = XMLElementUtil.getInnerElement(envelope, SOAPConstants.SOAP_ELEM_HEADER);
                XMLElementUtil.clearInnerElements(newHeader);
                XMLElementUtil.addInnerElement(newHeader, wsaHeaderFields[0]);
                XMLElementUtil.addInnerElement(newHeader, wsaHeaderFields[1]);
                XMLElementUtil.addInnerElement(newHeader, wsaHeaderFields[2]);
                XMLElementUtil.clearInnerElements(body);
                XMLSerialization.serializeActionResponse(tmpAction, body);
                if (tmpAction.hasBinaryOutputParameter()) respOut.sendResponse(envelope, tmpAction); else respOut.sendResponse(envelope);
            } else {
                if (!Properties.getInstance().getGlobalBooleanProperty(Properties.PROP_ASYNC_ONEWAY_OPS)) respOut.sendResponse(null);
            }
            tmpAction.cleanUp();
        } catch (IOException e) {
            Log.printStackTrace(e);
        }
    }

    /**
	 * Returns <code>true</code> if the actions of this porttype are evented.
	 * 
	 * @param portType Qualified name holds port type to check.
	 * @return <code>true</code> if the actions of this port type are events.
	 */
    public boolean isPortTypeEvented(QualifiedName portType) {
        return eventedPortTypes.contains(portType);
    }

    /**
	 * Adds an action to the service with given port type.
	 * 
	 * @param action Action to add.
	 * @param portType Port type.
	 */
    public void addAction(AbstractAction action, QualifiedName porttype) {
        if (isReadOnly) {
            Log.error("Can not add action. Service was already added, no changes allowed!");
            System.exit(0);
        }
        if (action.getService() != null) {
            Log.error("Can not add action. Actions already belongs to another service.");
            System.exit(0);
        }
        if (action == null || porttype == null) return;
        if (portType2ActionMap.size() > 0) {
            Enumeration enu = portType2ActionMap.keys();
            QualifiedName first = null;
            while (enu.hasMoreElements()) {
                first = (QualifiedName) enu.nextElement();
                break;
            }
            if (first != null && !first.getNamespace().equalsIgnoreCase(porttype.getNamespace())) {
                Log.error("Can not add action. The action does not match the namespace of this service.");
                System.exit(0);
            }
        }
        Hashtable actionMap = (Hashtable) portType2ActionMap.get(porttype);
        if (actionMap == null) {
            actionMap = new Hashtable();
            portType2ActionMap.put(porttype, actionMap);
        }
        String actName = action.getFullName();
        Hashtable signatureMap = (Hashtable) actionMap.get(actName);
        if (signatureMap == null) {
            signatureMap = new Hashtable();
        }
        String actSig = action.getSignature();
        signatureMap.put(actSig, action);
        actionMap.put(actName, signatureMap);
        action.setService(this);
        try {
            action.checkActionParameters();
        } catch (ActionTypeParameterMismatchException e) {
            Log.printStackTrace(e);
            System.exit(0);
        }
        action.setReadOnly();
        if (action.isEvented() && !isPortTypeEvented(porttype)) {
            eventedPortTypes.addElement(porttype);
        }
    }

    /**
	 * Adds an action to the service with given the port type found in the
	 * service. If the action is marked as evented action before added here, the
	 * port type will be changed.
	 * 
	 * @param action Action to add.
	 */
    public void addAction(AbstractAction action) {
        addAction(action, action.getPortType());
    }

    /**
	 * Remove an action by name.
	 * 
	 * @param name Name of the action.
	 * @param portType Qualified name holds port type to check.
	 *  
	 * @return The removed action.
	 */
    public Vector removeAction(String name, QualifiedName portType) {
        if (name == null || portType == null) return null;
        Hashtable actionMap = (Hashtable) portType2ActionMap.get(portType);
        if (actionMap == null) return null;
        Hashtable signatures = (Hashtable) actionMap.get(name);
        if (signatures.size() == 0) {
            actionMap.remove(name);
        }
        if (actionMap.size() == 0) {
            portType2ActionMap.remove(portType);
        }
        Vector v = new Vector();
        Enumeration actions = signatures.elements();
        while (actions.hasMoreElements()) {
            AbstractAction action = (AbstractAction) actions.nextElement();
            action.setService(null);
            v.addElement(v);
        }
        return v;
    }

    /**
	 * Get QualifiedName object including port type and namespace by action
	 * name.
	 * 
	 * @param actionName Name of the action whose QualifiedName object should be
	 *            returned.
	 * @return QualifiedName object.
	 */
    public QualifiedName getPortType(String actionName) {
        for (Enumeration enQns = portType2ActionMap.keys(); enQns.hasMoreElements(); ) {
            QualifiedName qn = (QualifiedName) enQns.nextElement();
            Hashtable actionMap = (Hashtable) portType2ActionMap.get(qn);
            if (actionMap.containsKey(actionName)) return qn;
        }
        return null;
    }

    /**
	 * Get port types in QualifiedNameVector 
	 * 
	 * @return QualifiedNameVector holding QualifiedName objects with port types.
	 */
    public QualifiedNameVector getPortTypes() {
        QualifiedNameVector vQNames = new QualifiedNameVector();
        for (Enumeration enQns = portType2ActionMap.keys(); enQns.hasMoreElements(); ) {
            vQNames.addElement((QualifiedName) enQns.nextElement());
        }
        return vQNames;
    }

    /**
	 * <b>Use the result only for reading!</b> To add actions, use the
	 * addAction() method!
	 * 
	 * @param qnPortType Port type to search for.
	 * @return Vector containing actions matching the given port type.
	 */
    public Vector getActions(QualifiedName qnPortType) {
        if (qnPortType == null) return null;
        Hashtable actionMap = (Hashtable) portType2ActionMap.get(qnPortType);
        Vector v = new Vector();
        if (actionMap == null) {
            return v;
        }
        Enumeration signatures = actionMap.elements();
        while (signatures.hasMoreElements()) {
            Hashtable signatureMap = (Hashtable) signatures.nextElement();
            Enumeration actions = signatureMap.elements();
            while (actions.hasMoreElements()) {
                AbstractAction action = (AbstractAction) actions.nextElement();
                v.addElement(action);
            }
        }
        return v;
    }

    public Vector getActions() {
        Vector actions = new Vector();
        Enumeration enu = portType2ActionMap.keys();
        while (enu.hasMoreElements()) {
            QualifiedName qn = (QualifiedName) enu.nextElement();
            Hashtable acs = (Hashtable) portType2ActionMap.get(qn);
            Enumeration enua = acs.elements();
            while (enua.hasMoreElements()) {
                Hashtable a = (Hashtable) enua.nextElement();
                Enumeration signatures = a.elements();
                while (signatures.hasMoreElements()) {
                    AbstractAction action = (AbstractAction) signatures.nextElement();
                    actions.addElement(action);
                }
            }
        }
        return actions;
    }

    /**
	 * Returns the first action matching the name with given port type.
	 * 
	 * @param name The name of the requested action.
	 * @return <code>null</code> or the action.
	 */
    public AbstractAction getAction(String name, QualifiedName portType) {
        if (name == null || portType == null) return null;
        Hashtable actions = (Hashtable) portType2ActionMap.get(portType);
        if (actions == null) return null;
        String key = new String(portType + "/" + name);
        Hashtable signatrues = (Hashtable) actions.get(key);
        if (signatrues == null) return null;
        Enumeration enu = signatrues.elements();
        while (enu.hasMoreElements()) {
            AbstractAction a = (AbstractAction) enu.nextElement();
            if (a != null) {
                return a;
            }
        }
        return null;
    }

    /**
	 * Returns the first action matching the name with given port type.
	 * 
	 * @param name The name of the requested action.
	 * @return <code>null</code> or the action.
	 */
    public Vector getActions(String name, QualifiedName portType) {
        Vector v = new Vector();
        if (name == null || portType == null) return v;
        Hashtable actions = (Hashtable) portType2ActionMap.get(portType);
        if (actions == null) return v;
        Hashtable signatrues = (Hashtable) actions.get(name);
        Enumeration enu = signatrues.elements();
        while (enu.hasMoreElements()) {
            AbstractAction a = (AbstractAction) enu.nextElement();
            if (a != null) {
                v.addElement(a);
            }
        }
        return v;
    }

    /**
	 * Add single port type.
	 * 
	 * @param portType The port type without namespace.
	 */
    public void addPortType(QualifiedName portType) {
        if (!portType2ActionMap.containsKey(portType)) portType2ActionMap.put(portType, new Hashtable());
    }

    /**
	 * Returns the ServiceId of this service.
	 * 
	 * @return The ServiceId of this service.
	 */
    public String getServiceId() {
        if (serviceId == null) {
            serviceId = constructServiceId();
        }
        return serviceId;
    }

    /**
	 * Construct the ServiceId of this service.
	 * 
	 * @return The namespace of the service and the endpoint path of the current
	 *         service.
	 */
    public String constructServiceId() {
        try {
            return getEndpointLocation().toString();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
	 * Set the ServiceId of this service.
	 * 
	 * @param serviceId
	 */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
	 * Compares this abstract service to the specified object. The result is
	 * true if and only if the argument is not null and is an abstract service
	 * object that represents the same properties as this abstract service.
	 * 
	 * @param obj the Object to compare this abstract service vector against.
	 * @return true if the abstract services are equal; false otherwise.
	 */
    public boolean equals(Object obj) {
        boolean isEuqal = false;
        if (obj != null && obj instanceof QualifiedNameVector) {
            isEuqal = equals((QualifiedNameVector) obj);
        }
        return isEuqal;
    }

    /**
	 * This method compares two abstract services if they are equal or not. Both
	 * abstract services are equal if the unique names build and the containing
	 * qualified name vectors equals each other.
	 * 
	 * @param service2 The abstract service to compare this object against.
	 * @return <code>true</code> if abstract services are equal, otherwise
	 *         <code>false</code>.
	 */
    public boolean equals(AbstractService service2) {
        boolean isEqual = false;
        AbstractService service1 = this;
        if (service1 != null && service2 != null && service1.buildUniqueName().equals(service2.buildUniqueName()) && service1.getPortTypes().equals(service2.getPortTypes())) {
            isEqual = true;
        }
        return isEqual;
    }

    /**
	 * This method creates a string for this service, which is unique along all
	 * existing service.
	 * 
	 * @return A string containing the unique string or null if this service has
	 *         missing information.
	 */
    public String buildUniqueName() {
        String retString = null;
        String endpoint = null;
        endpoint = getEndpointLocation().toString();
        if (endpoint.startsWith(ServerEngine.DEVICE_NO_ADDRESS)) {
            QualifiedNameVector qulfdNamesVec = getPortTypes();
            Hashtable qulfdNamesSorted = new Hashtable();
            String[] qulfdNameArr = new String[qulfdNamesVec.size()];
            String namespace = null, porttype = null;
            for (int i = 0; i < qulfdNamesVec.size(); i++) {
                qulfdNameArr[i] = qulfdNamesVec.elementAt(i).getLocalPart();
                qulfdNamesSorted.put(qulfdNameArr[i], qulfdNamesVec.elementAt(i).getNamespace());
            }
            QuickSorterString.sort(qulfdNameArr);
            if (qulfdNameArr.length > 0) {
                porttype = qulfdNameArr[0];
                namespace = (String) qulfdNamesSorted.get(porttype);
            }
            if (namespace != null && porttype != null) {
                retString = OBJECT_ID_DPWSSERVICE_PREFIX + namespace + porttype;
            }
        } else if (endpoint != null) {
            retString = OBJECT_ID_DPWSSERVICE_PREFIX + endpoint;
        }
        return retString;
    }

    public void setHTTPUser(HTTPUser user) {
        this.user = user;
    }

    public HTTPUser getHTTPUser() {
        return user;
    }

    /**
	 * Set service to read only. No more action addition allowed
	 */
    public void setReadOnly() {
        isReadOnly = true;
    }

    public void setEndpointPath(String endpointPath) {
        if (isReadOnly) {
            Log.error("Can not change the endpoint path of this endpoint. Endpoint is read only.");
            return;
        }
        super.setEndpointPath(endpointPath);
    }

    /**
	 * Checks if abstract service is a remote service or device.
	 * 
	 * @return <code>true</code> if abstract service is a remote service/device,
	 *         <code>false</code> if abstract service is a local service/device.
	 */
    public boolean isRemote() {
        return ((serviceType == SERVICE_TYPE_REMOTE_SERVICE) || (serviceType == SERVICE_TYPE_REMOTE_DEVICE));
    }
}
