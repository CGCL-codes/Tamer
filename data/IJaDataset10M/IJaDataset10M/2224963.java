package org.isurf.cpfr.security;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SecurityManagerProxy {

    private static final String DEFAULT_INITIAL_CONTEXT_FACTORY = "org.jnp.interfaces.NamingContextFactory";

    private static final String DEFAULT_URL_PKG_PREFIXES = "org.jnp.interfaces";

    private static final String DEFAULT_REPOSITORY_JNDI_NAME = "CPFR/SecurityManager/local";

    private static ISecurityManager iSecMgr;

    protected InitialContext getInitialContextFromProxyProperties() throws NamingException {
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, DEFAULT_INITIAL_CONTEXT_FACTORY);
        properties.put(Context.URL_PKG_PREFIXES, DEFAULT_URL_PKG_PREFIXES);
        InitialContext ctx = new InitialContext(properties);
        return ctx;
    }

    public synchronized ISecurityManager getSecurityManager() throws Exception {
        if (iSecMgr == null) {
            try {
                InitialContext ctx = getInitialContextFromProxyProperties();
                iSecMgr = (ISecurityManager) ctx.lookup(DEFAULT_REPOSITORY_JNDI_NAME);
            } catch (NamingException e) {
                throw new Exception("Error occured while attempting to access SecurityManager!", e);
            }
        }
        return iSecMgr;
    }
}
