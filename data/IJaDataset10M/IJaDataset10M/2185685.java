package org.xaware.server.security;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * A simple implementation of CallbackHandler that sets a username and password in the handle(Callback[]) method to that
 * passed in to the constructor. This is suitable for environments that need non-interactive JAAS logins.
 * 
 * @see javax.security.auth.callback.CallbackHandler
 * @see #handle(Callback[])
 */
public class UsernamePasswordHandler implements CallbackHandler {

    private transient String username;

    private transient char[] password;

    /**
     * Initialize the UsernamePasswordHandler with the usernmae and password to use.
     */
    public UsernamePasswordHandler(final String username, final char[] password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Sets any NameCallback name property to the instance username and sets any PasswordCallback password property to
     * the instance password.
     * 
     * @exception UnsupportedCallbackException,
     *                thrown if any callback of type other than NameCallback or PasswordCallback are seen.
     */
    public void handle(final Callback[] callbacks) throws UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof NameCallback) {
                final NameCallback nc = (NameCallback) callbacks[i];
                nc.setName(username);
            } else if (callbacks[i] instanceof PasswordCallback) {
                final PasswordCallback pc = (PasswordCallback) callbacks[i];
                pc.setPassword(password);
            } else {
                throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
            }
        }
    }
}
