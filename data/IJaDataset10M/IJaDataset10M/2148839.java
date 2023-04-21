package com.integrationpath.mengine.service.impl;

import java.util.List;
import com.integrationpath.mengine.dao.RoleDao;
import com.integrationpath.mengine.model.Role;
import com.integrationpath.mengine.service.RoleManager;

/**
 * Implementation of RoleManager interface.
 * 
 * @author <a href="mailto:colaru@gmail.com">Cristian Olaru</a>
 */
public class RoleManagerImpl extends UniversalManagerImpl implements RoleManager {

    private RoleDao dao;

    public void setRoleDao(RoleDao dao) {
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    public List<Role> getRoles(Role role) {
        return dao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    public Role getRole(String rolename) {
        return dao.getRoleByName(rolename);
    }

    /**
     * {@inheritDoc}
     */
    public Role saveRole(Role role) {
        return dao.save(role);
    }

    /**
     * {@inheritDoc}
     */
    public void removeRole(String rolename) {
        dao.removeRole(rolename);
    }
}
