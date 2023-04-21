package com.osgix.authorize.dao;

import com.osgix.authorize.model.RolePrivilege;
import com.osgix.common.orm.ibatis.EntityDao;

public interface RolePrivilegeDao extends EntityDao<RolePrivilege, java.lang.Long> {

    public void saveOrUpdate(RolePrivilege rolePrivilege);
}
