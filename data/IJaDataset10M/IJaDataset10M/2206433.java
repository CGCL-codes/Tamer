package net.community.chest.net.ldap;

import java.io.IOException;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.ContextNotEmptyException;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.InvalidAttributeValueException;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Jul 20, 2008 12:50:35 PM
 */
public abstract class AbstractLDAPAccessorHelper extends AbstractLDAPAccessor {

    /**
	 * LDAP access initialization properties 
	 */
    private final Properties _env = new Properties();

    public Properties getContextEnvironment() {
        return _env;
    }

    protected AbstractLDAPAccessorHelper() {
        super();
    }

    private DirContext _context;

    protected DirContext getContext() {
        return _context;
    }

    protected long updateEnvironmentIntParam(final String propName, final String propVal) throws NamingException {
        if (getContext() != null) throw new ContextNotEmptyException("updateEnvironmentIntParam(" + propName + ")[" + propVal + "] context already initialized");
        try {
            if ((null == propName) || (propName.length() <= 0) || (null == propVal) || (propVal.length() <= 0)) throw new NumberFormatException("Missing property name/value");
            final long retVal = Long.parseLong(propVal);
            final Properties env = getContextEnvironment();
            env.put(propName, propVal);
            return retVal;
        } catch (NumberFormatException e) {
            throw new InvalidAttributeValueException("updateEnvironmentIntParam(" + propName + ")[" + propVal + "] " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    public void setProtocolVersion(int ver) throws NamingException {
        if (ver <= 0) throw new InvalidAttributeValueException("setProtocolVersion(" + ver + ") invalid value");
        updateEnvironmentIntParam("java.naming.ldap.version", String.valueOf(ver));
    }

    @Override
    public void bind(String host, int port, String bindDN, String bindPassword) throws NamingException {
        if (_context != null) throw new ContextNotEmptyException("bind(" + host + ")[" + port + "] - context already activated");
        if ((null == host) || (host.length() <= 0) || (port <= 0) || (port >= 0x0000FFFF)) throw new InvalidAttributeValueException("bind(" + host + ")[" + port + "] invalid network location");
        final String accURL = "ldap://" + host + ":" + port;
        final Properties env = getContextEnvironment();
        env.put(Context.PROVIDER_URL, accURL);
        if ((bindDN != null) && (bindDN.length() > 0)) {
            if ((null == bindPassword) || (bindPassword.length() <= 0)) throw new InvalidAttributeValueException("bind(" + host + ")[" + port + "] no bind password provided");
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, bindDN);
            env.put(Context.SECURITY_CREDENTIALS, bindPassword);
        } else env.put(Context.SECURITY_AUTHENTICATION, "none");
        _context = new InitialDirContext(env);
    }

    @Override
    public void close() throws IOException {
        if (_context != null) {
            try {
                _context.close();
            } catch (NamingException e) {
                throw new IOException(e.getClass().getName() + " while close context: " + e.getMessage());
            } finally {
                _context = null;
            }
        }
    }

    @Override
    public boolean isOpen() {
        return getContext() != null;
    }
}
