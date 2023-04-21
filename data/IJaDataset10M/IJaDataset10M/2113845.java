package at.campus02.datapit.core.dao.hibernate;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.util.Assert;

/**
 * Code from
 * http://raykrueger.blogspot.com/2007/09/adding-generics-to-abstracthibernatedao.html
 * 
 * @author Ray Krueger
 */
public abstract class AbstractHibernateDao<E> {

    private final Class<E> entityClass;

    private final SessionFactory sessionFactory;

    public AbstractHibernateDao(Class<E> entityClass, SessionFactory sessionFactory) {
        Assert.notNull(entityClass, "entityClass must not be null");
        Assert.notNull(sessionFactory, "sessionFactory must not be null");
        this.entityClass = entityClass;
        this.sessionFactory = sessionFactory;
    }

    protected Criteria criteria() {
        return currentSession().createCriteria(entityClass);
    }

    protected Query query(String hql) {
        return currentSession().createQuery(hql);
    }

    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    protected List<E> all() {
        return list(criteria());
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    @SuppressWarnings("unchecked")
    protected List<E> list(Criteria criteria) {
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    protected List<E> list(Query query) {
        return query.list();
    }

    @SuppressWarnings("unchecked")
    protected E uniqueResult(Criteria criteria) {
        return (E) criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    protected E uniqueResult(Query query) {
        return (E) query.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    protected E get(Serializable id) {
        return (E) currentSession().get(entityClass, id);
    }
}
