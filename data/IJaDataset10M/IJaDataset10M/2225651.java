package com.liferay.portlet.wiki.service.persistence;

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
import com.liferay.portlet.wiki.NoSuchPageResourceException;
import com.liferay.portlet.wiki.model.WikiPageResource;
import com.liferay.portlet.wiki.model.impl.WikiPageResourceImpl;
import com.liferay.util.dao.hibernate.QueryUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <a href="WikiPageResourcePersistenceImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class WikiPageResourcePersistenceImpl extends BasePersistence implements WikiPageResourcePersistence {

    public WikiPageResource create(long resourcePrimKey) {
        WikiPageResource wikiPageResource = new WikiPageResourceImpl();
        wikiPageResource.setNew(true);
        wikiPageResource.setPrimaryKey(resourcePrimKey);
        return wikiPageResource;
    }

    public WikiPageResource remove(long resourcePrimKey) throws NoSuchPageResourceException, SystemException {
        Session session = null;
        try {
            session = openSession();
            WikiPageResource wikiPageResource = (WikiPageResource) session.get(WikiPageResourceImpl.class, new Long(resourcePrimKey));
            if (wikiPageResource == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn("No WikiPageResource exists with the primary key " + resourcePrimKey);
                }
                throw new NoSuchPageResourceException("No WikiPageResource exists with the primary key " + resourcePrimKey);
            }
            return remove(wikiPageResource);
        } catch (NoSuchPageResourceException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public WikiPageResource remove(WikiPageResource wikiPageResource) throws SystemException {
        ModelListener listener = _getListener();
        if (listener != null) {
            listener.onBeforeRemove(wikiPageResource);
        }
        wikiPageResource = removeImpl(wikiPageResource);
        if (listener != null) {
            listener.onAfterRemove(wikiPageResource);
        }
        return wikiPageResource;
    }

    protected WikiPageResource removeImpl(WikiPageResource wikiPageResource) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            session.delete(wikiPageResource);
            session.flush();
            return wikiPageResource;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
            FinderCache.clearCache(WikiPageResource.class.getName());
        }
    }

    public WikiPageResource update(com.liferay.portlet.wiki.model.WikiPageResource wikiPageResource) throws SystemException {
        return update(wikiPageResource, false);
    }

    public WikiPageResource update(com.liferay.portlet.wiki.model.WikiPageResource wikiPageResource, boolean merge) throws SystemException {
        ModelListener listener = _getListener();
        boolean isNew = wikiPageResource.isNew();
        if (listener != null) {
            if (isNew) {
                listener.onBeforeCreate(wikiPageResource);
            } else {
                listener.onBeforeUpdate(wikiPageResource);
            }
        }
        wikiPageResource = updateImpl(wikiPageResource, merge);
        if (listener != null) {
            if (isNew) {
                listener.onAfterCreate(wikiPageResource);
            } else {
                listener.onAfterUpdate(wikiPageResource);
            }
        }
        return wikiPageResource;
    }

    public WikiPageResource updateImpl(com.liferay.portlet.wiki.model.WikiPageResource wikiPageResource, boolean merge) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            if (merge) {
                session.merge(wikiPageResource);
            } else {
                if (wikiPageResource.isNew()) {
                    session.save(wikiPageResource);
                }
            }
            session.flush();
            wikiPageResource.setNew(false);
            return wikiPageResource;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
            FinderCache.clearCache(WikiPageResource.class.getName());
        }
    }

    public WikiPageResource findByPrimaryKey(long resourcePrimKey) throws NoSuchPageResourceException, SystemException {
        WikiPageResource wikiPageResource = fetchByPrimaryKey(resourcePrimKey);
        if (wikiPageResource == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("No WikiPageResource exists with the primary key " + resourcePrimKey);
            }
            throw new NoSuchPageResourceException("No WikiPageResource exists with the primary key " + resourcePrimKey);
        }
        return wikiPageResource;
    }

    public WikiPageResource fetchByPrimaryKey(long resourcePrimKey) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            return (WikiPageResource) session.get(WikiPageResourceImpl.class, new Long(resourcePrimKey));
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public WikiPageResource findByN_T(long nodeId, String title) throws NoSuchPageResourceException, SystemException {
        WikiPageResource wikiPageResource = fetchByN_T(nodeId, title);
        if (wikiPageResource == null) {
            StringMaker msg = new StringMaker();
            msg.append("No WikiPageResource exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("nodeId=");
            msg.append(nodeId);
            msg.append(", ");
            msg.append("title=");
            msg.append(title);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            if (_log.isWarnEnabled()) {
                _log.warn(msg.toString());
            }
            throw new NoSuchPageResourceException(msg.toString());
        }
        return wikiPageResource;
    }

    public WikiPageResource fetchByN_T(long nodeId, String title) throws SystemException {
        String finderClassName = WikiPageResource.class.getName();
        String finderMethodName = "fetchByN_T";
        String[] finderParams = new String[] { Long.class.getName(), String.class.getName() };
        Object[] finderArgs = new Object[] { new Long(nodeId), title };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portlet.wiki.model.WikiPageResource WHERE ");
                query.append("nodeId = ?");
                query.append(" AND ");
                if (title == null) {
                    query.append("title IS NULL");
                } else {
                    query.append("title = ?");
                }
                query.append(" ");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, nodeId);
                if (title != null) {
                    q.setString(queryPos++, title);
                }
                List list = q.list();
                FinderCache.putResult(finderClassName, finderMethodName, finderParams, finderArgs, list);
                if (list.size() == 0) {
                    return null;
                } else {
                    return (WikiPageResource) list.get(0);
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
                return (WikiPageResource) list.get(0);
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
        String finderClassName = WikiPageResource.class.getName();
        String finderMethodName = "findAll";
        String[] finderParams = new String[] { "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { String.valueOf(begin), String.valueOf(end), String.valueOf(obc) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portlet.wiki.model.WikiPageResource ");
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

    public void removeByN_T(long nodeId, String title) throws NoSuchPageResourceException, SystemException {
        WikiPageResource wikiPageResource = findByN_T(nodeId, title);
        remove(wikiPageResource);
    }

    public void removeAll() throws SystemException {
        Iterator itr = findAll().iterator();
        while (itr.hasNext()) {
            remove((WikiPageResource) itr.next());
        }
    }

    public int countByN_T(long nodeId, String title) throws SystemException {
        String finderClassName = WikiPageResource.class.getName();
        String finderMethodName = "countByN_T";
        String[] finderParams = new String[] { Long.class.getName(), String.class.getName() };
        Object[] finderArgs = new Object[] { new Long(nodeId), title };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("SELECT COUNT(*) ");
                query.append("FROM com.liferay.portlet.wiki.model.WikiPageResource WHERE ");
                query.append("nodeId = ?");
                query.append(" AND ");
                if (title == null) {
                    query.append("title IS NULL");
                } else {
                    query.append("title = ?");
                }
                query.append(" ");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, nodeId);
                if (title != null) {
                    q.setString(queryPos++, title);
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
        String finderClassName = WikiPageResource.class.getName();
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
                query.append("FROM com.liferay.portlet.wiki.model.WikiPageResource");
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

    private static final String _LISTENER = GetterUtil.getString(PropsUtil.get("value.object.listener.com.liferay.portlet.wiki.model.WikiPageResource"));

    private static Log _log = LogFactory.getLog(WikiPageResourcePersistenceImpl.class);
}
