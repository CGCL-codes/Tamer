package search.model.dao.hibernate;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import search.model.Role;
import search.model.dao.RoleDao;

public class RoleDaoImpl extends HibernateDaoSupport implements RoleDao {

    public Role getRoleById(Integer id) {
        return (Role) getHibernateTemplate().get(Role.class, id);
    }

    public Role getRoleByName(String name) {
        List results = getHibernateTemplate().find("from Role role where role.name = ?", name);
        return results.size() == 0 ? null : (Role) results.get(0);
    }
}
