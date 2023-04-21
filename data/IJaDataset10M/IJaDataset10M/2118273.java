package org.ldaptive.auth;

import java.util.Arrays;
import org.ldaptive.Credential;

/**
 * Contains the data required to perform an ldap authentication.
 *
 * @author  Middleware Services
 * @version  $Revision: 2352 $ $Date: 2012-04-11 10:00:33 -0400 (Wed, 11 Apr 2012) $
 */
public class AuthenticationRequest {

    /** User identifier. */
    private String user;

    /** User credential. */
    private Credential credential;

    /** User attributes to return. */
    private String[] retAttrs = new String[0];

    /** Default constructor. */
    public AuthenticationRequest() {
    }

    /**
   * Creates a new authentication request.
   *
   * @param  id  that identifies the user
   * @param  c  credential to authenticate the user
   */
    public AuthenticationRequest(final String id, final Credential c) {
        setUser(id);
        setCredential(c);
    }

    /**
   * Creates a new authentication request.
   *
   * @param  id  that identifies the user
   * @param  c  credential to authenticate the user
   * @param  attrs  attributes to return
   */
    public AuthenticationRequest(final String id, final Credential c, final String... attrs) {
        setUser(id);
        setCredential(c);
        setReturnAttributes(attrs);
    }

    /**
   * Returns the user.
   *
   * @return  user identifier
   */
    public String getUser() {
        return user;
    }

    /**
   * Sets the user.
   *
   * @param  id  of the user
   */
    public void setUser(final String id) {
        user = id;
    }

    /**
   * Returns the credential.
   *
   * @return  user credential
   */
    public Credential getCredential() {
        return credential;
    }

    /**
   * Sets the credential.
   *
   * @param  c  user credential
   */
    public void setCredential(final Credential c) {
        credential = c;
    }

    /**
   * Returns the return attributes.
   *
   * @return  attributes to return
   */
    public String[] getReturnAttributes() {
        return retAttrs;
    }

    /**
   * Sets the return attributes.
   *
   * @param  attrs  return attributes
   */
    public void setReturnAttributes(final String... attrs) {
        retAttrs = attrs;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format("[%s@%d::user=%s, retAttrs=%s]", getClass().getName(), hashCode(), user, Arrays.toString(retAttrs));
    }
}
