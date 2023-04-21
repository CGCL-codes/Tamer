package com.googlecode.janrain4j.api.engage;

/**
 * <code>ErrorResponeException</code> is thrown when the Janrain Engage API 
 * returns an error response.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ErrorResponeException extends Exception {

    public static final int SERVICE_TEMPORARY_UNAVAILABLE_ERROR = -1;

    public static final int MISSING_PARAMETER_ERROR = 0;

    public static final int INVALID_PARAMETER_ERROR = 1;

    public static final int DATA_NOT_FOUND_ERROR = 2;

    public static final int AUTHENTICATION_ERROR = 3;

    public static final int FACEBOOK_ERROR = 4;

    public static final int MAPPING_EXISTS_ERROR = 5;

    public static final int INTERACTING_WITH_PREVIOUSLY_OPERATIONAL_PROVIDER_ERROR = 6;

    public static final int ENGAGE_ACCOUNT_UPGRADE_NEEDED_ERROR = 7;

    public static final int MISSING_THIRD_PARTY_CREDENTIALS_ERROR = 8;

    public static final int THIRD_PARTY_CREDENTIALS_REVOKED_ERROR = 9;

    public static final int APPLICATION_NOT_PROPERLY_CONFIGURED_ERROR = 10;

    public static final int FEATURE_NOT_SUPPORTED_ERROR = 11;

    public static final int GOOGLE_ERROR = 12;

    public static final int TWITTER_ERROR = 13;

    public static final int LINKED_IN_ERROR = 14;

    public static final int LIVE_ID_ERROR = 15;

    public static final int MY_SPACE_ERROR = 16;

    public static final int YAHOO_ERROR = 17;

    private int code;

    private String jsonResponse = null;

    public ErrorResponeException(int code, String message, String jsonResponse) {
        super(message);
        this.code = code;
        this.jsonResponse = jsonResponse;
    }

    /**
     * Return the Janrain Engage error code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Returns the JSON response if available, otherwise <code>null</code>.
     */
    public String getJsonResponse() {
        return jsonResponse;
    }
}
