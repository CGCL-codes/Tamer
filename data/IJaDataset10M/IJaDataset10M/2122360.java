package org.dbe.composer.wfengine.bpel.webserver.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import javax.wsdl.OperationType;
import javax.xml.namespace.QName;
import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.description.ServiceDesc;
import org.apache.axis.handlers.soap.SOAPService;
import org.apache.axis.message.SOAPBody;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.message.SOAPFault;
import org.apache.axis.utils.XMLUtils;
import org.apache.log4j.Logger;
import org.dbe.composer.wfengine.SdlException;
import org.dbe.composer.wfengine.bpel.ISdlFault;
import org.dbe.composer.wfengine.bpel.IServiceEndpointReference;
import org.dbe.composer.wfengine.bpel.SDLDefHelper;
import org.dbe.composer.wfengine.bpel.SdlBusinessProcessException;
import org.dbe.composer.wfengine.bpel.WSDLDefHelper;
import org.dbe.composer.wfengine.bpel.def.SdlPartnerLinkDef;
import org.dbe.composer.wfengine.bpel.def.SdlProcessDef;
import org.dbe.composer.wfengine.bpel.impl.ServiceEndpointReference;
import org.dbe.composer.wfengine.bpel.message.ISdlMessageData;
import org.dbe.composer.wfengine.bpel.server.IServiceProcessDeployment;
import org.dbe.composer.wfengine.bpel.server.addressing.SdlEndpointReferenceSourceType;
import org.dbe.composer.wfengine.bpel.server.engine.SdlEngineFactory;
import org.dbe.composer.wfengine.bpel.server.engine.ServiceReplyResponseReceiver;
import org.dbe.composer.wfengine.bpel.serviceid.IServiceId;
import org.dbe.composer.wfengine.bpel.webserver.SdlHandler;
import org.dbe.composer.wfengine.bpel.webserver.SdlServiceDesc;
import org.dbe.composer.wfengine.sdl.def.BPELExtendedSDLDef;
import org.dbe.composer.wfengine.util.SdlUtil;
import org.dbe.composer.wfengine.wsdl.def.BPELExtendedWSDLDef;
import org.dbe.composer.wfengine.wsdl.def.IWsdlPartnerLinkType;
import org.dbe.composer.wfengine.wsdl.def.IWsdlRole;
import org.dbe.composer.wfengine.wsio.receive.SdlMessageContext;
import org.dbe.studio.editor.sdl.Interface;
import org.dbe.studio.editor.sdl.Operation;
import org.dbe.studio.editor.sdl.SimpleMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The base BPEL handler for web services under an Axis framework.
 */
public abstract class BpelHandler extends SdlHandler {

    /** for deployment logging purposes */
    protected static final Logger logger = Logger.getLogger(BpelHandler.class.getName());

    /** Key for property which holds the process QName. */
    private static final String PROCESS_ENTRY = "org.dbe.composer.wfengine.bpel.webserver.ProcessEntry";

    /** Key for property which specifies the SdlPartnerLinkDef which this service operates against*/
    public static final String PARTNER_LINK_ENTRY = "org.dbe.composer.wfengine.bpel.webserver.PartnerLinkEntry";

    /** Identifier tag used for process name element */
    protected static final String PROCESS_NAME_TAG = "processName";

    /** Identifier tag used for process namespace element */
    protected static final String PROCESS_NAMESPACE_TAG = "processNamespace";

    /** Identifier tag used for partner link element */
    protected static final String PARTNER_LINK_TAG = "partnerLink";

