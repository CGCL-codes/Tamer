package com.faceye.core.service.security.service.iface;

import java.io.Serializable;
import com.faceye.core.componentsupport.service.iface.IDomainService;
import com.faceye.core.service.security.model.Role;

public interface IRoleService extends IDomainService {

    /**
     * 对角色进行授权,确定角色可以使用的模块.
     * @param roleId
     * @param modelIds
     */
    public void saveOrUpdateRoleTree(Serializable roleId, Serializable[] treeIds);

    /**
     * 添加 role
     * @param role
     */
    public void saveOrUpdateRole(Role role);

    public String getPageRoles(int startIndex);

    /**
     * 根据用户取得角色,为授权做准备
     * @param userId
     * @param exists
     * @return
     */
    public String getRolesByUser(Serializable userId, boolean exists);

    /**
     * 对角色进行授权,授于资源访问权限
     * @param roleId
     * @param permissionIds
     */
    public void saveOrUpdateRolePermission(Serializable roleId, Serializable[] permissionIds);

    public void removeRole(Serializable roleId);

    public void removeRoles(Serializable[] roleIds);
}
