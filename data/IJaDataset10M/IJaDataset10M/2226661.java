package net.jixi.database.beans;

/**
 * DOCUMENT ME!
 *
 * @author $Author: eschnepel $
 * @version $Revision: 1.8 $
 */
public class DBRoleBean implements DBBeanInterface {

    /** DOCUMENT ME! */
    private String roleName;

    /** DOCUMENT ME! */
    private boolean roleCanpathadmin;

    /** DOCUMENT ME! */
    private boolean roleCanread;

    /** DOCUMENT ME! */
    private boolean roleCanroleadmin;

    /** DOCUMENT ME! */
    private boolean roleCanuseradmin;

    /** DOCUMENT ME! */
    private boolean roleCanwrite;

    /** DOCUMENT ME! */
    private int roleId;

    /** DOCUMENT ME! */
    private int roleUp_count;

    /**
     * DOCUMENT ME!
     *
     * @param roleCanpathadmin The roleCanpathadmin to set.
     */
    public void setRoleCanpathadmin(boolean roleCanpathadmin) {
        this.roleCanpathadmin = roleCanpathadmin;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the roleCanpathadmin.
     */
    public boolean getRoleCanpathadmin() {
        return roleCanpathadmin;
    }

    /**
     * DOCUMENT ME!
     *
     * @param roleCanread The roleCanread to set.
     */
    public void setRoleCanread(boolean roleCanread) {
        this.roleCanread = roleCanread;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the roleCanread.
     */
    public boolean getRoleCanread() {
        return roleCanread;
    }

    /**
     * DOCUMENT ME!
     *
     * @param roleCanroleadmin The roleCanroleadmin to set.
     */
    public void setRoleCanroleadmin(boolean roleCanroleadmin) {
        this.roleCanroleadmin = roleCanroleadmin;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the roleCanroleadmin.
     */
    public boolean getRoleCanroleadmin() {
        return roleCanroleadmin;
    }

    /**
     * DOCUMENT ME!
     *
     * @param roleCanuseradmin The roleCanuseradmin to set.
     */
    public void setRoleCanuseradmin(boolean roleCanuseradmin) {
        this.roleCanuseradmin = roleCanuseradmin;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the roleCanuseradmin.
     */
    public boolean getRoleCanuseradmin() {
        return roleCanuseradmin;
    }

    /**
     * DOCUMENT ME!
     *
     * @param roleCanwrite The roleCanwrite to set.
     */
    public void setRoleCanwrite(boolean roleCanwrite) {
        this.roleCanwrite = roleCanwrite;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the roleCanwrite.
     */
    public boolean getRoleCanwrite() {
        return roleCanwrite;
    }

    /**
     * DOCUMENT ME!
     *
     * @param roleId The roleId to set.
     */
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the roleId.
     */
    public int getRoleId() {
        return roleId;
    }

    /**
     * DOCUMENT ME!
     *
     * @param roleName The roleName to set.
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the roleName.
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param roleUp_count DOCUMENT ME!
     */
    public void setRoleUp_count(int roleUp_count) {
        this.roleUp_count = roleUp_count;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRoleUp_count() {
        return roleUp_count;
    }
}
