package com.unytech.watersoil.service.security.impl;

import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.unytech.watersoil.entity.security.Permission;
import com.unytech.watersoil.service.base.BaseDao;
import com.unytech.watersoil.service.security.PermissionService;

@Service
@Transactional
public class PermissionServiceImpl extends BaseDao<Permission> implements PermissionService {

    String entityname = getEntityName(this.entityClass);

    public Set<Permission> getPermissionEntity(List permissionids) {
        Set<Permission> permissionset = null;
        Permission permtemp = null;
        for (int i = 0; i < permissionset.size(); i++) {
            permtemp = em.find(Permission.class, permissionids.get(i));
            if (permtemp != null) {
                permissionset.add(permtemp);
            }
        }
        return permissionset;
    }
}
