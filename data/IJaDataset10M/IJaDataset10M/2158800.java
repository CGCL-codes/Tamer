package com.gdpu.project.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.gdpu.page.vo.QueryResult;
import com.gdpu.project.dao.CgInnovateDao;
import com.gdpu.project.vo.CgConference;
import com.gdpu.project.vo.CgInnovate;

/**
 * A data access object (DAO) providing persistence and search support for
 * CgInnovate entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.gdpu.project.vo.CgInnovate
 * @author MyEclipse Persistence Tools
 */
public class CgInnovateDaoImpl extends HibernateDaoSupport implements CgInnovateDao {

    private static final Log log = LogFactory.getLog(CgInnovateDaoImpl.class);

    public static final String INNOVATE_ID = "innovateId";

    public static final String TEACHER_ID = "teacherId";

    public static final String INNOVATE_NAME = "innovateName";

    public static final String RANKING = "ranking";

    public static final String PRIZE_LEVEL = "prizeLevel";

    public static final String KE_YAN_FEN = "keYanFen";

    public static final String COMMENT = "comment";

    public static final String TEACHER_NAME = "teacherName";

    public static final String ITEM_ID = "itemId";

    public static final String ITEM_NAME = "itemName";

    protected void initDao() {
    }

    public void save(CgInnovate transientInstance) {
        log.debug("saving CgInnovate instance");
        try {
            this.getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(CgInnovate persistentInstance) {
        log.debug("deleting CgInnovate instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public CgInnovate findById(java.lang.Integer id) {
        log.debug("getting CgInnovate instance with id: " + id);
        try {
            CgInnovate instance = (CgInnovate) getHibernateTemplate().get("com.gdpu.project.vo.CgInnovate", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public QueryResult<CgInnovate> findByExample(CgInnovate instance, int firstindex, int maxresult) {
        log.debug("finding CgInnovate instance by example");
        try {
            QueryResult<CgInnovate> queryResult = new QueryResult<CgInnovate>();
            List<CgInnovate> results = getHibernateTemplate().findByExample(instance).subList(firstindex, firstindex + maxresult - 1);
            queryResult.setResultlist(results);
            int count = getHibernateTemplate().findByExample(instance).size();
            queryResult.setTotalrecord(count);
            log.debug("find by example successful, result size: " + results.size());
            return queryResult;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public QueryResult<CgInnovate> findByProperty(String propertyName, Object value, int firstindex, int maxresult) {
        log.debug("finding CgInnovate instance with property: " + propertyName + ", value: " + value);
        try {
            QueryResult<CgInnovate> queryResult = new QueryResult<CgInnovate>();
            Query query1 = getSessionFactory().getCurrentSession().createQuery("from CgInnovate as model where model." + propertyName + " like :propertyName");
            Query query2 = getSessionFactory().getCurrentSession().createQuery("select count(*) from CgInnovate as model where model." + propertyName + " like :propertyName");
            if (value != null) {
                query1.setParameter("propertyName", value);
                query2.setParameter("propertyName", value);
            } else {
                query1.setParameter("propertyName", "%%");
                query2.setParameter("propertyName", "%%");
            }
            if (firstindex != -1 && maxresult != -1) {
                query1.setFirstResult(firstindex).setMaxResults(maxresult);
            }
            queryResult.setResultlist(query1.list());
            queryResult.setTotalrecord((Long) query2.uniqueResult());
            return queryResult;
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public QueryResult<CgInnovate> findByInnovateId(Object innovateId, int firstindex, int maxresult) {
        return findByProperty(INNOVATE_ID, innovateId, firstindex, maxresult);
    }

    public QueryResult<CgInnovate> findByTeacherId(Object teacherId, int firstindex, int maxresult) {
        return findByProperty(TEACHER_ID, teacherId, firstindex, maxresult);
    }

    public QueryResult<CgInnovate> findByTeacherId(Object teacherId) {
        return findByProperty(TEACHER_ID, teacherId, -1, -1);
    }

    public QueryResult<CgInnovate> findByInnovateName(Object innovateName, int firstindex, int maxresult) {
        return findByProperty(INNOVATE_NAME, innovateName, firstindex, maxresult);
    }

    public QueryResult<CgInnovate> findByRanking(Object ranking, int firstindex, int maxresult) {
        return findByProperty(RANKING, ranking, firstindex, maxresult);
    }

    public QueryResult<CgInnovate> findByPrizeLevel(Object prizeLevel, int firstindex, int maxresult) {
        return findByProperty(PRIZE_LEVEL, prizeLevel, firstindex, maxresult);
    }

    public QueryResult<CgInnovate> findByKeYanFen(Object keYanFen, int firstindex, int maxresult) {
        return findByProperty(KE_YAN_FEN, keYanFen, firstindex, maxresult);
    }

    public QueryResult<CgInnovate> findByComment(Object comment, int firstindex, int maxresult) {
        return findByProperty(COMMENT, comment, firstindex, maxresult);
    }

    public QueryResult<CgInnovate> findByTeacher(Object teacherName, int firstindex, int maxresult) {
        return findByProperty(TEACHER_NAME, teacherName, firstindex, maxresult);
    }

    public QueryResult<CgInnovate> findByItemId(Object itemId, int firstindex, int maxresult) {
        return findByProperty(ITEM_ID, itemId, firstindex, maxresult);
    }

    public QueryResult<CgInnovate> findByItemName(Object itemName, int firstindex, int maxresult) {
        return findByProperty(ITEM_NAME, itemName, firstindex, maxresult);
    }

    public QueryResult<CgInnovate> findAll(int firstindex, int maxresult) {
        log.debug("finding all CgInnovate instances");
        try {
            QueryResult<CgInnovate> queryResult = new QueryResult<CgInnovate>();
            Query query = getSessionFactory().getCurrentSession().createQuery("from CgInnovate cgInnovate");
            if (firstindex != -1 && maxresult != -1) {
                query.setFirstResult(firstindex).setMaxResults(maxresult);
            }
            queryResult.setResultlist(query.list());
            query = getSessionFactory().getCurrentSession().createQuery("select count(*) from CgInnovate cgInnovate");
            queryResult.setTotalrecord((Long) query.uniqueResult());
            return queryResult;
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public CgInnovate merge(CgInnovate detachedInstance) {
        log.debug("merging CgInnovate instance");
        try {
            CgInnovate result = (CgInnovate) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(CgInnovate instance) {
        log.debug("attaching dirty CgInnovate instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(CgInnovate instance) {
        log.debug("attaching clean CgInnovate instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public static CgInnovateDao getFromApplicationContext(ApplicationContext ctx) {
        return (CgInnovateDao) ctx.getBean("CgInnovateDAO");
    }

    @SuppressWarnings("unchecked")
    public List<CgInnovate> nfindByProperty(String propertyName, String value, Date begin, Date end) {
        log.debug("finding CgInnovate instance with property: " + propertyName + ", value: " + value);
        try {
            StringBuilder builder = new StringBuilder();
            String queryString = "from CgInnovate as model where model." + propertyName + "= ?";
            builder.append(queryString);
            if (begin != null) {
                builder.append(" and model.date>=? ");
            }
            if (end != null) {
                builder.append(" and model.date<? ");
            }
            List<Object> pramList = new ArrayList<Object>();
            if (value != null) {
                pramList.add(value);
            }
            if (begin != null) {
                pramList.add(begin);
            }
            if (end != null) {
                pramList.add(end);
            }
            return (List<CgInnovate>) getHibernateTemplate().find(builder.toString(), pramList.toArray());
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    /**
	 * 分页查询科技创新
	 */
    public QueryResult<CgInnovate> nfindByProperty(String propertyName, List<String> value, Date begin, Date end, int firstindex, int maxresult) {
        log.debug("finding CgInnovate instance with property: " + propertyName + ", value: " + value);
        try {
            QueryResult<CgInnovate> queryResult = new QueryResult<CgInnovate>();
            StringBuffer queryStr = new StringBuffer();
            queryStr.append("from CgInnovate as model where model." + propertyName + " in (:value)");
            if (begin != null) {
                queryStr.append(" and model.date>=:begin");
            }
            if (end != null) {
                queryStr.append(" and model.date<:end");
            }
            Query query = getSessionFactory().getCurrentSession().createQuery(queryStr.toString());
            query.setParameterList("value", value);
            if (begin != null) {
                query.setParameter("begin", begin);
            }
            if (end != null) {
                query.setParameter("end", end);
            }
            if (firstindex != -1 && maxresult != -1) {
                query.setFirstResult(firstindex).setMaxResults(maxresult);
            }
            if (value.size() > 0) {
                queryResult.setResultlist(query.list());
            }
            queryStr.delete(0, queryStr.length());
            queryStr.append("select count(*) from CgInnovate as model where model." + propertyName + " in (:value)");
            if (begin != null) {
                queryStr.append(" and model.date>=:begin");
            }
            if (end != null) {
                queryStr.append(" and model.date<:end");
            }
            query = getSessionFactory().getCurrentSession().createQuery(queryStr.toString());
            query.setParameterList("value", value);
            if (query.getQueryString().indexOf(" and model.date>=:begin") != -1) {
                query.setParameter("begin", begin);
            }
            if (query.getQueryString().indexOf(" and model.date<:end") != -1) {
                query.setParameter("end", end);
            }
            if (value.size() > 0) {
                queryResult.setTotalrecord((Long) query.uniqueResult());
            }
            return queryResult;
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public int count(String teacherId) {
        String hql = "from CgInnovate as c where c.teacherId=?";
        int count = 0;
        List<CgInnovate> list = (List<CgInnovate>) getHibernateTemplate().find(hql, teacherId);
        Iterator<CgInnovate> it = list.iterator();
        while (it.hasNext()) {
            CgInnovate c = it.next();
            if (c.getKeYanFen() == 0f) {
                count = count + 0;
            } else {
                count = count + (int) c.getKeYanFen();
            }
        }
        return count;
    }
}
