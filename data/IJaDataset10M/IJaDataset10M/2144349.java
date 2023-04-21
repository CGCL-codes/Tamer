package com.liferay.portal.service.persistence;

import com.liferay.portal.NoSuchResourceException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.dao.DynamicQuery;
import com.liferay.portal.kernel.dao.DynamicQueryInitializer;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringMaker;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.model.Resource;
import com.liferay.portal.model.impl.ResourceImpl;
import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.spring.hibernate.FinderCache;
import com.liferay.portal.spring.hibernate.HibernateUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.util.dao.hibernate.QueryUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <a href="ResourcePersistenceImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ResourcePersistenceImpl extends BasePersistence implements ResourcePersistence {

    public Resource create(long resourceId) {
        Resource resource = new ResourceImpl();
        resource.setNew(true);
        resource.setPrimaryKey(resourceId);
        return resource;
    }

    public Resource remove(long resourceId) throws NoSuchResourceException, SystemException {
        Session session = null;
        try {
            session = openSession();
            Resource resource = (Resource) session.get(ResourceImpl.class, new Long(resourceId));
            if (resource == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn("No Resource exists with the primary key " + resourceId);
                }
                throw new NoSuchResourceException("No Resource exists with the primary key " + resourceId);
            }
            return remove(resource);
        } catch (NoSuchResourceException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public Resource remove(Resource resource) throws SystemException {
        ModelListener listener = _getListener();
        if (listener != null) {
            listener.onBeforeRemove(resource);
        }
        resource = removeImpl(resource);
        if (listener != null) {
            listener.onAfterRemove(resource);
        }
        return resource;
    }

    protected Resource removeImpl(Resource resource) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            session.delete(resource);
            session.flush();
            return resource;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
            FinderCache.clearCache(Resource.class.getName());
        }
    }

    public Resource update(com.liferay.portal.model.Resource resource) throws SystemException {
        return update(resource, false);
    }

    public Resource update(com.liferay.portal.model.Resource resource, boolean merge) throws SystemException {
        ModelListener listener = _getListener();
        boolean isNew = resource.isNew();
        if (listener != null) {
            if (isNew) {
                listener.onBeforeCreate(resource);
            } else {
                listener.onBeforeUpdate(resource);
            }
        }
        resource = updateImpl(resource, merge);
        if (listener != null) {
            if (isNew) {
                listener.onAfterCreate(resource);
            } else {
                listener.onAfterUpdate(resource);
            }
        }
        return resource;
    }

    public Resource updateImpl(com.liferay.portal.model.Resource resource, boolean merge) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            if (merge) {
                session.merge(resource);
            } else {
                if (resource.isNew()) {
                    session.save(resource);
                }
            }
            session.flush();
            resource.setNew(false);
            return resource;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
            FinderCache.clearCache(Resource.class.getName());
        }
    }

    public Resource findByPrimaryKey(long resourceId) throws NoSuchResourceException, SystemException {
        Resource resource = fetchByPrimaryKey(resourceId);
        if (resource == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("No Resource exists with the primary key " + resourceId);
            }
            throw new NoSuchResourceException("No Resource exists with the primary key " + resourceId);
        }
        return resource;
    }

    public Resource fetchByPrimaryKey(long resourceId) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            return (Resource) session.get(ResourceImpl.class, new Long(resourceId));
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List findByCodeId(long codeId) throws SystemException {
        String finderClassName = Resource.class.getName();
        String finderMethodName = "findByCodeId";
        String[] finderParams = new String[] { Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(codeId) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portal.model.Resource WHERE ");
                query.append("codeId = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, codeId);
                List list = q.list();
                FinderCache.putResult(finderClassName, finderMethodName, finderParams, finderArgs, list);
                return list;
            } catch (Exception e) {
                throw HibernateUtil.processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return (List) result;
        }
    }

    public List findByCodeId(long codeId, int begin, int end) throws SystemException {
        return findByCodeId(codeId, begin, end, null);
    }

    public List findByCodeId(long codeId, int begin, int end, OrderByComparator obc) throws SystemException {
        String finderClassName = Resource.class.getName();
        String finderMethodName = "findByCodeId";
        String[] finderParams = new String[] { Long.class.getName(), "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { new Long(codeId), String.valueOf(begin), String.valueOf(end), String.valueOf(obc) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portal.model.Resource WHERE ");
                query.append("codeId = ?");
                query.append(" ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, codeId);
                List list = QueryUtil.list(q, getDialect(), begin, end);
                FinderCache.putResult(finderClassName, finderMethodName, finderParams, finderArgs, list);
                return list;
            } catch (Exception e) {
                throw HibernateUtil.processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return (List) result;
        }
    }

    public Resource findByCodeId_First(long codeId, OrderByComparator obc) throws NoSuchResourceException, SystemException {
        List list = findByCodeId(codeId, 0, 1, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No Resource exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("codeId=");
            msg.append(codeId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchResourceException(msg.toString());
        } else {
            return (Resource) list.get(0);
        }
    }

    public Resource findByCodeId_Last(long codeId, OrderByComparator obc) throws NoSuchResourceException, SystemException {
        int count = countByCodeId(codeId);
        List list = findByCodeId(codeId, count - 1, count, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No Resource exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("codeId=");
            msg.append(codeId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchResourceException(msg.toString());
        } else {
            return (Resource) list.get(0);
        }
    }

    public Resource[] findByCodeId_PrevAndNext(long resourceId, long codeId, OrderByComparator obc) throws NoSuchResourceException, SystemException {
        Resource resource = findByPrimaryKey(resourceId);
        int count = countByCodeId(codeId);
        Session session = null;
        try {
            session = openSession();
            StringMaker query = new StringMaker();
            query.append("FROM com.liferay.portal.model.Resource WHERE ");
            query.append("codeId = ?");
            query.append(" ");
            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            Query q = session.createQuery(query.toString());
            int queryPos = 0;
            q.setLong(queryPos++, codeId);
            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, resource);
            Resource[] array = new ResourceImpl[3];
            array[0] = (Resource) objArray[0];
            array[1] = (Resource) objArray[1];
            array[2] = (Resource) objArray[2];
            return array;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public Resource findByC_P(long codeId, String primKey) throws NoSuchResourceException, SystemException {
        Resource resource = fetchByC_P(codeId, primKey);
        if (resource == null) {
            StringMaker msg = new StringMaker();
            msg.append("No Resource exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("codeId=");
            msg.append(codeId);
            msg.append(", ");
            msg.append("primKey=");
            msg.append(primKey);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            if (_log.isWarnEnabled()) {
                _log.warn(msg.toString());
            }
            throw new NoSuchResourceException(msg.toString());
        }
        return resource;
    }

    public Resource fetchByC_P(long codeId, String primKey) throws SystemException {
        String finderClassName = Resource.class.getName();
        String finderMethodName = "fetchByC_P";
        String[] finderParams = new String[] { Long.class.getName(), String.class.getName() };
        Object[] finderArgs = new Object[] { new Long(codeId), primKey };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portal.model.Resource WHERE ");
                query.append("codeId = ?");
                query.append(" AND ");
                if (primKey == null) {
                    query.append("primKey IS NULL");
                } else {
                    query.append("primKey = ?");
                }
                query.append(" ");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, codeId);
                if (primKey != null) {
                    q.setString(queryPos++, primKey);
                }
                List list = q.list();
                FinderCache.putResult(finderClassName, finderMethodName, finderParams, finderArgs, list);
                if (list.size() == 0) {
                    return null;
                } else {
                    return (Resource) list.get(0);
                }
            } catch (Exception e) {
                throw HibernateUtil.processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            List list = (List) result;
            if (list.size() == 0) {
                return null;
            } else {
                return (Resource) list.get(0);
            }
        }
    }

    public List findWithDynamicQuery(DynamicQueryInitializer queryInitializer) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            DynamicQuery query = queryInitializer.initialize(session);
            return query.list();
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List findWithDynamicQuery(DynamicQueryInitializer queryInitializer, int begin, int end) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            DynamicQuery query = queryInitializer.initialize(session);
            query.setLimit(begin, end);
            return query.list();
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List findAll() throws SystemException {
        return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
    }

    public List findAll(int begin, int end) throws SystemException {
        return findAll(begin, end, null);
    }

    public List findAll(int begin, int end, OrderByComparator obc) throws SystemException {
        String finderClassName = Resource.class.getName();
        String finderMethodName = "findAll";
        String[] finderParams = new String[] { "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { String.valueOf(begin), String.valueOf(end), String.valueOf(obc) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portal.model.Resource ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                Query q = session.createQuery(query.toString());
                List list = QueryUtil.list(q, getDialect(), begin, end);
                if (obc == null) {
                    Collections.sort(list);
                }
                FinderCache.putResult(finderClassName, finderMethodName, finderParams, finderArgs, list);
                return list;
            } catch (Exception e) {
                throw HibernateUtil.processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return (List) result;
        }
    }

    public void removeByCodeId(long codeId) throws SystemException {
        Iterator itr = findByCodeId(codeId).iterator();
        while (itr.hasNext()) {
            Resource resource = (Resource) itr.next();
            remove(resource);
        }
    }

    public void removeByC_P(long codeId, String primKey) throws NoSuchResourceException, SystemException {
        Resource resource = findByC_P(codeId, primKey);
        remove(resource);
    }

    public void removeAll() throws SystemException {
        Iterator itr = findAll().iterator();
        while (itr.hasNext()) {
            remove((Resource) itr.next());
        }
    }

    public int countByCodeId(long codeId) throws SystemException {
        String finderClassName = Resource.class.getName();
        String finderMethodName = "countByCodeId";
        String[] finderParams = new String[] { Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(codeId) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("SELECT COUNT(*) ");
                query.append("FROM com.liferay.portal.model.Resource WHERE ");
                query.append("codeId = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, codeId);
                Long count = null;
                Iterator itr = q.list().iterator();
                if (itr.hasNext()) {
                    count = (Long) itr.next();
                }
                if (count == null) {
                    count = new Long(0);
                }
                FinderCache.putResult(finderClassName, finderMethodName, finderParams, finderArgs, count);
                return count.intValue();
            } catch (Exception e) {
                throw HibernateUtil.processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return ((Long) result).intValue();
        }
    }

    public int countByC_P(long codeId, String primKey) throws SystemException {
        String finderClassName = Resource.class.getName();
        String finderMethodName = "countByC_P";
        String[] finderParams = new String[] { Long.class.getName(), String.class.getName() };
        Object[] finderArgs = new Object[] { new Long(codeId), primKey };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("SELECT COUNT(*) ");
                query.append("FROM com.liferay.portal.model.Resource WHERE ");
                query.append("codeId = ?");
                query.append(" AND ");
                if (primKey == null) {
                    query.append("primKey IS NULL");
                } else {
                    query.append("primKey = ?");
                }
                query.append(" ");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, codeId);
                if (primKey != null) {
                    q.setString(queryPos++, primKey);
                }
                Long count = null;
                Iterator itr = q.list().iterator();
                if (itr.hasNext()) {
                    count = (Long) itr.next();
                }
                if (count == null) {
                    count = new Long(0);
                }
                FinderCache.putResult(finderClassName, finderMethodName, finderParams, finderArgs, count);
                return count.intValue();
            } catch (Exception e) {
                throw HibernateUtil.processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return ((Long) result).intValue();
        }
    }

    public int countAll() throws SystemException {
        String finderClassName = Resource.class.getName();
        String finderMethodName = "countAll";
        String[] finderParams = new String[] {};
        Object[] finderArgs = new Object[] {};
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("SELECT COUNT(*) ");
                query.append("FROM com.liferay.portal.model.Resource");
                Query q = session.createQuery(query.toString());
                Long count = null;
                Iterator itr = q.list().iterator();
                if (itr.hasNext()) {
                    count = (Long) itr.next();
                }
                if (count == null) {
                    count = new Long(0);
                }
                FinderCache.putResult(finderClassName, finderMethodName, finderParams, finderArgs, count);
                return count.intValue();
            } catch (Exception e) {
                throw HibernateUtil.processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return ((Long) result).intValue();
        }
    }

    protected void initDao() {
    }

    private static ModelListener _getListener() {
        if (Validator.isNotNull(_LISTENER)) {
            try {
                return (ModelListener) Class.forName(_LISTENER).newInstance();
            } catch (Exception e) {
                _log.error(e);
            }
        }
        return null;
    }

    private static final String _LISTENER = GetterUtil.getString(PropsUtil.get("value.object.listener.com.liferay.portal.model.Resource"));

    private static Log _log = LogFactory.getLog(ResourcePersistenceImpl.class);
}
