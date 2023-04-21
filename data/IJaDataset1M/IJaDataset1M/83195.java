package org.apache.harmony.security.tests.support.acl;

import java.security.Principal;

/**
 * Additional class for verification Principal interface 
 */
public class PrincipalImpl implements Principal {

    private String user;

    public PrincipalImpl(String s) {
        user = s;
    }

    public boolean equals(Object obj) {
        if (obj instanceof PrincipalImpl) {
            PrincipalImpl principalimpl = (PrincipalImpl) obj;
            return user.equals(principalimpl.toString());
        } else {
            return false;
        }
    }

    public String toString() {
        return user;
    }

    public int hashCode() {
        return user.hashCode();
    }

    public String getName() {
        return user;
    }
}
