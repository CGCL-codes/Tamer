package de.objectcode.openk.soa.auth.store.persistence;

import java.io.Serializable;
import javax.persistence.Column;

public class UserRolesId implements Serializable {

    private static final long serialVersionUID = 1L;

    String userId;

    String mandantorId;

    String roleId;

    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "MANDANTOR_ID")
    public String getMandantorId() {
        return mandantorId;
    }

    public void setMandantorId(String mandantorId) {
        this.mandantorId = mandantorId;
    }

    @Column(name = "ROLE_ID")
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof UserRolesId)) {
            return false;
        }
        UserRolesId castObj = (UserRolesId) obj;
        return userId.equals(castObj.userId) && mandantorId.equals(castObj.mandantorId) && roleId.equals(castObj.roleId);
    }

    @Override
    public int hashCode() {
        int hash = userId.hashCode();
        hash = 13 * hash + mandantorId.hashCode();
        hash = 13 * hash + roleId.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer("UserRolesId(");
        buffer.append("userId=").append(userId);
        buffer.append(", mandantorId=").append(mandantorId);
        buffer.append(", roleId=").append(roleId);
        return buffer.toString();
    }
}
