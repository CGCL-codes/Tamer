package org.atlantal.jaas;

import java.security.Principal;

/**
 * @author f.masurel
 *
 */
public class UserPrincipal implements Principal, java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String name;

    /**
     * @param name name
     */
    public UserPrincipal(String name) {
        if (name == null) {
            throw new NullPointerException("illegal null input");
        }
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        String className = getClass().getName();
        return className.substring(className.lastIndexOf('.') + 1) + ": " + name;
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public int hashCode() {
        return name.hashCode();
    }
}
