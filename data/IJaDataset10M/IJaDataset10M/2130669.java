package org.subethamail.smtp.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.subethamail.smtp.AuthenticationHandler;
import org.subethamail.smtp.AuthenticationHandlerFactory;
import org.subethamail.smtp.RejectException;
import org.subethamail.smtp.util.Base64;

/**
 * Implements the SMTP AUTH PLAIN mechanism.<br>
 * You are only required to plug your UsernamePasswordValidator implementation
 * for username and password validation to take effect.
 *
 * @author Marco Trevisan <mrctrevisan@yahoo.it>
 * @author Jeff Schnitzer
 * @author Ian White <ibwhite@gmail.com>
 */
public class PlainAuthenticationHandlerFactory implements AuthenticationHandlerFactory {

    static List<String> MECHANISMS = new ArrayList<String>(1);

    static {
        MECHANISMS.add("PLAIN");
    }

    private UsernamePasswordValidator helper;

    /** */
    public PlainAuthenticationHandlerFactory(UsernamePasswordValidator helper) {
        this.helper = helper;
    }

    /** */
    public List<String> getAuthenticationMechanisms() {
        return MECHANISMS;
    }

    /** */
    public AuthenticationHandler create() {
        return new Handler();
    }

    /**
	 */
    class Handler implements AuthenticationHandler {

        private String username;

        private String password;

        public String auth(String clientInput) throws RejectException {
            StringTokenizer stk = new StringTokenizer(clientInput);
            String secret = stk.nextToken();
            if (secret.trim().equalsIgnoreCase("AUTH")) {
                if (!stk.nextToken().trim().equalsIgnoreCase("PLAIN")) {
                    throw new RejectException(504, "AUTH mechanism mismatch");
                }
                if (stk.hasMoreTokens()) {
                    secret = stk.nextToken();
                } else {
                    return "334 Ok";
                }
            }
            byte[] decodedSecret = Base64.decode(secret);
            if (decodedSecret == null) throw new RejectException(501, "Invalid command argument, not a valid Base64 string");
            int i, j;
            for (i = 0; i < decodedSecret.length && decodedSecret[i] != 0; i++) ;
            if (i >= decodedSecret.length) {
                throw new RejectException(501, "Invalid command argument, does not contain NUL");
            }
            for (j = i + 1; j < decodedSecret.length && decodedSecret[j] != 0; j++) ;
            if (j >= decodedSecret.length) {
                throw new RejectException(501, "Invalid command argument, does not contain the second NUL");
            }
            @SuppressWarnings("unused") String authorizationId = new String(decodedSecret, 0, i);
            String authenticationId = new String(decodedSecret, i + 1, j - i - 1);
            String passwd = new String(decodedSecret, j + 1, decodedSecret.length - j - 1);
            this.username = authenticationId;
            this.password = passwd;
            try {
                PlainAuthenticationHandlerFactory.this.helper.login(this.username.toString(), this.password);
            } catch (LoginFailedException lfe) {
                throw new RejectException(535, "Authentication credentials invalid");
            }
            return null;
        }

        public Object getIdentity() {
            return this.username;
        }
    }
}
