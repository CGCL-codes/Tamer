package org.oauth4j.util;

import com.sun.jersey.oauth.signature.OAuthParameters;

/**
 * Some constant stuffs are here!!
 * 
 * @author jasphior
 * 
 */
public class OAuth4JConstants {

    public static final String PARAMETER_DELIMITER = "&";

    public static final String KEY_VAL_DELIMITER = "=";

    public static final String AUTH_URL = "xoauth_request_auth_url";

    public static final String CONSUMER_SECRET = "oauth_consumer_secret";

    public static final String OUT_OF_BAND = "oob";

    public static final String SESSION_HANDLE = "oauth_session_handle";

    public static final String OAUTH_EXPIRY = "oauth_expires_in";

    public static final String OAUTH_AUTH_EXPIRY = "oauth_authorization_expires_in";

    public static final String TOKEN = OAuthParameters.TOKEN;

    public static final String VERIFIER = OAuthParameters.VERIFIER;

    public static final String ACCESS_TOKEN = "access_token";

    public static final String CLIENT_API_ID = "clientId";

    public static final String CLIENT_API_KEY = "clientKey";

    public static final String[] SESSION_PARAMS = new String[] { SESSION_HANDLE, YAHOO.GUID };

    class YAHOO {

        public static final String GUID = "xoauth_yahoo_guid";
    }
}