    /**
     * Initialize the service description by loading parameters
     * in order to obtain the deployment plan for it.
     * @param aService
     * @param aContext
     */
    public void initServiceDesc(SOAPService aService, MessageContext aContext) throws AxisFault {
        logger.info("initServiceDesc(SOAPService, MessageContext)");
        if (aContext == null) {
            return;
        }
        String transportURL = (String) aContext.getProperty(MessageContext.TRANS_URL);
        if (transportURL == null) {
            return;
        }
        logger.info("transportURL=" + transportURL);
        try {
            SdlServiceDesc serviceDesc = createServiceDescriptor();
            serviceDesc.setName(aService.getName());
            aService.setServiceDescription(serviceDesc);
            String namespace = (String) aService.getOption(PROCESS_NAMESPACE_TAG);
            if (namespace == null) {
                logger.error("Process namespace not specified for service.");
                throw new AxisFault("Process namespace not specified for service.");
            }
            String processName = (String) aService.getOption(PROCESS_NAME_TAG);
            if (processName == null) {
                logger.error("Process name not specified for service.");
                throw new AxisFault("Process name not specified for service.");
            }
            String partnerLink = (String) aService.getOption(PARTNER_LINK_TAG);
            if (partnerLink == null) {
                logger.error("Partner Link not specified for service.");
                throw new AxisFault("Partner Link not specified for service.");
            }
            QName process = new QName(namespace, processName);
            setProcessName(serviceDesc, process);
            IServiceProcessDeployment deployment = getDeploymentPlan(serviceDesc);
            if (deployment == null) {
                logger.error("Unable to locate business process deployment descriptor file: " + process);
                throw new AxisFault("Unable to locate business process deployment descriptor file: " + process);
            }
            serviceDesc.setDefaultNamespace(namespace);
            SdlProcessDef processDef = deployment.getSdlProcessDef();
            SdlPartnerLinkDef plinkDef = processDef.getPartnerLink(partnerLink);
            QName plinkType = plinkDef.getPartnerLinkTypeName();
            logger.info("looking up partnerLink=" + plinkType);
            BPELExtendedWSDLDef cachedDef = WSDLDefHelper.getWSDLDefinitionForPLT(deployment, plinkType);
            BPELExtendedWSDLDef wsdlDef = SdlEngineFactory.getServiceCatalog().makeWsdlCopy(cachedDef);
            if (wsdlDef == null) {
                logger.error("Unable to load sdl or wsdl definition for partner link type=" + plinkType);
                throw new AxisFault("Unable to load sdl or wsdl definition for partner link type.");
            }
            setExtendedWSDLDef(serviceDesc, wsdlDef);
            String roleName = "";
            IWsdlPartnerLinkType plType = null;
            SdlPartnerLinkDef plDef = deployment.getSdlProcessDef().getPartnerLink(partnerLink);
            if (plDef != null) {
                roleName = plDef.getMyRole();
                serviceDesc.setProperty(PARTNER_LINK_ENTRY, plDef);
                plType = wsdlDef.getPartnerLinkType(plDef.getPartnerLinkTypeName().getLocalPart());
            }
            if (plType == null) {
                logger.error("Unable to locate partner link type in the associated process.");
                throw new AxisFault("Unable to locate partner link type in the associated process.");
            }
            ArrayList allowedMethods = new ArrayList();
            IWsdlRole role = plType.findRole(roleName);
            if (role == null || role.getPortType() == null) {
                logger.error("Unable to locate role for partner link in the associated process.");
                throw new AxisFault("Unable to locate role for partner link in the associated process.");
            }
            QName portTypeName = role.getPortType().getQName();
            setPortTypeQName(serviceDesc, portTypeName);
            int i = 0;
            logger.info("looking up in WSDL for operations in portType=" + portTypeName);
            for (Iterator iter = wsdlDef.getOperations(role.getPortType().getQName()); iter.hasNext(); ) {
                javax.wsdl.Operation operation = (javax.wsdl.Operation) iter.next();
                addWsdlOperationToService(serviceDesc, operation);
                allowedMethods.add(operation.getName());
                i++;
            }
            if (i == 0) {
                logger.info("looking up in SDL for operations in portType=" + portTypeName);
                BPELExtendedSDLDef sdlDef = SDLDefHelper.getSDLDefinitionForInterface(deployment, portTypeName);
                for (Iterator iter = wsdlDef.getOperations(role.getPortType().getQName()); iter.hasNext(); ) {
                    org.dbe.studio.editor.sdl.Operation operation = (org.dbe.studio.editor.sdl.Operation) iter.next();
                    addSdlOperationToService(serviceDesc, operation, sdlDef);
                    allowedMethods.add(operation.getElName());
                    i++;
                }
                serviceDesc.setAllowedMethods(allowedMethods);
                trimServiceDefinition(serviceDesc, aContext, true);
            } else {
                serviceDesc.setAllowedMethods(allowedMethods);
                trimServiceDefinition(serviceDesc, aContext, false);
            }
            serviceDesc.setInitialized(true);
        } catch (Exception ex) {
            logger.error("Error initializing ServiceDesc.");
            SdlException.logError(ex, "Error initializing ServiceDesc.");
            throw AxisFault.makeFault(ex);
        }
    }

    /**
     * Setter for the process qname.
     * @param aService
     * @param aProcessQName
     */
    protected void setProcessName(ServiceDesc aService, QName aProcessQName) {
        aService.setProperty(PROCESS_ENTRY, aProcessQName);
    }

    /**
     * Gets the qname for the process that this service targets.
     * @param aService
     */
    protected QName getProcessName(ServiceDesc aService) {
        return (QName) aService.getProperty(PROCESS_ENTRY);
    }

