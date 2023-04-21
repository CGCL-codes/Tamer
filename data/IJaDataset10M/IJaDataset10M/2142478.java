package it.cilea.osd.common.dao.impl;

import it.cilea.osd.common.dao.IApplicationDao;
import it.cilea.osd.common.model.Identifiable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * {@link IApplicationDao} implementations based on Hibernate
 * 
 * @author cilea
 * 
 */
public class ApplicationDao extends HibernateDaoSupport implements IApplicationDao {

    /**
     * The Log4J logger
     */
    private static final Log log = LogFactory.getLog(ApplicationDao.class);

    private static final int MAX_IN_CLAUSE = 500;

    /**
     * {@inheritDoc}
     */
    public void evict(Identifiable identifiable) {
        getSession().evict(identifiable);
    }

    /**
     * {@inheritDoc}
     */
    public <T, PK extends Serializable> List<T> getList(Class<T> clazz, List<PK> allIds) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(clazz);
        final int maxResults = allIds.size();
        if (maxResults == 0) return null;
        int loop = maxResults / MAX_IN_CLAUSE;
        boolean exact = maxResults % MAX_IN_CLAUSE == 0;
        if (!exact) loop++;
        Disjunction disjunction = Restrictions.disjunction();
        for (int index = 0; index < loop; index++) {
            int max = index * MAX_IN_CLAUSE + MAX_IN_CLAUSE <= maxResults ? index * MAX_IN_CLAUSE + MAX_IN_CLAUSE : maxResults;
            List<PK> ids = new ArrayList<PK>(max - index * MAX_IN_CLAUSE);
            for (int entityInfoIndex = index * MAX_IN_CLAUSE; entityInfoIndex < max; entityInfoIndex++) {
                ids.add(allIds.get(entityInfoIndex));
            }
            disjunction.add(Restrictions.in("id", ids));
        }
        criteria.add(disjunction);
        criteria.list();
        List<T> result = new ArrayList<T>(allIds.size());
        for (PK id : allIds) {
            T element = (T) session.load(clazz, id);
            if (Hibernate.isInitialized(element)) {
                result.add(element);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Object with id: " + id + " not in database: " + clazz);
                }
            }
        }
        return result;
    }
}
