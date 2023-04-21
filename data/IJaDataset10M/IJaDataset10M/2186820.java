package woko.tracker.model;

import org.hibernate.Query;
import java.util.List;
import net.sf.woko.util.Util;
import net.sf.woko.persistence.PersistenceUtil;

public class TrackerManager {

    private PersistenceUtil persistenceUtil;

    public PersistenceUtil getPersistenceUtil() {
        return persistenceUtil;
    }

    public void setPersistenceUtil(PersistenceUtil persistenceUtil) {
        this.persistenceUtil = persistenceUtil;
    }

    public List<Tracker> getAllTrackers() {
        String hql = "from Tracker as t order by t.name";
        Query q = getPersistenceUtil().getSession().createQuery(hql);
        return q.list();
    }
}
