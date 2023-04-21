package openschool.domain.dao.hibernate;

import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import openschool.domain.dao.*;
import openschool.domain.model.Role;

@Repository
public class RoleDAOHibernate implements RoleDAO {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    HibernateTemplate hibernateTemplate;

    @Override
    public Role getRole(Long id) {
        return (Role) sessionFactory.getCurrentSession().get(Role.class, id);
    }

    @Override
    public void updateRole(Role role) {
        sessionFactory.getCurrentSession().update(role);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Role getRoleByName(String name) {
        List<Role> roles = hibernateTemplate.findByNamedQueryAndNamedParam("Role.findRoleByName", "name", name);
        if (roles.size() == 1) {
            return roles.get(0);
        } else {
            return null;
        }
    }
}
