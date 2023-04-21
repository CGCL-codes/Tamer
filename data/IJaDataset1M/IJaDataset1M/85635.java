package com.gotopweb.sgzbt.dao;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.gotopweb.sgzbt.pojos.SrcOaMessage;

/**
 * A data access object (DAO) providing persistence and search support for
 * SrcOaMessage entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.gotopweb.sgzbt.pojos.SrcOaMessage
 * @author MyEclipse Persistence Tools
 */
public class SrcOaMessageDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(SrcOaMessageDAO.class);

    public static final String EMPLOYEE_CODE = "employeeCode";

    public static final String EMPLOYEE_NAME = "employeeName";

    public static final String DEPT_CODE = "deptCode";

    public static final String DEPT_NAME = "deptName";

    public static final String VENDER_NAME = "venderName";

    public static final String MESSAGE_TITLE = "messageTitle";

    public static final String MOBILE = "mobile";

    public static final String CONSIGN_TITLE = "consignTitle";

    public static final String CONSIGN_NUMBER = "consignNumber";

    protected void initDao() {
    }

    public void save(SrcOaMessage transientInstance) {
        log.debug("saving SrcOaMessage instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(SrcOaMessage persistentInstance) {
        log.debug("deleting SrcOaMessage instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public SrcOaMessage findById(java.util.Date id) {
        log.debug("getting SrcOaMessage instance with id: " + id);
        try {
            SrcOaMessage instance = (SrcOaMessage) getHibernateTemplate().get("com.gotopweb.sgzbt.pojos.SrcOaMessage", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByExample(SrcOaMessage instance) {
        log.debug("finding SrcOaMessage instance by example");
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
        log.debug("finding SrcOaMessage instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from SrcOaMessage as model where model." + propertyName + "= ?";
            return getHibernateTemplate().find(queryString, value);
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findByEmployeeCode(Object employeeCode) {
        return findByProperty(EMPLOYEE_CODE, employeeCode);
    }

    public List findByEmployeeName(Object employeeName) {
        return findByProperty(EMPLOYEE_NAME, employeeName);
    }

    public List findByDeptCode(Object deptCode) {
        return findByProperty(DEPT_CODE, deptCode);
    }

    public List findByDeptName(Object deptName) {
        return findByProperty(DEPT_NAME, deptName);
    }

    public List findByVenderName(Object venderName) {
        return findByProperty(VENDER_NAME, venderName);
    }

    public List findByMessageTitle(Object messageTitle) {
        return findByProperty(MESSAGE_TITLE, messageTitle);
    }

    public List findByMobile(Object mobile) {
        return findByProperty(MOBILE, mobile);
    }

    public List findByConsignTitle(Object consignTitle) {
        return findByProperty(CONSIGN_TITLE, consignTitle);
    }

    public List findByConsignNumber(Object consignNumber) {
        return findByProperty(CONSIGN_NUMBER, consignNumber);
    }

    public List findAll() {
        log.debug("finding all SrcOaMessage instances");
        try {
            String queryString = "from SrcOaMessage";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public SrcOaMessage merge(SrcOaMessage detachedInstance) {
        log.debug("merging SrcOaMessage instance");
        try {
            SrcOaMessage result = (SrcOaMessage) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(SrcOaMessage instance) {
        log.debug("attaching dirty SrcOaMessage instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(SrcOaMessage instance) {
        log.debug("attaching clean SrcOaMessage instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public static SrcOaMessageDAO getFromApplicationContext(ApplicationContext ctx) {
        return (SrcOaMessageDAO) ctx.getBean("SrcOaMessageDAO");
    }
}
