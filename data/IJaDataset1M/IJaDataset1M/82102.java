package net.services.servicebus;

import com.sun.xml.ws.util.localization.Localizable;
import com.sun.xml.ws.util.localization.LocalizableMessageFactory;
import com.sun.xml.ws.util.localization.Localizer;

/**
 * Localization of Log SBMessages 
 * 
 * @author Anish
 */
public final class SBMessages {

    private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("net.services.servicebus.Messages");

    private static final Localizer localizer = new Localizer();

    public static Localizable localizablePROCESS_SERVER_START() {
        return messageFactory.getMessage("PROCESS_SERVER_START");
    }

    /**
     * Server going to start
     * 
     */
    public static String PROCESS_SERVER_START() {
        return localizer.localize(localizablePROCESS_SERVER_START());
    }

    public static Localizable localizableSB_CONTEXT_CREATE() {
        return messageFactory.getMessage("SB_CONTEXT_CREATE");
    }

    /**
     * Context SB created
     * 
     */
    public static String SB_CONTEXT_CREATE() {
        return localizer.localize(localizableSB_CONTEXT_CREATE());
    }

    public static Localizable localizableSB_DEPLOYMENT_PARSE() {
        return messageFactory.getMessage("SB_DEPLOYMENT_PARSE");
    }

    /**
     * Deployment SB parsed
     * 
     */
    public static String SB_DEPLOYMENT_PARSE() {
        return localizer.localize(localizableSB_DEPLOYMENT_PARSE());
    }

    public static Localizable localizablePROCESS_SERVER_STARTED() {
        return messageFactory.getMessage("PROCESS_SERVER_STARTED");
    }

    /**
     * Server started
     * 
     */
    public static String PROCESS_SERVER_STARTED() {
        return localizer.localize(localizablePROCESS_SERVER_STARTED());
    }

    public static Localizable localizableSERVER_HOSTED_URI(String uri) {
        return messageFactory.getMessage("SERVER_HOSTED_URI", uri);
    }

    /**
     * Server URI: {0}
     * 
     */
    public static String SERVER_HOSTED_URI(String uri) {
        return localizer.localize(localizableSERVER_HOSTED_URI(uri));
    }

    public static Localizable localizableSB_HANDLER_CREATE(String uri) {
        return messageFactory.getMessage("SB_HANDLER_CREATE", uri);
    }

    /**
     * SB Handler created for URI: {0}
     * 
     */
    public static String SB_HANDLER_CREATE(String uri) {
        return localizer.localize(localizableSB_HANDLER_CREATE(uri));
    }

    public static Localizable localizableSB_SUBSCRIPTION_START(String uri) {
        return messageFactory.getMessage("SB_SUBSCRIPTION_START", uri);
    }

    /**
     * SB subscription going to start for URI: {0}
     * 
     */
    public static String SB_SUBSCRIPTION_START(String uri) {
        return localizer.localize(localizableSB_SUBSCRIPTION_START(uri));
    }

    public static Localizable localizableEXCEPTION_DUPLICATE_LISTENER(String uri) {
        return messageFactory.getMessage("EXCEPTION_DUPLICATE_LISTENER");
    }

    /**
     * For {0} a duplicate listener found
     * 
     */
    public static String EXCEPTION_DUPLICATE_LISTENER(String uri) {
        return localizer.localize(localizableEXCEPTION_DUPLICATE_LISTENER(uri));
    }

    public static Localizable localizableSB_SUBSCRIPTION_STARTED(String uri) {
        return messageFactory.getMessage("SB_SUBSCRIPTION_STARTED", uri);
    }

    /**
     * SB subscription started for URI: {0}
     * 
     */
    public static String SB_SUBSCRIPTION_STARTED(String uri) {
        return localizer.localize(localizableSB_SUBSCRIPTION_STARTED(uri));
    }

    public static Localizable localizableSUBSCRIBER_COUNT(int count) {
        return messageFactory.getMessage("SUBSCRIBER_COUNT", count);
    }

    /**
     * Subscriber count: {0}
     * 
     */
    public static String SUBSCRIBER_COUNT(int count) {
        return localizer.localize(localizableSUBSCRIBER_COUNT(count));
    }

