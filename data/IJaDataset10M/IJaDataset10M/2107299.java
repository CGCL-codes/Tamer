package com.rb.ft.database.dao.impl;

import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.rb.ft.database.dao.IFtScoreDAO;
import com.rb.ft.database.pojo.FtScore;

/**
 * @类功能说明: 积分DAO implementation
 * @类修改者:     
 * @修改日期:   
 * @修改说明:   
 * @作者:       robin
 * @创建时间:   2011-8-30 下午01:32:31
 * @版本:       1.0.0
 */
public class FtScoreDAOImpl extends HibernateDaoSupport implements IFtScoreDAO {

    private static final Logger log = LoggerFactory.getLogger(FtScoreDAOImpl.class);

    public static final String RANK = "rank";

    public static final String GAMES = "games";

    public static final String WIN = "win";

    public static final String TIE = "tie";

    public static final String LOSE = "lose";

    public static final String GOAL = "goal";

    public static final String GOALLOSE = "goallose";

    public static final String GOALWIN = "goalwin";

    public static final String SCORE = "score";

    protected void initDao() {
    }

    public void save(FtScore transientInstance) {
        log.debug("saving FtScore instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(FtScore persistentInstance) {
        log.debug("deleting FtScore instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public FtScore findById(java.lang.Long id) {
        log.debug("getting FtScore instance with id: " + id);
        try {
            FtScore instance = (FtScore) getHibernateTemplate().get("FtScore", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public FtScore merge(FtScore detachedInstance) {
        log.debug("merging FtScore instance");
        try {
            FtScore result = (FtScore) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(FtScore instance) {
        log.debug("attaching dirty FtScore instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(FtScore instance) {
        log.debug("attaching clean FtScore instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public static FtScoreDAOImpl getFromApplicationContext(ApplicationContext ctx) {
        return (FtScoreDAOImpl) ctx.getBean("FtScoreDAO");
    }
}
