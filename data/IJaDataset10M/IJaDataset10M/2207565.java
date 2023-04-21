package com.vangent.hieos.xutil.services.framework;

import com.vangent.hieos.xutil.atna.ATNAAuditEvent;
import com.vangent.hieos.xutil.atna.ATNAAuditEventStart;
import com.vangent.hieos.xutil.atna.ATNAAuditEventStop;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.response.AdhocQueryResponse;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.exception.ExceptionUtil;
import com.vangent.hieos.xutil.response.RegistryErrorList;
import com.vangent.hieos.xutil.response.RegistryResponse;
import com.vangent.hieos.xutil.response.RetrieveMultipleResponse;
import com.vangent.hieos.xutil.xlog.client.XLogger;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.TransportHeaders;
import org.apache.log4j.Logger;
import org.apache.axis2.engine.ServiceLifeCycle;
import org.apache.axis2.service.Lifecycle;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import com.vangent.hieos.xutil.atna.XATNALogger;
import com.vangent.hieos.xutil.exception.SOAPFaultException;
import com.vangent.hieos.xutil.xconfig.XConfigActor;
import com.vangent.hieos.xutil.xua.client.XServiceProvider;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author NIST
 * @author Bernie Thuman (BHT) - Comments, rewrites, lifecycle logging, streamlining.
 */
public abstract class XAbstractService implements ServiceLifeCycle, Lifecycle {

    private static final Logger logger = Logger.getLogger(XAbstractService.class);

    /**
     *
     */
    protected XLogMessage log_message = null;

    private String serviceName;

    private boolean active = true;

    /**
     *
     */
    public enum ActorType {

        /**
         * 
         */
        REGISTRY, /**
         *
         */
        REPOSITORY, /**
         * 
         */
        PIXMGR, /**
         *
         */
        PDS, /**
         * 
         */
        XCPD_GW, /**
         *
         */
        DOCRECIPIENT, /**
         * 
         */
        STS, /**
         *
         */
        PDP, /**
         * 
         */
        PIP, /**
         *
         */
        XDSBRIDGE
    }

    /**
     *
     * @return
     */
    protected abstract XConfigActor getConfigActor();

    /**
     *
     * @return
     */
    protected MessageContext getMessageContext() {
        return MessageContext.getCurrentMessageContext();
    }

    /**
     *
     * @return
     * @throws SOAPFaultException
     */
    public static OMElement getSAMLAssertionFromRequest() throws SOAPFaultException {
        return XServiceProvider.getSAMLAssertionFromRequest(MessageContext.getCurrentMessageContext());
    }

    /**
     *
     * @return
     */
    protected String getServiceName() {
        return this.serviceName;
    }

    /**
     *
     * @return
     * @throws SOAPFaultException
     */
    protected MessageContext getResponseMessageContext() throws SOAPFaultException {
        try {
            MessageContext messageContext = this.getMessageContext();
            MessageContext responseMessageContext = messageContext.getOperationContext().getMessageContext("Out");
            return responseMessageContext;
        } catch (AxisFault ex) {
            throw new SOAPFaultException("Unable to get response message context", ex);
        }
    }

    /**
     *
     * @return
     */
    public String getSOAPAction() {
        MessageContext mc = this.getMessageContext();
        return mc.getSoapAction();
    }

    /**
     * 
     * @throws SOAPFaultException
     */
    protected void validateWS() throws SOAPFaultException {
        checkSOAP12();
        if (isAsync()) {
            this.throwSOAPFaultException("Asynchronous web service request not acceptable on this endpoint" + " - replyTo is " + getMessageContext().getReplyTo().getAddress());
        }
    }

    /**
     * This method ensures that an asynchronous request has been sent. It evaluates the message
     * context to dtermine if "ReplyTo" is non-null and is not anonymous. It also ensures that
     * "MessageID" is non-null. It throws an exception if that is not the case.
     * @throws SOAPFaultException
     */
    protected void validateAsyncWS() throws SOAPFaultException {
        checkSOAP12();
        if (!isAsync()) {
            this.throwSOAPFaultException("Asynchronous web service required on this endpoint" + " - replyTo is " + getMessageContext().getReplyTo().getAddress());
        }
    }

