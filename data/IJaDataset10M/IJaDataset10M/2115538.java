package br.ufmg.saotome.arangiSecurity.jaas;

import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;

/**
 * Ancestral para modulos de login.
 * @author Cesar Correia 
 */
public class AncestorLoginModule {

    protected Subject subject;

    protected CallbackHandler callbackHandler;

    protected Map sharedState;

    protected Map options;

    protected static final java.util.ResourceBundle rb = java.util.ResourceBundle.getBundle("sun.security.util.AuthResources");

    /**
     * Login do usu�rio.
     */
    protected String login;

    /**
     * Senha do usu�rio.
     */
    protected String senha;

    public AncestorLoginModule() {
        super();
    }

    protected void getUsernamePassword() throws LoginException {
        if (callbackHandler == null) throw new LoginException("Error: no CallbackHandler available " + "to garner authentication information from the user");
        String protocol = "ldap";
        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback(protocol + " " + rb.getString("username: "));
        callbacks[1] = new PasswordCallback(protocol + " " + rb.getString("password: "), false);
        try {
            callbackHandler.handle(callbacks);
            login = ((NameCallback) callbacks[0]).getName();
            char[] tmpPassword = ((PasswordCallback) callbacks[1]).getPassword();
            senha = new String(tmpPassword);
            ((PasswordCallback) callbacks[1]).clearPassword();
        } catch (java.io.IOException ioe) {
            throw new LoginException(ioe.toString());
        } catch (UnsupportedCallbackException uce) {
            throw new LoginException("Error: " + uce.getCallback().toString() + " not available to garner authentication information " + "from the user");
        }
    }
}
