package org.zend.webapi.core.connection.auth;

/**
 * Provides access to the Web API credentials used for accessing Zend Server
 * services: key name and secret access key. These credentials are used to
 * securely sign requests to Web API services.
 * 
 * API requests authentication is done by creating a digital signature of some
 * request specific parameters using an account-specific secret key, and sending
 * this signature, as well as the key name, in the custom X-Zend-Signature HTTP
 * header.
 * 
 * @author Roy, 2011
 * 
 */
public interface WebApiCredentials {

    /**
	 * @return
	 */
    public abstract String getKeyName();

    /**
	 * @return
	 */
    public abstract String getSecretKey();
}
