package net.sf.brightside.stockswatcher.server.service.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import net.sf.brightside.stockswatcher.server.metamodel.Share;
import net.sf.brightside.stockswatcher.server.metamodel.ShareChange;
import net.sf.brightside.stockswatcher.server.metamodel.beans.ShareChangeBean;
import net.sf.brightside.stockswatcher.server.service.api.hibernate.IGetChangesByDate;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class GetChangesByDateImpl extends HibernateDaoSupport implements IGetChangesByDate {

    public Session getManager() {
        SessionFactory sf = getSessionFactory();
        return sf.getCurrentSession();
    }

    @SuppressWarnings({ "unchecked", "static-access" })
    @Transactional
    public List<ShareChange> getChangesByDate(Share share, Date endDate) {
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(endDate);
        startDate.add(startDate.DAY_OF_MONTH, -1);
        Session session = getManager();
        Criteria criteria = session.createCriteria(ShareChangeBean.class);
        criteria.add(Restrictions.like("history", share.getHistory()));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.between("timestamp", startDate.getTime().getTime(), endDate.getTime()));
        List<ShareChange> changes = (List<ShareChange>) criteria.list();
        return changes;
    }
}