    /**
     *
     * @throws SOAPFaultException
     */
    protected void validateNoMTOM() throws SOAPFaultException {
        if (getMessageContext().isDoingMTOM()) {
            this.throwSOAPFaultException("This transaction must use SIMPLE SOAP, MTOM found");
        }
    }

    /**
     *
     * @throws SOAPFaultException
     */
    protected void validateMTOM() throws SOAPFaultException {
        if (!getMessageContext().isDoingMTOM()) {
            this.throwSOAPFaultException("This transaction must use MTOM, SIMPLE SOAP found");
        }
    }

    /**
     *
     * @param serviceName
     * @param request
     * @throws SOAPFaultException
     */
    protected void beginTransaction(String serviceName, OMElement request) throws SOAPFaultException {
        this.serviceName = serviceName;
        MessageContext messageContext = this.getMessageContext();
        String remoteIP = (String) messageContext.getProperty(MessageContext.REMOTE_ADDR);
        XLogger xlogger = XLogger.getInstance();
        log_message = xlogger.getNewMessage(remoteIP);
        log_message.setTestMessage(this.serviceName);
        logger.info("Start " + serviceName + " " + log_message.getMessageID() + " : " + remoteIP + " : " + messageContext.getTo().toString());
        if (log_message.isLogEnabled()) {
            log_message.addOtherParam(Fields.service, serviceName);
            boolean is_secure = messageContext.getTo().toString().indexOf("https://") != -1;
            log_message.setSecureConnection(is_secure);
            log_message.addHTTPParam(Fields.isSecure, (is_secure) ? "true" : "false");
            log_message.addHTTPParam(Fields.date, getDateTime());
            if (request != null) {
                log_message.addOtherParam("Request", request);
            } else {
                log_message.addErrorParam("Error", "Cannot access request body in XAbstractService");
            }
        }
        if (request == null) {
            this.throwSOAPFaultException("Request body is null");
        }
        if (log_message.isLogEnabled()) {
            TransportHeaders transportHeaders = (TransportHeaders) messageContext.getProperty("TRANSPORT_HEADERS");
            for (Object o_key : transportHeaders.keySet()) {
                String key = (String) o_key;
                String value = (String) transportHeaders.get(key);
                List<String> thdrs = new ArrayList<String>();
                thdrs.add(key + " : " + value);
                this.addHttp("HTTP Header", thdrs);
            }
            if (messageContext.getEnvelope().getHeader() != null) {
                try {
                    log_message.addSOAPParam("Soap Header", messageContext.getEnvelope().getHeader());
                } catch (OMException e) {
                }
            }
            if (messageContext.getEnvelope().getBody() != null) {
                try {
                    log_message.addSOAPParam("Soap Envelope", messageContext.getEnvelope());
                } catch (OMException e) {
                }
            }
            log_message.addHTTPParam(Fields.fromIpAddress, remoteIP);
            log_message.addHTTPParam(Fields.endpoint, messageContext.getTo().toString());
        }
        this.validateXUA();
    }

    /**
     *
     * @throws SOAPFaultException
     */
    private void validateXUA() throws SOAPFaultException {
        XConfigActor configActor = this.getConfigActor();
        if (configActor == null) {
            this.throwSOAPFaultException("Configuration not established for Actor");
        }
        if (!configActor.isXUAEnabled()) {
            return;
        }
        XServiceProvider xServiceProvider = new XServiceProvider(log_message);
        XServiceProvider.Status response = XServiceProvider.Status.ABORT;
        MessageContext messageCtx = this.getMessageContext();
        try {
            response = xServiceProvider.run(this.getConfigActor(), messageCtx);
        } catch (Exception ex) {
            this.throwSOAPFaultException("XUA:ERROR - SAML Validation Exception (ignoring request) " + ex.getMessage());
        }
        if (response != XServiceProvider.Status.CONTINUE) {
            this.throwSOAPFaultException("XUA:ERROR - SAML Assertion did not pass validation!");
        }
    }

    /**
     *
     * @param status
     */
    protected void endTransaction(boolean status) {
        if (active && logger.isInfoEnabled()) {
            active = false;
            logger.info("End " + serviceName + " " + ((log_message == null) ? "null" : log_message.getMessageID()) + " : " + ((status) ? "Pass" : "Fail"));
        }
        stopXLogger();
    }

