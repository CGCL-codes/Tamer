package de.sicari.kernel;

import java.security.Permission;
import java.security.PermissionCollection;
import de.sicari.util.WildcardedCharSequence;

/**
 * This <code>Permission</code> grants free access to the specified user
 * account name mask, i.e. access is granted without the usual
 * password authentication.
 *
 * @author Matthias Pressfreund
 * @version $Id: SubstituteUserPermission.java 204 2007-07-11 19:26:55Z jpeters $
 */
public class SubstituteUserPermission extends Permission {

    private static final long serialVersionUID = -1863974712837168311L;

    /**
     * The superuser name, which may contain wildcards
     */
    private WildcardedCharSequence name_;

    /**
     * Create a <code>SubstituteUserPermission</code>.
     *
     * @param name The user name, which may contain wildcards according to
     *   the <code>WildcardedCharSequence</code> syntax
     */
    public SubstituteUserPermission(String name) {
        super(name);
        name_ = new WildcardedCharSequence(name);
    }

    /**
     * Create a <code>SubstituteUserPermission</code>.
     *
     * @param name The user name, which may contain wildcards according to
     *   the <code>WildcardedCharSequence</code> syntax
     */
    public SubstituteUserPermission(WildcardedCharSequence name) {
        super(name.toString());
        name_ = name;
    }

    @Override
    public boolean implies(Permission permission) {
        if (permission == this) return true;
        if (permission.getClass().equals(SubstituteUserPermission.class)) {
            return name_.implies(((SubstituteUserPermission) permission).name_);
        }
        return false;
    }

    @Override
    public String getActions() {
        return null;
    }

    @Override
    public PermissionCollection newPermissionCollection() {
        return new GenericPermissionCollection(getClass());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj.getClass().equals(SubstituteUserPermission.class)) {
            return name_.equals(((SubstituteUserPermission) obj).name_);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (name_.hashCode() + 129);
    }
}
