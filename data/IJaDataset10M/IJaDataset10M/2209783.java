package org.apache.rampart.handler;

/**
 * Constants specific to the Axis2 security module
 */
public class WSSHandlerConstants {

    private WSSHandlerConstants() {
    }

    /**
     * Name of the .mar file
     */
    public static final String SECURITY_MODULE_NAME = "rampart";

    /**
     * Inflow security parameter
     */
    public static final String INFLOW_SECURITY = "InflowSecurity";

    public static final String INFLOW_SECURITY_SERVER = "InflowSecurity-server";

    public static final String INFLOW_SECURITY_CLIENT = "InflowSecurity-client";

    /**
     * Outflow security parameter 
     */
    public static final String OUTFLOW_SECURITY = "OutflowSecurity";

    public static final String OUTFLOW_SECURITY_SERVER = "OutflowSecurity-server";

    public static final String OUTFLOW_SECURITY_CLIENT = "OutflowSecurity-client";

    /**
     * Inflow security parameter of a client to talk to an STS 
     * when sec conv is used
     */
    public static final String STS_INFLOW_SECURITY = "STSInflowSecurity";

    /**
     * Outflow security parameter of a client to talk to an STS 
     * when sec conv is used
     */
    public static final String STS_OUTFLOW_SECURITY = "STSOutflowSecurity";

    public static final String ACTION = "action";

    public static final String ACTION_ITEMS = "items";

    /**
     *  Repetition count
     */
    public static final String SENDER_REPEAT_COUNT = "senderRepeatCount";

    /**
	 * The current repetition
	 */
    public static final String CURRENT_REPETITON = "currentRepetition";

    /**
	 * This is used to indicate the XPath expression used to indicate the
	 * Elements whose first child (must be a text node) is to be optimized  
	 */
    public static final String OPTIMIZE_PARTS = "optimizeParts";

    public static final String PRESERVE_ORIGINAL_ENV = "preserveOriginalEnvelope";

    public static final String BST_DIRECT_REFERENCE = "DirectReference";

    public static final String ISSUER_SERIAL = "IssuerSerial";

    public static final String X509_KEY_IDENTIFIER = "X509KeyIdentifier";

    public static final String SKI_KEY_IDENTIFIER = "SKIKeyIdentifier";

    public static final String EMBEDDED_KEYNAME = "EmbeddedKeyName";

    public static final String THUMBPRINT_IDENTIFIER = "Thumbprint";

    public static final String SIGN_ALL_HEADERS = "signAllHeaders";

    public static final String SIGN_BODY = "signBody";

    public static final String ENCRYPT_BODY = "encryptBody";

    /**
     * Key to be used to set a flag in msg ctx to enable/disable using doom
     */
    public static final String USE_DOOM = "useDoom";

    /**
     * Key to hold the map of security context identifiers against the 
     * service epr addresses (service scope) or wsa:Action values (operation 
     * scope).
     */
    public static final String CONTEXT_MAP_KEY = "contextMap";

    /**
     * The <code>java.util.Properties</code> object holding the properties 
     * of a <code>org.apache.ws.security.components.crypto.Crypto</code> impl.
     * 
     * This should ONLY be used when the CRYPTO_CLASS_KEY is specified.
     * 
     * @see org.apache.ws.security.components.crypto.Crypto
     */
    public static final String CRYPTO_PROPERTIES_KEY = "cryptoPropertiesRef";

    /**
     * The class that implements 
     * <code>org.apache.ws.security.components.crypto.Crypto</code>.
     */
    public static final String CRYPTO_CLASS_KEY = "cryptoClass";

    public static final String RST_ACTON_SCT = "http://schemas.xmlsoap.org/ws/2005/02/trust/RST/SCT";

    public static final String RSTR_ACTON_SCT = "http://schemas.xmlsoap.org/ws/2005/02/trust/RSTR/SCT";

    public static final String RSTR_ACTON_ISSUE = "http://schemas.xmlsoap.org/ws/2005/02/trust/RSTR/Issue";

    public static final String TOK_TYPE_SCT = "http://schemas.xmlsoap.org/ws/2005/02/sc/sct";

    public static final String WST_NS = "http://schemas.xmlsoap.org/ws/2005/02/trust";

    public static final String REQUEST_SECURITY_TOKEN_RESPONSE_LN = "RequestSecurityTokenResponse";
}
