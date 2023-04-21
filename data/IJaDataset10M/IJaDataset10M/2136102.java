package org.acegisecurity.providers.rememberme;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.providers.AbstractAuthenticationToken;
import org.springframework.util.Assert;
import java.io.Serializable;

/**
 * Represents a remembered <code>Authentication</code>.
 * 
 * <p>
 * A remembered <code>Authentication</code> must provide a fully valid
 * <code>Authentication</code>, including the <code>GrantedAuthority</code>[]s
 * that apply.
 * </p>
 *
 * @author Ben Alex
 * @version $Id: RememberMeAuthenticationToken.java,v 1.6 2006/02/09 04:16:50 benalex Exp $
 */
public class RememberMeAuthenticationToken extends AbstractAuthenticationToken implements Serializable {

    private Object principal;

    private int keyHash;

    /**
     * Constructor.
     *
     * @param key to identify if this object made by an authorised client
     * @param principal the principal (typically a <code>UserDetails</code>)
     * @param authorities the authorities granted to the principal
     *
     * @throws IllegalArgumentException if a <code>null</code> was passed
     */
    public RememberMeAuthenticationToken(String key, Object principal, GrantedAuthority[] authorities) {
        super(authorities);
        if ((key == null) || ("".equals(key)) || (principal == null) || "".equals(principal) || (authorities == null) || (authorities.length == 0)) {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }
        for (int i = 0; i < authorities.length; i++) {
            Assert.notNull(authorities[i], "Granted authority element " + i + " is null - GrantedAuthority[] cannot contain any null elements");
        }
        this.keyHash = key.hashCode();
        this.principal = principal;
        setAuthenticated(true);
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (obj instanceof RememberMeAuthenticationToken) {
            RememberMeAuthenticationToken test = (RememberMeAuthenticationToken) obj;
            if (this.getKeyHash() != test.getKeyHash()) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Always returns an empty <code>String</code>
     *
     * @return an empty String
     */
    public Object getCredentials() {
        return "";
    }

    public int getKeyHash() {
        return this.keyHash;
    }

    public Object getPrincipal() {
        return this.principal;
    }
}
