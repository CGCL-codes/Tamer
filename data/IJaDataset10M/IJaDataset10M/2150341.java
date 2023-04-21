package org.verus.ngl.client.main.security.inbuilt;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.verus.ngl.client.logging.NGLLogging;

/**
 *
 * @author root
 */
public class NGLCallbackHandler implements CallbackHandler {

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[0] instanceof NGLLogin) {
                NGLLogging.getInstance().getLogger().finest("Entered the call back");
                NGLLogin login = (NGLLogin) callbacks[0];
                login.setName("Siddartha");
            }
        }
    }
}
