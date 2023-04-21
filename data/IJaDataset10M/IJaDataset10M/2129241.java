package com.liferay.portlet.tasks.service.persistence;

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
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.BatchSessionUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portlet.tasks.NoSuchProposalException;
import com.liferay.portlet.tasks.model.TasksProposal;
import com.liferay.portlet.tasks.model.impl.TasksProposalImpl;
import com.liferay.portlet.tasks.model.impl.TasksProposalModelImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <a href="TasksProposalPersistenceImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class TasksProposalPersistenceImpl extends BasePersistenceImpl implements TasksProposalPersistence {

    public TasksProposal create(long proposalId) {
        TasksProposal tasksProposal = new TasksProposalImpl();
        tasksProposal.setNew(true);
        tasksProposal.setPrimaryKey(proposalId);
        return tasksProposal;
    }

    public TasksProposal remove(long proposalId) throws NoSuchProposalException, SystemException {
        Session session = null;
        try {
            session = openSession();
            TasksProposal tasksProposal = (TasksProposal) session.get(TasksProposalImpl.class, new Long(proposalId));
            if (tasksProposal == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn("No TasksProposal exists with the primary key " + proposalId);
                }
                throw new NoSuchProposalException("No TasksProposal exists with the primary key " + proposalId);
            }
            return remove(tasksProposal);
        } catch (NoSuchProposalException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public TasksProposal remove(TasksProposal tasksProposal) throws SystemException {
        if (_listeners.length > 0) {
            for (ModelListener listener : _listeners) {
                listener.onBeforeRemove(tasksProposal);
            }
        }
        tasksProposal = removeImpl(tasksProposal);
        if (_listeners.length > 0) {
            for (ModelListener listener : _listeners) {
                listener.onAfterRemove(tasksProposal);
            }
        }
        return tasksProposal;
    }

    protected TasksProposal removeImpl(TasksProposal tasksProposal) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            if (BatchSessionUtil.isEnabled()) {
                Object staleObject = session.get(TasksProposalImpl.class, tasksProposal.getPrimaryKeyObj());
                if (staleObject != null) {
                    session.evict(staleObject);
                }
            }
            session.delete(tasksProposal);
            session.flush();
            return tasksProposal;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
            FinderCacheUtil.clearCache(TasksProposal.class.getName());
        }
    }

    /**
	 * @deprecated Use <code>update(TasksProposal tasksProposal, boolean merge)</code>.
	 */
    public TasksProposal update(TasksProposal tasksProposal) throws SystemException {
        if (_log.isWarnEnabled()) {
            _log.warn("Using the deprecated update(TasksProposal tasksProposal) method. Use update(TasksProposal tasksProposal, boolean merge) instead.");
        }
        return update(tasksProposal, false);
    }

    /**
	 * Add, update, or merge, the entity. This method also calls the model
	 * listeners to trigger the proper events associated with adding, deleting,
	 * or updating an entity.
	 *
	 * @param        tasksProposal the entity to add, update, or merge
	 * @param        merge boolean value for whether to merge the entity. The
	 *                default value is false. Setting merge to true is more
	 *                expensive and should only be true when tasksProposal is
	 *                transient. See LEP-5473 for a detailed discussion of this
	 *                method.
	 * @return        true if the portlet can be displayed via Ajax
	 */
    public TasksProposal update(TasksProposal tasksProposal, boolean merge) throws SystemException {
        boolean isNew = tasksProposal.isNew();
        if (_listeners.length > 0) {
            for (ModelListener listener : _listeners) {
                if (isNew) {
                    listener.onBeforeCreate(tasksProposal);
                } else {
                    listener.onBeforeUpdate(tasksProposal);
                }
            }
        }
        tasksProposal = updateImpl(tasksProposal, merge);
        if (_listeners.length > 0) {
            for (ModelListener listener : _listeners) {
                if (isNew) {
                    listener.onAfterCreate(tasksProposal);
                } else {
                    listener.onAfterUpdate(tasksProposal);
                }
            }
        }
        return tasksProposal;
    }

    public TasksProposal updateImpl(com.liferay.portlet.tasks.model.TasksProposal tasksProposal, boolean merge) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            BatchSessionUtil.update(session, tasksProposal, merge);
            tasksProposal.setNew(false);
            return tasksProposal;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
            FinderCacheUtil.clearCache(TasksProposal.class.getName());
        }
    }

    public TasksProposal findByPrimaryKey(long proposalId) throws NoSuchProposalException, SystemException {
        TasksProposal tasksProposal = fetchByPrimaryKey(proposalId);
        if (tasksProposal == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("No TasksProposal exists with the primary key " + proposalId);
            }
            throw new NoSuchProposalException("No TasksProposal exists with the primary key " + proposalId);
        }
        return tasksProposal;
    }

    public TasksProposal fetchByPrimaryKey(long proposalId) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            return (TasksProposal) session.get(TasksProposalImpl.class, new Long(proposalId));
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<TasksProposal> findByGroupId(long groupId) throws SystemException {
        boolean finderClassNameCacheEnabled = TasksProposalModelImpl.CACHE_ENABLED;
        String finderClassName = TasksProposal.class.getName();
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
                query.append("FROM com.liferay.portlet.tasks.model.TasksProposal WHERE ");
                query.append("groupId = ?");
                query.append(" ");
                query.append("ORDER BY ");
                query.append("dueDate ASC, ");
                query.append("createDate ASC");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(groupId);
                List<TasksProposal> list = q.list();
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, list);
                return list;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return (List<TasksProposal>) result;
        }
    }

    public List<TasksProposal> findByGroupId(long groupId, int start, int end) throws SystemException {
        return findByGroupId(groupId, start, end, null);
    }

    public List<TasksProposal> findByGroupId(long groupId, int start, int end, OrderByComparator obc) throws SystemException {
        boolean finderClassNameCacheEnabled = TasksProposalModelImpl.CACHE_ENABLED;
        String finderClassName = TasksProposal.class.getName();
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
                query.append("FROM com.liferay.portlet.tasks.model.TasksProposal WHERE ");
                query.append("groupId = ?");
                query.append(" ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                } else {
                    query.append("ORDER BY ");
                    query.append("dueDate ASC, ");
                    query.append("createDate ASC");
                }
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(groupId);
                List<TasksProposal> list = (List<TasksProposal>) QueryUtil.list(q, getDialect(), start, end);
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, list);
                return list;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return (List<TasksProposal>) result;
        }
    }

    public TasksProposal findByGroupId_First(long groupId, OrderByComparator obc) throws NoSuchProposalException, SystemException {
        List<TasksProposal> list = findByGroupId(groupId, 0, 1, obc);
        if (list.size() == 0) {
            StringBuilder msg = new StringBuilder();
            msg.append("No TasksProposal exists with the key {");
            msg.append("groupId=" + groupId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchProposalException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public TasksProposal findByGroupId_Last(long groupId, OrderByComparator obc) throws NoSuchProposalException, SystemException {
        int count = countByGroupId(groupId);
        List<TasksProposal> list = findByGroupId(groupId, count - 1, count, obc);
        if (list.size() == 0) {
            StringBuilder msg = new StringBuilder();
            msg.append("No TasksProposal exists with the key {");
            msg.append("groupId=" + groupId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchProposalException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public TasksProposal[] findByGroupId_PrevAndNext(long proposalId, long groupId, OrderByComparator obc) throws NoSuchProposalException, SystemException {
        TasksProposal tasksProposal = findByPrimaryKey(proposalId);
        int count = countByGroupId(groupId);
        Session session = null;
        try {
            session = openSession();
            StringBuilder query = new StringBuilder();
            query.append("FROM com.liferay.portlet.tasks.model.TasksProposal WHERE ");
            query.append("groupId = ?");
            query.append(" ");
            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            } else {
                query.append("ORDER BY ");
                query.append("dueDate ASC, ");
                query.append("createDate ASC");
            }
            Query q = session.createQuery(query.toString());
            QueryPos qPos = QueryPos.getInstance(q);
            qPos.add(groupId);
            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, tasksProposal);
            TasksProposal[] array = new TasksProposalImpl[3];
            array[0] = (TasksProposal) objArray[0];
            array[1] = (TasksProposal) objArray[1];
            array[2] = (TasksProposal) objArray[2];
            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<TasksProposal> findByG_U(long groupId, long userId) throws SystemException {
        boolean finderClassNameCacheEnabled = TasksProposalModelImpl.CACHE_ENABLED;
        String finderClassName = TasksProposal.class.getName();
        String finderMethodName = "findByG_U";
        String[] finderParams = new String[] { Long.class.getName(), Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(groupId), new Long(userId) };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("FROM com.liferay.portlet.tasks.model.TasksProposal WHERE ");
                query.append("groupId = ?");
                query.append(" AND ");
                query.append("userId = ?");
                query.append(" ");
                query.append("ORDER BY ");
                query.append("dueDate ASC, ");
                query.append("createDate ASC");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(groupId);
                qPos.add(userId);
                List<TasksProposal> list = q.list();
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, list);
                return list;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return (List<TasksProposal>) result;
        }
    }

    public List<TasksProposal> findByG_U(long groupId, long userId, int start, int end) throws SystemException {
        return findByG_U(groupId, userId, start, end, null);
    }

    public List<TasksProposal> findByG_U(long groupId, long userId, int start, int end, OrderByComparator obc) throws SystemException {
        boolean finderClassNameCacheEnabled = TasksProposalModelImpl.CACHE_ENABLED;
        String finderClassName = TasksProposal.class.getName();
        String finderMethodName = "findByG_U";
        String[] finderParams = new String[] { Long.class.getName(), Long.class.getName(), "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { new Long(groupId), new Long(userId), String.valueOf(start), String.valueOf(end), String.valueOf(obc) };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("FROM com.liferay.portlet.tasks.model.TasksProposal WHERE ");
                query.append("groupId = ?");
                query.append(" AND ");
                query.append("userId = ?");
                query.append(" ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                } else {
                    query.append("ORDER BY ");
                    query.append("dueDate ASC, ");
                    query.append("createDate ASC");
                }
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(groupId);
                qPos.add(userId);
                List<TasksProposal> list = (List<TasksProposal>) QueryUtil.list(q, getDialect(), start, end);
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, list);
                return list;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return (List<TasksProposal>) result;
        }
    }

    public TasksProposal findByG_U_First(long groupId, long userId, OrderByComparator obc) throws NoSuchProposalException, SystemException {
        List<TasksProposal> list = findByG_U(groupId, userId, 0, 1, obc);
        if (list.size() == 0) {
            StringBuilder msg = new StringBuilder();
            msg.append("No TasksProposal exists with the key {");
            msg.append("groupId=" + groupId);
            msg.append(", ");
            msg.append("userId=" + userId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchProposalException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public TasksProposal findByG_U_Last(long groupId, long userId, OrderByComparator obc) throws NoSuchProposalException, SystemException {
        int count = countByG_U(groupId, userId);
        List<TasksProposal> list = findByG_U(groupId, userId, count - 1, count, obc);
        if (list.size() == 0) {
            StringBuilder msg = new StringBuilder();
            msg.append("No TasksProposal exists with the key {");
            msg.append("groupId=" + groupId);
            msg.append(", ");
            msg.append("userId=" + userId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchProposalException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public TasksProposal[] findByG_U_PrevAndNext(long proposalId, long groupId, long userId, OrderByComparator obc) throws NoSuchProposalException, SystemException {
        TasksProposal tasksProposal = findByPrimaryKey(proposalId);
        int count = countByG_U(groupId, userId);
        Session session = null;
        try {
            session = openSession();
            StringBuilder query = new StringBuilder();
            query.append("FROM com.liferay.portlet.tasks.model.TasksProposal WHERE ");
            query.append("groupId = ?");
            query.append(" AND ");
            query.append("userId = ?");
            query.append(" ");
            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            } else {
                query.append("ORDER BY ");
                query.append("dueDate ASC, ");
                query.append("createDate ASC");
            }
            Query q = session.createQuery(query.toString());
            QueryPos qPos = QueryPos.getInstance(q);
            qPos.add(groupId);
            qPos.add(userId);
            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, tasksProposal);
            TasksProposal[] array = new TasksProposalImpl[3];
            array[0] = (TasksProposal) objArray[0];
            array[1] = (TasksProposal) objArray[1];
            array[2] = (TasksProposal) objArray[2];
            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public TasksProposal findByC_C(long classNameId, String classPK) throws NoSuchProposalException, SystemException {
        TasksProposal tasksProposal = fetchByC_C(classNameId, classPK);
        if (tasksProposal == null) {
            StringBuilder msg = new StringBuilder();
            msg.append("No TasksProposal exists with the key {");
            msg.append("classNameId=" + classNameId);
            msg.append(", ");
            msg.append("classPK=" + classPK);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            if (_log.isWarnEnabled()) {
                _log.warn(msg.toString());
            }
            throw new NoSuchProposalException(msg.toString());
        }
        return tasksProposal;
    }

    public TasksProposal fetchByC_C(long classNameId, String classPK) throws SystemException {
        boolean finderClassNameCacheEnabled = TasksProposalModelImpl.CACHE_ENABLED;
        String finderClassName = TasksProposal.class.getName();
        String finderMethodName = "fetchByC_C";
        String[] finderParams = new String[] { Long.class.getName(), String.class.getName() };
        Object[] finderArgs = new Object[] { new Long(classNameId), classPK };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("FROM com.liferay.portlet.tasks.model.TasksProposal WHERE ");
                query.append("classNameId = ?");
                query.append(" AND ");
                if (classPK == null) {
                    query.append("classPK IS NULL");
                } else {
                    query.append("classPK = ?");
                }
                query.append(" ");
                query.append("ORDER BY ");
                query.append("dueDate ASC, ");
                query.append("createDate ASC");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(classNameId);
                if (classPK != null) {
                    qPos.add(classPK);
                }
                List<TasksProposal> list = q.list();
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
            List<TasksProposal> list = (List<TasksProposal>) result;
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

    public List<TasksProposal> findAll() throws SystemException {
        return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
    }

    public List<TasksProposal> findAll(int start, int end) throws SystemException {
        return findAll(start, end, null);
    }

    public List<TasksProposal> findAll(int start, int end, OrderByComparator obc) throws SystemException {
        boolean finderClassNameCacheEnabled = TasksProposalModelImpl.CACHE_ENABLED;
        String finderClassName = TasksProposal.class.getName();
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
                query.append("FROM com.liferay.portlet.tasks.model.TasksProposal ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                } else {
                    query.append("ORDER BY ");
                    query.append("dueDate ASC, ");
                    query.append("createDate ASC");
                }
                Query q = session.createQuery(query.toString());
                List<TasksProposal> list = (List<TasksProposal>) QueryUtil.list(q, getDialect(), start, end);
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
            return (List<TasksProposal>) result;
        }
    }

    public void removeByGroupId(long groupId) throws SystemException {
        for (TasksProposal tasksProposal : findByGroupId(groupId)) {
            remove(tasksProposal);
        }
    }

    public void removeByG_U(long groupId, long userId) throws SystemException {
        for (TasksProposal tasksProposal : findByG_U(groupId, userId)) {
            remove(tasksProposal);
        }
    }

    public void removeByC_C(long classNameId, String classPK) throws NoSuchProposalException, SystemException {
        TasksProposal tasksProposal = findByC_C(classNameId, classPK);
        remove(tasksProposal);
    }

    public void removeAll() throws SystemException {
        for (TasksProposal tasksProposal : findAll()) {
            remove(tasksProposal);
        }
    }

    public int countByGroupId(long groupId) throws SystemException {
        boolean finderClassNameCacheEnabled = TasksProposalModelImpl.CACHE_ENABLED;
        String finderClassName = TasksProposal.class.getName();
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
                query.append("FROM com.liferay.portlet.tasks.model.TasksProposal WHERE ");
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

    public int countByG_U(long groupId, long userId) throws SystemException {
        boolean finderClassNameCacheEnabled = TasksProposalModelImpl.CACHE_ENABLED;
        String finderClassName = TasksProposal.class.getName();
        String finderMethodName = "countByG_U";
        String[] finderParams = new String[] { Long.class.getName(), Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(groupId), new Long(userId) };
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
                query.append("FROM com.liferay.portlet.tasks.model.TasksProposal WHERE ");
                query.append("groupId = ?");
                query.append(" AND ");
                query.append("userId = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(groupId);
                qPos.add(userId);
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

    public int countByC_C(long classNameId, String classPK) throws SystemException {
        boolean finderClassNameCacheEnabled = TasksProposalModelImpl.CACHE_ENABLED;
        String finderClassName = TasksProposal.class.getName();
        String finderMethodName = "countByC_C";
        String[] finderParams = new String[] { Long.class.getName(), String.class.getName() };
        Object[] finderArgs = new Object[] { new Long(classNameId), classPK };
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
                query.append("FROM com.liferay.portlet.tasks.model.TasksProposal WHERE ");
                query.append("classNameId = ?");
                query.append(" AND ");
                if (classPK == null) {
                    query.append("classPK IS NULL");
                } else {
                    query.append("classPK = ?");
                }
                query.append(" ");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(classNameId);
                if (classPK != null) {
                    qPos.add(classPK);
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
        boolean finderClassNameCacheEnabled = TasksProposalModelImpl.CACHE_ENABLED;
        String finderClassName = TasksProposal.class.getName();
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
                Query q = session.createQuery("SELECT COUNT(*) FROM com.liferay.portlet.tasks.model.TasksProposal");
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
        String[] listenerClassNames = StringUtil.split(GetterUtil.getString(com.liferay.portal.util.PropsUtil.get("value.object.listener.com.liferay.portlet.tasks.model.TasksProposal")));
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

    private static Log _log = LogFactory.getLog(TasksProposalPersistenceImpl.class);

    private ModelListener[] _listeners = new ModelListener[0];
}