    /**
     * Adds an operation to the given service.
     * @param aServiceDesc the service description we will be adding to
     * @param aOperation the operation info to be added
     */
    protected void addSdlOperationToService(ServiceDesc aServiceDesc, org.dbe.studio.editor.sdl.Operation aOperation, BPELExtendedSDLDef sdlDef) throws Exception {
        OperationDesc operation = new OperationDesc();
        operation.setParent(aServiceDesc);
        operation.setUse(getUse());
        operation.setName(aOperation.getElName());
        operation.setMethod(BpelHandler.class.getMethod("invoke", new Class[] { MessageContext.class }));
        if (aOperation.getRefInputMessage() != null) {
            addParamsToSdlOperation(operation, aOperation.getRefInputMessage(), true, sdlDef);
        }
        if (aOperation.getRefOutputMessage() != null) {
            addParamsToSdlOperation(operation, aOperation.getRefOutputMessage(), false, sdlDef);
        }
        aServiceDesc.addOperationDesc(operation);
    }

    /**
     * Adds an operation to the given service.
     * @param aServiceDesc the service description we will be adding to
     * @param aOperation the operation info to be added
     */
    protected void addWsdlOperationToService(ServiceDesc aServiceDesc, javax.wsdl.Operation aOperation) throws Exception {
        OperationDesc operation = new OperationDesc();
        operation.setParent(aServiceDesc);
        operation.setUse(getUse());
        operation.setName(aOperation.getName());
        operation.setMethod(BpelHandler.class.getMethod("invoke", new Class[] { MessageContext.class }));
        if (aOperation.getInput() != null) {
            addParamsToWsdlOperation(operation, aOperation.getInput().getMessage(), true);
        }
        if (aOperation.getOutput() != null) {
            addParamsToWsdlOperation(operation, aOperation.getOutput().getMessage(), false);
        }
        aServiceDesc.addOperationDesc(operation);
    }

    /** Returns the appropriate Use instance based on the style of the service */
    protected abstract Use getUse();

    /**
     * Adds all parameters for the given message to the given operation.
     * @param aOperation The operation we will be adding parameters for
     * @param aMsg the message we will get parameter information from
     * @param aIsInput flag indicating if this is an input message (true)
     */
    protected void addParamsToSdlOperation(OperationDesc aOperation, SimpleMessage aMsg, boolean aIsInput, BPELExtendedSDLDef sdlDef) {
        for (Iterator iter = aMsg.getCmpPart().iterator(); iter.hasNext(); ) {
            org.dbe.studio.editor.sdl.Part part = (org.dbe.studio.editor.sdl.Part) iter.next();
            QName typeName = sdlDef.getTypeName(part.getElName());
            ParameterDesc param = new ParameterDesc();
            if (aIsInput) {
                param.setMode(ParameterDesc.IN);
            } else {
                param.setMode(ParameterDesc.OUT);
            }
            param.setName(part.getElName());
            param.setTypeQName(typeName);
            aOperation.addParameter(param);
        }
    }

    /**
     * Adds all parameters for the given message to the given operation.
     * @param aOperation The operation we will be adding parameters for
     * @param aMsg the message we will get parameter information from
     * @param aIsInput flag indicating if this is an input message (true)
     */
    protected void addParamsToWsdlOperation(OperationDesc aOperation, javax.wsdl.Message aMsg, boolean aIsInput) {
        for (Iterator iter = aMsg.getOrderedParts(null).iterator(); iter.hasNext(); ) {
            javax.wsdl.Part part = (javax.wsdl.Part) iter.next();
            QName typeName = part.getElementName();
            if (typeName == null) {
                typeName = part.getTypeName();
            }
            ParameterDesc param = new ParameterDesc();
            if (aIsInput) {
                param.setMode(ParameterDesc.IN);
            } else {
                param.setMode(ParameterDesc.OUT);
            }
            param.setName(part.getName());
            param.setTypeQName(typeName);
            aOperation.addParameter(param);
        }
    }

