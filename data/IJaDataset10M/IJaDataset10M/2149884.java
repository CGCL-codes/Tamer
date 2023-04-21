package com.liferay.wsrp.service.persistence;

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
import com.liferay.wsrp.NoSuchConfiguredProducerException;
import com.liferay.wsrp.model.WSRPConfiguredProducer;
import com.liferay.wsrp.model.impl.WSRPConfiguredProducerImpl;
import com.liferay.wsrp.model.impl.WSRPConfiguredProducerModelImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <a href="WSRPConfiguredProducerPersistenceImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class WSRPConfiguredProducerPersistenceImpl extends BasePersistenceImpl implements WSRPConfiguredProducerPersistence {

    public WSRPConfiguredProducer create(long configuredProducerId) {
        WSRPConfiguredProducer wsrpConfiguredProducer = new WSRPConfiguredProducerImpl();
        wsrpConfiguredProducer.setNew(true);
        wsrpConfiguredProducer.setPrimaryKey(configuredProducerId);
        return wsrpConfiguredProducer;
    }

    public WSRPConfiguredProducer remove(long configuredProducerId) throws NoSuchConfiguredProducerException, SystemException {
        Session session = null;
        try {
            session = openSession();
            WSRPConfiguredProducer wsrpConfiguredProducer = (WSRPConfiguredProducer) session.get(WSRPConfiguredProducerImpl.class, new Long(configuredProducerId));
            if (wsrpConfiguredProducer == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn("No WSRPConfiguredProducer exists with the primary key " + configuredProducerId);
                }
                throw new NoSuchConfiguredProducerException("No WSRPConfiguredProducer exists with the primary key " + configuredProducerId);
            }
            return remove(wsrpConfiguredProducer);
        } catch (NoSuchConfiguredProducerException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public WSRPConfiguredProducer remove(WSRPConfiguredProducer wsrpConfiguredProducer) throws SystemException {
        if (_listeners.length > 0) {
            for (ModelListener listener : _listeners) {
                listener.onBeforeRemove(wsrpConfiguredProducer);
            }
        }
        wsrpConfiguredProducer = removeImpl(wsrpConfiguredProducer);
        if (_listeners.length > 0) {
            for (ModelListener listener : _listeners) {
                listener.onAfterRemove(wsrpConfiguredProducer);
            }
        }
        return wsrpConfiguredProducer;
    }

    protected WSRPConfiguredProducer removeImpl(WSRPConfiguredProducer wsrpConfiguredProducer) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            if (BatchSessionUtil.isEnabled()) {
                Object staleObject = session.get(WSRPConfiguredProducerImpl.class, wsrpConfiguredProducer.getPrimaryKeyObj());
                if (staleObject != null) {
                    session.evict(staleObject);
                }
            }
            session.delete(wsrpConfiguredProducer);
            session.flush();
            return wsrpConfiguredProducer;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
            FinderCacheUtil.clearCache(WSRPConfiguredProducer.class.getName());
        }
    }

    /**
	 * @deprecated Use <code>update(WSRPConfiguredProducer wsrpConfiguredProducer, boolean merge)</code>.
	 */
    public WSRPConfiguredProducer update(WSRPConfiguredProducer wsrpConfiguredProducer) throws SystemException {
        if (_log.isWarnEnabled()) {
            _log.warn("Using the deprecated update(WSRPConfiguredProducer wsrpConfiguredProducer) method. Use update(WSRPConfiguredProducer wsrpConfiguredProducer, boolean merge) instead.");
        }
        return update(wsrpConfiguredProducer, false);
    }

    /**
	 * Add, update, or merge, the entity. This method also calls the model
	 * listeners to trigger the proper events associated with adding, deleting,
	 * or updating an entity.
	 *
	 * @param        wsrpConfiguredProducer the entity to add, update, or merge
	 * @param        merge boolean value for whether to merge the entity. The
	 *                default value is false. Setting merge to true is more
	 *                expensive and should only be true when wsrpConfiguredProducer is
	 *                transient. See LEP-5473 for a detailed discussion of this
	 *                method.
	 * @return        true if the portlet can be displayed via Ajax
	 */
    public WSRPConfiguredProducer update(WSRPConfiguredProducer wsrpConfiguredProducer, boolean merge) throws SystemException {
        boolean isNew = wsrpConfiguredProducer.isNew();
        if (_listeners.length > 0) {
            for (ModelListener listener : _listeners) {
                if (isNew) {
                    listener.onBeforeCreate(wsrpConfiguredProducer);
                } else {
                    listener.onBeforeUpdate(wsrpConfiguredProducer);
                }
            }
        }
        wsrpConfiguredProducer = updateImpl(wsrpConfiguredProducer, merge);
        if (_listeners.length > 0) {
            for (ModelListener listener : _listeners) {
                if (isNew) {
                    listener.onAfterCreate(wsrpConfiguredProducer);
                } else {
                    listener.onAfterUpdate(wsrpConfiguredProducer);
                }
            }
        }
        return wsrpConfiguredProducer;
    }

    public WSRPConfiguredProducer updateImpl(com.liferay.wsrp.model.WSRPConfiguredProducer wsrpConfiguredProducer, boolean merge) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            BatchSessionUtil.update(session, wsrpConfiguredProducer, merge);
            wsrpConfiguredProducer.setNew(false);
            return wsrpConfiguredProducer;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
            FinderCacheUtil.clearCache(WSRPConfiguredProducer.class.getName());
        }
    }

    public WSRPConfiguredProducer findByPrimaryKey(long configuredProducerId) throws NoSuchConfiguredProducerException, SystemException {
        WSRPConfiguredProducer wsrpConfiguredProducer = fetchByPrimaryKey(configuredProducerId);
        if (wsrpConfiguredProducer == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("No WSRPConfiguredProducer exists with the primary key " + configuredProducerId);
            }
            throw new NoSuchConfiguredProducerException("No WSRPConfiguredProducer exists with the primary key " + configuredProducerId);
        }
        return wsrpConfiguredProducer;
    }

    public WSRPConfiguredProducer fetchByPrimaryKey(long configuredProducerId) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            return (WSRPConfiguredProducer) session.get(WSRPConfiguredProducerImpl.class, new Long(configuredProducerId));
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List<WSRPConfiguredProducer> findByP_N(String portalId, String namespace) throws SystemException {
        boolean finderClassNameCacheEnabled = WSRPConfiguredProducerModelImpl.CACHE_ENABLED;
        String finderClassName = WSRPConfiguredProducer.class.getName();
        String finderMethodName = "findByP_N";
        String[] finderParams = new String[] { String.class.getName(), String.class.getName() };
        Object[] finderArgs = new Object[] { portalId, namespace };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("FROM com.liferay.wsrp.model.WSRPConfiguredProducer WHERE ");
                if (portalId == null) {
                    query.append("portalId IS NULL");
                } else {
                    query.append("portalId = ?");
                }
                query.append(" AND ");
                if (namespace == null) {
                    query.append("namespace IS NULL");
                } else {
                    query.append("namespace = ?");
                }
                query.append(" ");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                if (portalId != null) {
                    qPos.add(portalId);
                }
                if (namespace != null) {
                    qPos.add(namespace);
                }
                List<WSRPConfiguredProducer> list = q.list();
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, list);
                return list;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return (List<WSRPConfiguredProducer>) result;
        }
    }

    public List<WSRPConfiguredProducer> findByP_N(String portalId, String namespace, int start, int end) throws SystemException {
        return findByP_N(portalId, namespace, start, end, null);
    }

    public List<WSRPConfiguredProducer> findByP_N(String portalId, String namespace, int start, int end, OrderByComparator obc) throws SystemException {
        boolean finderClassNameCacheEnabled = WSRPConfiguredProducerModelImpl.CACHE_ENABLED;
        String finderClassName = WSRPConfiguredProducer.class.getName();
        String finderMethodName = "findByP_N";
        String[] finderParams = new String[] { String.class.getName(), String.class.getName(), "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { portalId, namespace, String.valueOf(start), String.valueOf(end), String.valueOf(obc) };
        Object result = null;
        if (finderClassNameCacheEnabled) {
            result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        }
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringBuilder query = new StringBuilder();
                query.append("FROM com.liferay.wsrp.model.WSRPConfiguredProducer WHERE ");
                if (portalId == null) {
                    query.append("portalId IS NULL");
                } else {
                    query.append("portalId = ?");
                }
                query.append(" AND ");
                if (namespace == null) {
                    query.append("namespace IS NULL");
                } else {
                    query.append("namespace = ?");
                }
                query.append(" ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                if (portalId != null) {
                    qPos.add(portalId);
                }
                if (namespace != null) {
                    qPos.add(namespace);
                }
                List<WSRPConfiguredProducer> list = (List<WSRPConfiguredProducer>) QueryUtil.list(q, getDialect(), start, end);
                FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, list);
                return list;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                closeSession(session);
            }
        } else {
            return (List<WSRPConfiguredProducer>) result;
        }
    }

    public WSRPConfiguredProducer findByP_N_First(String portalId, String namespace, OrderByComparator obc) throws NoSuchConfiguredProducerException, SystemException {
        List<WSRPConfiguredProducer> list = findByP_N(portalId, namespace, 0, 1, obc);
        if (list.size() == 0) {
            StringBuilder msg = new StringBuilder();
            msg.append("No WSRPConfiguredProducer exists with the key {");
            msg.append("portalId=" + portalId);
            msg.append(", ");
            msg.append("namespace=" + namespace);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchConfiguredProducerException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public WSRPConfiguredProducer findByP_N_Last(String portalId, String namespace, OrderByComparator obc) throws NoSuchConfiguredProducerException, SystemException {
        int count = countByP_N(portalId, namespace);
        List<WSRPConfiguredProducer> list = findByP_N(portalId, namespace, count - 1, count, obc);
        if (list.size() == 0) {
            StringBuilder msg = new StringBuilder();
            msg.append("No WSRPConfiguredProducer exists with the key {");
            msg.append("portalId=" + portalId);
            msg.append(", ");
            msg.append("namespace=" + namespace);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchConfiguredProducerException(msg.toString());
        } else {
            return list.get(0);
        }
    }

    public WSRPConfiguredProducer[] findByP_N_PrevAndNext(long configuredProducerId, String portalId, String namespace, OrderByComparator obc) throws NoSuchConfiguredProducerException, SystemException {
        WSRPConfiguredProducer wsrpConfiguredProducer = findByPrimaryKey(configuredProducerId);
        int count = countByP_N(portalId, namespace);
        Session session = null;
        try {
            session = openSession();
            StringBuilder query = new StringBuilder();
            query.append("FROM com.liferay.wsrp.model.WSRPConfiguredProducer WHERE ");
            if (portalId == null) {
                query.append("portalId IS NULL");
            } else {
                query.append("portalId = ?");
            }
            query.append(" AND ");
            if (namespace == null) {
                query.append("namespace IS NULL");
            } else {
                query.append("namespace = ?");
            }
            query.append(" ");
            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            }
            Query q = session.createQuery(query.toString());
            QueryPos qPos = QueryPos.getInstance(q);
            if (portalId != null) {
                qPos.add(portalId);
            }
            if (namespace != null) {
                qPos.add(namespace);
            }
            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, wsrpConfiguredProducer);
            WSRPConfiguredProducer[] array = new WSRPConfiguredProducerImpl[3];
            array[0] = (WSRPConfiguredProducer) objArray[0];
            array[1] = (WSRPConfiguredProducer) objArray[1];
            array[2] = (WSRPConfiguredProducer) objArray[2];
            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
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

    public List<WSRPConfiguredProducer> findAll() throws SystemException {
        return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
    }

    public List<WSRPConfiguredProducer> findAll(int start, int end) throws SystemException {
        return findAll(start, end, null);
    }

    public List<WSRPConfiguredProducer> findAll(int start, int end, OrderByComparator obc) throws SystemException {
        boolean finderClassNameCacheEnabled = WSRPConfiguredProducerModelImpl.CACHE_ENABLED;
        String finderClassName = WSRPConfiguredProducer.class.getName();
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
                query.append("FROM com.liferay.wsrp.model.WSRPConfiguredProducer ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                }
                Query q = session.createQuery(query.toString());
                List<WSRPConfiguredProducer> list = (List<WSRPConfiguredProducer>) QueryUtil.list(q, getDialect(), start, end);
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
            return (List<WSRPConfiguredProducer>) result;
        }
    }

    public void removeByP_N(String portalId, String namespace) throws SystemException {
        for (WSRPConfiguredProducer wsrpConfiguredProducer : findByP_N(portalId, namespace)) {
            remove(wsrpConfiguredProducer);
        }
    }

    public void removeAll() throws SystemException {
        for (WSRPConfiguredProducer wsrpConfiguredProducer : findAll()) {
            remove(wsrpConfiguredProducer);
        }
    }

    public int countByP_N(String portalId, String namespace) throws SystemException {
        boolean finderClassNameCacheEnabled = WSRPConfiguredProducerModelImpl.CACHE_ENABLED;
        String finderClassName = WSRPConfiguredProducer.class.getName();
        String finderMethodName = "countByP_N";
        String[] finderParams = new String[] { String.class.getName(), String.class.getName() };
        Object[] finderArgs = new Object[] { portalId, namespace };
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
                query.append("FROM com.liferay.wsrp.model.WSRPConfiguredProducer WHERE ");
                if (portalId == null) {
                    query.append("portalId IS NULL");
                } else {
                    query.append("portalId = ?");
                }
                query.append(" AND ");
                if (namespace == null) {
                    query.append("namespace IS NULL");
                } else {
                    query.append("namespace = ?");
                }
                query.append(" ");
                Query q = session.createQuery(query.toString());
                QueryPos qPos = QueryPos.getInstance(q);
                if (portalId != null) {
                    qPos.add(portalId);
                }
                if (namespace != null) {
                    qPos.add(namespace);
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
        boolean finderClassNameCacheEnabled = WSRPConfiguredProducerModelImpl.CACHE_ENABLED;
        String finderClassName = WSRPConfiguredProducer.class.getName();
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
                Query q = session.createQuery("SELECT COUNT(*) FROM com.liferay.wsrp.model.WSRPConfiguredProducer");
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
        String[] listenerClassNames = StringUtil.split(GetterUtil.getString(com.liferay.portal.util.PropsUtil.get("value.object.listener.com.liferay.wsrp.model.WSRPConfiguredProducer")));
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

    private static Log _log = LogFactory.getLog(WSRPConfiguredProducerPersistenceImpl.class);

    private ModelListener[] _listeners = new ModelListener[0];
}
