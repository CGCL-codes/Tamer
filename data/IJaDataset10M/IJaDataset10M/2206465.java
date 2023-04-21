package flex.messaging.io;

/**
 * @exclude
 */
public interface MessageIOConstants {

    int AMF0 = 0;

    int AMF1 = 1;

    int AMF3 = 3;

    Double AMF3_INFO_PROPERTY = new Double(3);

    String CONTENT_TYPE_XML = "text/xml; charset=utf-8";

    String AMF_CONTENT_TYPE = "application/x-amf";

    String XML_CONTENT_TYPE = "application/xml";

    String RESULT_METHOD = "/onResult";

    String STATUS_METHOD = "/onStatus";

    int STATUS_OK = 0;

    int STATUS_ERR = 1;

    int STATUS_NOTAMF = 2;

    String SECURITY_HEADER_NAME = "Credentials";

    String SECURITY_PRINCIPAL = "userid";

    String SECURITY_CREDENTIALS = "password";

    String URL_APPEND_HEADER = "AppendToGatewayUrl";

    String SERVICE_TYPE_HEADER = "ServiceType";

    String REMOTE_CLASS_FIELD = "_remoteClass";

    String SUPPORT_REMOTE_CLASS = "SupportRemoteClass";

    String SUPPORT_DATES_BY_REFERENCE = "SupportDatesByReference";

    String METHOD_POST = "POST";

    String HEADER_SOAP_ACTION = "SOAPAction";
}
