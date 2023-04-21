package org.apache.ws.axis.oasis;

import org.apache.ws.security.WSPasswordCallback;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

/**
 * Class PWCallback
 */
public class PWCallback1 implements CallbackHandler {

    /** Field key */
    private static final byte[] key = { (byte) 0x31, (byte) 0xfd, (byte) 0xcb, (byte) 0xda, (byte) 0xfb, (byte) 0xcd, (byte) 0x6b, (byte) 0xa8, (byte) 0xe6, (byte) 0x19, (byte) 0xa7, (byte) 0xbf, (byte) 0x51, (byte) 0xf7, (byte) 0xc7, (byte) 0x3e, (byte) 0x80, (byte) 0xae, (byte) 0x98, (byte) 0x51, (byte) 0xc8, (byte) 0x51, (byte) 0x34, (byte) 0x04 };

    /**
     * Method handle
     * 
     * @param callbacks 
     * @throws java.io.IOException                  
     * @throws javax.security.auth.callback.UnsupportedCallbackException 
     */
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof WSPasswordCallback) {
                WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
                if (pc.getUsage() == WSPasswordCallback.USERNAME_TOKEN_UNKNOWN) {
                    if (pc.getIdentifier().equals("Ron") && pc.getPassword().equals("noR")) {
                        return;
                    }
                    if (pc.getPassword().equals("sirhC")) {
                        return;
                    }
                    throw new UnsupportedCallbackException(callbacks[i], "check failed");
                }
                if (pc.getUsage() == WSPasswordCallback.KEY_NAME) {
                    pc.setKey(key);
                } else if (pc.getIdentifier().equals("alice")) {
                    pc.setPassword("password");
                } else if (pc.getIdentifier().equals("bob")) {
                    pc.setPassword("password");
                } else if (pc.getIdentifier().equals("Ron")) {
                    pc.setPassword("noR");
                } else {
                    pc.setPassword("sirhC");
                }
            } else {
                throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
            }
        }
    }
}