    public static Localizable localizableMESSAGE_BUFFER_CREATE() {
        return messageFactory.getMessage("MESSAGE_BUFFER_CREATE");
    }

    /**
     * Message Buffer going to create
     * 
     */
    public static String MESSAGE_BUFFER_CREATE() {
        return localizer.localize(localizableMESSAGE_BUFFER_CREATE());
    }

    public static Localizable localizableMESSAGE_BUFFER_CREATED() {
        return messageFactory.getMessage("MESSAGE_BUFFER_CREATED");
    }

    /**
     * Message Buffer created
     * 
     */
    public static String MESSAGE_BUFFER_CREATED() {
        return localizer.localize(localizableMESSAGE_BUFFER_CREATED());
    }

    public static Localizable localizableWEB_SERVICE_OPERATION_START() {
        return messageFactory.getMessage("WEB_SERVICE_OPERATION_START");
    }

    /**
     * Web service operation started
     * 
     */
    public static String WEB_SERVICE_OPERATION_START() {
        return localizer.localize(localizableWEB_SERVICE_OPERATION_START());
    }

    public static Localizable localizableSB_WSDL_URL(String url) {
        return messageFactory.getMessage("SB_WSDL_URL", url);
    }

    /**
     * WSDL URL: {0}
     * 
     */
    public static String SB_WSDL_URL(String url) {
        return localizer.localize(localizableSB_WSDL_URL(url));
    }

    public static Localizable localizableEXCEPTION_URL_CREATE(String ex) {
        return messageFactory.getMessage("EXCEPTION_URL_CREATE", ex);
    }

    /**
     * Exception while creating the URL for the WSDL - {0}
     * 
     */
    public static String EXCEPTION_URL_CREATE(String ex) {
        return localizer.localize(localizableEXCEPTION_URL_CREATE(ex));
    }

    public static Localizable localizableWEB_SERVICE_CREATED() {
        return messageFactory.getMessage("WEB_SERVICE_CREATED");
    }

    /**
     * Web service created
     * 
     */
    public static String WEB_SERVICE_CREATED() {
        return localizer.localize(localizableWEB_SERVICE_CREATED());
    }

    public static Localizable localizableWEB_SERVICE_PORT_CREATED() {
        return messageFactory.getMessage("WEB_SERVICE_PORT_CREATED");
    }

    /**
     * Web service port created
     * 
     */
    public static String WEB_SERVICE_PORT_CREATED() {
        return localizer.localize(localizableWEB_SERVICE_PORT_CREATED());
    }

    public static Localizable localizableMESSAGE_BUFFER_MANAGER_EXPIRY(long expiry) {
        return messageFactory.getMessage("MESSAGE_BUFFER_MANAGER_EXPIRY", expiry);
    }

    /**
     * Message buffer manager expiry: {0}
     * 
     */
    public static String MESSAGE_BUFFER_MANAGER_EXPIRY(long expiry) {
        return localizer.localize(localizableMESSAGE_BUFFER_MANAGER_EXPIRY(expiry));
    }

    public static Localizable localizableMESSAGE_BUFFER_MANAGER_URI(String uri) {
        return messageFactory.getMessage("MESSAGE_BUFFER_MANAGER_URI", uri);
    }

    /**
     * Message buffer manager URI: {0}
     * 
     */
    public static String MESSAGE_BUFFER_MANAGER_URI(String uri) {
        return localizer.localize(localizableMESSAGE_BUFFER_MANAGER_URI(uri));
    }

    public static Localizable localizableSCHEDULE_RENEW_TIMER(long renewTime) {
        return messageFactory.getMessage("SCHEDULE_RENEW_TIMER", renewTime);
    }

    /**
     * Renew schedule timer: {0}
     * 
     */
    public static String SCHEDULE_RENEW_TIMER(long renewTime) {
        return localizer.localize(localizableSCHEDULE_RENEW_TIMER(renewTime));
    }

