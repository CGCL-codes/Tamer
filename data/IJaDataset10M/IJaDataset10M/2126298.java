package be.roam.drest.service.bloglines;

import java.net.HttpURLConnection;

public class BloglinesException extends RuntimeException {

    public static final int HTTP_RESPONSE_CODE_INCORRECT_EMAIL_ADDRESS_OR_PASSWORD = 401;

    public static final int HTTP_RESPONSE_NO_ENTRIES = 304;

    public static final int HTTP_INVALID_BLOGLINES_ID = 403;

    public static final int HTTP_DELETED_SUBSCRIPTION = 410;

    /**
     * Dummy serial version UID.
     */
    private static final long serialVersionUID = 1;

    private int httpResponseCode;

    public BloglinesException(int httpResponseCode) {
        super(createMessage(httpResponseCode));
        this.httpResponseCode = httpResponseCode;
    }

    public int getHttpResponseCode() {
        return httpResponseCode;
    }

    public static BloglinesException checkForException(int httpResponseCode) {
        if (httpResponseCode == HttpURLConnection.HTTP_OK || httpResponseCode == HTTP_RESPONSE_NO_ENTRIES) {
            return null;
        }
        return new BloglinesException(httpResponseCode);
    }

    private static String createMessage(int code) {
        if (code == HTTP_RESPONSE_CODE_INCORRECT_EMAIL_ADDRESS_OR_PASSWORD) {
            return "Incorrect user and/or password";
        }
        if (code == HTTP_INVALID_BLOGLINES_ID) {
            return "Invalid Bloglines ID";
        }
        if (code == HTTP_DELETED_SUBSCRIPTION) {
            return "Subscription has been deleted";
        }
        return "Unknown exception";
    }
}
