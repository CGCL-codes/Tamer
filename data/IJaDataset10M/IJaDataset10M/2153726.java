package com.osgix.demo.pojo;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import com.osgix.common.orm.ibatis.BaseEntity;

public class UserRole extends BaseEntity implements java.io.Serializable {

    private static final long serialVersionUID = 5454155825314635342L;

    public static final String TABLE_ALIAS = "UserRole";

    public static final String ALIAS_USER_ROLE_ID = "userRoleId";

    public static final String ALIAS_ROLE_ID = "roleId";

    public static final String ALIAS_USER_ID = "userId";

    private java.lang.Long userRoleId;

    private java.lang.Long roleId;

    private java.lang.Long userId;

    public UserRole() {
    }

    public UserRole(java.lang.Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public void setUserRoleId(java.lang.Long value) {
        this.userRoleId = value;
    }

    public java.lang.Long getUserRoleId() {
        return this.userRoleId;
    }

    public void setRoleId(java.lang.Long value) {
        this.roleId = value;
    }

    public java.lang.Long getRoleId() {
        return this.roleId;
    }

    public void setUserId(java.lang.Long value) {
        this.userId = value;
    }

    public java.lang.Long getUserId() {
        return this.userId;
    }

    private UserInfo userInfo;

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("UserRoleId", getUserRoleId()).append("RoleId", getRoleId()).append("UserId", getUserId()).toString();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getUserRoleId()).toHashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof UserRole == false) return false;
        if (this == obj) return true;
        UserRole other = (UserRole) obj;
        return new EqualsBuilder().append(getUserRoleId(), other.getUserRoleId()).isEquals();
    }
}
