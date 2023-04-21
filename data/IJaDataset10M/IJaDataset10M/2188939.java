package com.javaeye.common.service;

import java.util.List;
import com.javaeye.common.dao.RoleDAO;
import com.javaeye.common.dto.Role;

public class RoleService implements IRoleService {

    private RoleDAO roleDao;

    public void setRoleDao(RoleDAO roleDao) {
        this.roleDao = roleDao;
    }

    public List<Role> getRoles() {
        List<Role> result = roleDao.getRoles();
        return result;
    }

    public void saveRole(Role role) {
        roleDao.saveRole(role);
    }

    public boolean removeRole(int roleId) {
        roleDao.removeRole(roleId);
        return true;
    }

    public Role getRole(int roleId) {
        Role role = roleDao.getRole(roleId);
        return role;
    }
}
