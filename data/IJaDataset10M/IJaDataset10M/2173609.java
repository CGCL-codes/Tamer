package org.likken.core;

import java.util.Hashtable;
import javax.naming.*;
import javax.naming.directory.*;

/**
 * @author Stephane Boisson
 * @version $Revision: 1.2 $ $Date: 2001/03/02 19:49:41 $
 */
public class LikkenContext {

    private Hashtable env;

    private String serverName;

    private int serverPort;

    private DistinguishedName suffix;

    public LikkenContext() {
        env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "none");
        env.put("java.naming.ldap.attributes.binary", "image logo");
    }

    public void setServer(final String theServerName) {
        serverName = theServerName;
        setServerURL(makeServerURL());
    }

    public void setServer(final String theServerName, final int theServerPort) {
        serverName = theServerName;
        serverPort = theServerPort;
        setServerURL(makeServerURL());
    }

    public void setSuffix(final String theBaseDN) {
        suffix = new DistinguishedName(theBaseDN);
        setServerURL(makeServerURL());
    }

    public void setSuffix(final DistinguishedName theBaseDN) {
        suffix = theBaseDN;
        setServerURL(makeServerURL());
    }

    public DistinguishedName getSuffix() {
        if (suffix == null) {
            return new DistinguishedName();
        }
        return suffix;
    }

    public void setAnonymous() {
        env.put(Context.SECURITY_AUTHENTICATION, "none");
    }

    public void setUser(final String thePrincipals, final String theCredentials) {
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, thePrincipals);
        env.put(Context.SECURITY_CREDENTIALS, (theCredentials != null) ? theCredentials : "");
    }

    public DistinguishedName getUserDN() {
        if (env.get(Context.SECURITY_AUTHENTICATION).equals("simple")) {
            return new DistinguishedName((String) env.get(Context.SECURITY_PRINCIPAL));
        }
        return new DistinguishedName();
    }

    private String makeServerURL() {
        StringBuffer sb = new StringBuffer("ldap://");
        sb.append((serverName != null) ? serverName : "127.0.0.1");
        sb.append(':');
        sb.append((serverPort > 0) ? serverPort : 389);
        sb.append('/');
        if (suffix != null) {
            sb.append(suffix.toString());
        }
        return sb.toString();
    }

    public void setServerURL(final String anURL) {
        env.put(Context.PROVIDER_URL, anURL);
    }

    protected Hashtable getEnvironment() {
        return env;
    }

    public boolean isValid() {
        try {
            DirContext ctx = new InitialDirContext(getEnvironment());
            ctx.close();
            return true;
        } catch (final NamingException e) {
            System.err.println(e.toString());
            return false;
        }
    }

    public String toString() {
        return env.toString();
    }
}
