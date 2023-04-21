package org.broadleafcommerce.security.test;

import javax.annotation.Resource;
import org.broadleafcommerce.security.domain.AdminUser;
import org.broadleafcommerce.security.service.AdminSecurityService;
import org.broadleafcommerce.security.test.dataprovider.AdminUserDataProvider;
import org.broadleafcommerce.test.integration.BaseTest;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

public class AdminUserTest extends BaseTest {

    @Resource
    AdminSecurityService adminSecurityService;

    @Test(groups = { "testAdminUserSave" }, dataProvider = "setupAdminUser", dataProviderClass = AdminUserDataProvider.class)
    @Rollback(true)
    public void testAdminUserSave(AdminUser user) throws Exception {
        AdminUser newUser = adminSecurityService.saveAdminUser(user);
        AdminUser userFromDB = adminSecurityService.readAdminUserById(newUser.getId());
        assert (userFromDB != null);
    }
}
