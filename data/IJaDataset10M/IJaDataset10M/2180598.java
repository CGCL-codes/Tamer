package com.kongur.network.erp.dao.system.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.kongur.network.erp.dao.BaseDaoiBatis;
import com.kongur.network.erp.dao.system.SystemUserRoleDao;
import com.kongur.network.erp.domain.system.SystemUserRole;

@Repository("systemUserRoleDao")
public class SystemUserRoleDaoImpl extends BaseDaoiBatis<SystemUserRole> implements SystemUserRoleDao {

    @Override
    public Long addUserRole(SystemUserRole systemUserRole) {
        return (Long) this.getSqlMapClientTemplate().insert("SystemUserRole.addUserRole", systemUserRole);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SystemUserRole> getUserRoleByUserId(Long userId) {
        return this.getSqlMapClientTemplate().queryForList("SystemUserRole.getUserRoleByUserId", userId);
    }

    @Override
    public int removeUserRoleByUserId(Long userId) {
        return this.getSqlMapClientTemplate().delete("SystemUserRole.removeUserRoleByUserId", userId);
    }

    @Override
    public int getUserRoleByRoleId(Long roleId) {
        return (Integer) this.getSqlMapClientTemplate().queryForObject("SystemUserRole.getUserRoleByRoleId", roleId);
    }
}