    /**
     *
     * @param request
     * @param e
     * @param actor
     * @param message
     * @return
     */
    protected OMElement endTransaction(OMElement request, Exception e, ActorType actor, String message) {
        if (message == null || message.equals("")) {
            message = e.getMessage();
        }
        logger.error("Exception thrown while processing web service request", e);
        OMElement errorResult = this.start_up_error(request, e, actor, message);
        if (log_message.isLogEnabled() && (errorResult != null)) {
            log_message.addOtherParam("Response - ERROR", errorResult);
        }
        endTransaction(false);
        return errorResult;
    }

    /**
     *
     * @param request
     * @param e
     * @param actor
     * @param message
     * @return
     */
    protected OMElement start_up_error(OMElement request, Exception e, ActorType actor, String message) {
        return start_up_error(request, e, actor, message, true);
    }

    /**
     *
     * @param request
     * @param e
     * @param actor
     * @param message
     * @param log
     * @return
     */
    public OMElement start_up_error(OMElement request, Object e, ActorType actor, String message, boolean log) {
        String error_type = (actor == ActorType.REGISTRY) ? MetadataSupport.XDSRegistryError : MetadataSupport.XDSRepositoryError;
        try {
            OMNamespace ns = (request != null) ? request.getNamespace() : MetadataSupport.ebRSns2;
            if (ns.getNamespaceURI().equals(MetadataSupport.ebRSns3.getNamespaceURI()) || ns.getNamespaceURI().equals("urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0")) {
                RegistryErrorList rel = new RegistryErrorList(log);
                rel.add_error(error_type, message, exception_details(e), log_message);
                return new RegistryResponse(rel).getResponse();
            }
            if (ns.getNamespaceURI().equals(MetadataSupport.xdsB.getNamespaceURI())) {
                RegistryErrorList rel = new RegistryErrorList(log);
                rel.add_error(error_type, message, exception_details(e), log_message);
                if (request.getLocalName().equals("RetrieveDocumentSetRequest")) {
                    OMElement res = new RetrieveMultipleResponse(rel).getResponse();
                    return res;
                } else {
                    return new RegistryResponse(rel).getResponse();
                }
            }
            if (ns.getNamespaceURI().equals(MetadataSupport.ebQns3.getNamespaceURI())) {
                RegistryErrorList rel = new RegistryErrorList(log);
                rel.add_error(error_type, message, exception_details(e), log_message);
                return new AdhocQueryResponse(rel).getResponse();
            }
        } catch (XdsInternalException e1) {
            return null;
        }
        return null;
    }

    /**
     * Stop the test log facility.
     */
    protected void stopXLogger() {
        if (log_message != null) {
            log_message.store();
            log_message = null;
        }
    }

    /**
     *
     * @param e
     * @return
     */
    protected String exception_details(Object e) {
        if (e == null) {
            return "No Additional Details Available";
        }
        if (e instanceof Exception) {
            return exception_details((Exception) e);
        }
        if (e instanceof String) {
            return exception_details((String) e);
        }
        return exception_details(e.toString());
    }

    /**
     *
     * @param e
     * @return
     */
    protected String exception_details(Exception e) {
        return ExceptionUtil.exception_details(e);
    }

    /**
     *
     * @param e
     * @return
     */
    protected String exception_details(String e) {
        return e;
    }

    /**
     *
     * @param title
     * @param t
     */
    private void addHttp(String title, List<String> t) {
        StringBuilder buffer = new StringBuilder();
        for (String s : t) {
            buffer.append(s).append("  ");
        }
        log_message.addHTTPParam(title, buffer.toString());
    }

    /**
     *
     * @return
     */
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     *
     * @throws SOAPFaultException
     */
    protected void checkSOAP12() throws SOAPFaultException {
        if (MessageContext.getCurrentMessageContext().isSOAP11()) {
            throwSOAPFaultException("SOAP 1.1 not supported");
        }
        SOAPEnvelope env = MessageContext.getCurrentMessageContext().getEnvelope();
        if (env == null) {
            throwSOAPFaultException("No SOAP envelope found");
        }
        SOAPHeader hdr = env.getHeader();
        if (hdr == null) {
            throwSOAPFaultException("No SOAP header found");
        }
        if (!hdr.getChildrenWithName(new QName("http://www.w3.org/2005/08/addressing", "Action")).hasNext()) {
            throwSOAPFaultException("WS-Action required in header");
        }
    }

