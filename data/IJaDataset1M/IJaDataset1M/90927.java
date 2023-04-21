package com.emc.esu.api;

/**
 * Used to grant a permission to a grantee (a user or group)
 */
public class Grant {

    private Grantee grantee;

    private String permission;

    /**
     * Creates a new grant
     * @param grantee the recipient of the permission
     * @param permission the rights to grant to the grantee.  Use
     * the constants in the Permission class.
     */
    public Grant(Grantee grantee, String permission) {
        this.grantee = grantee;
        this.permission = permission;
    }

    /**
     * Gets the recipient of the grant
     * @return the grantee
     */
    public Grantee getGrantee() {
        return grantee;
    }

    /**
     * Gets the rights assigned the grantee
     * @return the permissions assigned
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Returns the grant in string form: grantee=permission
     */
    public String toString() {
        return grantee.getName() + "=" + permission;
    }

    /**
     * Checks to see if grants are equal.  This is true if the grantee and
     * permission are equal.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Grant)) {
            return false;
        }
        Grant g = (Grant) obj;
        return g.permission.equals(permission) && g.grantee.equals(grantee);
    }

    /**
     * Returns a hash code for the Grant.
     */
    public int hashCode() {
        return toString().hashCode();
    }
}
