package com.vi.dao;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.vi.pojo.SvgFile;
import com.vi.pojo.SvgFileId;

/**
 * A data access object (DAO) providing persistence and search support for
 * SvgFile entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.vi.pojo.SvgFile
 * @author MyEclipse Persistence Tools
 */
public class SvgFileDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(SvgFileDAO.class);

    protected void initDao() {
    }

    public void save(SvgFile transientInstance) {
        log.debug("saving SvgFile instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(SvgFile persistentInstance) {
        log.debug("deleting SvgFile instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public SvgFile findById(com.vi.pojo.SvgFileId id) {
        log.debug("getting SvgFile instance with id: " + id);
        try {
            SvgFile instance = (SvgFile) getHibernateTemplate().get("com.vi.pojo.SvgFile", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByExample(SvgFile instance) {
        log.debug("finding SvgFile instance by example");
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
        log.debug("finding SvgFile instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from SvgFile as model where model." + propertyName + "= ?";
            return getHibernateTemplate().find(queryString, value);
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findAll() {
        log.debug("finding all SvgFile instances");
        try {
            String queryString = "from SvgFile";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public SvgFile merge(SvgFile detachedInstance) {
        log.debug("merging SvgFile instance");
        try {
            SvgFile result = (SvgFile) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(SvgFile instance) {
        log.debug("attaching dirty SvgFile instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(SvgFile instance) {
        log.debug("attaching clean SvgFile instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public static SvgFileDAO getFromApplicationContext(ApplicationContext ctx) {
        return (SvgFileDAO) ctx.getBean("SvgFileDAO");
    }
}
