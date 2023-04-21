package ipman.app.base.daoimpl;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import ipman.app.base.domain.Milestone;
import ipman.app.base.dao.MilestoneDao;

/**
*
*/
public class MilestoneDaoImpl extends HibernateDaoSupport implements MilestoneDao {

    /**
    *
    */
    public Milestone loadMilestoneById(long id) {
        return (Milestone) getHibernateTemplate().get(Milestone.class, id);
    }

    /**
    *
    */
    public List<Milestone> loadAllMilestones() {
        return (List<Milestone>) getHibernateTemplate().loadAll(Milestone.class);
    }

    /**
    *
    */
    public long createMilestone(Milestone domain) {
        long id = (Long) getHibernateTemplate().save(domain);
        return id;
    }

    /**
    *
    */
    public boolean updateMilestone(Milestone domain) {
        if (domain.getId() > 0) {
            getHibernateTemplate().saveOrUpdate(domain);
            return true;
        }
        return false;
    }

    /**
    *
    */
    public boolean deleteMilestone(Milestone domain) {
        if (domain.getId() > 0) {
            getHibernateTemplate().delete(domain);
            return true;
        }
        return false;
    }

    /**
    *
    */
    public boolean deleteMilestoneById(long id) {
        return true;
    }
}
