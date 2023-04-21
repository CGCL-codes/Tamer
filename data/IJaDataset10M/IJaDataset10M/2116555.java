package com.google.gdata.client.authn.oauthproxy;

/**
 * Defines various constants used by the OAuth Proxy.
 *
 * 
 */
public class OAuthProxyProtocol {

    private OAuthProxyProtocol() {
    }

    /** Defines header keys used by the OAuth Proxy. */
    public interface Header {

        public static final String X_OAUTH_APPROVAL_URL = "x_oauth_approval_url";

        public static final String X_OAUTH_ACCESS_TOKEN_URL = "x_oauth_access_token_url";

        public static final String X_OAUTH_AUTHORIZATION_URL = "x_oauth_authorization_url";

        public static final String X_OAUTH_DESIRED_CALLBACK_URL = "x_oauth_desired_callback_url";

        public static final String X_OAUTH_ERROR = "x_oauth_error";

        public static final String X_OAUTH_ERROR_TEXT = "x_oauth_error_text";

        public static final String X_OAUTH_RECEIVED_CALLBACK_URL = "x_oauth_received_callback_url";

        public static final String X_OAUTH_REQUEST_TOKEN_URL = "x_oauth_request_token_url";

        public static final String X_OAUTH_SERVICE_NAME = "x_oauth_service_name";

        public static final String X_OAUTH_STATE = "x_oauth_state";

        public static final String X_OAUTH_TOKEN_NAME = "x_oauth_token_name";

        public static final String X_OAUTH_USE_TOKEN = "x_oauth_use_token";
    }
}
