package cn.collin.commons.dao;

import org.apache.log4j.Logger;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.hibernate3.HibernateAccessor;
import org.springframework.orm.hibernate3.HibernateCallback;
import cn.collin.commons.domain.PageResult;
import cn.collin.commons.utils.StringUtils;

/**
 * http://collincode.googlecode.com
 * 
 * @author collin.code@gmail.com
 * 
 */
public abstract class MyDaoSupport extends org.springframework.orm.hibernate3.support.HibernateDaoSupport {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(MyDaoSupport.class);

    protected Class entityClass;

    public MyDaoSupport() {
        setEntityClass();
    }

    public abstract void setEntityClass();

    /**
	 * retrieve entity by id
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 */
    public Object retrieveById(Serializable id) {
        if (logger.isDebugEnabled()) {
            logger.debug("entityClass:" + entityClass + " id:" + id);
        }
        return retrieveById(entityClass, id);
    }

    /**
	 * retrieve entity by id
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 */
    public Object retrieveById(Class entityClass, Serializable id) {
        if (logger.isDebugEnabled()) {
            logger.debug("entityClass:" + entityClass.getName() + " id:" + id);
        }
        return getHibernateTemplate().load(entityClass, id);
    }

    /**
	 * get entity by id
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 */
    public Object getById(Serializable id) {
        if (logger.isDebugEnabled()) {
            logger.debug("entityClass:" + entityClass.getName() + " id:" + id);
        }
        return getById(entityClass, id);
    }

    /**
	 * get entity by id
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 */
    public Object getById(Class entityClass, Serializable id) {
        if (logger.isDebugEnabled()) {
            logger.debug("entityClass:" + entityClass.getName() + " id:" + id);
        }
        return getHibernateTemplate().get(entityClass, id);
    }

    /**
	 * 
	 * @param entity
	 */
    public void update(Object entity) {
        getHibernateTemplate().saveOrUpdate(entity);
    }

    /**
	 * 
	 * @param entity
	 */
    public void create(Object entity) {
        getHibernateTemplate().save(entity);
    }

    public void delete(Serializable id) {
        if (logger.isDebugEnabled()) {
            logger.debug("entityClass:" + entityClass.getName() + " id:" + id);
        }
        delete(entityClass, id);
    }

    public void delete(Class entityClass, Serializable id) {
        if (logger.isDebugEnabled()) {
            logger.debug("entityClass:" + entityClass.getName() + " id:" + id);
        }
        getHibernateTemplate().delete(getHibernateTemplate().load(entityClass, id));
    }

    /**
	 * 
	 * @param ids
	 */
    public void deleteWithIndependent(String[] ids) {
        if (logger.isDebugEnabled()) {
            logger.debug("entityClass:" + entityClass.getName() + " ids:" + StringUtils.arrayToString(ids, ","));
        }
        deleteWithIndependent(entityClass.getName(), ids);
    }

    /**
	 * 
	 * @param ids
	 */
    public void deleteWithIndependent(String entityName, String[] ids) {
        if (ids.length <= 0) return;
        if (logger.isDebugEnabled()) {
            logger.debug("entityClass:" + entityName + " ids:" + StringUtils.arrayToString(ids, ","));
        }
        String condition = "";
        for (int i = 0; i < ids.length; i++) {
            condition += ids[i];
            if (i < ids.length - 1) condition += ",";
        }
        getHibernateTemplate().bulkUpdate("delete from " + entityName + " where id in (" + condition + ")");
    }

    /**
	 * 
	 * @param ids
	 */
    public void deleteWithDependent(String[] ids) {
        if (logger.isDebugEnabled()) {
            logger.debug("entityClass:" + entityClass.getName() + " ids:" + StringUtils.arrayToString(ids, ","));
        }
        deleteWithDependent(entityClass, ids);
    }

    /**
	 * 
	 * @param ids
	 */
    public void deleteWithDependent(Class entityClass, String[] ids) {
        if (logger.isDebugEnabled()) {
            logger.debug("entityClass:" + entityClass.getName() + " ids:" + StringUtils.arrayToString(ids, ","));
        }
        for (int i = 0; i < ids.length; i++) {
            getHibernateTemplate().delete(getHibernateTemplate().load(entityClass, Long.valueOf(ids[i])));
        }
    }

