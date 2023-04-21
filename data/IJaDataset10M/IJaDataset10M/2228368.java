package jumpingnotes.dao.hibernate;

import jumpingnotes.dao.GroupAdminDao;
import jumpingnotes.model.entity.GroupAdmin;

public class GroupAdminHibernateDao extends GenericHibernateDao<GroupAdmin> implements GroupAdminDao {

    public GroupAdminHibernateDao() {
        super(GroupAdmin.class);
    }
}
