package com.liferay.portlet.softwarecatalog.service.persistence;

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
import com.liferay.portlet.softwarecatalog.NoSuchLicenseException;
import com.liferay.portlet.softwarecatalog.model.SCLicense;
import com.liferay.portlet.softwarecatalog.model.impl.SCLicenseImpl;
import com.liferay.util.dao.hibernate.QueryUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <a href="SCLicensePersistenceImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class SCLicensePersistenceImpl extends BasePersistence implements SCLicensePersistence {

    public SCLicense create(long licenseId) {
        SCLicense scLicense = new SCLicenseImpl();
        scLicense.setNew(true);
        scLicense.setPrimaryKey(licenseId);
        return scLicense;
    }

    public SCLicense remove(long licenseId) throws NoSuchLicenseException, SystemException {
        Session session = null;
        try {
            session = openSession();
            SCLicense scLicense = (SCLicense) session.get(SCLicenseImpl.class, new Long(licenseId));
            if (scLicense == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn("No SCLicense exists with the primary key " + licenseId);
                }
                throw new NoSuchLicenseException("No SCLicense exists with the primary key " + licenseId);
            }
            return remove(scLicense);
        } catch (NoSuchLicenseException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public SCLicense remove(SCLicense scLicense) throws SystemException {
        ModelListener listener = _getListener();
        if (listener != null) {
            listener.onBeforeRemove(scLicense);
        }
        scLicense = removeImpl(scLicense);
        if (listener != null) {
            listener.onAfterRemove(scLicense);
        }
        return scLicense;
    }

    protected SCLicense removeImpl(SCLicense scLicense) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            session.delete(scLicense);
            session.flush();
            return scLicense;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
            FinderCache.clearCache(SCLicense.class.getName());
        }
    }

    public SCLicense update(com.liferay.portlet.softwarecatalog.model.SCLicense scLicense) throws SystemException {
        return update(scLicense, false);
    }

    public SCLicense update(com.liferay.portlet.softwarecatalog.model.SCLicense scLicense, boolean merge) throws SystemException {
        ModelListener listener = _getListener();
        boolean isNew = scLicense.isNew();
        if (listener != null) {
            if (isNew) {
                listener.onBeforeCreate(scLicense);
            } else {
                listener.onBeforeUpdate(scLicense);
            }
        }
        scLicense = updateImpl(scLicense, merge);
        if (listener != null) {
            if (isNew) {
                listener.onAfterCreate(scLicense);
            } else {
                listener.onAfterUpdate(scLicense);
            }
        }
        return scLicense;
    }

    public SCLicense updateImpl(com.liferay.portlet.softwarecatalog.model.SCLicense scLicense, boolean merge) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            if (merge) {
                session.merge(scLicense);
            } else {
                if (scLicense.isNew()) {
                    session.save(scLicense);
                }
            }
            session.flush();
            scLicense.setNew(false);
            return scLicense;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
            FinderCache.clearCache(SCLicense.class.getName());
        }
    }

    public SCLicense findByPrimaryKey(long licenseId) throws NoSuchLicenseException, SystemException {
        SCLicense scLicense = fetchByPrimaryKey(licenseId);
        if (scLicense == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("No SCLicense exists with the primary key " + licenseId);
            }
            throw new NoSuchLicenseException("No SCLicense exists with the primary key " + licenseId);
        }
        return scLicense;
    }

    public SCLicense fetchByPrimaryKey(long licenseId) throws SystemException {
        Session session = null;
        try {
            session = openSession();
            return (SCLicense) session.get(SCLicenseImpl.class, new Long(licenseId));
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List findByActive(boolean active) throws SystemException {
        String finderClassName = SCLicense.class.getName();
        String finderMethodName = "findByActive";
        String[] finderParams = new String[] { Boolean.class.getName() };
        Object[] finderArgs = new Object[] { Boolean.valueOf(active) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portlet.softwarecatalog.model.SCLicense WHERE ");
                query.append("active_ = ?");
                query.append(" ");
                query.append("ORDER BY ");
                query.append("name ASC");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setBoolean(queryPos++, active);
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

    public List findByActive(boolean active, int begin, int end) throws SystemException {
        return findByActive(active, begin, end, null);
    }

    public List findByActive(boolean active, int begin, int end, OrderByComparator obc) throws SystemException {
        String finderClassName = SCLicense.class.getName();
        String finderMethodName = "findByActive";
        String[] finderParams = new String[] { Boolean.class.getName(), "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { Boolean.valueOf(active), String.valueOf(begin), String.valueOf(end), String.valueOf(obc) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portlet.softwarecatalog.model.SCLicense WHERE ");
                query.append("active_ = ?");
                query.append(" ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                } else {
                    query.append("ORDER BY ");
                    query.append("name ASC");
                }
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setBoolean(queryPos++, active);
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

    public SCLicense findByActive_First(boolean active, OrderByComparator obc) throws NoSuchLicenseException, SystemException {
        List list = findByActive(active, 0, 1, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No SCLicense exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("active=");
            msg.append(active);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchLicenseException(msg.toString());
        } else {
            return (SCLicense) list.get(0);
        }
    }

    public SCLicense findByActive_Last(boolean active, OrderByComparator obc) throws NoSuchLicenseException, SystemException {
        int count = countByActive(active);
        List list = findByActive(active, count - 1, count, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No SCLicense exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("active=");
            msg.append(active);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchLicenseException(msg.toString());
        } else {
            return (SCLicense) list.get(0);
        }
    }

    public SCLicense[] findByActive_PrevAndNext(long licenseId, boolean active, OrderByComparator obc) throws NoSuchLicenseException, SystemException {
        SCLicense scLicense = findByPrimaryKey(licenseId);
        int count = countByActive(active);
        Session session = null;
        try {
            session = openSession();
            StringMaker query = new StringMaker();
            query.append("FROM com.liferay.portlet.softwarecatalog.model.SCLicense WHERE ");
            query.append("active_ = ?");
            query.append(" ");
            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            } else {
                query.append("ORDER BY ");
                query.append("name ASC");
            }
            Query q = session.createQuery(query.toString());
            int queryPos = 0;
            q.setBoolean(queryPos++, active);
            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, scLicense);
            SCLicense[] array = new SCLicenseImpl[3];
            array[0] = (SCLicense) objArray[0];
            array[1] = (SCLicense) objArray[1];
            array[2] = (SCLicense) objArray[2];
            return array;
        } catch (Exception e) {
            throw HibernateUtil.processException(e);
        } finally {
            closeSession(session);
        }
    }

    public List findByA_R(boolean active, boolean recommended) throws SystemException {
        String finderClassName = SCLicense.class.getName();
        String finderMethodName = "findByA_R";
        String[] finderParams = new String[] { Boolean.class.getName(), Boolean.class.getName() };
        Object[] finderArgs = new Object[] { Boolean.valueOf(active), Boolean.valueOf(recommended) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portlet.softwarecatalog.model.SCLicense WHERE ");
                query.append("active_ = ?");
                query.append(" AND ");
                query.append("recommended = ?");
                query.append(" ");
                query.append("ORDER BY ");
                query.append("name ASC");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setBoolean(queryPos++, active);
                q.setBoolean(queryPos++, recommended);
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

    public List findByA_R(boolean active, boolean recommended, int begin, int end) throws SystemException {
        return findByA_R(active, recommended, begin, end, null);
    }

    public List findByA_R(boolean active, boolean recommended, int begin, int end, OrderByComparator obc) throws SystemException {
        String finderClassName = SCLicense.class.getName();
        String finderMethodName = "findByA_R";
        String[] finderParams = new String[] { Boolean.class.getName(), Boolean.class.getName(), "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { Boolean.valueOf(active), Boolean.valueOf(recommended), String.valueOf(begin), String.valueOf(end), String.valueOf(obc) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portlet.softwarecatalog.model.SCLicense WHERE ");
                query.append("active_ = ?");
                query.append(" AND ");
                query.append("recommended = ?");
                query.append(" ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                } else {
                    query.append("ORDER BY ");
                    query.append("name ASC");
                }
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setBoolean(queryPos++, active);
                q.setBoolean(queryPos++, recommended);
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

    public SCLicense findByA_R_First(boolean active, boolean recommended, OrderByComparator obc) throws NoSuchLicenseException, SystemException {
        List list = findByA_R(active, recommended, 0, 1, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No SCLicense exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("active=");
            msg.append(active);
            msg.append(", ");
            msg.append("recommended=");
            msg.append(recommended);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchLicenseException(msg.toString());
        } else {
            return (SCLicense) list.get(0);
        }
    }

    public SCLicense findByA_R_Last(boolean active, boolean recommended, OrderByComparator obc) throws NoSuchLicenseException, SystemException {
        int count = countByA_R(active, recommended);
        List list = findByA_R(active, recommended, count - 1, count, obc);
        if (list.size() == 0) {
            StringMaker msg = new StringMaker();
            msg.append("No SCLicense exists with the key ");
            msg.append(StringPool.OPEN_CURLY_BRACE);
            msg.append("active=");
            msg.append(active);
            msg.append(", ");
            msg.append("recommended=");
            msg.append(recommended);
            msg.append(StringPool.CLOSE_CURLY_BRACE);
            throw new NoSuchLicenseException(msg.toString());
        } else {
            return (SCLicense) list.get(0);
        }
    }

    public SCLicense[] findByA_R_PrevAndNext(long licenseId, boolean active, boolean recommended, OrderByComparator obc) throws NoSuchLicenseException, SystemException {
        SCLicense scLicense = findByPrimaryKey(licenseId);
        int count = countByA_R(active, recommended);
        Session session = null;
        try {
            session = openSession();
            StringMaker query = new StringMaker();
            query.append("FROM com.liferay.portlet.softwarecatalog.model.SCLicense WHERE ");
            query.append("active_ = ?");
            query.append(" AND ");
            query.append("recommended = ?");
            query.append(" ");
            if (obc != null) {
                query.append("ORDER BY ");
                query.append(obc.getOrderBy());
            } else {
                query.append("ORDER BY ");
                query.append("name ASC");
            }
            Query q = session.createQuery(query.toString());
            int queryPos = 0;
            q.setBoolean(queryPos++, active);
            q.setBoolean(queryPos++, recommended);
            Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc, scLicense);
            SCLicense[] array = new SCLicenseImpl[3];
            array[0] = (SCLicense) objArray[0];
            array[1] = (SCLicense) objArray[1];
            array[2] = (SCLicense) objArray[2];
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
        String finderClassName = SCLicense.class.getName();
        String finderMethodName = "findAll";
        String[] finderParams = new String[] { "java.lang.Integer", "java.lang.Integer", "com.liferay.portal.kernel.util.OrderByComparator" };
        Object[] finderArgs = new Object[] { String.valueOf(begin), String.valueOf(end), String.valueOf(obc) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("FROM com.liferay.portlet.softwarecatalog.model.SCLicense ");
                if (obc != null) {
                    query.append("ORDER BY ");
                    query.append(obc.getOrderBy());
                } else {
                    query.append("ORDER BY ");
                    query.append("name ASC");
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

    public void removeByActive(boolean active) throws SystemException {
        Iterator itr = findByActive(active).iterator();
        while (itr.hasNext()) {
            SCLicense scLicense = (SCLicense) itr.next();
            remove(scLicense);
        }
    }

    public void removeByA_R(boolean active, boolean recommended) throws SystemException {
        Iterator itr = findByA_R(active, recommended).iterator();
        while (itr.hasNext()) {
            SCLicense scLicense = (SCLicense) itr.next();
            remove(scLicense);
        }
    }

    public void removeAll() throws SystemException {
        Iterator itr = findAll().iterator();
        while (itr.hasNext()) {
            remove((SCLicense) itr.next());
        }
    }

    public int countByActive(boolean active) throws SystemException {
        String finderClassName = SCLicense.class.getName();
        String finderMethodName = "countByActive";
        String[] finderParams = new String[] { Boolean.class.getName() };
        Object[] finderArgs = new Object[] { Boolean.valueOf(active) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("SELECT COUNT(*) ");
                query.append("FROM com.liferay.portlet.softwarecatalog.model.SCLicense WHERE ");
                query.append("active_ = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setBoolean(queryPos++, active);
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

    public int countByA_R(boolean active, boolean recommended) throws SystemException {
        String finderClassName = SCLicense.class.getName();
        String finderMethodName = "countByA_R";
        String[] finderParams = new String[] { Boolean.class.getName(), Boolean.class.getName() };
        Object[] finderArgs = new Object[] { Boolean.valueOf(active), Boolean.valueOf(recommended) };
        Object result = FinderCache.getResult(finderClassName, finderMethodName, finderParams, finderArgs, getSessionFactory());
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                StringMaker query = new StringMaker();
                query.append("SELECT COUNT(*) ");
                query.append("FROM com.liferay.portlet.softwarecatalog.model.SCLicense WHERE ");
                query.append("active_ = ?");
                query.append(" AND ");
                query.append("recommended = ?");
                query.append(" ");
                Query q = session.createQuery(query.toString());
                int queryPos = 0;
                q.setBoolean(queryPos++, active);
                q.setBoolean(queryPos++, recommended);
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
        String finderClassName = SCLicense.class.getName();
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
                query.append("FROM com.liferay.portlet.softwarecatalog.model.SCLicense");
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

    private static final String _LISTENER = GetterUtil.getString(PropsUtil.get("value.object.listener.com.liferay.portlet.softwarecatalog.model.SCLicense"));

    private static Log _log = LogFactory.getLog(SCLicensePersistenceImpl.class);
}
