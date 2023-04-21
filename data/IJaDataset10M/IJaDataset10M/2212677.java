package org.scopemvc.core.security;

import java.security.Principal;

/**
 * <p> This class implements the <code>Principal</code> interface
 * and represents a user.
 *
 */
public class UserPrincipal implements Principal, java.io.Serializable {

    private String mName;

    /**
     * Create a UserPrincipal with a username.
     *
     * @param name the username for this user.
     * @exception NullPointerException if the <code>name</code>
     *                  is <code>null</code>.
     */
    public UserPrincipal(String name) {
        if (name == null) {
            throw new NullPointerException("illegal null input");
        }
        this.mName = name;
    }

    /**
     * @return the username for this <code>UserPrincipal</code>
     */
    public String getName() {
        return mName;
    }

    /**
     * @return a string representation of this <code>UserPrincipal</code>.
     */
    public String toString() {
        return ("UserPrincipal:  " + mName);
    }

    /**
     * Compares the specified Object with this <code>UserPrincipal</code> for
     * equality. Returns true if the given object is also a <code>UserPrincipal</code>
     * and the two SamplePrincipals have the same username.
     *
     * @param o Object to be compared for equality with this <code>UserPrincipal</code>
     * @return true if the specified Object is equal equal to this <code>UserPrincipal</code>
     */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPrincipal)) {
            return false;
        }
        UserPrincipal that = (UserPrincipal) o;
        if (this.getName().equals(that.getName())) {
            return true;
        }
        return false;
    }

    /**
     *
     * @return a hash code for this <code>UserPrincipal</code>.
     */
    public int hashCode() {
        return mName.hashCode();
    }
}
