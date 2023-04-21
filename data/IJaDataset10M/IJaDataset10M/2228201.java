package com.rb.ft.database.dao.impl;

import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.rb.ft.database.dao.IFtGameShooterDAO;
import com.rb.ft.database.pojo.FtGameShooter;
import com.rb.ft.database.pojo.FtGameShooterId;

/**
 * @类功能说明: 赛事记录人员DAO implementation
 * @类修改者:     
 * @修改日期:   
 * @修改说明:   
 * @作者:       robin
 * @创建时间:   2011-8-30 下午01:28:46
 * @版本:       1.0.0
 */
public class FtGameShooterDAOImpl extends HibernateDaoSupport implements IFtGameShooterDAO {

    private static final Logger log = LoggerFactory.getLogger(FtGameShooterDAOImpl.class);

    public static final String NUMBER = "number";

    protected void initDao() {
    }

    public void save(FtGameShooter transientInstance) {
        log.debug("saving FtGameShooter instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(FtGameShooter persistentInstance) {
        log.debug("deleting FtGameShooter instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public FtGameShooter findById(FtGameShooterId id) {
        log.debug("getting FtGameShooter instance with id: " + id);
        try {
            FtGameShooter instance = (FtGameShooter) getHibernateTemplate().get("FtGameShooter", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public FtGameShooter merge(FtGameShooter detachedInstance) {
        log.debug("merging FtGameShooter instance");
        try {
            FtGameShooter result = (FtGameShooter) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(FtGameShooter instance) {
        log.debug("attaching dirty FtGameShooter instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(FtGameShooter instance) {
        log.debug("attaching clean FtGameShooter instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public static FtGameShooterDAOImpl getFromApplicationContext(ApplicationContext ctx) {
        return (FtGameShooterDAOImpl) ctx.getBean("FtGameShooterDAO");
    }
}
