package com.marcinjunger.utils.hibernate.database;

import java.util.List;
import javax.naming.InitialContext;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class Dic.
 * 
 * @see com.marcinjunger.utils.hibernate.database.Dic
 * @author Marcin Junger, mjunger@hornet.eu.org
 */
public class DicHome {

    private final SessionFactory sessionFactory = getSessionFactory();

    protected SessionFactory getSessionFactory() {
        try {
            return (SessionFactory) new InitialContext().lookup("SessionFactory");
        } catch (Exception e) {
            throw new IllegalStateException("Could not locate SessionFactory in JNDI");
        }
    }

    public void persist(Dic transientInstance) {
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public void attachDirty(Dic instance) {
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public void attachClean(Dic instance) {
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public void delete(Dic persistentInstance) {
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public Dic merge(Dic detachedInstance) {
        try {
            Dic result = (Dic) sessionFactory.getCurrentSession().merge(detachedInstance);
            return result;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public Dic findById(int id) {
        try {
            Dic instance = (Dic) sessionFactory.getCurrentSession().get("com.marcinjunger.simplecrm.hibernate.Dic", id);
            return instance;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Dic> findByExample(Dic instance) {
        try {
            List<Dic> results = (List<Dic>) sessionFactory.getCurrentSession().createCriteria("com.marcinjunger.simplecrm.hibernate.Dic").add(create(instance)).list();
            return results;
        } catch (RuntimeException re) {
            throw re;
        }
    }
}
