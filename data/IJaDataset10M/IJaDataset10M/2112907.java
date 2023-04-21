package net.sourceforge.eci.integration;

import java.util.List;
import net.sourceforge.eci.dao.hibernate.UniversalDaoHibernate;
import org.displaytag.properties.SortOrderEnum;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

public class PagingLookupDaoHibernate extends UniversalDaoHibernate implements PagingLookupDao {

    @SuppressWarnings("unchecked")
    public int getAllRecordsCount(Class clazz) {
        DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
        criteria.setProjection(Projections.rowCount());
        List results = getHibernateTemplate().findByCriteria(criteria);
        int count = ((Integer) results.get(0)).intValue();
        return count;
    }

    @SuppressWarnings("unchecked")
    public List getAllRecordsPage(Class clazz, int firstResult, int maxResults, SortOrderEnum sortDirection, String sortCriterion) {
        DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
        if (sortCriterion != null) {
            if (sortDirection.equals(SortOrderEnum.ASCENDING)) {
                criteria.addOrder(Order.asc(sortCriterion));
            }
            if (sortDirection.equals(SortOrderEnum.DESCENDING)) {
                criteria.addOrder(Order.desc(sortCriterion));
            }
        }
        List results = getHibernateTemplate().findByCriteria(criteria, firstResult, maxResults);
        return results;
    }
}
