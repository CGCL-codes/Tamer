package com.tysanclan.site.projectewok.dataaccess;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.jeroensteenbeeke.hyperion.data.DomainObject;
import com.jeroensteenbeeke.hyperion.data.HibernateDAO;

/**
 * A Data Access Object that uses the Hibernate ORM package
 * 
 * @author Jeroen Steenbeeke
 * @param <T>
 *            The domain object loadable by this DAO
 */
@SuppressWarnings("unchecked")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public abstract class EwokHibernateDAO<T extends DomainObject> extends HibernateDAO<T> implements EwokDAO<T> {

    private Class<T> domainClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    private SessionFactory sessionFactory;

    /**
	 * Get a list of all objects
	 * 
	 * @return The list of objects, which may be empty
	 */
    @Override
    public List<T> findAll() {
        Criteria crit = getSession().createCriteria(domainClass);
        return crit.list();
    }

    /**
	 * @return The Session for this factory
	 */
    @Override
    public Session getSession() {
        Session session = sessionFactory.getCurrentSession();
        session.setFlushMode(FlushMode.COMMIT);
        return session;
    }

    /**
	 * @return The session factory
	 */
    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public T load(Serializable id) {
        return (T) getSession().load(domainClass, id);
    }

    @Override
    public T get(Serializable id) {
        return (T) getSession().get(domainClass, id);
    }

    /**
	 * Save the indicated object
	 * 
	 * @param object
	 *            The object to save
	 */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void save(T object) {
        getSession().save(object);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(T object) {
        getSession().update(object);
    }

    /**
	 * Sets the session factory of this DAO
	 * 
	 * @param sessionFactory
	 *            The factory to set
	 */
    @Override
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
