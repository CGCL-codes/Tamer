package ua.eshop.dao;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.eshop.data.NewsMessage;
import ua.eshop.data.Order;
import ua.eshop.data.User;

@Repository
public class NewsDao extends BaseDao<NewsMessage> {

    @Override
    protected Class<NewsMessage> getEntityClass() {
        return NewsMessage.class;
    }

    @SuppressWarnings({ "unused", "unchecked" })
    @Transactional(propagation = Propagation.REQUIRED)
    public Collection<NewsMessage> getExpiredNews() {
        Session session = (Session) getEntityManager().getDelegate();
        DetachedCriteria dcAllUsersThatHaveDocuments = DetachedCriteria.forClass(NewsMessage.class);
        dcAllUsersThatHaveDocuments.setProjection(Projections.distinct(Projections.property("createdBy.username")));
        Criteria critAllUsersThatHaveDocuments = dcAllUsersThatHaveDocuments.getExecutableCriteria(session);
        DetachedCriteria dcAllUsersThatHaveNewDocuments = DetachedCriteria.forClass(NewsMessage.class);
        Calendar currentMinusMonth = Calendar.getInstance();
        currentMinusMonth.add(Calendar.MONTH, -1);
        dcAllUsersThatHaveNewDocuments.add(Restrictions.ge("dateAdded", currentMinusMonth.getTime()));
        dcAllUsersThatHaveNewDocuments.setProjection(Projections.distinct(Projections.property("createdBy.username")));
        Criteria critAllUsersThatHaveNewDocuments = dcAllUsersThatHaveNewDocuments.getExecutableCriteria(session);
        DetachedCriteria dcAllUsersThatDontHaveNewDocumentsButHaveDocuments = DetachedCriteria.forClass(User.class);
        dcAllUsersThatDontHaveNewDocumentsButHaveDocuments.add(Restrictions.not(Restrictions.in("username", critAllUsersThatHaveNewDocuments.list())));
        dcAllUsersThatDontHaveNewDocumentsButHaveDocuments.add(Restrictions.in("username", critAllUsersThatHaveDocuments.list()));
        dcAllUsersThatDontHaveNewDocumentsButHaveDocuments.setProjection(Projections.distinct(Projections.property("username")));
        Criteria critAllUsersThatDontHaveNewDocumentsButHaveDocuments = dcAllUsersThatDontHaveNewDocumentsButHaveDocuments.getExecutableCriteria(session);
        DetachedCriteria dcOrders = DetachedCriteria.forClass(Order.class);
        Disjunction disjunction = Restrictions.disjunction();
        disjunction.add(Restrictions.in("customer.username", critAllUsersThatHaveNewDocuments.list()));
        Conjunction conjunction = Restrictions.conjunction();
        conjunction.add(Restrictions.not(Restrictions.in("customer.username", critAllUsersThatHaveDocuments.list())));
        conjunction.add(Restrictions.le("deliveryDate", currentMinusMonth.getTime()));
        disjunction.add(conjunction);
        dcOrders.add(disjunction);
        Criteria critOrders = dcOrders.getExecutableCriteria(session);
        return critOrders.list();
    }
}