    public static Localizable localizableRENEW_MESSAGE_BUFFER_CALLED_BACK() {
        return messageFactory.getMessage("RENEW_MESSAGE_BUFFER_CALLED_BACK");
    }

    /**
     * Renew message buffer call back..
     * 
     */
    public static String RENEW_MESSAGE_BUFFER_CALLED_BACK() {
        return localizer.localize(localizableRENEW_MESSAGE_BUFFER_CALLED_BACK());
    }

    public static Localizable localizableWSDL_ENDPOINT_OVERRIDE() {
        return messageFactory.getMessage("WSDL_ENDPOINT_OVERRIDE");
    }

    /**
     * Invoking and overriding endpoint address in WSDL
     * 
     */
    public static String WSDL_ENDPOINT_OVERRIDE() {
        return localizer.localize(localizableWSDL_ENDPOINT_OVERRIDE());
    }

    public static Localizable localizableENDPOINT_ADDRESS_SET(String endpointAddress) {
        return messageFactory.getMessage("ENDPOINT_ADDRESS_SET", endpointAddress);
    }

    /**
     * Setting end point address: {0}
     * 
     */
    public static String ENDPOINT_ADDRESS_SET(String endpointAddress) {
        return localizer.localize(localizableENDPOINT_ADDRESS_SET(endpointAddress));
    }

    public static Localizable localizableEXCEPTION_AT_SUBSCRIPTION(String ex) {
        return messageFactory.getMessage("EXCEPTION_AT_SUBSCRIPTION", ex);
    }

    /**
     * Subscribing was not successful. Closing Buffer: {0}
     * 
     */
    public static String EXCEPTION_AT_SUBSCRIPTION(String ex) {
        return localizer.localize(localizableEXCEPTION_AT_SUBSCRIPTION(ex));
    }

    public static Localizable localizableTIMER_SCHEDULE() {
        return messageFactory.getMessage("TIMER_SCHEDULE");
    }

    /**
     * Timer scheduled
     * 
     */
    public static String TIMER_SCHEDULE() {
        return localizer.localize(localizableTIMER_SCHEDULE());
    }

    public static Localizable localizableEXCEPTION_LISTENER_PARSE(String ex) {
        return messageFactory.getMessage("EXCEPTION_LISTENER_PARSE", ex);
    }

    /**
     * SB listener parser failed. {0}
     * 
     */
    public static String EXCEPTION_LISTENER_PARSE(String ex) {
        return localizer.localize(localizableEXCEPTION_LISTENER_PARSE(ex));
    }

    public static Localizable localizableSB_UNSUBSCRIPTION_COMPLETE(String uri) {
        return messageFactory.getMessage("SB_UNSUBSCRIPTION_COMPLETE", uri);
    }

    /**
     * SB unsubscription completed for URI: {0}
     * 
     */
    public static String SB_UNSUBSCRIPTION_COMPLETE(String uri) {
        return localizer.localize(localizableSB_UNSUBSCRIPTION_COMPLETE(uri));
    }

    public static Localizable localizablePROCESS_SERVER_STOP() {
        return messageFactory.getMessage("PROCESS_SERVER_STOP");
    }

    /**
     * Server going to stop
     * 
     */
    public static String PROCESS_SERVER_STOP() {
        return localizer.localize(localizablePROCESS_SERVER_STOP());
    }

    public static Localizable localizablePROCESS_SERVER_STOPPED() {
        return messageFactory.getMessage("PROCESS_SERVER_STOPPED");
    }

    /**
     * Server stopped
     * 
     */
    public static String PROCESS_SERVER_STOPPED() {
        return localizer.localize(localizablePROCESS_SERVER_STOPPED());
    }

    public static Localizable localizableEXCEPTION_UNSUBSCRIPTION(String ex) {
        return messageFactory.getMessage("EXCEPTION_UNSUBSCRIPTION", ex);
    }

    /**
     * SB unsubscription exception occured: {0}
     * 
     */
    public static String EXCEPTION_UNSUBSCRIPTION(String ex) {
        return localizer.localize(localizableEXCEPTION_UNSUBSCRIPTION(ex));
    }

