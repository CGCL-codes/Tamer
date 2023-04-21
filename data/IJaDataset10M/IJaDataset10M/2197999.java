package cgl.shindig;

import java.io.File;
import java.io.IOException;
import javax.security.auth.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cgl.shindig.config.ConfigurationException;
import cgl.shindig.config.PortalConfig;
import cgl.shindig.security.AuthContext;
import cgl.shindig.security.Credentials;
import cgl.shindig.security.DefaultSecurityManager;
import cgl.shindig.usermanage.UserManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Portal {

    private static final Logger log = LoggerFactory.getLogger(Portal.class);

    public boolean initialized = false;

    private PortalConfig portalConfig;

    private DefaultSecurityManager securityManager;

    private AuthContext authContext;

    private UserManager userManager;

    public void setSecurityManager(DefaultSecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    @Inject
    public Portal() throws ConfigurationException, IOException, Exception {
        String dir = new File(".").getCanonicalPath();
        portalConfig = PortalConfig.install(new File(dir));
        securityManager = new DefaultSecurityManager();
        securityManager.init(this);
        userManager = securityManager.getUserManager();
    }

    public PortalConfig getConfig() {
        return portalConfig;
    }

    public DefaultSecurityManager getSecurityManager() {
        return this.securityManager;
    }

    public boolean login(Credentials creds, Subject subject) {
        try {
            if (creds == null) {
                log.error("Passed credentials is null.");
                return false;
            }
            if (subject == null) subject = new Subject();
            AuthContext authContext = securityManager.getAuthContext(creds, userManager, subject);
            authContext.login();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * get the value of authContext
     * @return the value of authContext
     */
    public AuthContext getAuthContext() {
        return this.authContext;
    }

    public UserManager getUserManager() {
        return this.userManager;
    }
}