    /**
     *
     * @throws SOAPFaultException
     */
    protected void checkSOAP11() throws SOAPFaultException {
        if (!MessageContext.getCurrentMessageContext().isSOAP11()) {
            throwSOAPFaultException("SOAP 1.2 not supported");
        }
        SOAPEnvelope env = MessageContext.getCurrentMessageContext().getEnvelope();
        if (env == null) {
            throwSOAPFaultException("No SOAP envelope found");
        }
    }

    /**
     *
     * @throws SOAPFaultException
     */
    protected void checkSOAPAny() throws SOAPFaultException {
        if (MessageContext.getCurrentMessageContext().isSOAP11()) {
            checkSOAP11();
        } else {
            checkSOAP12();
        }
    }

    /**
     *
     * @param msg
     * @throws SOAPFaultException
     */
    private void throwSOAPFaultException(String msg) throws SOAPFaultException {
        if (log_message != null) {
            log_message.addErrorParam("SOAPError", msg);
            log_message.addOtherParam("Response", "SOAPFault: " + msg);
        }
        endTransaction(false);
        throw new SOAPFaultException(msg);
    }

    /**
     *
     * @param ex
     * @throws AxisFault
     */
    public void throwAxisFault(SOAPFaultException ex) throws AxisFault {
        if (log_message != null) {
            log_message.addErrorParam("SOAPError", ex.getMessage());
            log_message.addOtherParam("Response", "SOAPFault: " + ex.getMessage());
        }
        endTransaction(false);
        throw new AxisFault(ex.getMessage());
    }

    /**
     *
     * @return
     */
    protected boolean isAsync() {
        MessageContext mc = getMessageContext();
        return mc.getMessageID() != null && !mc.getMessageID().equals("") && mc.getReplyTo() != null && !mc.getReplyTo().hasAnonymousAddress();
    }

    /**
     *
     * @return
     */
    boolean isSync() {
        return !isAsync();
    }

    /**
     * This is called when a new instance of the implementing class has been created.
     * This occurs in sync with session/ServiceContext creation. This method gives classes
     * a chance to do any setup work (grab resources, establish connections, etc) before
     * they are invoked by a service request.
     * @param serviceContext 
     * @throws AxisFault
     */
    public void init(ServiceContext serviceContext) throws AxisFault {
    }

    /**
     * This is called when Axis2 decides that it is finished with a particular instance
     * of the back-end service class. It allows classes to clean up resources.
     * @param serviceContext
     */
    public void destroy(ServiceContext serviceContext) {
    }

    /**
     * This will be called during the deployment time of the service.
     * Irrespective of the service scope this method will be called
     * @param configctx 
     * @param service
     */
    public void startUp(ConfigurationContext configctx, AxisService service) {
    }

    /**
     * This will be called during the system shut down time. Irrespective
     * of the service scope this method will be called
     * @param configctx
     * @param service
     */
    public void shutDown(ConfigurationContext configctx, AxisService service) {
    }

    /**
     *
     * @param actorType
     */
    public void ATNAlogStop(ATNAAuditEvent.ActorType actorType) {
        try {
            XATNALogger xATNALogger = new XATNALogger();
            if (xATNALogger.isPerformAudit()) {
                ATNAAuditEventStop auditEvent = new ATNAAuditEventStop();
                auditEvent.setTransaction(ATNAAuditEvent.IHETransaction.STOP);
                auditEvent.setActorType(actorType);
                xATNALogger.audit(auditEvent);
            }
        } catch (Exception e) {
            logger.error("Could not perform ATNA audit (stop)", e);
        }
    }

    /**
     *
     * @param actorType
     */
    public void ATNAlogStart(ATNAAuditEvent.ActorType actorType) {
        try {
            XATNALogger xATNALogger = new XATNALogger();
            if (xATNALogger.isPerformAudit()) {
                ATNAAuditEventStart auditEvent = new ATNAAuditEventStart();
                auditEvent.setTransaction(ATNAAuditEvent.IHETransaction.START);
                auditEvent.setActorType(actorType);
                xATNALogger.audit(auditEvent);
            }
        } catch (Exception e) {
            logger.error("Could not perform ATNA audit (start)", e);
        }
    }
}
