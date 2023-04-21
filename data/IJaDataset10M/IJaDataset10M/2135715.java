package com.zhiyun.admin.service.impl;

import com.zhiyun.admin.dao.AdminUserDao;
import com.zhiyun.admin.service.IEbAdminUserService;
import com.zhiyun.admin.vo.EbAdminUser;

public class EbAdminUserServiceImpl implements IEbAdminUserService {

    private AdminUserDao adminUserDao;

    public AdminUserDao getAdminUserDao() {
        return adminUserDao;
    }

    public void setAdminUserDao(AdminUserDao adminUserDao) {
        this.adminUserDao = adminUserDao;
    }

    public EbAdminUser getLoginUser(EbAdminUser user) throws Exception {
        return adminUserDao.findLoginUser(user);
    }
}
