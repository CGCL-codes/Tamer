package org.broadleafcommerce.gwt.server.security.dao;

import java.util.List;
import org.broadleafcommerce.gwt.server.security.domain.AdminUser;

/**
 * 
 * @author jfischer
 *
 */
public interface AdminUserDao {

    public List<AdminUser> readAllAdminUsers();

    public AdminUser readAdminUserById(Long id);

    public AdminUser readAdminUserByUserName(String userName);

    public AdminUser saveAdminUser(AdminUser user);

    public void deleteAdminUser(AdminUser user);
}
