package com.osgix.authorize.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Id;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import com.osgix.common.orm.ibatis.BaseEntity;

public class RoleInfo extends BaseEntity implements java.io.Serializable {

    private static final long serialVersionUID = 5454155825314635342L;

    public static final String TABLE_ALIAS = "RoleInfo";

    public static final String ALIAS_ROLE_INFO_ID = "roleInfoId";

    public static final String ALIAS_ROLE_NAME = "roleName";

    public static final String ALIAS_SUPER_ROLE_ID = "superRoleId";

    public static final String ALIAS_NOTE = "note";

    @Id
    private java.lang.Long roleInfoId;

    @NotBlank
    @Length(max = 128)
    private java.lang.String roleName;

    private java.lang.Long superRoleId;

    @Length(max = 1024)
    private java.lang.String note;

    public RoleInfo() {
    }

    public RoleInfo(java.lang.Long roleInfoId) {
        this.roleInfoId = roleInfoId;
    }

    public void setRoleInfoId(java.lang.Long value) {
        this.roleInfoId = value;
    }

    public java.lang.Long getRoleInfoId() {
        return this.roleInfoId;
    }

    public void setRoleName(java.lang.String value) {
        this.roleName = value;
    }

    public java.lang.String getRoleName() {
        return this.roleName;
    }

    public void setSuperRoleId(java.lang.Long value) {
        this.superRoleId = value;
    }

    public java.lang.Long getSuperRoleId() {
        return this.superRoleId;
    }

    public void setNote(java.lang.String value) {
        this.note = value;
    }

    public java.lang.String getNote() {
        return this.note;
    }

    private Set userRoles = new HashSet(0);

    public void setUserRoles(Set<UserRole> userRole) {
        this.userRoles = userRole;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    private Set rolePrivileges = new HashSet(0);

    public void setRolePrivileges(Set<RolePrivilege> rolePrivilege) {
        this.rolePrivileges = rolePrivilege;
    }

    public Set<RolePrivilege> getRolePrivileges() {
        return rolePrivileges;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("RoleInfoId", getRoleInfoId()).append("RoleName", getRoleName()).append("SuperRoleId", getSuperRoleId()).append("Note", getNote()).toString();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getRoleInfoId()).toHashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof RoleInfo == false) return false;
        if (this == obj) return true;
        RoleInfo other = (RoleInfo) obj;
        return new EqualsBuilder().append(getRoleInfoId(), other.getRoleInfoId()).isEquals();
    }
}
