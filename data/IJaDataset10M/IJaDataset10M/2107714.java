package com.eaio.geoscope;

import com.eaio.lol.exceptions.LoException;

/**
 * Instances of this class are thrown from configurable Objects.
 * 
 * @see com.eaio.geoscope.configuration.RegistryProvider
 * @see com.eaio.lol.exceptions.LoException 
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version $Id: GeoScopeException.java,v 1.7 2005/12/21 20:13:14 grnull Exp $
 */
public class GeoScopeException extends LoException {

    /**
     * The serial version UID.
     */
    static final long serialVersionUID = 3257281414054754610L;

    /**
     * Constructor for GeoScopeException.
     */
    public GeoScopeException() {
        super();
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param message
     */
    public GeoScopeException(String message) {
        super(message);
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param message
     * @param cause
     */
    public GeoScopeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param cause
     */
    public GeoScopeException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param messageID
     */
    public GeoScopeException(int messageID) {
        super(messageID);
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param messageID
     * @param cause
     */
    public GeoScopeException(int messageID, Throwable cause) {
        super(messageID, cause);
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param messageID
     * @param params
     * @param cause
     */
    public GeoScopeException(int messageID, Object[] params, Throwable cause) {
        super(messageID, params, cause);
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param complex
     * @param messageID
     */
    public GeoScopeException(Object complex, int messageID) {
        super(complex, messageID);
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param complex
     * @param messageID
     * @param cause
     */
    public GeoScopeException(Object complex, int messageID, Throwable cause) {
        super(complex, messageID, cause);
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param complex
     * @param messageID
     */
    public GeoScopeException(Object complex, String messageID) {
        super(complex, messageID);
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param complex
     * @param messageID
     * @param cause
     */
    public GeoScopeException(Object complex, String messageID, Throwable cause) {
        super(complex, messageID, cause);
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param bundleName
     * @param messageID
     */
    public GeoScopeException(String bundleName, String messageID) {
        super(bundleName, messageID);
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param bundleName
     * @param messageID
     * @param cause
     */
    public GeoScopeException(String bundleName, String messageID, Throwable cause) {
        super(bundleName, messageID, cause);
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param messageID
     * @param params
     */
    public GeoScopeException(int messageID, Object[] params) {
        super(messageID, params);
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param messageID
     * @param params
     */
    public GeoScopeException(String messageID, Object[] params) {
        super(messageID, params);
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param messageID
     * @param params
     * @param cause
     */
    public GeoScopeException(String messageID, Object[] params, Throwable cause) {
        super(messageID, params, cause);
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param complex
     * @param messageID
     * @param params
     */
    public GeoScopeException(Object complex, int messageID, Object[] params) {
        super(complex, messageID, params);
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param complex
     * @param messageID
     * @param params
     * @param cause
     */
    public GeoScopeException(Object complex, int messageID, Object[] params, Throwable cause) {
        super(complex, messageID, params, cause);
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param complex
     * @param messageID
     * @param params
     */
    public GeoScopeException(Object complex, String messageID, Object[] params) {
        super(complex, messageID, params);
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param complex
     * @param messageID
     * @param params
     * @param cause
     */
    public GeoScopeException(Object complex, String messageID, Object[] params, Throwable cause) {
        super(complex, messageID, params, cause);
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param bundleName
     * @param messageID
     * @param params
     */
    public GeoScopeException(String bundleName, String messageID, Object[] params) {
        super(bundleName, messageID, params);
    }

    /**
     * Constructor for GeoScopeException.
     * 
     * @param bundleName
     * @param messageID
     * @param params
     * @param cause
     */
    public GeoScopeException(String bundleName, String messageID, Object[] params, Throwable cause) {
        super(bundleName, messageID, params, cause);
    }
}
