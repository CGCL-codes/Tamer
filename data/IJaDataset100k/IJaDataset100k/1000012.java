package org.antdepo.authorization;

import org.antdepo.common.Framework;
import org.antdepo.jndi.Jndi;
import org.antdepo.jndi.JndiConfig;
import org.antdepo.utils.PropertyLookup;
import org.apache.log4j.Logger;
import javax.naming.NamingException;
import java.io.File;
import java.io.IOException;

/**
 * JndiAuthorization, Provides basic authentication using Acls.xml and JNDI to lookup user roles.
 *
 * @author Chuck Scott <a href="mailto:chuck@controltier.com">chuck@controltier.com</a>
 */
public class JndiAuthorization extends BaseAclsAuthorization implements Authorization {

    static final String JNDI_PROPFILE = "jndi.properties";

    /**
     * reference to Jndi
     */
    private final Jndi jndi;

    /**
     * Default constructor
     *
     * @param framework
     * @param aclBaseDir
     * @throws IOException
     */
    public JndiAuthorization(final Framework framework, final File aclBaseDir) throws IOException {
        this(framework, new File(framework.getConfigDir(), JNDI_PROPFILE), aclBaseDir);
    }

    /**
     * Default constructor
     *
     * @param configFile
     * @param aclBaseDir
     * @throws IOException
     */
    public JndiAuthorization(final Framework framework, final File configFile, final File aclBaseDir) throws IOException {
        super(framework, aclBaseDir);
        final PropertyLookup lookup = PropertyLookup.create(configFile);
        final JndiConfigParser cp = new JndiConfigParser(lookup);
        final JndiConfig cfg = cp.parse();
        try {
            logger.debug("Connecting to JNDI Server: " + cfg.getConnectionUrl());
            jndi = new Jndi(cfg);
        } catch (NamingException e) {
            throw new AuthorizationException("Caught NameNotFoundException, error: " + e.getMessage() + ", Unable to connect to JNDI Server: " + cfg.getConnectionUrl() + " with connectionName: " + cfg.getConnectionName());
        }
        logger.debug(toString());
    }

    /**
     * reference to logger object
     */
    static Logger logger = Logger.getLogger(JndiAuthorization.class.getName());

    public String[] determineUserRoles(String user) {
        String[] roles;
        try {
            logger.debug("obtaining list of roles for user: " + user);
            roles = jndi.getRoles(user);
        } catch (NamingException e) {
            logger.error("Unable to obtain role memberships for user: " + user);
            throw new AuthorizationException("Caught NamingException, error: " + e.getMessage() + ", Unable to obtain role memberships for user: " + user);
        }
        return roles;
    }

    /**
     * return a string representation of this object
     *
     * @return String
     */
    public String toString() {
        return "JndiAuthorization{" + "aclBasedir=" + getAclBasedir() + "}";
    }
}
