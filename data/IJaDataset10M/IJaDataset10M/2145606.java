package net.sf.jguard.core.authorization.permissions;

import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.util.Enumeration;

/**
 * wrap a Permissions instance.
 *
 * @author <a href="mailto:diabolo512@users.sourceforge.net">Charles Gay</a>
 */
class AuditPermissions extends PermissionCollection {

    private Permissions permissions;

    public AuditPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    public void add(Permission permission) {
        permissions.add(new AuditPermission(permission));
    }

    /**
     * Checks to see if the specified permission is implied by
     * the collection of Permission objects held in this PermissionCollection.
     *
     * @param permission the Permission object to compare.
     * @return true if "permission" is implied by the  permissions in
     *         the collection, false if not.
     */
    public boolean implies(Permission permission) {
        return permissions.implies(permission);
    }

    /**
     * Returns an enumeration of all the Permission objects in the collection.
     *
     * @return an enumeration of all the Permissions.
     */
    public Enumeration<Permission> elements() {
        return permissions.elements();
    }
}
