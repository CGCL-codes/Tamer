package org.acegisecurity.providers.jaas;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

/**
 * An implementation of {@link LoginModule} that uses an Acegi Security
 * {@link org.acegisecurity.context.SecurityContext SecurityContext}
 * to provide authentication. <br />
 * This LoginModule provides opposite functionality to the {@link
 * JaasAuthenticationProvider} API, and should not really be used in
 * conjunction with it. <br />
 * The {@link JaasAuthenticationProvider} allows Acegi to authenticate against
 * Jaas. <br />
 * The SecurityContextLoginModule allows a Jaas based application to
 * authenticate against Acegi. If there is no Authentication in the {@link
 * SecurityContextHolder} the login() method will throw a LoginException by
 * default. This functionality can be changed with the
 * <tt>ignoreMissingAuthentication</tt> option by setting it to "true".
 * Setting  ignoreMissingAuthentication=true will tell the
 * SecurityContextLoginModule to simply return false and be ignored if the
 * authentication is null.
 *
 * @author Brian Moseley
 * @author Ray Krueger
 */
public class SecurityContextLoginModule implements LoginModule {

    private static final Log log = LogFactory.getLog(SecurityContextLoginModule.class);

    private Authentication authen;

    private Subject subject;

    private boolean ignoreMissingAuthentication = false;

    /**
     * Abort the authentication process by forgetting the Acegi Security
     * <code>Authentication</code>.
     *
     * @return true if this method succeeded, or false if this
     *         <code>LoginModule</code> should be ignored.
     *
     * @exception LoginException if the abort fails
     */
    public boolean abort() throws LoginException {
        if (authen == null) {
            return false;
        }
        authen = null;
        return true;
    }

    /**
     * Authenticate the <code>Subject</code> (phase two) by adding the Acegi
     * Security <code>Authentication</code> to the <code>Subject</code>'s
     * principals.
     *
     * @return true if this method succeeded, or false if this
     *         <code>LoginModule</code> should be ignored.
     *
     * @exception LoginException if the commit fails
     */
    public boolean commit() throws LoginException {
        if (authen == null) {
            return false;
        }
        subject.getPrincipals().add(authen);
        return true;
    }

    /**
     * Initialize this <code>LoginModule</code>. Ignores the callback handler,
     * since the code establishing the <code>LoginContext</code> likely won't
     * provide one that understands Acegi Security. Also ignores the
     * <code>sharedState</code> and <code>options</code> parameters, since
     * none are recognized.
     *
     * @param subject the <code>Subject</code> to be authenticated. <p>
     * @param callbackHandler is ignored
     * @param sharedState is ignored
     * @param options are ignored
     */
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
        this.subject = subject;
        if (options != null) {
            ignoreMissingAuthentication = "true".equals(options.get("ignoreMissingAuthentication"));
        }
    }

    /**
     * Authenticate the <code>Subject</code> (phase one) by extracting the
     * Acegi Security <code>Authentication</code> from the current
     * <code>SecurityContext</code>.
     *
     * @return true if the authentication succeeded, or false if this
     *         <code>LoginModule</code> should be ignored.
     *
     * @throws LoginException if the authentication fails
     */
    public boolean login() throws LoginException {
        authen = SecurityContextHolder.getContext().getAuthentication();
        if (authen == null) {
            String msg = "Login cannot complete, authentication not found in security context";
            if (ignoreMissingAuthentication) {
                log.warn(msg);
                return false;
            } else {
                throw new LoginException(msg);
            }
        }
        return true;
    }

    /**
     * Log out the <code>Subject</code>.
     *
     * @return true if this method succeeded, or false if this
     *         <code>LoginModule</code> should be ignored.
     *
     * @exception LoginException if the logout fails
     */
    public boolean logout() throws LoginException {
        if (authen == null) {
            return false;
        }
        subject.getPrincipals().remove(authen);
        authen = null;
        return true;
    }

    Authentication getAuthentication() {
        return authen;
    }

    Subject getSubject() {
        return subject;
    }
}
