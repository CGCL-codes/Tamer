package net.ppcos.hibernate.dao;

import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 	* A data access object (DAO) providing persistence and search support for Image entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see net.ppcos.hibernate.dao.Image
  * @author MyEclipse Persistence Tools 
 */
public class ImageDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(ImageDAO.class);

    public static final String ADDRESS = "address";

    protected void initDao() {
    }

    public void save(Image transientInstance) {
        log.debug("saving Image instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(Image persistentInstance) {
        log.debug("deleting Image instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public Image findById(java.lang.Long id) {
        log.debug("getting Image instance with id: " + id);
        try {
            Image instance = (Image) getHibernateTemplate().get("net.ppcos.hibernate.dao.Image", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByExample(Image instance) {
        log.debug("finding Image instance by example");
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
        log.debug("finding Image instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from Image as model where model." + propertyName + "= ?";
            return getHibernateTemplate().find(queryString, value);
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findByAddress(Object address) {
        return findByProperty(ADDRESS, address);
    }

    public List findAll() {
        log.debug("finding all Image instances");
        try {
            String queryString = "from Image";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public Image merge(Image detachedInstance) {
        log.debug("merging Image instance");
        try {
            Image result = (Image) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Image instance) {
        log.debug("attaching dirty Image instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(Image instance) {
        log.debug("attaching clean Image instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public static ImageDAO getFromApplicationContext(ApplicationContext ctx) {
        return (ImageDAO) ctx.getBean("ImageDAO");
    }
}
