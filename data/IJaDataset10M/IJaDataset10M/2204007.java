package org.imogene.sync.server.hibernate;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.imogene.sync.server.history.SyncHistory;
import org.imogene.sync.server.history.SyncHistoryDao;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * sync history DAO implementation using Hibernate.
 * 
 * @author Medes-IMPS
 */
public class SyncHistoryHibernateDao extends HibernateDaoSupport implements SyncHistoryDao {

    private static final String TERMINAL_PROPERTY = "terminalId";

    private static final String TIME_PROPERTY = "time";

    public void saveOrUpdate(SyncHistory history) {
        getHibernateTemplate().saveOrUpdate(history);
    }

    @SuppressWarnings("unchecked")
    public SyncHistory loadLastHistory(String terminalId) {
        Criteria crit = getSession().createCriteria(SyncHistory.class);
        crit.add(Restrictions.eq(TERMINAL_PROPERTY, terminalId));
        crit.addOrder(Property.forName(TIME_PROPERTY).desc());
        List<SyncHistory> result = crit.list();
        if (result != null && result.size() > 0) {
            for (SyncHistory history : result) {
                if (history.getStatus() == SyncHistory.STATUS_OK) return history;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<SyncHistory> loadHistories(String terminalId) {
        Criteria crit = getSession().createCriteria(SyncHistory.class);
        crit.add(Restrictions.eq(TERMINAL_PROPERTY, terminalId));
        crit.addOrder(Property.forName(TIME_PROPERTY).desc());
        return crit.list();
    }

    @SuppressWarnings("unchecked")
    public List<SyncHistory> loadHistories() {
        Criteria crit = getSession().createCriteria(SyncHistory.class);
        crit.addOrder(Property.forName(TIME_PROPERTY).desc());
        return crit.list();
    }

    /** */
    private void deleteHistory(SyncHistory history) {
        getHibernateTemplate().delete(history);
    }

    @SuppressWarnings("unchecked")
    public void deleteOldHistories(String terminalId) {
        logger.debug("Deleting old Histories");
        boolean toDelete = false;
        boolean isStatusOK = false;
        Criteria crit = getSession().createCriteria(SyncHistory.class);
        crit.add(Restrictions.eq(TERMINAL_PROPERTY, terminalId));
        crit.addOrder(Property.forName(TIME_PROPERTY).desc());
        List<SyncHistory> result = crit.list();
        int i = 1;
        if (result != null && result.size() > 0) {
            for (SyncHistory history : result) {
                if (toDelete && isStatusOK) deleteHistory(history);
                if (!toDelete && i >= 3) toDelete = true;
                if (!isStatusOK && history.getStatus() == SyncHistory.STATUS_OK) isStatusOK = true;
                i = i + 1;
            }
        }
    }

    public void deleteHistories() {
        logger.debug("Deleting all Histories");
        getHibernateTemplate().deleteAll(loadHistories());
    }
}