    /**
     * Calls the process engine to handle the incoming message and waits for the reply
     * if this is not a one-way.  This is called by derived classes (document and rpc
     * handler) after they have properly figured out operation and input message
     * contexts.
     *
     * @param aDeployment
     * @param aContext context used for supplying output message to the invoker
     * @param aServiceDesc the service desc we are associated with
     * @param aOperation the operation we are invoking
     * @param aInterface the interface of the operation
     * @param aInputMsg the input message for the operation
     * @throws SdlBusinessProcessException
     */
    protected void invokeSdlProcessEngine(IServiceProcessDeployment aDeployment, MessageContext aContext, ServiceDesc aServiceDesc, Operation aOperation, Interface aInterface, ISdlMessageData aInputMsg) throws SdlBusinessProcessException {
        logger.debug("invokeSdlProcessEngine(...) " + aOperation.getElName() + " in " + aInterface.getElName());
        String plinkName = ((SdlPartnerLinkDef) aServiceDesc.getProperty(PARTNER_LINK_ENTRY)).getName();
        ServiceReplyResponseReceiver invokeResp = null;
        if (aOperation.getRefOutputMessage() != null) {
            invokeResp = new ServiceReplyResponseReceiver();
        }
        IServiceEndpointReference myEndpoint = new ServiceEndpointReference();
        myEndpoint.setAddress((String) aContext.getProperty("transport.url"));
        myEndpoint.setServiceName(new QName(aInterface.getElName(), aServiceDesc.getName()));
        myEndpoint.setServicePort(aServiceDesc.getName() + "Port");
        SdlEndpointReferenceSourceType type = aDeployment.getEndpointSourceType(plinkName);
        IServiceEndpointReference embeddedEndpointRef = null;
        IServiceId serviceId = null;
        if (type == SdlEndpointReferenceSourceType.INVOKER) {
            if (SdlEngineFactory.getPartnerAddressing().isServiceId()) {
                serviceId = SdlEngineFactory.getPartnerAddressing().readServiceIdFromContext(aContext);
            } else {
                embeddedEndpointRef = SdlEngineFactory.getPartnerAddressing().readFromContext(aContext);
            }
        }
        SdlMessageContext context = new SdlMessageContext();
        context.setEmbeddedEndpointReference(embeddedEndpointRef);
        context.setOperation(aOperation.getElName());
        context.setPartnerLinkName(plinkName);
        context.setPrincipal(aContext.getUsername());
        context.setProcessName(aDeployment.getSdlProcessDef().getQName());
        context.setMyEndpointReference(myEndpoint);
        SdlEngineFactory.getSdlEngine().queueReceiveSdlData(aDeployment, aInputMsg, invokeResp, context);
        if (invokeResp != null) {
            synchronized (invokeResp) {
                if (invokeResp.getMessageData() == null && invokeResp.getFault() == null) {
                    try {
                        invokeResp.wait(600000);
                    } catch (InterruptedException ex) {
                        logger.error("InterruptedException: Thread interrupted waiting for reply");
                        throw new SdlBusinessProcessException("Thread interrupted waiting for reply");
                    }
                }
            }
            if (invokeResp.getMessageData() != null) {
                mapOutputData(aContext, invokeResp.getMessageData());
            } else if (invokeResp.getFault() != null) {
                logger.error("invoke/response contains faults: " + invokeResp.getFault().getDetailedInfo());
                mapFaultData(aContext, invokeResp.getFault());
            } else {
                ;
            }
        }
    }

    /**
     * Calls the process engine to handle the incoming message and waits for the reply
     * if this is not a one-way.  This is called by derived classes (document and rpc
     * handler) after they have properly figured out operation and input message
     * contexts.
     *
     * @param aDeployment
     * @param aContext context used for supplying output message to the invoker
     * @param aServiceDesc the service desc we are associated with
     * @param aOperation the operation we are invoking
     * @param aInterface the interface of the operation
     * @param aInputMsg the input message for the operation
     * @throws SdlBusinessProcessException
     */
    protected void invokeWsdlProcessEngine(IServiceProcessDeployment aDeployment, MessageContext aContext, ServiceDesc aServiceDesc, javax.wsdl.Operation aOperation, javax.wsdl.PortType aPortType, ISdlMessageData aInputMsg) throws SdlBusinessProcessException {
        logger.debug("invokeWsdlProcessEngine(...) " + aOperation.getName() + " in " + aPortType.getQName());
        String plinkName = ((SdlPartnerLinkDef) aServiceDesc.getProperty(PARTNER_LINK_ENTRY)).getName();
        ServiceReplyResponseReceiver invokeResp = null;
        if (aOperation.getStyle() == OperationType.REQUEST_RESPONSE) invokeResp = new ServiceReplyResponseReceiver();
        IServiceEndpointReference myEndpoint = new ServiceEndpointReference();
        myEndpoint.setAddress((String) aContext.getProperty("transport.url"));
        myEndpoint.setServiceName(new QName(aPortType.getQName().getNamespaceURI(), aServiceDesc.getName()));
        myEndpoint.setServicePort(aServiceDesc.getName() + "Port");
        SdlEndpointReferenceSourceType type = aDeployment.getEndpointSourceType(plinkName);
        IServiceEndpointReference embeddedEndpointRef = null;
        if (type == SdlEndpointReferenceSourceType.INVOKER) {
            embeddedEndpointRef = SdlEngineFactory.getPartnerAddressing().readFromContext(aContext);
        }
        SdlMessageContext context = new SdlMessageContext();
        context.setEmbeddedEndpointReference(embeddedEndpointRef);
        context.setOperation(aOperation.getName());
        context.setPartnerLinkName(plinkName);
        context.setPrincipal(aContext.getUsername());
        context.setProcessName(aDeployment.getSdlProcessDef().getQName());
        context.setMyEndpointReference(myEndpoint);
        SdlEngineFactory.getSdlEngine().queueReceiveWsdlData(aDeployment, aInputMsg, invokeResp, context);
        if (invokeResp != null) {
            synchronized (invokeResp) {
                if (invokeResp.getMessageData() == null && invokeResp.getFault() == null) {
                    try {
                        invokeResp.wait(600000);
                    } catch (InterruptedException ex) {
                        logger.error("InterruptedException: Thread interrupted waiting for reply");
                        throw new SdlBusinessProcessException("Thread interrupted waiting for reply");
                    }
                }
            }
            if (invokeResp.getMessageData() != null) {
                mapOutputData(aContext, invokeResp.getMessageData());
            } else if (invokeResp.getFault() != null) {
                logger.error("invoke/response contains faults: " + invokeResp.getFault().getDetailedInfo());
                mapFaultData(aContext, invokeResp.getFault());
            } else {
                ;
            }
        }
    }

