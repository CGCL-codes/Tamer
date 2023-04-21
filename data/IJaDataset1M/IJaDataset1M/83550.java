package it.gestioneimmobili.hibernate.dao;

import it.gestioneimmobili.hibernate.HibernateSessionFactory;
import it.gestioneimmobili.hibernate.bean.Utente;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;

public class UtenteDAO implements IBaseHibernateDAO {

    private static final Log log = LogFactory.getLog(UtenteDAO.class);

    public static final String USERNAME = "loginn";

    public static final String PASSWORD = "passwd";

    public Session getSession() {
        return HibernateSessionFactory.getSession();
    }

    public void save(Utente transientInstance) {
        log.debug("saving Utente instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void update(Utente transientInstance) {
        log.debug("saving Utente instance");
        try {
            getSession().update(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(Utente persistentInstance) {
        log.debug("deleting Utente instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public Utente findById(java.lang.Integer id) {
        log.debug("getting Utente instance with id: " + id);
        try {
            Utente instance = (Utente) getSession().get("it.gestioneimmobili.hibernate.bean.Utente", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByExample(Utente instance) {
        log.debug("finding Fitto instance by example");
        try {
            List results = getSession().createCriteria("it.gestioneimmobili.hibernate.bean.Utente").add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding Utente instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from Utente as model where model." + propertyName + "= ?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findByUsername(Object condominio) {
        return findByProperty(USERNAME, condominio);
    }

    public List findByPassword(Object cauzione) {
        return findByProperty(PASSWORD, cauzione);
    }

    public List findAll() {
        log.debug("finding all Utente instances");
        try {
            String queryString = "from Utente";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public Utente merge(Utente detachedInstance) {
        log.debug("merging Utente instance");
        try {
            Utente result = (Utente) getSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Utente instance) {
        log.debug("attaching dirty Fitto instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(Utente instance) {
        log.debug("attaching clean Fitto instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public Integer selectByLoginName(String login, String password) {
        Integer result = null;
        try {
            Query q = getSession().getNamedQuery("select.utente").setString("password", password).setString("login", login);
            result = (Integer) q.uniqueResult();
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
        return result;
    }
}