    public static Localizable localizableMESSAGE_BUFFER_DELETE() {
        return messageFactory.getMessage("MESSAGE_BUFFER_DELETE");
    }

    /**
     * Message buffer deleted
     * 
     */
    public static String MESSAGE_BUFFER_DELETE() {
        return localizer.localize(localizableMESSAGE_BUFFER_DELETE());
    }

    public static Localizable localizableMESSAGE_ID(String id) {
        return messageFactory.getMessage("MESSAGE_ID", id);
    }

    /**
     * Mesage ID: {0}
     * 
     */
    public static String MESSAGE_ID(String id) {
        return localizer.localize(localizableMESSAGE_ID(id));
    }

    public static Localizable localizableMESSAGE_CONTENT_TYPE(String contentType) {
        return messageFactory.getMessage("MESSAGE_CONTENT_TYPE", contentType);
    }

    /**
     * Message content type: {0}
     * 
     */
    public static String MESSAGE_CONTENT_TYPE(String contentType) {
        return localizer.localize(localizableMESSAGE_CONTENT_TYPE(contentType));
    }

    public static Localizable localizableSB_ONE_WAY_REQUEST() {
        return messageFactory.getMessage("SB_ONE_WAY_REQUEST");
    }

    /**
     * Its a one way request, returns null as response
     * 
     */
    public static String SB_ONE_WAY_REQUEST() {
        return localizer.localize(localizableSB_ONE_WAY_REQUEST());
    }

    public static Localizable localizableEXCEPTION_TIME_OUT() {
        return messageFactory.getMessage("EXCEPTION_TIME_OUT");
    }

    /**
     * Time out exception..!!
     * 
     */
    public static String EXCEPTION_TIME_OUT() {
        return localizer.localize(localizableEXCEPTION_TIME_OUT());
    }

    public static Localizable localizableSB_TWO_WAY_REQUEST() {
        return messageFactory.getMessage("SB_TWO_WAY_REQUEST");
    }

    /**
     * Two request found and returns reply
     * 
     */
    public static String SB_TWO_WAY_REQUEST() {
        return localizer.localize(localizableSB_TWO_WAY_REQUEST());
    }

    public static Localizable localizableEXCEPTION_WEB_SERVICE(String ex) {
        return messageFactory.getMessage("EXCEPTION_WEB_SERVICE", ex);
    }

    /**
     * Web service exception: {0}
     * 
     */
    public static String EXCEPTION_WEB_SERVICE(String ex) {
        return localizer.localize(localizableEXCEPTION_WEB_SERVICE(ex));
    }

    public static Localizable localizableEXCEPTION_THROWN(String ex) {
        return messageFactory.getMessage("EXCEPTION_THROWN", ex);
    }

    /**
     * Exception thrown: {0}
     * 
     */
    public static String EXCEPTION_THROWN(String ex) {
        return localizer.localize(localizableEXCEPTION_THROWN(ex));
    }

    public static Localizable localizableEXCEPTION_PROCESS(String ex) {
        return messageFactory.getMessage("EXCEPTION_PROCESS", ex);
    }

    /**
     * Process Exception, shouldn't be called: {0}
     * 
     */
    public static String EXCEPTION_PROCESS(String ex) {
        return localizer.localize(localizableEXCEPTION_PROCESS(ex));
    }

    public static Localizable localizableEXCEPTION_PROCESS_RESPONSE() {
        return messageFactory.getMessage("EXCEPTION_PROCESS_RESPONSE");
    }

    /**
     * Process response shouldn't be called
     * 
     */
    public static String EXCEPTION_PROCESS_RESPONSE() {
        return localizer.localize(localizableEXCEPTION_PROCESS_RESPONSE());
    }

    public static Localizable localizableLISTENER_FOUND_URI(String uri) {
        return messageFactory.getMessage("LISTENER_FOUND_URI", uri);
    }

    /**
     * Found {0} URI in Listener
     * 
     */
    public static String LISTENER_FOUND_URI(String uri) {
        return localizer.localize(localizableLISTENER_FOUND_URI(uri));
    }

    public static Localizable localizableLISTENER_INSERTION_URI(String uri) {
        return messageFactory.getMessage("LISTENER_INSERTION_URI", uri);
    }

