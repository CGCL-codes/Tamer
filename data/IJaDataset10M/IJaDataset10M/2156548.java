package net.cepra.timecard.server.dao.impl;

import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import net.cepra.core.server.util.AbstractDAO;
import net.cepra.timecard.domain.Activity;
import net.cepra.timecard.server.dao.IActivityDAO;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class ActivityDAO extends AbstractDAO implements IActivityDAO {

    static final String query = "from " + Activity.class.getName() + " as act where act.resource.id=:res and act.timeFrom>=:begin AND act.timeFrom<:end order by act.timeFrom";

    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.SUPPORTS)
    public Activity[] search(Integer resID, Date begin, Date end) {
        Query createQuery = getEM().createQuery(query);
        createQuery.setParameter("res", resID);
        createQuery.setParameter("begin", begin);
        createQuery.setParameter("end", end);
        List<Activity> activityList = createQuery.getResultList();
        Activity[] activities = activityList.toArray(new Activity[activityList.size()]);
        if (activities == null) activities = new Activity[0];
        return activities;
    }

    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.SUPPORTS)
    public Activity[] loadLastActivities(Integer resID) {
        Criteria c = getSession().createCriteria(Activity.class);
        c.setMaxResults(200).addOrder(Order.desc("timeFrom"));
        c.add(Restrictions.sqlRestriction("this_.resource_id = ?", resID, Hibernate.INTEGER));
        List<Activity> activityList = c.list();
        Activity[] activities = activityList.toArray(new Activity[activityList.size()]);
        if (activities == null) activities = new Activity[0];
        return activities;
    }
}
