package org.nodevision.portal.hibernate.om;

public class NvRoles implements java.io.Serializable {

    private java.lang.String RoleName;

    private java.util.Set SetOfNvUserRoles;

    /** default constructor */
    public NvRoles() {
    }

    /** constructor with id */
    public NvRoles(java.lang.String RoleName) {
        this.RoleName = RoleName;
    }

    /**
     */
    public java.lang.String getRoleName() {
        return this.RoleName;
    }

    public void setRoleName(java.lang.String RoleName) {
        this.RoleName = RoleName;
    }

    /**
     */
    public java.util.Set getSetOfNvUserRoles() {
        return this.SetOfNvUserRoles;
    }

    public void setSetOfNvUserRoles(java.util.Set SetOfNvUserRoles) {
        this.SetOfNvUserRoles = SetOfNvUserRoles;
    }
}
