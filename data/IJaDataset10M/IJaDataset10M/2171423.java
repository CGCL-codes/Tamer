package org.acegisecurity.ldap;

import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.NamingException;

/**
 * A user representation which is used internally by the Ldap provider.
 *
 * It contains the user's distinguished name and a set of attributes that
 * have been retrieved from the Ldap server.
 * <p>
 * An instance may be created as the result of a search, or when user information
 * is retrieved during authentication.
 * </p>
 * <p>
 * An instance of this class will be used by the <tt>LdapAuthenticationProvider</tt>
 * to construct the final user details object that it returns.
 * </p>
 *
 * @author Luke Taylor
 * @version $Id: LdapUserInfo.java,v 1.1 2006/04/16 13:56:36 luke_t Exp $
 */
public class LdapUserInfo {

    private String dn;

    private Attributes attributes;

    /**
     *
     * @param dn the full DN of the user
     * @param attributes any attributes loaded from the user's directory entry.
     */
    public LdapUserInfo(String dn, Attributes attributes) {
        this.dn = dn;
        this.attributes = attributes;
    }

    public String getDn() {
        return dn;
    }

    public String getRelativeName(DirContext ctx) throws NamingException {
        return LdapUtils.getRelativeName(dn, ctx);
    }

    public Attributes getAttributes() {
        return (Attributes) attributes.clone();
    }
}
