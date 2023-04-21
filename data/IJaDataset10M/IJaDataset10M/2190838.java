package de.cue4net.eventservice.model.user.dao;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import de.cue4net.eventservice.model.user.Role;

/**
 * RoleDAO class:
 * this class provides functions which interacts with hibernate
 * for CRUD operations and conveniences
 * @author Keino Uelze - cue4net
 * @version $Id: RoleDAO.java,v 1.4 2008-06-05 12:19:09 keino Exp $
 */
public class RoleDAO extends HibernateDaoSupport {

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    public boolean createRole(Role role) {
        boolean result = false;
        if (this.findRoleByName(role.getName()) != null) return result;
        try {
            getHibernateTemplate().saveOrUpdate(role);
            result = true;
        } catch (RuntimeException re) {
            throw re;
        }
        return result;
    }

    public Role getDefaultRole() {
        Role result;
        result = findRoleByName("ROLE_USER");
        if (result == null) {
            try {
                getHibernateTemplate().saveOrUpdate(new Role("ROLE_USER", "The default role for all users.", "user", Role.SYSTEM_ROLE));
            } catch (RuntimeException re) {
                throw re;
            }
            result = findRoleByName("ROLE_USER");
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public Role findRoleByName(String rolename) {
        List<Role> roles = null;
        roles = getHibernateTemplate().find("from Role where name=?", rolename);
        if (roles.size() > 0) return roles.get(0); else return null;
    }
}
