package org.objectweb.petals.usecase.soapsecurity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.ws.security.WSPasswordCallback;

/**
 * 
 * @author ofabre - eBMWebsourcing
 *
 */
public class UserPasswordHandler implements CallbackHandler {

    private Properties userPasswords = new Properties();

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        init();
        for (int i = 0; i < callbacks.length; i++) {
            WSPasswordCallback pwcb = (WSPasswordCallback) callbacks[i];
            String id = pwcb.getIdentifer();
            String clearPassword = userPasswords.getProperty(id);
            if (clearPassword != null) {
                pwcb.setPassword(clearPassword);
            }
        }
    }

    private void init() throws FileNotFoundException, IOException {
        File userPasswordFile = new File(System.getenv("PETALS_HOME"), "conf" + File.separator + "security" + File.separator + "users.properties");
        userPasswords.load(new java.io.FileInputStream(userPasswordFile));
    }
}
