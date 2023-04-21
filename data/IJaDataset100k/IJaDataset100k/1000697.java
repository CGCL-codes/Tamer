package net.java.sip.communicator.sip.simple;

/**
 * The class is used to convey responses to subscription authorization requests
 * from the
 * <p>Company: Network Research Team, Louis Pasteur University</p>
 * @author Emil Ivov
 */
public class SubscriptionAuthorizationResponse {

    public static final String AUTHORISATION_GRANTED = "Authorisation Granted";

    public static final String AUTHORISATION_REFUSED = "Authorisation Refused";

    public static final String[] ACCEPTED_RESPONSES = new String[] { AUTHORISATION_GRANTED, AUTHORISATION_REFUSED };

    /** @todo add advanced codes such as IGNORE, APPEAR_INVISIBLE and etc.*/
    private String responseCode = AUTHORISATION_REFUSED;

    private SubscriptionAuthorizationResponse() {
    }

    /**
     * Creates a subscription authorization response using the specified response
     * code.
     * @param responseID A string representation of the response;
     * @return a subscription authorization response created using the specified
     * response code.
     */
    public static SubscriptionAuthorizationResponse createResponse(String responseID) {
        SubscriptionAuthorizationResponse response = new SubscriptionAuthorizationResponse();
        response.responseCode = responseID;
        return response;
    }

    /**
     * Retruns the response code of this response. Response codes are used to
     * identify Authorisation responses.
     * @return the response code of this response.
     */
    public String getResponseCode() {
        return responseCode;
    }
}
