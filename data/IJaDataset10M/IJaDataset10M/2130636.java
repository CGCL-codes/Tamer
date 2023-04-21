package com.liferay.portal.service.persistence;

import com.liferay.portal.NoSuchWebsiteException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.dao.DynamicQuery;
import com.liferay.portal.kernel.dao.DynamicQueryInitializer;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringMaker;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.model.Website;
import com.liferay.portal.model.impl.WebsiteImpl;
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
 * <a href="WebsitePersistenceImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class WebsitePersistenceImpl extends BasePersistence implements WebsitePersistence {

    public Website create(long websiteId) {
        Website website = new WebsiteImpl();
        website.setNew(true);
        website.setPrimaryKey(websiteId);
        return website;
    }

    public Website remove(long websiteId) throws NoSuchWebsiteException, SystemException {
        Session session = null;
        try {
            session = openSession();
            Website website = (Website) session.get(WebsiteImpl.class, new Long(websiteId));
            if (website == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn("No Website exists with the primary key " + websiteId);
                }
                throw new NoSuchWebsiteException("No Website exists with the primary key " + websiteId);
            }
            return remove(website);
        } catch (NoSuchWebsiteException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public Website remove(Website website) throws SystemException {
        ModelListener listener = _getListener();
        if (listener != null) {
            listener.onBeforeRemove(website);
        }
        website = removeImpl(website);
        if (listener != null) {
            listener.onAfterRemove(website);
        }
        return website;
    }

    protected Website removeImpl(Website website) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            session.delete(website);
            session.flush();
            return website;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
            FinderCache.clearCache(Website.class.getName());
        }
    }

    public Website update(com.liferay.portal.model.Website website) throws SystemException {
        return update(website, false);
    }

    public Website update(com.liferay.portal.model.Website website, boolean merge) throws SystemException {
        ModelListener listener = _getListener();
        boolean isNew = website.isNew();
        if (listener != null) {
            if (isNew) {
                listener.onBeforeCreate(website);
            } else {
                listener.onBeforeUpdate(website);
            }
        }
        website = updateImpl(website, merge);
        if (listener != null) {
            if (isNew) {
                listener.onAfterCreate(website);
            } else {
                listener.onAfterUpdate(website);
            }
        }
        return website;
    }

    public Website updateImpl(com.liferay.portal.model.Website website, boolean merge) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            if (merge) {
                session.merge(website);
            } else {
                if (website.isNew()) {
                    session.save(website);
                }
            }
            session.flush();
            website.setNew(false);
            return website;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
            FinderCache.clearCache(Website.class.getName());
        }
    }

    public Website findByPrimaryKey(long websiteId) throws NoSuchWebsiteException, SystemException {
        Website website = fetchByPrimaryKey(websiteId);
        if (website == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("No Website exists with the primary key " + websiteId);
            }
            throw new NoSuchWebsiteException("No Website exists with the primary key " + websiteId);
        }
        return website;
    }

    public Website fetchByPrimaryKey(long websiteId) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            return (Website) session.get(WebsiteImpl.class, new Long(websiteId));
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List findByCompanyId(long companyId) throws SystemException {
        String finderClassName = Website.class.getName();
        String finderMethodName = "findByCompanyId";
        String[] finderParams = new String[] { Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(companyId) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portal.model.Website WHERE ");
                query.append("companyId = ?");
                query.append(" ");
                query.append("ORDER BY ");
                query.append("createDate ASC");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, companyId);
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

    public List findByCompanyId(long companyId, int begin, int end) throws SystemException {
        return findByCompanyId(companyId, begin, end, null);
    }

    public List findByCompanyId(long companyId, int begin, int end, OrderByComparator obc) throws SystemException {
        String finderClassName = Website.class.getName();
        String finderMethodName = "findByCompanyId";
        String[] finderParams = new String[] { Long.class.getName(), "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { new Long(companyId), String.valueOf(begin), String.valueOf(end), String.valueOf(obc) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portal.model.Website WHERE ");
                query.append("companyId = ?");
                query.append(" ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                } else {
                    query.append("ORDER BY ");
                    query.append("createDate ASC");
                }
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, companyId);
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

    public Website findByCompanyId_First(long companyId, OrderByComparator obc) throws NoSuchWebsiteException, SystemException {
        List list = findByCompanyId(companyId, 0, 1, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No Website exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("companyId=");
            msg.append(companyId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchWebsiteException(msg.toString());
        } else {
            return (Website) list.get(0);
        }
    }

    public Website findByCompanyId_Last(long companyId, OrderByComparator obc) throws NoSuchWebsiteException, SystemException {
        int count = countByCompanyId(companyId);
        List list = findByCompanyId(companyId, count - 1, count, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No Website exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("companyId=");
            msg.append(companyId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchWebsiteException(msg.toString());
        } else {
            return (Website) list.get(0);
        }
    }

    public Website[] findByCompanyId_PrevAndNext(long websiteId, long companyId, OrderByComparator obc) throws NoSuchWebsiteException, SystemException {
        Website website = findByPrimaryKey(websiteId);
        int count = countByCompanyId(companyId);
        Session session = null;
        try {
            session = openSession();
            StringMaker query = new StringMaker();
            query.append("FROM com.liferay.portal.model.Website WHERE ");
            query.append("companyId = ?");
            query.append(" ");
            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            } else {
                query.append("ORDER BY ");
                query.append("createDate ASC");
            }
            Query q = session.createQuery(query.toString());
            int queryPos = 0;
            q.setLong(queryPos++, companyId);
            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, website);
            Website[] array = new WebsiteImpl[3];
            array[0] = (Website) objArray[0];
            array[1] = (Website) objArray[1];
            array[2] = (Website) objArray[2];
            return array;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List findByUserId(long userId) throws SystemException {
        String finderClassName = Website.class.getName();
        String finderMethodName = "findByUserId";
        String[] finderParams = new String[] { Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(userId) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portal.model.Website WHERE ");
                query.append("userId = ?");
                query.append(" ");
                query.append("ORDER BY ");
                query.append("createDate ASC");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, userId);
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

    public List findByUserId(long userId, int begin, int end) throws SystemException {
        return findByUserId(userId, begin, end, null);
    }

    public List findByUserId(long userId, int begin, int end, OrderByComparator obc) throws SystemException {
        String finderClassName = Website.class.getName();
        String finderMethodName = "findByUserId";
        String[] finderParams = new String[] { Long.class.getName(), "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { new Long(userId), String.valueOf(begin), String.valueOf(end), String.valueOf(obc) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portal.model.Website WHERE ");
                query.append("userId = ?");
                query.append(" ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                } else {
                    query.append("ORDER BY ");
                    query.append("createDate ASC");
                }
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, userId);
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

    public Website findByUserId_First(long userId, OrderByComparator obc) throws NoSuchWebsiteException, SystemException {
        List list = findByUserId(userId, 0, 1, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No Website exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("userId=");
            msg.append(userId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchWebsiteException(msg.toString());
        } else {
            return (Website) list.get(0);
        }
    }

    public Website findByUserId_Last(long userId, OrderByComparator obc) throws NoSuchWebsiteException, SystemException {
        int count = countByUserId(userId);
        List list = findByUserId(userId, count - 1, count, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No Website exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("userId=");
            msg.append(userId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchWebsiteException(msg.toString());
        } else {
            return (Website) list.get(0);
        }
    }

    public Website[] findByUserId_PrevAndNext(long websiteId, long userId, OrderByComparator obc) throws NoSuchWebsiteException, SystemException {
        Website website = findByPrimaryKey(websiteId);
        int count = countByUserId(userId);
        Session session = null;
        try {
            session = openSession();
            StringMaker query = new StringMaker();
            query.append("FROM com.liferay.portal.model.Website WHERE ");
            query.append("userId = ?");
            query.append(" ");
            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            } else {
                query.append("ORDER BY ");
                query.append("createDate ASC");
            }
            Query q = session.createQuery(query.toString());
            int queryPos = 0;
            q.setLong(queryPos++, userId);
            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, website);
            Website[] array = new WebsiteImpl[3];
            array[0] = (Website) objArray[0];
            array[1] = (Website) objArray[1];
            array[2] = (Website) objArray[2];
            return array;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List findByC_C(long companyId, long classNameId) throws SystemException {
        String finderClassName = Website.class.getName();
        String finderMethodName = "findByC_C";
        String[] finderParams = new String[] { Long.class.getName(), Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(companyId), new Long(classNameId) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portal.model.Website WHERE ");
                query.append("companyId = ?");
                query.append(" AND ");
                query.append("classNameId = ?");
                query.append(" ");
                query.append("ORDER BY ");
                query.append("createDate ASC");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, companyId);
                q.setLong(queryPos++, classNameId);
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

    public List findByC_C(long companyId, long classNameId, int begin, int end) throws SystemException {
        return findByC_C(companyId, classNameId, begin, end, null);
    }

    public List findByC_C(long companyId, long classNameId, int begin, int end, OrderByComparator obc) throws SystemException {
        String finderClassName = Website.class.getName();
        String finderMethodName = "findByC_C";
        String[] finderParams = new String[] { Long.class.getName(), Long.class.getName(), "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { new Long(companyId), new Long(classNameId), String.valueOf(begin), String.valueOf(end), String.valueOf(obc) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portal.model.Website WHERE ");
                query.append("companyId = ?");
                query.append(" AND ");
                query.append("classNameId = ?");
                query.append(" ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                } else {
                    query.append("ORDER BY ");
                    query.append("createDate ASC");
                }
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, companyId);
                q.setLong(queryPos++, classNameId);
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

    public Website findByC_C_First(long companyId, long classNameId, OrderByComparator obc) throws NoSuchWebsiteException, SystemException {
        List list = findByC_C(companyId, classNameId, 0, 1, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No Website exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("companyId=");
            msg.append(companyId);
            msg.append(", ");
            msg.append("classNameId=");
            msg.append(classNameId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchWebsiteException(msg.toString());
        } else {
            return (Website) list.get(0);
        }
    }

    public Website findByC_C_Last(long companyId, long classNameId, OrderByComparator obc) throws NoSuchWebsiteException, SystemException {
        int count = countByC_C(companyId, classNameId);
        List list = findByC_C(companyId, classNameId, count - 1, count, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No Website exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("companyId=");
            msg.append(companyId);
            msg.append(", ");
            msg.append("classNameId=");
            msg.append(classNameId);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchWebsiteException(msg.toString());
        } else {
            return (Website) list.get(0);
        }
    }

    public Website[] findByC_C_PrevAndNext(long websiteId, long companyId, long classNameId, OrderByComparator obc) throws NoSuchWebsiteException, SystemException {
        Website website = findByPrimaryKey(websiteId);
        int count = countByC_C(companyId, classNameId);
        Session session = null;
        try {
            session = openSession();
            StringMaker query = new StringMaker();
            query.append("FROM com.liferay.portal.model.Website WHERE ");
            query.append("companyId = ?");
            query.append(" AND ");
            query.append("classNameId = ?");
            query.append(" ");
            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            } else {
                query.append("ORDER BY ");
                query.append("createDate ASC");
            }
            Query q = session.createQuery(query.toString());
            int queryPos = 0;
            q.setLong(queryPos++, companyId);
            q.setLong(queryPos++, classNameId);
            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, website);
            Website[] array = new WebsiteImpl[3];
            array[0] = (Website) objArray[0];
            array[1] = (Website) objArray[1];
            array[2] = (Website) objArray[2];
            return array;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List findByC_C_C(long companyId, long classNameId, long classPK) throws SystemException {
        String finderClassName = Website.class.getName();
        String finderMethodName = "findByC_C_C";
        String[] finderParams = new String[] { Long.class.getName(), Long.class.getName(), Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(companyId), new Long(classNameId), new Long(classPK) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portal.model.Website WHERE ");
                query.append("companyId = ?");
                query.append(" AND ");
                query.append("classNameId = ?");
                query.append(" AND ");
                query.append("classPK = ?");
                query.append(" ");
                query.append("ORDER BY ");
                query.append("createDate ASC");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, companyId);
                q.setLong(queryPos++, classNameId);
                q.setLong(queryPos++, classPK);
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

    public List findByC_C_C(long companyId, long classNameId, long classPK, int begin, int end) throws SystemException {
        return findByC_C_C(companyId, classNameId, classPK, begin, end, null);
    }

    public List findByC_C_C(long companyId, long classNameId, long classPK, int begin, int end, OrderByComparator obc) throws SystemException {
        String finderClassName = Website.class.getName();
        String finderMethodName = "findByC_C_C";
        String[] finderParams = new String[] { Long.class.getName(), Long.class.getName(), Long.class.getName(), "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { new Long(companyId), new Long(classNameId), new Long(classPK), String.valueOf(begin), String.valueOf(end), String.valueOf(obc) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portal.model.Website WHERE ");
                query.append("companyId = ?");
                query.append(" AND ");
                query.append("classNameId = ?");
                query.append(" AND ");
                query.append("classPK = ?");
                query.append(" ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                } else {
                    query.append("ORDER BY ");
                    query.append("createDate ASC");
                }
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, companyId);
                q.setLong(queryPos++, classNameId);
                q.setLong(queryPos++, classPK);
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

    public Website findByC_C_C_First(long companyId, long classNameId, long classPK, OrderByComparator obc) throws NoSuchWebsiteException, SystemException {
        List list = findByC_C_C(companyId, classNameId, classPK, 0, 1, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No Website exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("companyId=");
            msg.append(companyId);
            msg.append(", ");
            msg.append("classNameId=");
            msg.append(classNameId);
            msg.append(", ");
            msg.append("classPK=");
            msg.append(classPK);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchWebsiteException(msg.toString());
        } else {
            return (Website) list.get(0);
        }
    }

    public Website findByC_C_C_Last(long companyId, long classNameId, long classPK, OrderByComparator obc) throws NoSuchWebsiteException, SystemException {
        int count = countByC_C_C(companyId, classNameId, classPK);
        List list = findByC_C_C(companyId, classNameId, classPK, count - 1, count, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No Website exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("companyId=");
            msg.append(companyId);
            msg.append(", ");
            msg.append("classNameId=");
            msg.append(classNameId);
            msg.append(", ");
            msg.append("classPK=");
            msg.append(classPK);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchWebsiteException(msg.toString());
        } else {
            return (Website) list.get(0);
        }
    }

    public Website[] findByC_C_C_PrevAndNext(long websiteId, long companyId, long classNameId, long classPK, OrderByComparator obc) throws NoSuchWebsiteException, SystemException {
        Website website = findByPrimaryKey(websiteId);
        int count = countByC_C_C(companyId, classNameId, classPK);
        Session session = null;
        try {
            session = openSession();
            StringMaker query = new StringMaker();
            query.append("FROM com.liferay.portal.model.Website WHERE ");
            query.append("companyId = ?");
            query.append(" AND ");
            query.append("classNameId = ?");
            query.append(" AND ");
            query.append("classPK = ?");
            query.append(" ");
            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            } else {
                query.append("ORDER BY ");
                query.append("createDate ASC");
            }
            Query q = session.createQuery(query.toString());
            int queryPos = 0;
            q.setLong(queryPos++, companyId);
            q.setLong(queryPos++, classNameId);
            q.setLong(queryPos++, classPK);
            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, website);
            Website[] array = new WebsiteImpl[3];
            array[0] = (Website) objArray[0];
            array[1] = (Website) objArray[1];
            array[2] = (Website) objArray[2];
            return array;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List findByC_C_C_P(long companyId, long classNameId, long classPK, boolean primary) throws SystemException {
        String finderClassName = Website.class.getName();
        String finderMethodName = "findByC_C_C_P";
        String[] finderParams = new String[] { Long.class.getName(), Long.class.getName(), Long.class.getName(), Boolean.class.getName() };
        Object[] finderArgs = new Object[] { new Long(companyId), new Long(classNameId), new Long(classPK), Boolean.valueOf(primary) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portal.model.Website WHERE ");
                query.append("companyId = ?");
                query.append(" AND ");
                query.append("classNameId = ?");
                query.append(" AND ");
                query.append("classPK = ?");
                query.append(" AND ");
                query.append("primary_ = ?");
                query.append(" ");
                query.append("ORDER BY ");
                query.append("createDate ASC");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, companyId);
                q.setLong(queryPos++, classNameId);
                q.setLong(queryPos++, classPK);
                q.setBoolean(queryPos++, primary);
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

    public List findByC_C_C_P(long companyId, long classNameId, long classPK, boolean primary, int begin, int end) throws SystemException {
        return findByC_C_C_P(companyId, classNameId, classPK, primary, begin, end, null);
    }

    public List findByC_C_C_P(long companyId, long classNameId, long classPK, boolean primary, int begin, int end, OrderByComparator obc) throws SystemException {
        String finderClassName = Website.class.getName();
        String finderMethodName = "findByC_C_C_P";
        String[] finderParams = new String[] { Long.class.getName(), Long.class.getName(), Long.class.getName(), Boolean.class.getName(), "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { new Long(companyId), new Long(classNameId), new Long(classPK), Boolean.valueOf(primary), String.valueOf(begin), String.valueOf(end), String.valueOf(obc) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portal.model.Website WHERE ");
                query.append("companyId = ?");
                query.append(" AND ");
                query.append("classNameId = ?");
                query.append(" AND ");
                query.append("classPK = ?");
                query.append(" AND ");
                query.append("primary_ = ?");
                query.append(" ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                } else {
                    query.append("ORDER BY ");
                    query.append("createDate ASC");
                }
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, companyId);
                q.setLong(queryPos++, classNameId);
                q.setLong(queryPos++, classPK);
                q.setBoolean(queryPos++, primary);
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

    public Website findByC_C_C_P_First(long companyId, long classNameId, long classPK, boolean primary, OrderByComparator obc) throws NoSuchWebsiteException, SystemException {
        List list = findByC_C_C_P(companyId, classNameId, classPK, primary, 0, 1, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No Website exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("companyId=");
            msg.append(companyId);
            msg.append(", ");
            msg.append("classNameId=");
            msg.append(classNameId);
            msg.append(", ");
            msg.append("classPK=");
            msg.append(classPK);
            msg.append(", ");
            msg.append("primary=");
            msg.append(primary);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchWebsiteException(msg.toString());
        } else {
            return (Website) list.get(0);
        }
    }

    public Website findByC_C_C_P_Last(long companyId, long classNameId, long classPK, boolean primary, OrderByComparator obc) throws NoSuchWebsiteException, SystemException {
        int count = countByC_C_C_P(companyId, classNameId, classPK, primary);
        List list = findByC_C_C_P(companyId, classNameId, classPK, primary, count - 1, count, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No Website exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("companyId=");
            msg.append(companyId);
            msg.append(", ");
            msg.append("classNameId=");
            msg.append(classNameId);
            msg.append(", ");
            msg.append("classPK=");
            msg.append(classPK);
            msg.append(", ");
            msg.append("primary=");
            msg.append(primary);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchWebsiteException(msg.toString());
        } else {
            return (Website) list.get(0);
        }
    }

    public Website[] findByC_C_C_P_PrevAndNext(long websiteId, long companyId, long classNameId, long classPK, boolean primary, OrderByComparator obc) throws NoSuchWebsiteException, SystemException {
        Website website = findByPrimaryKey(websiteId);
        int count = countByC_C_C_P(companyId, classNameId, classPK, primary);
        Session session = null;
        try {
            session = openSession();
            StringMaker query = new StringMaker();
            query.append("FROM com.liferay.portal.model.Website WHERE ");
            query.append("companyId = ?");
            query.append(" AND ");
            query.append("classNameId = ?");
            query.append(" AND ");
            query.append("classPK = ?");
            query.append(" AND ");
            query.append("primary_ = ?");
            query.append(" ");
            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            } else {
                query.append("ORDER BY ");
                query.append("createDate ASC");
            }
            Query q = session.createQuery(query.toString());
            int queryPos = 0;
            q.setLong(queryPos++, companyId);
            q.setLong(queryPos++, classNameId);
            q.setLong(queryPos++, classPK);
            q.setBoolean(queryPos++, primary);
            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, website);
            Website[] array = new WebsiteImpl[3];
            array[0] = (Website) objArray[0];
            array[1] = (Website) objArray[1];
            array[2] = (Website) objArray[2];
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
        String finderClassName = Website.class.getName();
        String finderMethodName = "findAll";
        String[] finderParams = new String[] { "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { String.valueOf(begin), String.valueOf(end), String.valueOf(obc) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portal.model.Website ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                } else {
                    query.append("ORDER BY ");
                    query.append("createDate ASC");
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

    public void removeByCompanyId(long companyId) throws SystemException {
        Iterator itr = findByCompanyId(companyId).iterator();
        while (itr.hasNext()) {
            Website website = (Website) itr.next();
            remove(website);
        }
    }

    public void removeByUserId(long userId) throws SystemException {
        Iterator itr = findByUserId(userId).iterator();
        while (itr.hasNext()) {
            Website website = (Website) itr.next();
            remove(website);
        }
    }

    public void removeByC_C(long companyId, long classNameId) throws SystemException {
        Iterator itr = findByC_C(companyId, classNameId).iterator();
        while (itr.hasNext()) {
            Website website = (Website) itr.next();
            remove(website);
        }
    }

    public void removeByC_C_C(long companyId, long classNameId, long classPK) throws SystemException {
        Iterator itr = findByC_C_C(companyId, classNameId, classPK).iterator();
        while (itr.hasNext()) {
            Website website = (Website) itr.next();
            remove(website);
        }
    }

    public void removeByC_C_C_P(long companyId, long classNameId, long classPK, boolean primary) throws SystemException {
        Iterator itr = findByC_C_C_P(companyId, classNameId, classPK, primary).iterator();
        while (itr.hasNext()) {
            Website website = (Website) itr.next();
            remove(website);
        }
    }

    public void removeAll() throws SystemException {
        Iterator itr = findAll().iterator();
        while (itr.hasNext()) {
            remove((Website) itr.next());
        }
    }

    public int countByCompanyId(long companyId) throws SystemException {
        String finderClassName = Website.class.getName();
        String finderMethodName = "countByCompanyId";
        String[] finderParams = new String[] { Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(companyId) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("SELECT COUNT(*) ");
                query.append("FROM com.liferay.portal.model.Website WHERE ");
                query.append("companyId = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, companyId);
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

    public int countByUserId(long userId) throws SystemException {
        String finderClassName = Website.class.getName();
        String finderMethodName = "countByUserId";
        String[] finderParams = new String[] { Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(userId) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("SELECT COUNT(*) ");
                query.append("FROM com.liferay.portal.model.Website WHERE ");
                query.append("userId = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, userId);
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

    public int countByC_C(long companyId, long classNameId) throws SystemException {
        String finderClassName = Website.class.getName();
        String finderMethodName = "countByC_C";
        String[] finderParams = new String[] { Long.class.getName(), Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(companyId), new Long(classNameId) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("SELECT COUNT(*) ");
                query.append("FROM com.liferay.portal.model.Website WHERE ");
                query.append("companyId = ?");
                query.append(" AND ");
                query.append("classNameId = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, companyId);
                q.setLong(queryPos++, classNameId);
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

    public int countByC_C_C(long companyId, long classNameId, long classPK) throws SystemException {
        String finderClassName = Website.class.getName();
        String finderMethodName = "countByC_C_C";
        String[] finderParams = new String[] { Long.class.getName(), Long.class.getName(), Long.class.getName() };
        Object[] finderArgs = new Object[] { new Long(companyId), new Long(classNameId), new Long(classPK) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("SELECT COUNT(*) ");
                query.append("FROM com.liferay.portal.model.Website WHERE ");
                query.append("companyId = ?");
                query.append(" AND ");
                query.append("classNameId = ?");
                query.append(" AND ");
                query.append("classPK = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, companyId);
                q.setLong(queryPos++, classNameId);
                q.setLong(queryPos++, classPK);
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

    public int countByC_C_C_P(long companyId, long classNameId, long classPK, boolean primary) throws SystemException {
        String finderClassName = Website.class.getName();
        String finderMethodName = "countByC_C_C_P";
        String[] finderParams = new String[] { Long.class.getName(), Long.class.getName(), Long.class.getName(), Boolean.class.getName() };
        Object[] finderArgs = new Object[] { new Long(companyId), new Long(classNameId), new Long(classPK), Boolean.valueOf(primary) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("SELECT COUNT(*) ");
                query.append("FROM com.liferay.portal.model.Website WHERE ");
                query.append("companyId = ?");
                query.append(" AND ");
                query.append("classNameId = ?");
                query.append(" AND ");
                query.append("classPK = ?");
                query.append(" AND ");
                query.append("primary_ = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setLong(queryPos++, companyId);
                q.setLong(queryPos++, classNameId);
                q.setLong(queryPos++, classPK);
                q.setBoolean(queryPos++, primary);
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
        String finderClassName = Website.class.getName();
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
                query.append("FROM com.liferay.portal.model.Website");
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

    private static final String _LISTENER = GetterUtil.getString(PropsUtil.get("value.object.listener.com.liferay.portal.model.Website"));

    private static Log _log = LogFactory.getLog(WebsitePersistenceImpl.class);
}
