package com.integrationpath.mengine.dao.hibernate;

import java.util.List;
import com.integrationpath.mengine.dao.RoleDao;
import com.integrationpath.mengine.model.Role;

/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve Role objects.
 *
 * @author <a href="mailto:colaru@gmail.com">Cristian Olaru</a>
 */
public class RoleDaoHibernate extends GenericDaoHibernate<Role, Long> implements RoleDao {

    /**
     * Constructor to create a Generics-based version using Role as the entity
     */
    public RoleDaoHibernate() {
        super(Role.class);
    }

    /**
     * {@inheritDoc}
     */
    public Role getRoleByName(String rolename) {
        List roles = getHibernateTemplate().find("from Role where name=?", rolename);
        if (roles.isEmpty()) {
            return null;
        } else {
            return (Role) roles.get(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeRole(String rolename) {
        Object role = getRoleByName(rolename);
        getHibernateTemplate().delete(role);
    }
}
