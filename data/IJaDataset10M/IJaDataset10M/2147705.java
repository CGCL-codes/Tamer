package javax.security.sasl;

import java.io.Serializable;
import javax.security.auth.callback.Callback;

/**
 * This callback is used by {@link SaslServer} to determine whether one entity
 * (identified by an authenticated authentication ID) can act on behalf of
 * another entity (identified by an authorization ID).
 *
 * @since 1.5
 */
public class AuthorizeCallback implements Callback, Serializable {

    private static final long serialVersionUID = -2353344186490470805L;

    /** @serial The (authenticated) authentication id to check. */
    private String authenticationID = null;

    /** @serial The authorization id to check. */
    private String authorizationID = null;

    /**
   * @serial The id of the authorized entity. If null, the id of the authorized
   * entity is authorizationID.
   */
    private String authorizedID = null;

    /**
   * @serial A flag indicating whether the authentication id is allowed to act
   * on behalf of the authorization id.
   */
    private boolean authorized = false;

    /**
   * Constructs an instance of <code>AuthorizeCallback</code>.
   *
   * @param authnID the (authenticated) authentication ID.
   * @param authzID the authorization ID.
   */
    public AuthorizeCallback(String authnID, String authzID) {
        super();
        this.authenticationID = authnID;
        this.authorizationID = authzID;
    }

    /**
   * Returns the authentication ID to check.
   *
   * @return the authentication ID to check
   */
    public String getAuthenticationID() {
        return authenticationID;
    }

    /**
   * Returns the authorization ID to check.
   *
   * @return the authorization ID to check.
   */
    public String getAuthorizationID() {
        return authorizationID;
    }

    /**
   * Determines if the identity represented by authentication ID is allowed to
   * act on behalf of the authorization ID.
   *
   * @return <code>true</code> if authorization is allowed; <code>false</code>
   * otherwise.
   * @see #setAuthorized(boolean)
   * @see #getAuthorizedID()
   */
    public boolean isAuthorized() {
        return authorized;
    }

    /**
   * Sets if authorization is allowed or not.
   *
   * @param authorized <code>true</code> if authorization is allowed;
   * <code>false</code> otherwise.
   * @see #isAuthorized()
   * @see #setAuthorizedID(String)
   */
    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    /**
   * Returns the ID of the authorized user.
   *
   * @return the ID of the authorized user. <code>null</code> means the
   * authorization failed.
   * @see #setAuthorized(boolean)
   * @see #setAuthorizedID(String)
   */
    public String getAuthorizedID() {
        if (!authorized) {
            return null;
        }
        return (authorizedID != null ? authorizedID : authorizationID);
    }

    /**
   * Sets the ID of the authorized entity. Called by handler only when the ID
   * is different from {@link #getAuthorizationID()}. For example, the ID might
   * need to be canonicalized for the environment in which it will be used.
   *
   * @see #setAuthorized(boolean)
   * @see #getAuthorizedID()
   */
    public void setAuthorizedID(String id) {
        this.authorizedID = id;
    }
}