    /**
     * Inserted {0} URI in Listener 
     * 
     */
    public static String LISTENER_INSERTION_URI(String uri) {
        return localizer.localize(localizableLISTENER_INSERTION_URI(uri));
    }

    public static Localizable localizableADAPTER_CONNECTION_CREATE() {
        return messageFactory.getMessage("ADAPTER_CONNECTION_CREATE");
    }

    /**
     * SB adapter connection created
     * 
     */
    public static String ADAPTER_CONNECTION_CREATE() {
        return localizer.localize(localizableADAPTER_CONNECTION_CREATE());
    }

    public static Localizable localizableEXCEPTION_ADAPTER_PACKET_CREATION(String ex) {
        return messageFactory.getMessage("EXCEPTION_ADAPTER_PACKET_CREATION", ex);
    }

    /**
     * Exception reported while creating packet in Adapter: {0}
     * 
     */
    public static String EXCEPTION_ADAPTER_PACKET_CREATION(String ex) {
        return localizer.localize(localizableEXCEPTION_ADAPTER_PACKET_CREATION(ex));
    }

    public static Localizable localizableADAPTER_CREATE(String uri) {
        return messageFactory.getMessage("ADAPTER_CREATE", uri);
    }

    /**
     * Adapter created for URI: {0}
     * 
     */
    public static String ADAPTER_CREATE(String uri) {
        return localizer.localize(localizableADAPTER_CREATE(uri));
    }

    public static Localizable localizableADAPTER_CONNECTION_CLOSE() {
        return messageFactory.getMessage("ADAPTER_CONNECTION_CLOSE");
    }

    /**
     * Adapter connection close
     * 
     */
    public static String ADAPTER_CONNECTION_CLOSE() {
        return localizer.localize(localizableADAPTER_CONNECTION_CLOSE());
    }

    public static Localizable localizableRESPONSE_ENDPOINT_ADDRESS(String endpointAddress) {
        return messageFactory.getMessage("RESPONSE_ENDPOINT_ADDRESS", endpointAddress);
    }

    /**
     * Response endpoint address: {0}
     * 
     */
    public static String RESPONSE_ENDPOINT_ADDRESS(String endpointAddress) {
        return localizer.localize(localizableRESPONSE_ENDPOINT_ADDRESS(endpointAddress));
    }

    public static Localizable localizableEXCEPTION_RESPONSE_CLIENT_ID(String messageID) {
        return messageFactory.getMessage("EXCEPTION_RESPONSE_CLIENT_ID", messageID);
    }

    /**
     * Exception found for the message id {0} while accessing its client address
     * 
     */
    public static String EXCEPTION_RESPONSE_CLIENT_ID(String messageID) {
        return localizer.localize(localizableEXCEPTION_RESPONSE_CLIENT_ID(messageID));
    }

    public static Localizable localizableEXCEPTION_RESPONSE_MESSAGE() {
        return messageFactory.getMessage("EXCEPTION_RESPONSE_MESSAGE");
    }

    /**
     * Exception found while processing response message
     * 
     */
    public static String EXCEPTION_RESPONSE_MESSAGE() {
        return localizer.localize(localizableEXCEPTION_RESPONSE_MESSAGE());
    }

    public static Localizable localizableEXCEPTION_ADAPTER_HANDLE(String ex) {
        return messageFactory.getMessage("EXCEPTION_ADAPTER_HANDLE", ex);
    }

    /**
     * Exception while handling adapter: {0}
     * 
     */
    public static String EXCEPTION_ADAPTER_HANDLE(String ex) {
        return localizer.localize(localizableEXCEPTION_ADAPTER_HANDLE(ex));
    }

    public static Localizable localizableEXCEPTION_MESSAGE_BUFFER_DELETE(String ex) {
        return messageFactory.getMessage("EXCEPTION_MESSAGE_BUFFER_DELETE", ex);
    }

    /**
     * Exception while deleting Message Buffer: {0}
     * 
     */
    public static String EXCEPTION_MESSAGE_BUFFER_DELETE(String ex) {
        return localizer.localize(localizableEXCEPTION_MESSAGE_BUFFER_DELETE(ex));
    }

