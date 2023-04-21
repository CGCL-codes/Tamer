package net.esle.sinadura.core.firma;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * @author zylk.net
 */
public class CallbackHandlerPKCS11 implements CallbackHandler {

    private String password = null;

    /**
	 * @param password
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback c : callbacks) {
            if (c instanceof PasswordCallback) {
                PasswordCallback pc = (PasswordCallback) c;
                char[] pin = this.password.toCharArray();
                pc.setPassword(pin);
            }
        }
    }
}
