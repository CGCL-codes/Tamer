package com.liferay.portlet.documentlibrary.service.persistence;

import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.dao.DynamicQuery;
import com.liferay.portal.kernel.dao.DynamicQueryInitializer;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringMaker;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.spring.hibernate.FinderCache;
import com.liferay.portal.spring.hibernate.HibernateUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portlet.documentlibrary.NoSuchFileShortcutException;
import com.liferay.portlet.documentlibrary.model.DLFileShortcut;
import com.liferay.portlet.documentlibrary.model.impl.DLFileShortcutImpl;
import com.liferay.util.dao.hibernate.QueryUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <a href="DLFileShortcutPersistenceImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class DLFileShortcutPersistenceImpl extends BasePersistence implements DLFileShortcutPersistence {

    public DLFileShortcut create(long fileShortcutId) {
        DLFileShortcut dlFileShortcut = new DLFileShortcutImpl();
        dlFileShortcut.setNew(true);
        dlFileShortcut.setPrimaryKey(fileShortcutId);
        return dlFileShortcut;
    }

    public DLFileShortcut remove(long fileShortcutId) throws NoSuchFileShortcutException, SystemException {
        Session session = null;
        try {
            session = openSession();
            DLFileShortcut dlFileShortcut = (DLFileShortcut) session.get(DLFileShortcutImpl.class, new Long(fileShortcutId));
            if (dlFileShortcut == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn("No DLFileShortcut exists with the primary key " + fileShortcutId);
                }
                throw new NoSuchFileShortcutException("No DLFileShortcut exists with the primary key " + fileShortcutId);
            }
            return remove(dlFileShortcut);
        } catch (NoSuchFileShortcutException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public DLFileShortcut remove(DLFileShortcut dlFileShortcut) throws SystemException {
        ModelListener listener = _getListener();
        if (listener != null) {
            listener.onBeforeRemove(dlFileShortcut);
        }
        dlFileShortcut = removeImpl(dlFileShortcut);
        if (listener != null) {
            listener.onAfterRemove(dlFileShortcut);
        }
        return dlFileShortcut;
    }

    protected DLFileShortcut removeImpl(DLFileShortcut dlFileShortcut) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            session.delete(dlFileShortcut);
            session.flush();
            return dlFileShortcut;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
            FinderCache.clearCache(DLFileShortcut.class.getName());
        }
    }

    public DLFileShortcut update(com.liferay.portlet.documentlibrary.model.DLFileShortcut dlFileShortcut) throws SystemException {
        return update(dlFileShortcut, false);
    }

    public DLFileShortcut update(com.liferay.portlet.documentlibrary.model.DLFileShortcut dlFileShortcut, boolean merge) throws SystemException {
        ModelListener listener = _getListener();
        boolean isNew = dlFileShortcut.isNew();
        if (listener != null) {
            if (isNew) {
                listener.onBeforeCreate(dlFileShortcut);
            } else {
                listener.onBeforeUpdate(dlFileShortcut);
            }
        }
        dlFileShortcut = updateImpl(dlFileShortcut, merge);
        if (listener != null) {
            if (isNew) {
                listener.onAfterCreate(dlFileShortcut);
            } else {
                listener.onAfterUpdate(dlFileShortcut);
            }
        }
        return dlFileShortcut;
    }

    public DLFileShortcut updateImpl(com.liferay.portlet.documentlibrary.model.DLFileShortcut dlFileShortcut, boolean merge) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            if (merge) {
                session.merge(dlFileShortcut);
            } else {
                if (dlFileShortcut.isNew()) {
                    session.save(dlFileShortcut);
                }
            }
            session.flush();
            dlFileShortcut.setNew(false);
            return dlFileShortcut;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
            FinderCache.clearCache(DLFileShortcut.class.getName());
        }
    }

    public DLFileShortcut findByPrimaryKey(long fileShortcutId) throws NoSuchFileShortcutException, SystemException {
        DLFileShortcut dlFileShortcut = fetchByPrimaryKey(fileShortcutId);
        if (dlFileShortcut == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("No DLFileShortcut exists with the primary key " + fileShortcutId);
            }
            throw new NoSuchFileShortcutException("No DLFileShortcut exists with the primary key " + fileShortcutId);
        }
        return dlFileShortcut;
    }

    public DLFileShortcut fetchByPrimaryKey(long fileShortcutId) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            return (DLFileShortcut) session.get(DLFileShortcutImpl.class, new Long(fileShortcutId));
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List findByFolderId(long folderId) throws SystemException {
        String finderClassName = DLFileShortcut.class.getName();
        String finderMethodName = "findByFolderId";
        String[] finderParams = new String[] { Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(folderId) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portlet.documentlibrary.model.DLFileShortcut WHERE ");
                query.append("folderId = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, folderId);
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

    public List findByFolderId(long folderId, int begin, int end) throws SystemException {
        return findByFolderId(folderId, begin, end, null);
    }

    public List findByFolderId(long folderId, int begin, int end, OrderByComparator obc) throws SystemException {
        String finderClassName = DLFileShortcut.class.getName();
        String finderMethodName = "findByFolderId";
        String[] finderParams = new String[] { Long.class.getName(), "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { new Long(folderId), String.valueOf(begin), String.valueOf(end), String.valueOf(obc) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portlet.documentlibrary.model.DLFileShortcut WHERE ");
                query.append("folderId = ?");
                query.append(" ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, folderId);
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

    public DLFileShortcut findByFolderId_First(long folderId, OrderByComparator obc) throws NoSuchFileShortcutException, SystemException {
        List list = findByFolderId(folderId, 0, 1, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No DLFileShortcut exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("folderId=");
            msg.append(folderId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchFileShortcutException(msg.toString());
        } else {
            return (DLFileShortcut) list.get(0);
        }
    }

    public DLFileShortcut findByFolderId_Last(long folderId, OrderByComparator obc) throws NoSuchFileShortcutException, SystemException {
        int count = countByFolderId(folderId);
        List list = findByFolderId(folderId, count - 1, count, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No DLFileShortcut exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("folderId=");
            msg.append(folderId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchFileShortcutException(msg.toString());
        } else {
            return (DLFileShortcut) list.get(0);
        }
    }

    public DLFileShortcut[] findByFolderId_PrevAndNext(long fileShortcutId, long folderId, OrderByComparator obc) throws NoSuchFileShortcutException, SystemException {
        DLFileShortcut dlFileShortcut = findByPrimaryKey(fileShortcutId);
        int count = countByFolderId(folderId);
        Session session = null;
        try {
            session = openSession();
            StringMaker query = new StringMaker();
            query.append("FROM com.liferay.portlet.documentlibrary.model.DLFileShortcut WHERE ");
            query.append("folderId = ?");
            query.append(" ");
            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            Query q = session.createQuery(query.toString());
            int queryPos = 0;
            q.setLong(queryPos++, folderId);
            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, dlFileShortcut);
            DLFileShortcut[] array = new DLFileShortcutImpl[3];
            array[0] = (DLFileShortcut) objArray[0];
            array[1] = (DLFileShortcut) objArray[1];
            array[2] = (DLFileShortcut) objArray[2];
            return array;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List findByTF_TN(long toFolderId, String toName) throws SystemException {
        String finderClassName = DLFileShortcut.class.getName();
        String finderMethodName = "findByTF_TN";
        String[] finderParams = new String[] { Long.class.getName(), String.class.getName() };
        Object[] finderArgs = new Object[] { new Long(toFolderId), toName };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portlet.documentlibrary.model.DLFileShortcut WHERE ");
                query.append("toFolderId = ?");
                query.append(" AND ");
                if (toName == null) {
                    query.append("toName IS NULL");
                } else {
                    query.append("toName = ?");
                }
                query.append(" ");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, toFolderId);
                if (toName != null) {
                    q.setString(queryPos++, toName);
                }
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

    public List findByTF_TN(long toFolderId, String toName, int begin, int end) throws SystemException {
        return findByTF_TN(toFolderId, toName, begin, end, null);
    }

    public List findByTF_TN(long toFolderId, String toName, int begin, int end, OrderByComparator obc) throws SystemException {
        String finderClassName = DLFileShortcut.class.getName();
        String finderMethodName = "findByTF_TN";
        String[] finderParams = new String[] { Long.class.getName(), String.class.getName(), "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { new Long(toFolderId), toName, String.valueOf(begin), String.valueOf(end), String.valueOf(obc) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portlet.documentlibrary.model.DLFileShortcut WHERE ");
                query.append("toFolderId = ?");
                query.append(" AND ");
                if (toName == null) {
                    query.append("toName IS NULL");
                } else {
                    query.append("toName = ?");
                }
                query.append(" ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, toFolderId);
                if (toName != null) {
                    q.setString(queryPos++, toName);
                }
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

    public DLFileShortcut findByTF_TN_First(long toFolderId, String toName, OrderByComparator obc) throws NoSuchFileShortcutException, SystemException {
        List list = findByTF_TN(toFolderId, toName, 0, 1, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No DLFileShortcut exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("toFolderId=");
            msg.append(toFolderId);
            msg.append(", ");
            msg.append("toName=");
            msg.append(toName);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchFileShortcutException(msg.toString());
        } else {
            return (DLFileShortcut) list.get(0);
        }
    }

    public DLFileShortcut findByTF_TN_Last(long toFolderId, String toName, OrderByComparator obc) throws NoSuchFileShortcutException, SystemException {
        int count = countByTF_TN(toFolderId, toName);
        List list = findByTF_TN(toFolderId, toName, count - 1, count, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No DLFileShortcut exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("toFolderId=");
            msg.append(toFolderId);
            msg.append(", ");
            msg.append("toName=");
            msg.append(toName);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchFileShortcutException(msg.toString());
        } else {
            return (DLFileShortcut) list.get(0);
        }
    }

    public DLFileShortcut[] findByTF_TN_PrevAndNext(long fileShortcutId, long toFolderId, String toName, OrderByComparator obc) throws NoSuchFileShortcutException, SystemException {
        DLFileShortcut dlFileShortcut = findByPrimaryKey(fileShortcutId);
        int count = countByTF_TN(toFolderId, toName);
        Session session = null;
        try {
            session = openSession();
            StringMaker query = new StringMaker();
            query.append("FROM com.liferay.portlet.documentlibrary.model.DLFileShortcut WHERE ");
            query.append("toFolderId = ?");
            query.append(" AND ");
            if (toName == null) {
                query.append("toName IS NULL");
            } else {
                query.append("toName = ?");
            }
            query.append(" ");
            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            Query q = session.createQuery(query.toString());
            int queryPos = 0;
            q.setLong(queryPos++, toFolderId);
            if (toName != null) {
                q.setString(queryPos++, toName);
            }
            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, dlFileShortcut);
            DLFileShortcut[] array = new DLFileShortcutImpl[3];
            array[0] = (DLFileShortcut) objArray[0];
            array[1] = (DLFileShortcut) objArray[1];
            array[2] = (DLFileShortcut) objArray[2];
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
        String finderClassName = DLFileShortcut.class.getName();
        String finderMethodName = "findAll";
        String[] finderParams = new String[] { "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { String.valueOf(begin), String.valueOf(end), String.valueOf(obc) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portlet.documentlibrary.model.DLFileShortcut ");
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

    public void removeByFolderId(long folderId) throws SystemException {
        Iterator itr = findByFolderId(folderId).iterator();
        while (itr.hasNext()) {
            DLFileShortcut dlFileShortcut = (DLFileShortcut) itr.next();
            remove(dlFileShortcut);
        }
    }

    public void removeByTF_TN(long toFolderId, String toName) throws SystemException {
        Iterator itr = findByTF_TN(toFolderId, toName).iterator();
        while (itr.hasNext()) {
            DLFileShortcut dlFileShortcut = (DLFileShortcut) itr.next();
            remove(dlFileShortcut);
        }
    }

    public void removeAll() throws SystemException {
        Iterator itr = findAll().iterator();
        while (itr.hasNext()) {
            remove((DLFileShortcut) itr.next());
        }
    }

    public int countByFolderId(long folderId) throws SystemException {
        String finderClassName = DLFileShortcut.class.getName();
        String finderMethodName = "countByFolderId";
        String[] finderParams = new String[] { Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(folderId) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("SELECT COUNT(*) ");
                query.append("FROM com.liferay.portlet.documentlibrary.model.DLFileShortcut WHERE ");
                query.append("folderId = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, folderId);
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

    public int countByTF_TN(long toFolderId, String toName) throws SystemException {
        String finderClassName = DLFileShortcut.class.getName();
        String finderMethodName = "countByTF_TN";
        String[] finderParams = new String[] { Long.class.getName(), String.class.getName() };
        Object[] finderArgs = new Object[] { new Long(toFolderId), toName };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("SELECT COUNT(*) ");
                query.append("FROM com.liferay.portlet.documentlibrary.model.DLFileShortcut WHERE ");
                query.append("toFolderId = ?");
                query.append(" AND ");
                if (toName == null) {
                    query.append("toName IS NULL");
                } else {
                    query.append("toName = ?");
                }
                query.append(" ");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, toFolderId);
                if (toName != null) {
                    q.setString(queryPos++, toName);
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
        String finderClassName = DLFileShortcut.class.getName();
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
                query.append("FROM com.liferay.portlet.documentlibrary.model.DLFileShortcut");
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

    private static final String _LISTENER = GetterUtil.getString(PropsUtil.get("value.object.listener.com.liferay.portlet.documentlibrary.model.DLFileShortcut"));

    private static Log _log = LogFactory.getLog(DLFileShortcutPersistenceImpl.class);
}
