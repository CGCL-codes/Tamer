package com.liferay.portlet.polls.service.persistence;

import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.bean.InitializingBean;
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
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portlet.polls.NoSuchVoteException;
import com.liferay.portlet.polls.model.PollsVote;
import com.liferay.portlet.polls.model.impl.PollsVoteImpl;
import com.liferay.portlet.polls.model.impl.PollsVoteModelImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PollsVotePersistenceImpl extends BasePersistenceImpl implements PollsVotePersistence, InitializingBean {

    private static Log _log = LogFactory.getLog(PollsVotePersistenceImpl.class);

    private ModelListener[] _listeners = new ModelListener[0];

    public PollsVote create(long voteId) {
        PollsVote pollsVote = new PollsVoteImpl();
        pollsVote.setNew(true);
        pollsVote.setPrimaryKey(voteId);
        return pollsVote;
    }

    public PollsVote remove(long voteId) throws NoSuchVoteException, SystemException {
        Session session = null;
        try {
            session = openSession();
            PollsVote pollsVote = (PollsVote) session.get(PollsVoteImpl.class, new Long(voteId));
            if (pollsVote == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn("No PollsVote exists with the primary key " + voteId);
                }
                throw new NoSuchVoteException("No PollsVote exists with the primary key " + voteId);
            }
            return remove(pollsVote);
        } catch (NoSuchVoteException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public PollsVote remove(PollsVote pollsVote) throws SystemException {
        if (_listeners.length > 0) {
            for (ModelListener listener : _listeners) {
                listener.onBeforeRemove(pollsVote);
            }
        }
        pollsVote = removeImpl(pollsVote);
        if (_listeners.length > 0) {
            for (ModelListener listener : _listeners) {
                listener.onAfterRemove(pollsVote);
            }
        }
        return pollsVote;
    }

    protected PollsVote removeImpl(PollsVote pollsVote) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            session.delete(pollsVote);
            session.flush();
            return pollsVote;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
            FinderCacheUtil.clearCache(PollsVote.class.getName());
        }
    }

    /**
     * @deprecated Use <code>update(PollsVote pollsVote, boolean merge)</code>.
     */
    public PollsVote update(PollsVote pollsVote) throws SystemException {
        if (_log.isWarnEnabled()) {
            _log.warn("Using the deprecated update(PollsVote pollsVote) method. Use update(PollsVote pollsVote, boolean merge) instead.");
        }
        return update(pollsVote, false);
    }

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param                pollsVote the entity to add, update, or merge
     * @param                merge boolean value for whether to merge the entity. The
     *                                default value is false. Setting merge to true is more
     *                                expensive and should only be true when pollsVote is
     *                                transient. See LEP-5473 for a detailed discussion of this
     *                                method.
     * @return                true if the portlet can be displayed via Ajax
     */
    public PollsVote update(PollsVote pollsVote, boolean merge) throws SystemException {
        boolean isNew = pollsVote.isNew();
        if (_listeners.length > 0) {
            for (ModelListener listener : _listeners) {
                if (isNew) {
                    listener.onBeforeCreate(pollsVote);
                } else {
                    listener.onBeforeUpdate(pollsVote);
                }
            }
        }
        pollsVote = updateImpl(pollsVote, merge);
        if (_listeners.length > 0) {
            for (ModelListener listener : _listeners) {
                if (isNew) {
                    listener.onAfterCreate(pollsVote);
                } else {
                    listener.onAfterUpdate(pollsVote);
                }
            }
        }
        return pollsVote;
    }

    public PollsVote updateImpl(com.liferay.portlet.polls.model.PollsVote pollsVote, boolean merge) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            if (merge) {
                session.merge(pollsVote);
            } else {
                if (pollsVote.isNew()) {
                    session.save(pollsVote);
                }
            }
            session.flush();
            pollsVote.setNew(false);
            return pollsVote;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
            FinderCacheUtil.clearCache(PollsVote.class.getName());
        }
    }

    public PollsVote findByPrimaryKey(long voteId) throws NoSuchVoteException, SystemException {
        PollsVote pollsVote = fetchByPrimaryKey(voteId);
        if (pollsVote == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("No PollsVote exists with the primary key " + voteId);
            }
            throw new NoSuchVoteException("No PollsVote exists with the primary key " + voteId);
        }
        return pollsVote;
    }

    public PollsVote fetchByPrimaryKey(long voteId) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            return (PollsVote) session.get(PollsVoteImpl.class, new Long(voteId));
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<PollsVote> findByQuestionId(long questionId) throws SystemException {
        boolean finderClassNameCacheEnabled = PollsVoteModelImpl.CACHE_ENABLED;
        String finderClassName = PollsVote.class.getName();
        String finderMethodName = "findByQuestionId";
        String[] finderParams = new String[] { Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(questionId) };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("FROM com.liferay.portlet.polls.model.PollsVote WHERE ");
                query.append("questionId = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(questionId);
                List<PollsVote> list = q.list();
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, list);
                return list;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return (List<PollsVote>) result;
        }
    }

    public List<PollsVote> findByQuestionId(long questionId, int start, int end) throws SystemException {
        return findByQuestionId(questionId, start, end, null);
    }

    public List<PollsVote> findByQuestionId(long questionId, int start, int end, OrderByComparator obc) throws SystemException {
        boolean finderClassNameCacheEnabled = PollsVoteModelImpl.CACHE_ENABLED;
        String finderClassName = PollsVote.class.getName();
        String finderMethodName = "findByQuestionId";
        String[] finderParams = new String[] { Long.class.getName(), "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { new Long(questionId), String.valueOf(start), String.valueOf(end), String.valueOf(obc) };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("FROM com.liferay.portlet.polls.model.PollsVote WHERE ");
                query.append("questionId = ?");
                query.append(" ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(questionId);
                List<PollsVote> list = (List<PollsVote>) QueryUtil.list(q, getDialect(), start, end);
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, list);
                return list;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return (List<PollsVote>) result;
        }
    }

    public PollsVote findByQuestionId_First(long questionId, OrderByComparator obc) throws NoSuchVoteException, SystemException {
        List<PollsVote> list = findByQuestionId(questionId, 0, 1, obc);
        if (list.size() == 0) {
            StringBuilder msg = new StringBuilder();
            msg.append("No PollsVote exists with the key {");
            msg.append("questionId=" + questionId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchVoteException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public PollsVote findByQuestionId_Last(long questionId, OrderByComparator obc) throws NoSuchVoteException, SystemException {
        int count = countByQuestionId(questionId);
        List<PollsVote> list = findByQuestionId(questionId, count - 1, count, obc);
        if (list.size() == 0) {
            StringBuilder msg = new StringBuilder();
            msg.append("No PollsVote exists with the key {");
            msg.append("questionId=" + questionId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchVoteException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public PollsVote[] findByQuestionId_PrevAndNext(long voteId, long questionId, OrderByComparator obc) throws NoSuchVoteException, SystemException {
        PollsVote pollsVote = findByPrimaryKey(voteId);
        int count = countByQuestionId(questionId);
        Session session = null;
        try {
            session = openSession();
            StringBuilder query = new StringBuilder();
            query.append("FROM com.liferay.portlet.polls.model.PollsVote WHERE ");
            query.append("questionId = ?");
            query.append(" ");
            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            Query q = session.createQuery(query.toString());
            QueryPos qPos = QueryPos.getInstance(q);
            qPos.add(questionId);
            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, pollsVote);
            PollsVote[] array = new PollsVoteImpl[3];
            array[0] = (PollsVote) objArray[0];
            array[1] = (PollsVote) objArray[1];
            array[2] = (PollsVote) objArray[2];
            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<PollsVote> findByChoiceId(long choiceId) throws SystemException {
        boolean finderClassNameCacheEnabled = PollsVoteModelImpl.CACHE_ENABLED;
        String finderClassName = PollsVote.class.getName();
        String finderMethodName = "findByChoiceId";
        String[] finderParams = new String[] { Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(choiceId) };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("FROM com.liferay.portlet.polls.model.PollsVote WHERE ");
                query.append("choiceId = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(choiceId);
                List<PollsVote> list = q.list();
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, list);
                return list;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return (List<PollsVote>) result;
        }
    }

    public List<PollsVote> findByChoiceId(long choiceId, int start, int end) throws SystemException {
        return findByChoiceId(choiceId, start, end, null);
    }

    public List<PollsVote> findByChoiceId(long choiceId, int start, int end, OrderByComparator obc) throws SystemException {
        boolean finderClassNameCacheEnabled = PollsVoteModelImpl.CACHE_ENABLED;
        String finderClassName = PollsVote.class.getName();
        String finderMethodName = "findByChoiceId";
        String[] finderParams = new String[] { Long.class.getName(), "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { new Long(choiceId), String.valueOf(start), String.valueOf(end), String.valueOf(obc) };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("FROM com.liferay.portlet.polls.model.PollsVote WHERE ");
                query.append("choiceId = ?");
                query.append(" ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(choiceId);
                List<PollsVote> list = (List<PollsVote>) QueryUtil.list(q, getDialect(), start, end);
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, list);
                return list;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return (List<PollsVote>) result;
        }
    }

    public PollsVote findByChoiceId_First(long choiceId, OrderByComparator obc) throws NoSuchVoteException, SystemException {
        List<PollsVote> list = findByChoiceId(choiceId, 0, 1, obc);
        if (list.size() == 0) {
            StringBuilder msg = new StringBuilder();
            msg.append("No PollsVote exists with the key {");
            msg.append("choiceId=" + choiceId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchVoteException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public PollsVote findByChoiceId_Last(long choiceId, OrderByComparator obc) throws NoSuchVoteException, SystemException {
        int count = countByChoiceId(choiceId);
        List<PollsVote> list = findByChoiceId(choiceId, count - 1, count, obc);
        if (list.size() == 0) {
            StringBuilder msg = new StringBuilder();
            msg.append("No PollsVote exists with the key {");
            msg.append("choiceId=" + choiceId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchVoteException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public PollsVote[] findByChoiceId_PrevAndNext(long voteId, long choiceId, OrderByComparator obc) throws NoSuchVoteException, SystemException {
        PollsVote pollsVote = findByPrimaryKey(voteId);
        int count = countByChoiceId(choiceId);
        Session session = null;
        try {
            session = openSession();
            StringBuilder query = new StringBuilder();
            query.append("FROM com.liferay.portlet.polls.model.PollsVote WHERE ");
            query.append("choiceId = ?");
            query.append(" ");
            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            Query q = session.createQuery(query.toString());
            QueryPos qPos = QueryPos.getInstance(q);
            qPos.add(choiceId);
            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, pollsVote);
            PollsVote[] array = new PollsVoteImpl[3];
            array[0] = (PollsVote) objArray[0];
            array[1] = (PollsVote) objArray[1];
            array[2] = (PollsVote) objArray[2];
            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public PollsVote findByQ_U(long questionId, long userId) throws NoSuchVoteException, SystemException {
        PollsVote pollsVote = fetchByQ_U(questionId, userId);
        if (pollsVote == null) {
            StringBuilder msg = new StringBuilder();
            msg.append("No PollsVote exists with the key {");
            msg.append("questionId=" + questionId);
            msg.append(", ");
            msg.append("userId=" + userId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            if (_log.isWarnEnabled()) {
                _log.warn(msg.toString());
            }
            throw new NoSuchVoteException(msg.toString());
        }
        return pollsVote;
    }

    public PollsVote fetchByQ_U(long questionId, long userId) throws SystemException {
        boolean finderClassNameCacheEnabled = PollsVoteModelImpl.CACHE_ENABLED;
        String finderClassName = PollsVote.class.getName();
        String finderMethodName = "fetchByQ_U";
        String[] finderParams = new String[] { Long.class.getName(), Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(questionId), new Long(userId) };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("FROM com.liferay.portlet.polls.model.PollsVote WHERE ");
                query.append("questionId = ?");
                query.append(" AND ");
                query.append("userId = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(questionId);
                qPos.add(userId);
                List<PollsVote> list = q.list();
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
            List<PollsVote> list = (List<PollsVote>) result;
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

    public List<PollsVote> findAll() throws SystemException {
        return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
    }

    public List<PollsVote> findAll(int start, int end) throws SystemException {
        return findAll(start, end, null);
    }

    public List<PollsVote> findAll(int start, int end, OrderByComparator obc) throws SystemException {
        boolean finderClassNameCacheEnabled = PollsVoteModelImpl.CACHE_ENABLED;
        String finderClassName = PollsVote.class.getName();
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
                query.append("FROM com.liferay.portlet.polls.model.PollsVote ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                Query q = session.createQuery(query.toString());
                List<PollsVote> list = (List<PollsVote>) QueryUtil.list(q, getDialect(), start, end);
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
            return (List<PollsVote>) result;
        }
    }

    public void removeByQuestionId(long questionId) throws SystemException {
        for (PollsVote pollsVote : findByQuestionId(questionId)) {
            remove(pollsVote);
        }
    }

    public void removeByChoiceId(long choiceId) throws SystemException {
        for (PollsVote pollsVote : findByChoiceId(choiceId)) {
            remove(pollsVote);
        }
    }

    public void removeByQ_U(long questionId, long userId) throws NoSuchVoteException, SystemException {
        PollsVote pollsVote = findByQ_U(questionId, userId);
        remove(pollsVote);
    }

    public void removeAll() throws SystemException {
        for (PollsVote pollsVote : findAll()) {
            remove(pollsVote);
        }
    }

    public int countByQuestionId(long questionId) throws SystemException {
        boolean finderClassNameCacheEnabled = PollsVoteModelImpl.CACHE_ENABLED;
        String finderClassName = PollsVote.class.getName();
        String finderMethodName = "countByQuestionId";
        String[] finderParams = new String[] { Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(questionId) };
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
                query.append("FROM com.liferay.portlet.polls.model.PollsVote WHERE ");
                query.append("questionId = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(questionId);
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

    public int countByChoiceId(long choiceId) throws SystemException {
        boolean finderClassNameCacheEnabled = PollsVoteModelImpl.CACHE_ENABLED;
        String finderClassName = PollsVote.class.getName();
        String finderMethodName = "countByChoiceId";
        String[] finderParams = new String[] { Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(choiceId) };
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
                query.append("FROM com.liferay.portlet.polls.model.PollsVote WHERE ");
                query.append("choiceId = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(choiceId);
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

    public int countByQ_U(long questionId, long userId) throws SystemException {
        boolean finderClassNameCacheEnabled = PollsVoteModelImpl.CACHE_ENABLED;
        String finderClassName = PollsVote.class.getName();
        String finderMethodName = "countByQ_U";
        String[] finderParams = new String[] { Long.class.getName(), Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(questionId), new Long(userId) };
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
                query.append("FROM com.liferay.portlet.polls.model.PollsVote WHERE ");
                query.append("questionId = ?");
                query.append(" AND ");
                query.append("userId = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(questionId);
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

    public int countAll() throws SystemException {
        boolean finderClassNameCacheEnabled = PollsVoteModelImpl.CACHE_ENABLED;
        String finderClassName = PollsVote.class.getName();
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
                Query q = session.createQuery("SELECT COUNT(*) FROM com.liferay.portlet.polls.model.PollsVote");
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
        String[] listenerClassNames = StringUtil.split(GetterUtil.getString(com.liferay.portal.util.PropsUtil.get("value.object.listener.com.liferay.portlet.polls.model.PollsVote")));
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
}