    /**
	 * 
	 * @param queryString
	 * @param countString
	 * @param pageIndex
	 * @param pageLength
	 * @return
	 */
    public PageResult findByPage(String queryString, String countString, int pageIndex, int pageLength) {
        logger.debug("start findByPage:" + System.currentTimeMillis());
        int firstResult = (pageIndex - 1) * pageLength;
        Query query = getSession().createQuery(queryString);
        query.setFirstResult(firstResult);
        query.setMaxResults(pageLength);
        List results = query.list();
        logger.debug("finished query articles:" + System.currentTimeMillis());
        int allResultsAmount = ((Long) getHibernateTemplate().find(countString).get(0)).intValue();
        int pagesAmount = new Double(Math.ceil(new Integer(allResultsAmount).doubleValue() / new Integer(pageLength).doubleValue())).intValue();
        logger.debug("end findByPage:" + System.currentTimeMillis());
        return new PageResult(pageIndex, pageLength, pagesAmount, allResultsAmount, results);
    }

    /**
	 * 
	 * @param queryString
	 * @param countString
	 * @param objects
	 * @param pageIndex
	 * @param pageLength
	 * @return
	 */
    public PageResult findByPage(String queryString, String countString, Object[] objects, int pageIndex, int pageLength) {
        int firstResult = (pageIndex - 1) * pageLength;
        Query query = getSession().createQuery(queryString);
        query.setFirstResult(firstResult);
        query.setMaxResults(pageLength);
        for (int i = 0; i < objects.length; i++) query.setParameter(i, objects[i]);
        int allResultsAmount = ((Long) getHibernateTemplate().find(countString, objects).get(0)).intValue();
        int pagesAmount = new Double(Math.ceil(new Integer(allResultsAmount).doubleValue() / new Integer(pageLength).doubleValue())).intValue();
        return new PageResult(pageIndex, pageLength, pagesAmount, allResultsAmount, query.list());
    }

    /**
	 * 
	 * @param queryString
	 * @param objects
	 * @param maxResults
	 * @return
	 */
    public List findByMaxResults(String queryString, Object[] objects, int maxResults) {
        Query query = getSession().createQuery(queryString);
        query.setFirstResult(0);
        query.setMaxResults(maxResults);
        if (objects != null) {
            for (int i = 0; i < objects.length; i++) query.setParameter(i, objects[i]);
        }
        return query.list();
    }

    /**
	 */
    public long countResults(String queryString, Object[] objects) {
        queryString = "select count(*) " + queryString;
        return ((Long) getHibernateTemplate().find(queryString, objects).get(0)).longValue();
    }

    private void checkWriteOperationAllowed(Session session) throws InvalidDataAccessApiUsageException {
        if (this.getHibernateTemplate().isCheckWriteOperations() && this.getHibernateTemplate().getFlushMode() != HibernateAccessor.FLUSH_EAGER && FlushMode.NEVER.equals(session.getFlushMode())) {
            throw new InvalidDataAccessApiUsageException("Write operations are not allowed in read-only mode (FlushMode.NEVER) - turn your Session " + "into FlushMode.AUTO or remove 'readOnly' marker from transaction definition");
        }
    }

    /**
	 * 
	 * @param entities
	 * @throws DataAccessException
	 */
    public void saveOrUpdateAll(final Collection entities) {
        this.getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                checkWriteOperationAllowed(session);
                int i = 0;
                for (Iterator it = entities.iterator(); it.hasNext(); ) {
                    session.save(it.next());
                    i++;
                    if (i % 20 == 0) {
                        session.flush();
                        session.clear();
                    }
                }
                session.flush();
                session.clear();
                return null;
            }
        }, true);
    }

    public List findAll() {
        List result = getHibernateTemplate().loadAll(this.entityClass);
        return (result != null) ? result : Collections.EMPTY_LIST;
    }
}
