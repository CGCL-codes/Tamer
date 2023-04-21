package net.sourceforge.solexatools.dao.hibernate;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import net.sourceforge.solexatools.dao.IUSDAO;
import net.sourceforge.solexatools.dao.LaneDAO;
import net.sourceforge.solexatools.model.IUS;
import net.sourceforge.solexatools.model.Lane;
import net.sourceforge.solexatools.model.SequencerRun;

public class IUSDAOHibernate extends HibernateDaoSupport implements IUSDAO {

    public IUSDAOHibernate() {
        super();
    }

    /**
	 * Inserts an instance of Lane into the database.
	 */
    public void insert(IUS obj) {
        this.getHibernateTemplate().save(obj);
    }

    /**
	 * Updates an instance of Lane in the database.
	 */
    public void update(IUS obj) {
        this.getHibernateTemplate().update(obj);
    }

    public IUS findByID(Integer id) {
        String query = "from IUS as ius where ius.iusId = ?";
        IUS obj = null;
        Object[] parameters = { id };
        List list = this.getHibernateTemplate().find(query, parameters);
        if (list.size() > 0) {
            obj = (IUS) list.get(0);
        }
        return obj;
    }
}
