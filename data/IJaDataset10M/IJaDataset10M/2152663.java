package com.tysanclan.site.projectewok.entities.dao.hibernate;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.MembershipStatusChange;
import com.tysanclan.site.projectewok.entities.dao.MembershipStatusChangeDAO;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class MembershipStatusChangeDAOImpl extends EwokHibernateDAO<MembershipStatusChange> implements com.tysanclan.site.projectewok.entities.dao.MembershipStatusChangeDAO {

    @Override
    protected Criteria createCriteria(SearchFilter<MembershipStatusChange> filter) {
        return getSession().createCriteria(MembershipStatusChangeDAO.class);
    }

    @Override
    public SortedMap<Date, Integer> getMutationsByDate(Date start, Date end) {
        Criteria criteria = getSession().createCriteria(MembershipStatusChange.class);
        if (start != null) criteria.add(Restrictions.ge("changeTime", start));
        if (end != null) criteria.add(Restrictions.le("changeTime", end));
        criteria.setProjection(Projections.projectionList().add(Projections.property("changeTime")).add(Projections.sum("memberSizeMutation")));
        List<Object[]> results = listOf(criteria);
        SortedMap<Date, Integer> map = new TreeMap<Date, Integer>(new Comparator<Date>() {

            @Override
            public int compare(Date d1, Date d2) {
                return -d1.compareTo(d2);
            }
        });
        for (Object[] res : results) {
            Date date = (Date) res[0];
            Integer sum = (Integer) res[1];
            map.put(date, sum);
        }
        return map;
    }
}
