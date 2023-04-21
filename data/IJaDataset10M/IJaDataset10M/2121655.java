package artem.finance.server.dao;

import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;
import artem.finance.server.dao.util.AppContext;
import artem.finance.server.persist.Persistancible;

public class GenericDaoImpl implements GenericDaoI {

    public static final Logger LOG = Logger.getLogger(GenericDaoImpl.class);

    @SuppressWarnings("unchecked")
    private Class persistentClass;

    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public GenericDaoImpl(Class persistentClass) {
        this.persistentClass = persistentClass;
        ApplicationContext ctx = AppContext.getApplicationContext();
        LOG.debug("Before setting session factory" + ctx);
        if (ctx != null) {
            sessionFactory = (SessionFactory) ctx.getBean("sessionFactory");
        }
        LOG.debug("After setting SF");
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        LOG.info("!!!Session Factory was created in hibernate generic dao!!!");
    }

    public SessionFactory getSessionFactory() {
        LOG.info("!!!Session Factory was returned from hibernate generic dao!!!");
        return this.sessionFactory;
    }

    public synchronized Persistancible findById(Long id) {
        Session session = this.sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Persistancible object = (Persistancible) session.get(getPersistentClass(), id);
            tx.commit();
            return object;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public synchronized void saveOrUpdate(Persistancible object) {
        Session session = this.sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(object);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public synchronized void save(Persistancible object) {
        Session session = this.sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(object);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public synchronized void delete(Persistancible object) {
        Session session = this.sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(object);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized List<Object> findAll() {
        Session session = this.sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(getPersistentClass());
            List objects = criteria.list();
            tx.commit();
            return objects;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    /**
	 * @return the persistentClass
	 */
    @SuppressWarnings("unchecked")
    public Class getPersistentClass() {
        return persistentClass;
    }
}
