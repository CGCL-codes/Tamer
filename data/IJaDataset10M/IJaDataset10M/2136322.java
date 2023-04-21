package com.liferay.portal.service.persistence;

import com.liferay.portal.NoSuchUserTrackerPathException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.dao.DynamicQuery;
import com.liferay.portal.kernel.dao.DynamicQueryInitializer;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringMaker;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.model.UserTrackerPath;
import com.liferay.portal.model.impl.UserTrackerPathImpl;
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
 * <a href="UserTrackerPathPersistenceImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class UserTrackerPathPersistenceImpl extends BasePersistence implements UserTrackerPathPersistence {

    public UserTrackerPath create(long userTrackerPathId) {
        UserTrackerPath userTrackerPath = new UserTrackerPathImpl();
        userTrackerPath.setNew(true);
        userTrackerPath.setPrimaryKey(userTrackerPathId);
        return userTrackerPath;
    }

    public UserTrackerPath remove(long userTrackerPathId) throws NoSuchUserTrackerPathException, SystemException {
        Session session = null;
        try {
            session = openSession();
            UserTrackerPath userTrackerPath = (UserTrackerPath) session.get(UserTrackerPathImpl.class, new Long(userTrackerPathId));
            if (userTrackerPath == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn("No UserTrackerPath exists with the primary key " + userTrackerPathId);
                }
                throw new NoSuchUserTrackerPathException("No UserTrackerPath exists with the primary key " + userTrackerPathId);
            }
            return remove(userTrackerPath);
        } catch (NoSuchUserTrackerPathException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public UserTrackerPath remove(UserTrackerPath userTrackerPath) throws SystemException {
        ModelListener listener = _getListener();
        if (listener != null) {
            listener.onBeforeRemove(userTrackerPath);
        }
        userTrackerPath = removeImpl(userTrackerPath);
        if (listener != null) {
            listener.onAfterRemove(userTrackerPath);
        }
        return userTrackerPath;
    }

    protected UserTrackerPath removeImpl(UserTrackerPath userTrackerPath) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            session.delete(userTrackerPath);
            session.flush();
            return userTrackerPath;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
            FinderCache.clearCache(UserTrackerPath.class.getName());
        }
    }

    public UserTrackerPath update(com.liferay.portal.model.UserTrackerPath userTrackerPath) throws SystemException {
        return update(userTrackerPath, false);
    }

    public UserTrackerPath update(com.liferay.portal.model.UserTrackerPath userTrackerPath, boolean merge) throws SystemException {
        ModelListener listener = _getListener();
        boolean isNew = userTrackerPath.isNew();
        if (listener != null) {
            if (isNew) {
                listener.onBeforeCreate(userTrackerPath);
            } else {
                listener.onBeforeUpdate(userTrackerPath);
            }
        }
        userTrackerPath = updateImpl(userTrackerPath, merge);
        if (listener != null) {
            if (isNew) {
                listener.onAfterCreate(userTrackerPath);
            } else {
                listener.onAfterUpdate(userTrackerPath);
            }
        }
        return userTrackerPath;
    }

    public UserTrackerPath updateImpl(com.liferay.portal.model.UserTrackerPath userTrackerPath, boolean merge) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            if (merge) {
                session.merge(userTrackerPath);
            } else {
                if (userTrackerPath.isNew()) {
                    session.save(userTrackerPath);
                }
            }
            session.flush();
            userTrackerPath.setNew(false);
            return userTrackerPath;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
            FinderCache.clearCache(UserTrackerPath.class.getName());
        }
    }

    public UserTrackerPath findByPrimaryKey(long userTrackerPathId) throws NoSuchUserTrackerPathException, SystemException {
        UserTrackerPath userTrackerPath = fetchByPrimaryKey(userTrackerPathId);
        if (userTrackerPath == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("No UserTrackerPath exists with the primary key " + userTrackerPathId);
            }
            throw new NoSuchUserTrackerPathException("No UserTrackerPath exists with the primary key " + userTrackerPathId);
        }
        return userTrackerPath;
    }

    public UserTrackerPath fetchByPrimaryKey(long userTrackerPathId) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            return (UserTrackerPath) session.get(UserTrackerPathImpl.class, new Long(userTrackerPathId));
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List findByUserTrackerId(long userTrackerId) throws SystemException {
        String finderClassName = UserTrackerPath.class.getName();
        String finderMethodName = "findByUserTrackerId";
        String[] finderParams = new String[] { Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(userTrackerId) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portal.model.UserTrackerPath WHERE ");
                query.append("userTrackerId = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, userTrackerId);
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

    public List findByUserTrackerId(long userTrackerId, int begin, int end) throws SystemException {
        return findByUserTrackerId(userTrackerId, begin, end, null);
    }

    public List findByUserTrackerId(long userTrackerId, int begin, int end, OrderByComparator obc) throws SystemException {
        String finderClassName = UserTrackerPath.class.getName();
        String finderMethodName = "findByUserTrackerId";
        String[] finderParams = new String[] { Long.class.getName(), "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { new Long(userTrackerId), String.valueOf(begin), String.valueOf(end), String.valueOf(obc) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portal.model.UserTrackerPath WHERE ");
                query.append("userTrackerId = ?");
                query.append(" ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, userTrackerId);
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

    public UserTrackerPath findByUserTrackerId_First(long userTrackerId, OrderByComparator obc) throws NoSuchUserTrackerPathException, SystemException {
        List list = findByUserTrackerId(userTrackerId, 0, 1, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No UserTrackerPath exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("userTrackerId=");
            msg.append(userTrackerId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchUserTrackerPathException(msg.toString());
        } else {
            return (UserTrackerPath) list.get(0);
        }
    }

    public UserTrackerPath findByUserTrackerId_Last(long userTrackerId, OrderByComparator obc) throws NoSuchUserTrackerPathException, SystemException {
        int count = countByUserTrackerId(userTrackerId);
        List list = findByUserTrackerId(userTrackerId, count - 1, count, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No UserTrackerPath exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("userTrackerId=");
            msg.append(userTrackerId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchUserTrackerPathException(msg.toString());
        } else {
            return (UserTrackerPath) list.get(0);
        }
    }

    public UserTrackerPath[] findByUserTrackerId_PrevAndNext(long userTrackerPathId, long userTrackerId, OrderByComparator obc) throws NoSuchUserTrackerPathException, SystemException {
        UserTrackerPath userTrackerPath = findByPrimaryKey(userTrackerPathId);
        int count = countByUserTrackerId(userTrackerId);
        Session session = null;
        try {
            session = openSession();
            StringMaker query = new StringMaker();
            query.append("FROM com.liferay.portal.model.UserTrackerPath WHERE ");
            query.append("userTrackerId = ?");
            query.append(" ");
            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            Query q = session.createQuery(query.toString());
            int queryPos = 0;
            q.setLong(queryPos++, userTrackerId);
            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, userTrackerPath);
            UserTrackerPath[] array = new UserTrackerPathImpl[3];
            array[0] = (UserTrackerPath) objArray[0];
            array[1] = (UserTrackerPath) objArray[1];
            array[2] = (UserTrackerPath) objArray[2];
            return array;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
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
        String finderClassName = UserTrackerPath.class.getName();
        String finderMethodName = "findAll";
        String[] finderParams = new String[] { "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { String.valueOf(begin), String.valueOf(end), String.valueOf(obc) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portal.model.UserTrackerPath ");
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

    public void removeByUserTrackerId(long userTrackerId) throws SystemException {
        Iterator itr = findByUserTrackerId(userTrackerId).iterator();
        while (itr.hasNext()) {
            UserTrackerPath userTrackerPath = (UserTrackerPath) itr.next();
            remove(userTrackerPath);
        }
    }

    public void removeAll() throws SystemException {
        Iterator itr = findAll().iterator();
        while (itr.hasNext()) {
            remove((UserTrackerPath) itr.next());
        }
    }

    public int countByUserTrackerId(long userTrackerId) throws SystemException {
        String finderClassName = UserTrackerPath.class.getName();
        String finderMethodName = "countByUserTrackerId";
        String[] finderParams = new String[] { Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(userTrackerId) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("SELECT COUNT(*) ");
                query.append("FROM com.liferay.portal.model.UserTrackerPath WHERE ");
                query.append("userTrackerId = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, userTrackerId);
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
        String finderClassName = UserTrackerPath.class.getName();
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
                query.append("FROM com.liferay.portal.model.UserTrackerPath");
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

    private static final String _LISTENER = GetterUtil.getString(PropsUtil.get("value.object.listener.com.liferay.portal.model.UserTrackerPath"));

    private static Log _log = LogFactory.getLog(UserTrackerPathPersistenceImpl.class);
}
