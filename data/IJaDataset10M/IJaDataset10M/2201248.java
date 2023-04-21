package com.ecs.etrade.da;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * Account entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.ecs.etrade.da.Account
 * @author MyEclipse Persistence Tools
 */
public class AccountDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(AccountDAO.class);

    public static final String VERSION = "version";

    public static final String ACTIVE_FLAG = "activeFlag";

    public static final String ACCOUNT_HOLDER = "accountHolder";

    public static final String DP_ID = "dpId";

    public static final String DP_NAME = "dpName";

    public static final String BANK_ACCOUNT = "bankAccount";

    public static final String PAN_CARD = "panCard";

    public static final String LOAN_INTEREST_RATE = "loanInterestRate";

    public static final String GRACE_PERIOD = "gracePeriod";

    protected void initDao() {
    }

    public void save(Account transientInstance) {
        log.debug("saving Account instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(Account persistentInstance) {
        log.debug("deleting Account instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public Account findById(java.lang.String id) {
        log.debug("getting Account instance with id: " + id);
        try {
            Account instance = (Account) getHibernateTemplate().get("com.ecs.etrade.da.Account", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByExample(Account instance) {
        log.debug("finding Account instance by example");
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
        log.debug("finding Account instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from Account as model where model." + propertyName + "= ?";
            return getHibernateTemplate().find(queryString, value);
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findByVersion(Object version) {
        return findByProperty(VERSION, version);
    }

    public List findByActiveFlag(Object activeFlag) {
        return findByProperty(ACTIVE_FLAG, activeFlag);
    }

    public List findByAccountHolder(Object accountHolder) {
        return findByProperty(ACCOUNT_HOLDER, accountHolder);
    }

    public List findByDpId(Object dpId) {
        return findByProperty(DP_ID, dpId);
    }

    public List findByDpName(Object dpName) {
        return findByProperty(DP_NAME, dpName);
    }

    public List findByBankAccount(Object bankAccount) {
        return findByProperty(BANK_ACCOUNT, bankAccount);
    }

    public List findByPanCard(Object panCard) {
        return findByProperty(PAN_CARD, panCard);
    }

    public List findByLoanInterestRate(Object loanInterestRate) {
        return findByProperty(LOAN_INTEREST_RATE, loanInterestRate);
    }

    public List findByGracePeriod(Object gracePeriod) {
        return findByProperty(GRACE_PERIOD, gracePeriod);
    }

    public List findAll() {
        log.debug("finding all Account instances");
        try {
            String queryString = "from Account";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public Account merge(Account detachedInstance) {
        log.debug("merging Account instance");
        try {
            Account result = (Account) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Account instance) {
        log.debug("attaching dirty Account instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(Account instance) {
        log.debug("attaching clean Account instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public static AccountDAO getFromApplicationContext(ApplicationContext ctx) {
        return (AccountDAO) ctx.getBean("AccountDAO");
    }
}
