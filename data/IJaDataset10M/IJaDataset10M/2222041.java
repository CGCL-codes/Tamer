package com.faceye.core.service.security.service.iface;

import java.io.Serializable;
import java.util.Set;
import com.faceye.core.componentsupport.service.iface.IDomainService;
import com.faceye.core.service.security.model.Permission;

public interface IPermissionService extends IDomainService {

    public String getPagePermissions(int startIndex);

    public void saveOrUpdatePermissionResources(Serializable permissionId, Serializable[] resourceIds);

    public void saveOrUpdatePermission(Permission permission);

    public void saveOrUpdatePermissions(Set permissions);

    /**
    * 根据角色取得角色的权限,为角色授权做准备
    * @param roleId
    * @return
    */
    public String getPermissionsByRole(Serializable roleId, boolean exists);

    public void removePermission(Serializable permissionId);

    public void removePermissions(Serializable[] permissionIds);
}
