package com.liferay.portal.auth;

import java.io.Serializable;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;

/**
 * <a href="PortalCallbackHandler.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.10 $
 *
 */
public class PortalCallbackHandler implements CallbackHandler, Serializable {

    public PortalCallbackHandler(String userId, String password) {
        _userId = userId;
        _password = password;
    }

    public void handle(Callback[] callbacks) {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof NameCallback) {
                NameCallback nameCallback = (NameCallback) callbacks[i];
                nameCallback.setName(_userId);
            } else if (callbacks[i] instanceof PasswordCallback) {
                PasswordCallback passCallback = (PasswordCallback) callbacks[i];
                passCallback.setPassword(_password.toCharArray());
            }
        }
    }

    private String _userId;

    private String _password;
}
