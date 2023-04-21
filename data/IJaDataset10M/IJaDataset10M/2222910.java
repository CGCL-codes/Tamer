package org.acegisecurity.context;

import org.acegisecurity.Authentication;

/**
 * Base implementation of {@link SecurityContext}.
 *
 * <p>
 * Used by default by {@link SecurityContextHolder} and {@link HttpSessionContextIntegrationFilter}.
 * </p>
 *
 * @author Ben Alex
 * @version $Id: SecurityContextImpl.java,v 1.4 2005/11/17 00:55:49 benalex Exp $
 */
public class SecurityContextImpl implements SecurityContext {

    private Authentication authentication;

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public boolean equals(Object obj) {
        if (obj instanceof SecurityContextImpl) {
            SecurityContextImpl test = (SecurityContextImpl) obj;
            if ((this.getAuthentication() == null) && (test.getAuthentication() == null)) {
                return true;
            }
            if ((this.getAuthentication() != null) && (test.getAuthentication() != null) && this.getAuthentication().equals(test.getAuthentication())) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(super.toString());
        if (this.authentication == null) {
            sb.append(": Null authentication");
        } else {
            sb.append(": Authentication: " + this.authentication);
        }
        return sb.toString();
    }

    public int hashCode() {
        if (this.authentication == null) {
            return -1;
        } else {
            return this.authentication.hashCode();
        }
    }
}