    public static Localizable localizableEXCEPTION_PROCESS_AT(String ex) {
        return messageFactory.getMessage("EXCEPTION_PROCESS_AT", ex);
    }

    /**
     * Exception while including 'ProcessAt' header: {0}
     * 
     */
    public static String EXCEPTION_PROCESS_AT(String ex) {
        return localizer.localize(localizableEXCEPTION_PROCESS_AT(ex));
    }

    public static Localizable localizableEXCEPTION_MESSAGE_BUFFER_CREATE(String ex) {
        return messageFactory.getMessage("EXCEPTION_MESSAGE_BUFFER_CREATE", ex);
    }

    /**
     * Exception during the process of creating Message Buffer: {0}
     * 
     */
    public static String EXCEPTION_MESSAGE_BUFFER_CREATE(String ex) {
        return localizer.localize(localizableEXCEPTION_MESSAGE_BUFFER_CREATE(ex));
    }

    public static Localizable localizableRESPONSE_CORRELATION_CONTEXT_CREATE(String id) {
        return messageFactory.getMessage("RESPONSE_CORRELATION_CONTEXT_CREATE", id);
    }

    /**
     * Response correlation context created for the id: {0}
     * 
     */
    public static String RESPONSE_CORRELATION_CONTEXT_CREATE(String id) {
        return localizer.localize(localizableRESPONSE_CORRELATION_CONTEXT_CREATE(id));
    }

    public static Localizable localizableRESPONSE_CORRELATION_CONTEXT_INSET(String id) {
        return messageFactory.getMessage("RESPONSE_CORRELATION_CONTEXT_INSET", id);
    }

    /**
     * Response correlation context inserted into context mapper for the id: {0}
     * 
     */
    public static String RESPONSE_CORRELATION_CONTEXT_INSET(String id) {
        return localizer.localize(localizableRESPONSE_CORRELATION_CONTEXT_INSET(id));
    }

    public static Localizable localizableRESPONSE_CORRELATION_CONTEXT_REMOVE(String id) {
        return messageFactory.getMessage("RESPONSE_CORRELATION_CONTEXT_REMOVE", id);
    }

    /**
     * Response correlation context removed from the context mapper for the id: {0}
     * 
     */
    public static String RESPONSE_CORRELATION_CONTEXT_REMOVE(String id) {
        return localizer.localize(localizableRESPONSE_CORRELATION_CONTEXT_REMOVE(id));
    }

    public static Localizable localizableRESPONSE_CORRELATION_CONTEXT_REMOVE(long time, String ex) {
        return messageFactory.getMessage("EXCEPTION_RESPONSE_WAIT_HANDLE", time, ex);
    }

    /**
     * Exception while wait handle failed to get response for {0} milliseconds time intervel: {1}
     * 
     */
    public static String EXCEPTION_RESPONSE_WAIT_HANDLE(long time, String ex) {
        return localizer.localize(localizableRESPONSE_CORRELATION_CONTEXT_REMOVE(time, ex));
    }

    public static Localizable localizableEXCEPTION_FILE_CLOSE(String fileName, String ex) {
        return messageFactory.getMessage("EXCEPTION_FILE_CLOSE", fileName, ex);
    }

    /**
     * Exception while closing {0} file: {1}
     * 
     */
    public static String EXCEPTION_FILE_CLOSE(String fileName, String ex) {
        return localizer.localize(localizableEXCEPTION_FILE_CLOSE(fileName, ex));
    }

    public static Localizable localizableEXCEPTION_FILE_HANDLE(String fileName, String ex) {
        return messageFactory.getMessage("EXCEPTION_FILE_HANDLE", fileName, ex);
    }

    /**
     * Exception during {0} file handling: {1}
     * 
     */
    public static String EXCEPTION_FILE_HANDLE(String fileName, String ex) {
        return localizer.localize(localizableEXCEPTION_FILE_HANDLE(fileName, ex));
    }

