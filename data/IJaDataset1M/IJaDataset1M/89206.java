package org.ss.psci.dao;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.ss.psci.entity.PmAssignment;
import org.ss.psci.entity.PmBid;

/**
 * Data access object (DAO) for domain model class PmBid.
 * 
 * @see org.ss.psci.entity.PmBid
 * @author MyEclipse Persistence Tools
 */
public class PmBidDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(PmBidDAO.class);

    public static final String BID_SEQ = "bidSeq";

    public static final String PROJECT_NAME = "projectName";

    public static final String ADDRESS = "address";

    public static final String COMPANY = "company";

    public static final String PHONE = "phone";

    public static final String BID_TIME = "bidTime";

    public static final String START_TIME = "startTime";

    public static final String END_TIME = "endTime";

    public static final String PRICE = "price";

    public static final String REMARK = "remark";

    public static final String MANAGER = "manager";

    public static final String FILEPATH = "filepath";

    public static final String STATUS = "status";

    public PmBid copyEntity(PmBid dest, PmBid orig) {
        if (dest == null || orig == null) {
            return new PmBid();
        }
        if (orig.getProjectName() != null) {
            dest.setProjectName(orig.getProjectName());
        }
        if (orig.getStatus() != null) {
            dest.setStatus(orig.getStatus());
        }
        if (orig.getCompany() != null) {
            dest.setCompany(orig.getCompany());
        }
        if (orig.getBidTime() != null) {
            dest.setBidTime(orig.getBidTime());
        }
        if (orig.getStartTime() != null) {
            dest.setStartTime(orig.getStartTime());
        }
        if (orig.getFilepath() != null) {
            dest.setFilepath(orig.getFilepath());
        }
        if (orig.getEndTime() != null) {
            dest.setEndTime(orig.getEndTime());
        }
        if (orig.getRemark() != null) {
            dest.setRemark(orig.getRemark());
        }
        if (orig.getPrice() != null) {
            dest.setPrice(orig.getPrice());
        }
        if (orig.getManager() != null) {
            dest.setManager(orig.getManager());
        }
        return dest;
    }

    protected void initDao() {
    }

    public void save(PmBid transientInstance) {
        log.debug("saving PmBid instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(PmBid persistentInstance) {
        log.debug("deleting PmBid instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public PmBid findById(java.lang.Integer id) {
        log.debug("getting PmBid instance with id: " + id);
        try {
            PmBid instance = (PmBid) getHibernateTemplate().get("org.ss.psci.entity.PmBid", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByExample(PmBid instance) {
        log.debug("finding PmBid instance by example");
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
        log.debug("finding PmBid instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from PmBid as model where model." + propertyName + "= ?";
            return getHibernateTemplate().find(queryString, value);
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findByBidSeq(Object bidSeq) {
        return findByProperty(BID_SEQ, bidSeq);
    }

    public List findByProjectName(Object projectName) {
        return findByProperty(PROJECT_NAME, projectName);
    }

    public List findByAddress(Object address) {
        return findByProperty(ADDRESS, address);
    }

    public List findByCompany(Object company) {
        return findByProperty(COMPANY, company);
    }

    public List findByPhone(Object phone) {
        return findByProperty(PHONE, phone);
    }

    public List findByBidTime(Object bidTime) {
        return findByProperty(BID_TIME, bidTime);
    }

    public List findByStartTime(Object startTime) {
        return findByProperty(START_TIME, startTime);
    }

    public List findByEndTime(Object endTime) {
        return findByProperty(END_TIME, endTime);
    }

    public List findByPrice(Object price) {
        return findByProperty(PRICE, price);
    }

    public List findByRemark(Object remark) {
        return findByProperty(REMARK, remark);
    }

    public List findByManager(Object manager) {
        return findByProperty(MANAGER, manager);
    }

    public List findByFilepath(Object filepath) {
        return findByProperty(FILEPATH, filepath);
    }

    public List findByStatus(Object status) {
        return findByProperty(STATUS, status);
    }

    public List findAll() {
        log.debug("finding all PmBid instances");
        try {
            String queryString = "from PmBid";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public PmBid merge(PmBid detachedInstance) {
        log.debug("merging PmBid instance");
        try {
            PmBid result = (PmBid) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(PmBid instance) {
        log.debug("attaching dirty PmBid instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(PmBid instance) {
        log.debug("attaching clean PmBid instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public static PmBidDAO getFromApplicationContext(ApplicationContext ctx) {
        return (PmBidDAO) ctx.getBean("PmBidDAO");
    }
}
