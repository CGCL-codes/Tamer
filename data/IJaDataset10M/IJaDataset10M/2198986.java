package com.liferay.portlet.journal.service.persistence;

import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.BatchSessionUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portlet.journal.NoSuchFeedException;
import com.liferay.portlet.journal.model.JournalFeed;
import com.liferay.portlet.journal.model.impl.JournalFeedImpl;
import com.liferay.portlet.journal.model.impl.JournalFeedModelImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <a href="JournalFeedPersistenceImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class JournalFeedPersistenceImpl extends BasePersistenceImpl implements JournalFeedPersistence {

    public JournalFeed create(long id) {
        JournalFeed journalFeed = new JournalFeedImpl();
        journalFeed.setNew(true);
        journalFeed.setPrimaryKey(id);
        String uuid = PortalUUIDUtil.generate();
        journalFeed.setUuid(uuid);
        return journalFeed;
    }

    public JournalFeed remove(long id) throws NoSuchFeedException, SystemException {
        Session session = null;
        try {
            session = openSession();
            JournalFeed journalFeed = (JournalFeed) session.get(JournalFeedImpl.class, new Long(id));
            if (journalFeed == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn("No JournalFeed exists with the primary key " + id);
                }
                throw new NoSuchFeedException("No JournalFeed exists with the primary key " + id);
            }
            return remove(journalFeed);
        } catch (NoSuchFeedException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public JournalFeed remove(JournalFeed journalFeed) throws SystemException {
        if (_listeners.length > 0) {
            for (ModelListener listener : _listeners) {
                listener.onBeforeRemove(journalFeed);
            }
        }
        journalFeed = removeImpl(journalFeed);
        if (_listeners.length > 0) {
            for (ModelListener listener : _listeners) {
                listener.onAfterRemove(journalFeed);
            }
        }
        return journalFeed;
    }

    protected JournalFeed removeImpl(JournalFeed journalFeed) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            if (BatchSessionUtil.isEnabled()) {
                Object staleObject = session.get(JournalFeedImpl.class, journalFeed.getPrimaryKeyObj());
                if (staleObject != null) {
                    session.evict(staleObject);
                }
            }
            session.delete(journalFeed);
            session.flush();
            return journalFeed;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
            FinderCacheUtil.clearCache(JournalFeed.class.getName());
        }
    }

    /**
	 * @deprecated Use <code>update(JournalFeed journalFeed, boolean merge)</code>.
	 */
    public JournalFeed update(JournalFeed journalFeed) throws SystemException {
        if (_log.isWarnEnabled()) {
            _log.warn("Using the deprecated update(JournalFeed journalFeed) method. Use update(JournalFeed journalFeed, boolean merge) instead.");
        }
        return update(journalFeed, false);
    }

    /**
	 * Add, update, or merge, the entity. This method also calls the model
	 * listeners to trigger the proper events associated with adding, deleting,
	 * or updating an entity.
	 *
	 * @param        journalFeed the entity to add, update, or merge
	 * @param        merge boolean value for whether to merge the entity. The
	 *                default value is false. Setting merge to true is more
	 *                expensive and should only be true when journalFeed is
	 *                transient. See LEP-5473 for a detailed discussion of this
	 *                method.
	 * @return        true if the portlet can be displayed via Ajax
	 */
    public JournalFeed update(JournalFeed journalFeed, boolean merge) throws SystemException {
        boolean isNew = journalFeed.isNew();
        if (_listeners.length > 0) {
            for (ModelListener listener : _listeners) {
                if (isNew) {
                    listener.onBeforeCreate(journalFeed);
                } else {
                    listener.onBeforeUpdate(journalFeed);
                }
            }
        }
        journalFeed = updateImpl(journalFeed, merge);
        if (_listeners.length > 0) {
            for (ModelListener listener : _listeners) {
                if (isNew) {
                    listener.onAfterCreate(journalFeed);
                } else {
                    listener.onAfterUpdate(journalFeed);
                }
            }
        }
        return journalFeed;
    }

    public JournalFeed updateImpl(com.liferay.portlet.journal.model.JournalFeed journalFeed, boolean merge) throws SystemException {
        if (Validator.isNull(journalFeed.getUuid())) {
            String uuid = PortalUUIDUtil.generate();
            journalFeed.setUuid(uuid);
        }
        Session session = null;
        try {
            session = openSession();
            BatchSessionUtil.update(session, journalFeed, merge);
            journalFeed.setNew(false);
            return journalFeed;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
            FinderCacheUtil.clearCache(JournalFeed.class.getName());
        }
    }

    public JournalFeed findByPrimaryKey(long id) throws NoSuchFeedException, SystemException {
        JournalFeed journalFeed = fetchByPrimaryKey(id);
        if (journalFeed == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("No JournalFeed exists with the primary key " + id);
            }
            throw new NoSuchFeedException("No JournalFeed exists with the primary key " + id);
        }
        return journalFeed;
    }

    public JournalFeed fetchByPrimaryKey(long id) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            return (JournalFeed) session.get(JournalFeedImpl.class, new Long(id));
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<JournalFeed> findByUuid(String uuid) throws SystemException {
        boolean finderClassNameCacheEnabled = JournalFeedModelImpl.CACHE_ENABLED;
        String finderClassName = JournalFeed.class.getName();
        String finderMethodName = "findByUuid";
        String[] finderParams = new String[] { String.class.getName() };
        Object[] finderArgs = new Object[] { uuid };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("FROM com.liferay.portlet.journal.model.JournalFeed WHERE ");
                if (uuid == null) {
                    query.append("uuid_ IS NULL");
                } else {
                    query.append("uuid_ = ?");
                }
                query.append(" ");
                query.append("ORDER BY ");
                query.append("feedId ASC");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                if (uuid != null) {
                    qPos.add(uuid);
                }
                List<JournalFeed> list = q.list();
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, list);
                return list;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return (List<JournalFeed>) result;
        }
    }

    public List<JournalFeed> findByUuid(String uuid, int start, int end) throws SystemException {
        return findByUuid(uuid, start, end, null);
    }

    public List<JournalFeed> findByUuid(String uuid, int start, int end, OrderByComparator obc) throws SystemException {
        boolean finderClassNameCacheEnabled = JournalFeedModelImpl.CACHE_ENABLED;
        String finderClassName = JournalFeed.class.getName();
        String finderMethodName = "findByUuid";
        String[] finderParams = new String[] { String.class.getName(), "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { uuid, String.valueOf(start), String.valueOf(end), String.valueOf(obc) };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("FROM com.liferay.portlet.journal.model.JournalFeed WHERE ");
                if (uuid == null) {
                    query.append("uuid_ IS NULL");
                } else {
                    query.append("uuid_ = ?");
                }
                query.append(" ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                } else {
                    query.append("ORDER BY ");
                    query.append("feedId ASC");
                }
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                if (uuid != null) {
                    qPos.add(uuid);
                }
                List<JournalFeed> list = (List<JournalFeed>) QueryUtil.list(q, getDialect(), start, end);
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, list);
                return list;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return (List<JournalFeed>) result;
        }
    }

    public JournalFeed findByUuid_First(String uuid, OrderByComparator obc) throws NoSuchFeedException, SystemException {
        List<JournalFeed> list = findByUuid(uuid, 0, 1, obc);
        if (list.size() == 0) {
            StringBuilder msg = new StringBuilder();
            msg.append("No JournalFeed exists with the key {");
            msg.append("uuid=" + uuid);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchFeedException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public JournalFeed findByUuid_Last(String uuid, OrderByComparator obc) throws NoSuchFeedException, SystemException {
        int count = countByUuid(uuid);
        List<JournalFeed> list = findByUuid(uuid, count - 1, count, obc);
        if (list.size() == 0) {
            StringBuilder msg = new StringBuilder();
            msg.append("No JournalFeed exists with the key {");
            msg.append("uuid=" + uuid);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchFeedException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public JournalFeed[] findByUuid_PrevAndNext(long id, String uuid, OrderByComparator obc) throws NoSuchFeedException, SystemException {
        JournalFeed journalFeed = findByPrimaryKey(id);
        int count = countByUuid(uuid);
        Session session = null;
        try {
            session = openSession();
            StringBuilder query = new StringBuilder();
            query.append("FROM com.liferay.portlet.journal.model.JournalFeed WHERE ");
            if (uuid == null) {
                query.append("uuid_ IS NULL");
            } else {
                query.append("uuid_ = ?");
            }
            query.append(" ");
            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            } else {
                query.append("ORDER BY ");
                query.append("feedId ASC");
            }
            Query q = session.createQuery(query.toString());
            QueryPos qPos = QueryPos.getInstance(q);
            if (uuid != null) {
                qPos.add(uuid);
            }
            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, journalFeed);
            JournalFeed[] array = new JournalFeedImpl[3];
            array[0] = (JournalFeed) objArray[0];
            array[1] = (JournalFeed) objArray[1];
            array[2] = (JournalFeed) objArray[2];
            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public JournalFeed findByUUID_G(String uuid, long groupId) throws NoSuchFeedException, SystemException {
        JournalFeed journalFeed = fetchByUUID_G(uuid, groupId);
        if (journalFeed == null) {
            StringBuilder msg = new StringBuilder();
            msg.append("No JournalFeed exists with the key {");
            msg.append("uuid=" + uuid);
            msg.append(", ");
            msg.append("groupId=" + groupId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            if (_log.isWarnEnabled()) {
                _log.warn(msg.toString());
            }
            throw new NoSuchFeedException(msg.toString());
        }
        return journalFeed;
    }

    public JournalFeed fetchByUUID_G(String uuid, long groupId) throws SystemException {
        boolean finderClassNameCacheEnabled = JournalFeedModelImpl.CACHE_ENABLED;
        String finderClassName = JournalFeed.class.getName();
        String finderMethodName = "fetchByUUID_G";
        String[] finderParams = new String[] { String.class.getName(), Long.class.getName() };
        Object[] finderArgs = new Object[] { uuid, new Long(groupId) };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("FROM com.liferay.portlet.journal.model.JournalFeed WHERE ");
                if (uuid == null) {
                    query.append("uuid_ IS NULL");
                } else {
                    query.append("uuid_ = ?");
                }
                query.append(" AND ");
                query.append("groupId = ?");
                query.append(" ");
                query.append("ORDER BY ");
                query.append("feedId ASC");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                if (uuid != null) {
                    qPos.add(uuid);
                }
                qPos.add(groupId);
                List<JournalFeed> list = q.list();
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, list);
                if (list.size() == 0) {
                    return null;
                } else {
                    return list.get(0);
                }
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            List<JournalFeed> list = (List<JournalFeed>) result;
            if (list.size() == 0) {
                return null;
            } else {
                return list.get(0);
            }
        }
    }

    public List<JournalFeed> findByGroupId(long groupId) throws SystemException {
        boolean finderClassNameCacheEnabled = JournalFeedModelImpl.CACHE_ENABLED;
        String finderClassName = JournalFeed.class.getName();
        String finderMethodName = "findByGroupId";
        String[] finderParams = new String[] { Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(groupId) };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("FROM com.liferay.portlet.journal.model.JournalFeed WHERE ");
                query.append("groupId = ?");
                query.append(" ");
                query.append("ORDER BY ");
                query.append("feedId ASC");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(groupId);
                List<JournalFeed> list = q.list();
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, list);
                return list;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return (List<JournalFeed>) result;
        }
    }

    public List<JournalFeed> findByGroupId(long groupId, int start, int end) throws SystemException {
        return findByGroupId(groupId, start, end, null);
    }

    public List<JournalFeed> findByGroupId(long groupId, int start, int end, OrderByComparator obc) throws SystemException {
        boolean finderClassNameCacheEnabled = JournalFeedModelImpl.CACHE_ENABLED;
        String finderClassName = JournalFeed.class.getName();
        String finderMethodName = "findByGroupId";
        String[] finderParams = new String[] { Long.class.getName(), "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { new Long(groupId), String.valueOf(start), String.valueOf(end), String.valueOf(obc) };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("FROM com.liferay.portlet.journal.model.JournalFeed WHERE ");
                query.append("groupId = ?");
                query.append(" ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                } else {
                    query.append("ORDER BY ");
                    query.append("feedId ASC");
                }
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(groupId);
                List<JournalFeed> list = (List<JournalFeed>) QueryUtil.list(q, getDialect(), start, end);
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, list);
                return list;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return (List<JournalFeed>) result;
        }
    }

    public JournalFeed findByGroupId_First(long groupId, OrderByComparator obc) throws NoSuchFeedException, SystemException {
        List<JournalFeed> list = findByGroupId(groupId, 0, 1, obc);
        if (list.size() == 0) {
            StringBuilder msg = new StringBuilder();
            msg.append("No JournalFeed exists with the key {");
            msg.append("groupId=" + groupId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchFeedException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public JournalFeed findByGroupId_Last(long groupId, OrderByComparator obc) throws NoSuchFeedException, SystemException {
        int count = countByGroupId(groupId);
        List<JournalFeed> list = findByGroupId(groupId, count - 1, count, obc);
        if (list.size() == 0) {
            StringBuilder msg = new StringBuilder();
            msg.append("No JournalFeed exists with the key {");
            msg.append("groupId=" + groupId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchFeedException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public JournalFeed[] findByGroupId_PrevAndNext(long id, long groupId, OrderByComparator obc) throws NoSuchFeedException, SystemException {
        JournalFeed journalFeed = findByPrimaryKey(id);
        int count = countByGroupId(groupId);
        Session session = null;
        try {
            session = openSession();
            StringBuilder query = new StringBuilder();
            query.append("FROM com.liferay.portlet.journal.model.JournalFeed WHERE ");
            query.append("groupId = ?");
            query.append(" ");
            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            } else {
                query.append("ORDER BY ");
                query.append("feedId ASC");
            }
            Query q = session.createQuery(query.toString());
            QueryPos qPos = QueryPos.getInstance(q);
            qPos.add(groupId);
            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, journalFeed);
            JournalFeed[] array = new JournalFeedImpl[3];
            array[0] = (JournalFeed) objArray[0];
            array[1] = (JournalFeed) objArray[1];
            array[2] = (JournalFeed) objArray[2];
            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public JournalFeed findByG_F(long groupId, String feedId) throws NoSuchFeedException, SystemException {
        JournalFeed journalFeed = fetchByG_F(groupId, feedId);
        if (journalFeed == null) {
            StringBuilder msg = new StringBuilder();
            msg.append("No JournalFeed exists with the key {");
            msg.append("groupId=" + groupId);
            msg.append(", ");
            msg.append("feedId=" + feedId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            if (_log.isWarnEnabled()) {
                _log.warn(msg.toString());
            }
            throw new NoSuchFeedException(msg.toString());
        }
        return journalFeed;
    }

    public JournalFeed fetchByG_F(long groupId, String feedId) throws SystemException {
        boolean finderClassNameCacheEnabled = JournalFeedModelImpl.CACHE_ENABLED;
        String finderClassName = JournalFeed.class.getName();
        String finderMethodName = "fetchByG_F";
        String[] finderParams = new String[] { Long.class.getName(), String.class.getName() };
        Object[] finderArgs = new Object[] { new Long(groupId), feedId };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("FROM com.liferay.portlet.journal.model.JournalFeed WHERE ");
                query.append("groupId = ?");
                query.append(" AND ");
                if (feedId == null) {
                    query.append("feedId IS NULL");
                } else {
                    query.append("feedId = ?");
                }
                query.append(" ");
                query.append("ORDER BY ");
                query.append("feedId ASC");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(groupId);
                if (feedId != null) {
                    qPos.add(feedId);
                }
                List<JournalFeed> list = q.list();
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, list);
                if (list.size() == 0) {
                    return null;
                } else {
                    return list.get(0);
                }
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            List<JournalFeed> list = (List<JournalFeed>) result;
            if (list.size() == 0) {
                return null;
            } else {
                return list.get(0);
            }
        }
    }

    public List<Object> findWithDynamicQuery(DynamicQuery dynamicQuery) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            dynamicQuery.compile(session);
            return dynamicQuery.list();
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<Object> findWithDynamicQuery(DynamicQuery dynamicQuery, int start, int end) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            dynamicQuery.setLimit(start, end);
            dynamicQuery.compile(session);
            return dynamicQuery.list();
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<JournalFeed> findAll() throws SystemException {
        return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
    }

    public List<JournalFeed> findAll(int start, int end) throws SystemException {
        return findAll(start, end, null);
    }

    public List<JournalFeed> findAll(int start, int end, OrderByComparator obc) throws SystemException {
        boolean finderClassNameCacheEnabled = JournalFeedModelImpl.CACHE_ENABLED;
        String finderClassName = JournalFeed.class.getName();
        String finderMethodName = "findAll";
        String[] finderParams = new String[] { "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { String.valueOf(start), String.valueOf(end), String.valueOf(obc) };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("FROM com.liferay.portlet.journal.model.JournalFeed ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                } else {
                    query.append("ORDER BY ");
                    query.append("feedId ASC");
                }
                Query q = session.createQuery(query.toString());
                List<JournalFeed> list = (List<JournalFeed>) QueryUtil.list(q, getDialect(), start, end);
                if (obc == null) {
                    Collections.sort(list);
                }
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, list);
                return list;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return (List<JournalFeed>) result;
        }
    }

    public void removeByUuid(String uuid) throws SystemException {
        for (JournalFeed journalFeed : findByUuid(uuid)) {
            remove(journalFeed);
        }
    }

    public void removeByUUID_G(String uuid, long groupId) throws NoSuchFeedException, SystemException {
        JournalFeed journalFeed = findByUUID_G(uuid, groupId);
        remove(journalFeed);
    }

    public void removeByGroupId(long groupId) throws SystemException {
        for (JournalFeed journalFeed : findByGroupId(groupId)) {
            remove(journalFeed);
        }
    }

    public void removeByG_F(long groupId, String feedId) throws NoSuchFeedException, SystemException {
        JournalFeed journalFeed = findByG_F(groupId, feedId);
        remove(journalFeed);
    }

    public void removeAll() throws SystemException {
        for (JournalFeed journalFeed : findAll()) {
            remove(journalFeed);
        }
    }

    public int countByUuid(String uuid) throws SystemException {
        boolean finderClassNameCacheEnabled = JournalFeedModelImpl.CACHE_ENABLED;
        String finderClassName = JournalFeed.class.getName();
        String finderMethodName = "countByUuid";
        String[] finderParams = new String[] { String.class.getName() };
        Object[] finderArgs = new Object[] { uuid };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("SELECT COUNT(*) ");
                query.append("FROM com.liferay.portlet.journal.model.JournalFeed WHERE ");
                if (uuid == null) {
                    query.append("uuid_ IS NULL");
                } else {
                    query.append("uuid_ = ?");
                }
                query.append(" ");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                if (uuid != null) {
                    qPos.add(uuid);
                }
                Long count = null;
                Iterator<Long> itr = q.list().iterator();
                if (itr.hasNext()) {
                    count = itr.next();
                }
                if (count == null) {
                    count = new Long(0);
                }
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, count);
                return count.intValue();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return ((Long) result).intValue();
        }
    }

    public int countByUUID_G(String uuid, long groupId) throws SystemException {
        boolean finderClassNameCacheEnabled = JournalFeedModelImpl.CACHE_ENABLED;
        String finderClassName = JournalFeed.class.getName();
        String finderMethodName = "countByUUID_G";
        String[] finderParams = new String[] { String.class.getName(), Long.class.getName() };
        Object[] finderArgs = new Object[] { uuid, new Long(groupId) };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("SELECT COUNT(*) ");
                query.append("FROM com.liferay.portlet.journal.model.JournalFeed WHERE ");
                if (uuid == null) {
                    query.append("uuid_ IS NULL");
                } else {
                    query.append("uuid_ = ?");
                }
                query.append(" AND ");
                query.append("groupId = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                if (uuid != null) {
                    qPos.add(uuid);
                }
                qPos.add(groupId);
                Long count = null;
                Iterator<Long> itr = q.list().iterator();
                if (itr.hasNext()) {
                    count = itr.next();
                }
                if (count == null) {
                    count = new Long(0);
                }
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, count);
                return count.intValue();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return ((Long) result).intValue();
        }
    }

    public int countByGroupId(long groupId) throws SystemException {
        boolean finderClassNameCacheEnabled = JournalFeedModelImpl.CACHE_ENABLED;
        String finderClassName = JournalFeed.class.getName();
        String finderMethodName = "countByGroupId";
        String[] finderParams = new String[] { Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(groupId) };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("SELECT COUNT(*) ");
                query.append("FROM com.liferay.portlet.journal.model.JournalFeed WHERE ");
                query.append("groupId = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(groupId);
                Long count = null;
                Iterator<Long> itr = q.list().iterator();
                if (itr.hasNext()) {
                    count = itr.next();
                }
                if (count == null) {
                    count = new Long(0);
                }
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, count);
                return count.intValue();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return ((Long) result).intValue();
        }
    }

    public int countByG_F(long groupId, String feedId) throws SystemException {
        boolean finderClassNameCacheEnabled = JournalFeedModelImpl.CACHE_ENABLED;
        String finderClassName = JournalFeed.class.getName();
        String finderMethodName = "countByG_F";
        String[] finderParams = new String[] { Long.class.getName(), String.class.getName() };
        Object[] finderArgs = new Object[] { new Long(groupId), feedId };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("SELECT COUNT(*) ");
                query.append("FROM com.liferay.portlet.journal.model.JournalFeed WHERE ");
                query.append("groupId = ?");
                query.append(" AND ");
                if (feedId == null) {
                    query.append("feedId IS NULL");
                } else {
                    query.append("feedId = ?");
                }
                query.append(" ");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(groupId);
                if (feedId != null) {
                    qPos.add(feedId);
                }
                Long count = null;
                Iterator<Long> itr = q.list().iterator();
                if (itr.hasNext()) {
                    count = itr.next();
                }
                if (count == null) {
                    count = new Long(0);
                }
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, count);
                return count.intValue();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return ((Long) result).intValue();
        }
    }

    public int countAll() throws SystemException {
        boolean finderClassNameCacheEnabled = JournalFeedModelImpl.CACHE_ENABLED;
        String finderClassName = JournalFeed.class.getName();
        String finderMethodName = "countAll";
        String[] finderParams = new String[] {};
        Object[] finderArgs = new Object[] {};
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                Query q = session.createQuery("SELECT COUNT(*) FROM com.liferay.portlet.journal.model.JournalFeed");
                Long count = null;
                Iterator<Long> itr = q.list().iterator();
                if (itr.hasNext()) {
                    count = itr.next();
                }
                if (count == null) {
                    count = new Long(0);
                }
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, count);
                return count.intValue();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return ((Long) result).intValue();
        }
    }

    public void registerListener(ModelListener listener) {
        List<ModelListener> listeners = ListUtil.fromArray(_listeners);
        listeners.add(listener);
        _listeners = listeners.toArray(new ModelListener[listeners.size()]);
    }

    public void unregisterListener(ModelListener listener) {
        List<ModelListener> listeners = ListUtil.fromArray(_listeners);
        listeners.remove(listener);
        _listeners = listeners.toArray(new ModelListener[listeners.size()]);
    }

    public void afterPropertiesSet() {
        String[] listenerClassNames = StringUtil.split(GetterUtil.getString(com.liferay.portal.util.PropsUtil.get("value.object.listener.com.liferay.portlet.journal.model.JournalFeed")));
        if (listenerClassNames.length > 0) {
            try {
                List<ModelListener> listeners = new ArrayList<ModelListener>();
                for (String listenerClassName : listenerClassNames) {
                    listeners.add((ModelListener) Class.forName(listenerClassName).newInstance());
                }
                _listeners = listeners.toArray(new ModelListener[listeners.size()]);
            } catch (Exception e) {
                _log.error(e);
            }
        }
    }

    private static Log _log = LogFactory.getLog(JournalFeedPersistenceImpl.class);

    private ModelListener[] _listeners = new ModelListener[0];
}
