package com.osgix.authorize.vo.query;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import com.osgix.common.orm.ibatis.BaseQuery;

public class RolePrivilegeQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 3148176768559230877L;

    public static final String ROLE_PRIVILEGE_ID = "role_privilege_id";

    public static final String ROLE_INFO_ID = "role_info_id";

    public static final String PRIVILEGE_INFO_ID = "privilege_info_id";

    /** rolePrivilegeId */
    private java.lang.Long rolePrivilegeId;

    /** roleInfoId */
    private java.lang.Long roleInfoId;

    /** privilegeInfoId */
    private java.lang.Long privilegeInfoId;

    public java.lang.Long getRolePrivilegeId() {
        return this.rolePrivilegeId;
    }

    public void setRolePrivilegeId(java.lang.Long value) {
        this.rolePrivilegeId = value;
    }

    public java.lang.Long getRoleInfoId() {
        return this.roleInfoId;
    }

    public void setRoleInfoId(java.lang.Long value) {
        this.roleInfoId = value;
    }

    public java.lang.Long getPrivilegeInfoId() {
        return this.privilegeInfoId;
    }

    public void setPrivilegeInfoId(java.lang.Long value) {
        this.privilegeInfoId = value;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
