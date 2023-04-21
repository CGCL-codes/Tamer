package de.sicari.kernel.security;

import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import de.fhg.igd.logging.LogLevel;
import de.fhg.igd.logging.Logger;
import de.fhg.igd.logging.LoggerFactory;

/**
 * This is one of the core components of the <i>JAAS</i> (Java Authentication
 * and Authorization Service) extension. It performs the actual user login and
 * logout on the <i>SicAri</i> platform by loading a local <code>KeyStore</code>
 * from a <tt>PKCS11</tt> based <i>Smart Card</i>.
 * <p>
 * This <code>LoginModule</code> recognized the following configuration
 * options defined in the <i>login configuration file</i>:
 * <ul>
 * <li><tt>keymaster.conf="&lt;path-to-keymaster-configuration-file&gt;"</tt>
 * </ul>
 *
 * @author Matthias Pressfreund
 * @version $Id$
 */
public class PKCS11LoginModule extends AbstractLoginModule {

    /**
     * The <code>Logger</code> instance for this class
     */
    private static Logger log_ = LoggerFactory.getLogger("sicari/authentication");

    /**
     * Create a <code>PKCS11LoginModule</code>.
     */
    public PKCS11LoginModule() {
        super();
        logId_ = "PKCS11-Login";
        log_.debug("Instance successfully created");
    }

    @Override
    public UID authenticate() throws LoginException {
        PrivateKeyMaster pkm;
        UID uid;
        uid = requestLoginName();
        if (!getIdentityManager().certificates(uid).hasNext()) {
            log_.debug("No certificates associated to user " + uid.getName() + ", skipping ...");
            throw new LoginException("Skipping");
        }
        try {
            pkm = new PrivateKeyMaster(uid, options_.get(OPTION_KEYMASTER_CONFIG_), null, null);
            sharedState_.put(SecurityTokenAttributes.AUTHENTICATION_METHOD, SecurityTokenAttributes.AM_SMART_CARD);
            sharedState_.put(SHARED_STATE_KEY_KEYMASTER, pkm);
            return uid;
        } catch (CertificateException e) {
            log_.caught(LogLevel.DEBUG, "Certificate does not match", e);
            System.out.println("[" + logId_ + "] " + e.getMessage());
            throw new FailedLoginException(e.toString());
        } catch (UnrecoverableKeyException e) {
            log_.caught(LogLevel.DEBUG, "Invalid password", e);
            System.out.println("[" + logId_ + "] Invalid password");
            throw new FailedLoginException(e.toString());
        } catch (Exception e) {
            log_.caught(LogLevel.DEBUG, "Creating SmartCard access failed", e);
            throw new LoginException("SmartCard not accessible");
        }
    }
}
