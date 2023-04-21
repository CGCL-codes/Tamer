package it.uniromadue.portaleuni.dao;

import it.uniromadue.portaleuni.dto.PeriodicitaCorsi;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * PeriodicitaCorsi entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see it.uniromadue.portaleuni.dao.PeriodicitaCorsi
 * @author MyEclipse Persistence Tools
 */
public class PeriodicitaCorsiDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(PeriodicitaCorsiDAO.class);

    protected void initDao() {
    }

    public void save(PeriodicitaCorsi transientInstance) {
        log.debug("saving PeriodicitaCorsi instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(PeriodicitaCorsi persistentInstance) {
        log.debug("deleting PeriodicitaCorsi instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public PeriodicitaCorsi findById(java.lang.Integer id) {
        log.debug("getting PeriodicitaCorsi instance with id: " + id);
        try {
            PeriodicitaCorsi instance = (PeriodicitaCorsi) getHibernateTemplate().get("it.uniromadue.portaleuni.dto.PeriodicitaCorsi", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByExample(PeriodicitaCorsi instance) {
        log.debug("finding PeriodicitaCorsi instance by example");
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
        log.debug("finding PeriodicitaCorsi instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from PeriodicitaCorsi as model where model." + propertyName + "= ?";
            return getHibernateTemplate().find(queryString, value);
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findAll() {
        log.debug("finding all PeriodicitaCorsi instances");
        try {
            String queryString = "from PeriodicitaCorsi";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public void attachDirty(PeriodicitaCorsi instance) {
        log.debug("attaching dirty PeriodicitaCorsi instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public static PeriodicitaCorsiDAO getFromApplicationContext(ApplicationContext ctx) {
        return (PeriodicitaCorsiDAO) ctx.getBean("PeriodicitaCorsiDAO");
    }
}
