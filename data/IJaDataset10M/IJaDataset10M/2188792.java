package net.community.chest.net.ldap;

import javax.naming.NamingException;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Jul 20, 2008 12:39:03 PM
 */
public abstract class AbstractLDAPAccessor implements LDAPAccessor {

    protected AbstractLDAPAccessor() {
        super();
    }

    @Override
    public void bind(String host, String bindDN, String bindPassword) throws NamingException {
        bind(host, LDAPProtocol.IPPORT_LDAP, bindDN, bindPassword);
    }

    @Override
    public void bind(String host, int port) throws NamingException {
        bind(host, port, null, null);
    }

    @Override
    public void bind(String host) throws NamingException {
        bind(host, LDAPProtocol.IPPORT_LDAP);
    }
}
