package org.broadleafcommerce.security.dao;

import java.util.List;
import org.broadleafcommerce.security.domain.AdminPermission;

public interface AdminPermissionDao {

    public List<AdminPermission> readAllAdminPermissions();

    public AdminPermission readAdminPermissionById(Long id);

    public AdminPermission saveAdminPermission(AdminPermission permission);

    public void deleteAdminPermission(AdminPermission permission);
}
