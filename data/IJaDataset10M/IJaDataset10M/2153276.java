package com.faceye.core.service.security.service.iface;

import java.io.Serializable;
import java.util.Set;
import com.faceye.core.componentsupport.service.iface.IDomainService;
import com.faceye.core.service.security.model.Resource;

public interface IResourceService extends IDomainService {

    public String getPageResources(int startIndex);

    /**
   * 根据权限ID取得本权限可以访问的资源列表
   * 如果exists=true
   * 则取得本权限目前已可以访问的资源列表
   * 如果exists=false
   * 则取得本权限目前不可以访问的资源列表
   * @param permissionId
   * @return
   */
    public String getResourcesByPermission(Serializable permissionId, boolean exists);

    public void saveOrUpdateResource(Resource resource);

    public void saveOrUpdateResources(Set resources);

    public void removeResource(Serializable resourceId);

    public void removeResources(Serializable[] resourceIds);
}
