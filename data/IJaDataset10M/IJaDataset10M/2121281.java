package com.genia.toolbox.projects.toolbox_basics_project.spring.manager.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.genia.toolbox.persistence.exception.PersistenceException;
import com.genia.toolbox.projects.toolbox_basics_project.bean.model.Group;
import com.genia.toolbox.projects.toolbox_basics_project.bean.model.Role;
import com.genia.toolbox.projects.toolbox_basics_project.spring.manager.GroupRoleManager;
import com.genia.toolbox.projects.toolbox_basics_project.spring.manager.RoleDAO;

/**
 * dumb implementation of {@link GroupRoleManager} that give the first role by
 * id to all {@link Group}.
 */
public class DumbGroupRoleManager implements GroupRoleManager {

    /**
   * the {@link RoleDAO} to use.
   */
    private RoleDAO roleDAO;

    /**
   * returns the {@link Set} of {@link Role} associated to a {@link Group}.
   * 
   * @param group
   *          the group to consider
   * @return the {@link Set} of {@link Role} associated to a {@link Group}
   * @throws PersistenceException
   *           when an error occured
   * @see com.genia.toolbox.projects.toolbox_basics_project.spring.manager.GroupRoleManager#getRoles(com.genia.toolbox.projects.toolbox_basics_project.bean.model.Group)
   */
    public Set<Role> getRoles(Group group) throws PersistenceException {
        HashSet<Role> res = new HashSet<Role>();
        List<Role> allRoles = getRoleDAO().getAll();
        Role currentRole = null;
        for (Role role : allRoles) {
            if (currentRole == null || currentRole.getIdentifier() > role.getIdentifier()) {
                currentRole = role;
            }
        }
        if (currentRole != null) {
            res.add(currentRole);
        }
        return res;
    }

    /**
   * returns the {@link Set} of {@link Role} associated to a {@link Set} of
   * {@link Group}s.
   * 
   * @param groups
   *          the groups to consider
   * @return the {@link Set} of {@link Role} associated to a {@link Set} of
   *         {@link Group}
   * @throws PersistenceException
   *           when an error occured
   * @see com.genia.toolbox.projects.toolbox_basics_project.spring.manager.GroupRoleManager#getRoles(java.util.Set)
   */
    public Set<Role> getRoles(Set<Group> groups) throws PersistenceException {
        HashSet<Role> res = new HashSet<Role>();
        for (Group group : groups) {
            res.addAll(getRoles(group));
        }
        return res;
    }

    /**
   * getter for the roleDAO property.
   * 
   * @return the roleDAO
   */
    public RoleDAO getRoleDAO() {
        return roleDAO;
    }

    /**
   * setter for the roleDAO property.
   * 
   * @param roleDAO
   *          the roleDAO to set
   */
    public void setRoleDAO(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }
}
