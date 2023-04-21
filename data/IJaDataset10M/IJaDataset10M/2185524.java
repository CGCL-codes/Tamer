package org.apache.harmony.javax.security.sasl;

import java.io.Serializable;
import org.apache.harmony.javax.security.auth.callback.Callback;

public class AuthorizeCallback implements Callback, Serializable {

    private static final long serialVersionUID = -2353344186490470805L;

    /**
     * Serialized field for storing authenticationID.
     */
    private final String authenticationID;

    /**
     * Serialized field for storing authorizationID.
     */
    private final String authorizationID;

    /**
     * Serialized field for storing authorizedID.
     */
    private String authorizedID;

    /**
     * Store authorized Serialized field.
     */
    private boolean authorized;

    public AuthorizeCallback(String authnID, String authzID) {
        super();
        authenticationID = authnID;
        authorizationID = authzID;
        authorizedID = authzID;
    }

    public String getAuthenticationID() {
        return authenticationID;
    }

    public String getAuthorizationID() {
        return authorizationID;
    }

    public String getAuthorizedID() {
        return (authorized ? authorizedID : null);
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean ok) {
        authorized = ok;
    }

    public void setAuthorizedID(String id) {
        if (id != null) {
            authorizedID = id;
        }
    }
}
