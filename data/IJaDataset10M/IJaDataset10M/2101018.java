package it.uniromadue.portaleuni.dao;

import it.uniromadue.portaleuni.dto.Utenti;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * Utenti entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see it.uniromadue.portaleuni.dao.Utenti
 * @author MyEclipse Persistence Tools
 */
public class UtentiDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(UtentiDAO.class);

    protected void initDao() {
    }

    public void save(Utenti transientInstance) {
        log.debug("saving Utenti instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(Utenti persistentInstance) {
        log.debug("deleting Utenti instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public Utenti findById(java.lang.Integer id) {
        log.debug("getting Utenti instance with id: " + id);
        try {
            Utenti instance = (Utenti) getHibernateTemplate().get("it.uniromadue.portaleuni.dto.Utenti", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByExample(Utenti instance) {
        log.debug("finding Utenti instance by example");
        try {
            List results = getHibernateTemplate().findByExample(instance);
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding Utenti instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from Utenti as model where model." + propertyName + "= ?";
            return getHibernateTemplate().find(queryString, value);
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findAll() {
        log.debug("finding all Utenti instances");
        try {
            String queryString = "from Utenti";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public List findAllUtenti() {
        Session session = getSessionFactory().openSession();
        try {
            String queryString = "SELECT utenti.nome FROM Utenti utenti";
            Query query = session.createQuery(queryString);
            return (ArrayList) query.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        } finally {
            session.close();
        }
    }

    public void attachDirty(Utenti instance) {
        log.debug("attaching dirty Utenti instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(Utenti instance) {
        log.debug("attaching clean Utenti instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public static UtentiDAO getFromApplicationContext(ApplicationContext ctx) {
        return (UtentiDAO) ctx.getBean("UtentiDAO");
    }

    public List findAllByTipoUtente(String[] types) {
        Session session = getSessionFactory().openSession();
        try {
            String queryString = "SELECT utenti FROM Utenti utenti";
            if (types.length > 0) {
                queryString += " where (";
                for (int i = 0; i < types.length; i++) {
                    if (i > 0) queryString += " or ";
                    queryString += "(utenti.tipoUtente = '" + types[i] + "')";
                }
                queryString += ")";
            }
            Query query = session.createQuery(queryString);
            return (ArrayList) query.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        } finally {
            session.close();
        }
    }

    public Integer getLastID() {
        Session session = getSessionFactory().openSession();
        try {
            String queryString = "SELECT utenti FROM Utenti utenti " + "order by utenti.idUtente desc";
            Query query = session.createQuery(queryString);
            List utenti = query.list();
            if (utenti.size() > 0) return ((Utenti) utenti.get(0)).getIdUtente(); else return new Integer(utenti.size());
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        } finally {
            session.close();
        }
    }

    public List findUtenti(String[] types, String nome, String cognome, String username) {
        Session session = getSessionFactory().openSession();
        try {
            String queryString = "SELECT utenti FROM Utenti utenti";
            queryString += " where 1=1";
            if (types.length > 0) {
                queryString += " and (";
                for (int i = 0; i < types.length; i++) {
                    if (i > 0) queryString += " or ";
                    queryString += "(utenti.tipoUtente = '" + types[i] + "')";
                }
                queryString += ")";
            }
            if (nome != null && !(nome.length() == 0)) queryString += " and utenti.nome = '" + nome + "'";
            if (cognome != null && !(cognome.length() == 0)) queryString += " and utenti.cognome = '" + cognome + "'";
            if (username != null && !(username.length() == 0)) queryString += " and utenti.username = '" + username + "'";
            Query query = session.createQuery(queryString);
            return (ArrayList) query.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        } finally {
            session.close();
        }
    }
}
