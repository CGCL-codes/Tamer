package com.ecyrd.jspwiki.auth.acl;

import java.io.Serializable;
import java.security.Principal;

/**
 * Represents a Principal, typically read from an ACL, that cannot
 * be resolved based on the current state of the user database, group
 * manager, and built-in role definitions.
 * Creating a principal marked "unresolved" allows
 * delayed resolution, which enables principals to be resolved
 * lazily during a later access control check. Conceptuallly,
 * UnresolvedPrincipal performs a function similar to
 * {@link java.security.UnresolvedPermission}.
 * 
 * @author Andrew Jaquith
 * @since 2.3
 */
public final class UnresolvedPrincipal implements Principal, Serializable {

    private static final long serialVersionUID = 1L;

    private final String m_name;

    /**
     * Constructs a new UnresolvedPrincipal instance.
     * @param name the name of the Principal
     */
    public UnresolvedPrincipal(String name) {
        m_name = name;
    }

    /**
     * Returns the name of the principal.
     * @return the name
     * @see java.security.Principal#getName()
     */
    public final String getName() {
        return m_name;
    }

    /**
     * Returns a String representation of the UnresolvedPrincipal.
     * @return the String
     */
    public final String toString() {
        return "[UnresolvedPrincipal: " + m_name + "]";
    }

    /**
     * An unresolved principal is equal to another
     * unresolved principal if their names match.
     * @param obj the object to compare to this one
     * @return the result of the equality test
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public final boolean equals(Object obj) {
        if (obj instanceof UnresolvedPrincipal) {
            return m_name.equals(((UnresolvedPrincipal) obj).m_name);
        }
        return false;
    }

    /**
     *  The hashCode of this object is equal to the hash code of its name.
     *  @return the hash code
     */
    public final int hashCode() {
        return m_name.hashCode();
    }
}
