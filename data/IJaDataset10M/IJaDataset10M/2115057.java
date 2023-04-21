package com.vangent.hieos.xutil.xua.utils;

/**
 * 
 * @author Fred Aabedi
 */
public interface XUAConstants {

    public static final String STSURL_PROPERTY = "STSValidatorURL";

    public static final String SERVICEURI_PROPERTY = "STSValidatorServiceURI";

    /**
     * WS-Security name space URL
     */
    public static final String WS_SECURITY_NS_URL = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

    public static final String SOAP_ACTION_ISSUE_TOKEN = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Issue";

    /**
     * WS-Security base element name
     */
    public static final String WS_SECURITY_ELEMENT_NAME = "Security";

    /**
     * WS-Security name space prefix
     */
    public static final String WS_SECURITY_NS_PREFIX = "wsse";

    /**
     * SOAPAction to validate the token
     */
    public static final String SOAP_ACTION_VALIDATE_TOKEN = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Validate";

    /**
     * WS-Trust Token Request body template
     */
    public static final String WS_TRUST_TOKEN_REQUEST_BODY = "<wst:RequestSecurityToken xmlns:wst=\"http://docs.oasis-open.org/ws-sx/ws-trust/200512\">" + "<wst:RequestType xmlns:wst=\"http://docs.oasis-open.org/ws-sx/ws-trust/200512\">http://docs.oasis-open.org/ws-sx/ws-trust/200512/Issue</wst:RequestType>" + "<wsp:AppliesTo xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\">" + "<wsa:EndpointReference xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">" + "<wsa:Address>{SERVICE}</wsa:Address>" + "</wsa:EndpointReference>" + "</wsp:AppliesTo>" + "{CLAIMS}" + "</wst:RequestSecurityToken>";

    /**
     * WS-Trust Token Request header template
     */
    public static final String WS_TRUST_USERNAME_TOKEN_REQUEST_HEADER = "<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" + "<wsu:Timestamp xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" wsu:Id=\"Timestamp-2\">" + "<wsu:Created>{CREATEDTIME}</wsu:Created>" + "<wsu:Expires>{EXPIREDTIME}</wsu:Expires>" + "</wsu:Timestamp>" + "<wsse:UsernameToken xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" + "<wsse:Username>{USERNAME}</wsse:Username>" + "<wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">{PASSWORD}</wsse:Password>" + "</wsse:UsernameToken>" + "</wsse:Security>";

    public static final String WS_TRUST_BINARY_TOKEN_REQUEST_HEADER = "<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" + "<wsu:Timestamp xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" wsu:Id=\"Timestamp-2\">" + "<wsu:Created>{CREATEDTIME}</wsu:Created>" + "<wsu:Expires>{EXPIREDTIME}</wsu:Expires>" + "</wsu:Timestamp>" + "<wsse:BinarySecurityToken wsu:Id=\"binarytoken\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">{CERT}</wsse:BinarySecurityToken>" + "</wsse:Security>";

    public static final String WS_TRUST_TOKEN_VALIDATE_HEADER = "<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" + "<wsu:Timestamp xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" wsu:Id=\"Timestamp-2\">" + "<wsu:Created>{CREATEDTIME}</wsu:Created>" + "<wsu:Expires>{EXPIREDTIME}</wsu:Expires>" + "</wsu:Timestamp>" + "</wsse:Security>";

    /**
     * WS-Trust Token Validate Request body template
     */
    public static final String WS_TRUST_TOKEN_VALIDATE_REQUEST_BODY = "<wst:RequestSecurityToken xmlns:wst=\"http://docs.oasis-open.org/ws-sx/ws-trust/200512\">" + "<wst:TokenType>urn:oasis:names:tc:SAML:2.0:assertion</wst:TokenType>" + "<wst:RequestType xmlns:wst=\"http://docs.oasis-open.org/ws-sx/ws-trust/200512\">http://docs.oasis-open.org/ws-sx/ws-trust/200512/Validate</wst:RequestType>" + "<wst:ValidateTarget xmlns:wst=\"http://docs.oasis-open.org/ws-sx/ws-trust/200512\">{TOKEN}</wst:ValidateTarget>" + "</wst:RequestSecurityToken>";
}