    /**
     * Gets the deployment plan for this service.
     * @param aServiceDesc
     */
    protected IServiceProcessDeployment getDeploymentPlan(ServiceDesc aServiceDesc) throws SdlBusinessProcessException {
        IServiceProcessDeployment deploymentPlan = (IServiceProcessDeployment) SdlEngineFactory.getSdlDeploymentProvider().findCurrentDeployment(getProcessName(aServiceDesc));
        return deploymentPlan;
    }

    /**
     * Maps fault fata to the message context.
     * @param aContext
     * @param aFault
     */
    protected void mapFaultData(MessageContext aContext, ISdlFault aFault) throws SdlBusinessProcessException {
        try {
            Element[] details = null;
            if (aFault.getMessageData() != null) {
                details = new Element[1];
                String partName = (String) aFault.getMessageData().getPartNames().next();
                Object partData = aFault.getMessageData().getData(partName);
                if (partData instanceof Document) {
                    Element element = ((Document) partData).getDocumentElement();
                    details[0] = element;
                } else {
                    details[0] = XMLUtils.StringToElement("", partName, partData.toString());
                }
            }
            String message = null;
            if (SdlUtil.notNullOrEmpty(aFault.getInfo())) {
                message = aFault.getInfo();
            } else {
                message = "Error invoking operation.";
            }
            AxisFault axisFault = new AxisFault(aFault.getFaultName(), message, "", details);
            if (details == null && SdlUtil.notNullOrEmpty(aFault.getDetailedInfo())) {
                axisFault.setFaultDetailString(aFault.getDetailedInfo());
            }
            getOrCreateResponseEnvelope(aContext);
            SOAPBody body = (SOAPBody) aContext.getResponseMessage().getSOAPBody();
            SOAPFault fault = (SOAPFault) body.addFault();
            fault.setFault(axisFault);
        } catch (Exception ex) {
            logger.error("Exception: Error building output message: " + ex);
            throw new SdlBusinessProcessException("Error building output message", ex);
        }
    }

    /**
     * Gets current response envelope or creates one if necessary.
     * @param aContext
     * @throws AxisFault
     */
    protected SOAPEnvelope getOrCreateResponseEnvelope(MessageContext aContext) throws AxisFault {
        SOAPEnvelope resEnv;
        if (aContext.getResponseMessage() != null) {
            resEnv = aContext.getResponseMessage().getSOAPEnvelope();
        } else {
            resEnv = new SOAPEnvelope(aContext.getSOAPConstants(), aContext.getSchemaVersion());
            aContext.setResponseMessage(new org.apache.axis.Message(resEnv));
        }
        return resEnv;
    }

    /**
     * @param aContext
     * @param aData
     */
    protected abstract void mapOutputData(MessageContext aContext, ISdlMessageData aData) throws SdlBusinessProcessException;

    /**
     * Creates a service descriptor, and allow handler ability to initialize.
     * return SdlServiceDesc
     */
    protected abstract SdlServiceDesc createServiceDescriptor();
}