    public static Localizable localizableEXCEPTION_RESOURCE_PATH_POPULATE(String path, String ex) {
        return messageFactory.getMessage("EXCEPTION_RESOURCE_PATH_POPULATE", path, ex);
    }

    /**
     * Exception while populating {0} resource: {1}
     * 
     */
    public static String EXCEPTION_RESOURCE_PATH_POPULATE(String path, String ex) {
        return localizer.localize(localizableEXCEPTION_RESOURCE_PATH_POPULATE(path, ex));
    }

    public static Localizable localizableEXCEPTION_MESSAGE_BUFFER_UNSUBSCRIBE(String uri, String ex) {
        return messageFactory.getMessage("EXCEPTION_MESSAGE_BUFFER_UNSUBSCRIBE", uri, ex);
    }

    /**
     * Exception while unsubscribing {0}: {1}
     * 
     */
    public static String EXCEPTION_MESSAGE_BUFFER_UNSUBSCRIBE(String uri, String ex) {
        return localizer.localize(localizableEXCEPTION_MESSAGE_BUFFER_UNSUBSCRIBE(uri, ex));
    }

    public static Localizable localizableEXCEPTION_EJB_CREATE(String ex) {
        return messageFactory.getMessage("EXCEPTION_EJB_CREATE", ex);
    }

    /**
     * Exception duing EJB create: {0}
     * 
     */
    public static String EXCEPTION_EJB_CREATE(String ex) {
        return localizer.localize(localizableEXCEPTION_EJB_CREATE(ex));
    }

    public static Localizable localizableEXCEPTION_SOAP_MESSAGE(String ex) {
        return messageFactory.getMessage("EXCEPTION_SOAP_MESSAGE", ex);
    }

    /**
     * Exception during parsing SOAP message: {0}
     * 
     */
    public static String EXCEPTION_SOAP_MESSAGE(String ex) {
        return localizer.localize(localizableEXCEPTION_SOAP_MESSAGE(ex));
    }

    public static Localizable localizableEXCEPTION_STREAM_CONVERSION(String ex) {
        return messageFactory.getMessage("EXCEPTION_STREAM_CONVERSION", ex);
    }

    /**
     * Exception while stream conversion: {0}
     * 
     */
    public static String EXCEPTION_STREAM_CONVERSION(String ex) {
        return localizer.localize(localizableEXCEPTION_STREAM_CONVERSION(ex));
    }

    public static Localizable localizableTANSPORT_CONNECTION_START(String uri) {
        return messageFactory.getMessage("TANSPORT_CONNECTION_START", uri);
    }

    /**
     * Client transport connection for {0} going to start
     * 
     */
    public static String TANSPORT_CONNECTION_START(String uri) {
        return localizer.localize(localizableTANSPORT_CONNECTION_START(uri));
    }

    public static Localizable localizableEXCEPTION_TRANSPORT_CONNECTION(String uri, String ex) {
        return messageFactory.getMessage("EXCEPTION_TRANSPORT_CONNECTION", uri, ex);
    }

    /**
     * Client transport connection for {0} failed: {1}
     * 
     */
    public static String EXCEPTION_TRANSPORT_CONNECTION(String uri, String ex) {
        return localizer.localize(localizableEXCEPTION_TRANSPORT_CONNECTION(uri, ex));
    }

    public static Localizable localizableEXCEPTION_HTTP_CONNECTION(String ex) {
        return messageFactory.getMessage("EXCEPTION_HTTP_CONNECTION", ex);
    }

    /**
     * Exception occured during HTTP connection
     * 
     */
    public static String EXCEPTION_HTTP_CONNECTION(String ex) {
        return localizer.localize(localizableEXCEPTION_HTTP_CONNECTION(ex));
    }

    public static Localizable localizableEXCEPTION_HTTP_STATUS(String ex) {
        return messageFactory.getMessage("EXCEPTION_HTTP_STATUS", ex);
    }

    /**
     * Unexpected HTTP status obtained {0}
     * 
     */
    public static String EXCEPTION_HTTP_STATUS(String ex) {
        return localizer.localize(localizableEXCEPTION_HTTP_STATUS(ex));
    }
}
