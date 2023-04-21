package com.xiaxueqi.entity.security;

import java.io.Serializable;
import java.util.Date;

/**
 * @author CodeGen --powered by Sean
 *
 */
public class AppRolePermission implements Serializable {

    private Integer id;

    private Integer roleId;

    private Integer permissionId;

    private Date createTime;

    private Date modifyTime;

    private String deleted;

    public Integer getId() {
        return id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}
