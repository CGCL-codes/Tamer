package com.liferay.portlet.shopping.service.persistence;

import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.util.StringMaker;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.spring.hibernate.CustomSQLUtil;
import com.liferay.portal.spring.hibernate.HibernateUtil;
import com.liferay.portlet.shopping.model.impl.ShoppingItemImpl;
import com.liferay.util.dao.hibernate.QueryPos;
import com.liferay.util.dao.hibernate.QueryUtil;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

/**
 * <a href="ShoppingItemFinder.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ShoppingItemFinder {

    public static String COUNT_BY_CATEGORY_IDS = ShoppingItemFinder.class.getName() + ".countByCategoryIds";

    public static int countByCategoryIds(List categoryIds) throws SystemException {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            String sql = CustomSQLUtil.get(COUNT_BY_CATEGORY_IDS);
            sql = StringUtil.replace(sql, "[$CATEGORY_ID$]", _getCategoryIds(categoryIds));
            SQLQuery q = session.createSQLQuery(sql);
            q.addScalar(HibernateUtil.getCountColumnName(), Hibernate.LONG);
            QueryPos qPos = QueryPos.getInstance(q);
            for (int i = 0; i < categoryIds.size(); i++) {
                Long categoryId = (Long) categoryIds.get(i);
                qPos.add(categoryId);
            }
            Iterator itr = q.list().iterator();
            if (itr.hasNext()) {
                Long count = (Long) itr.next();
                if (count != null) {
                    return count.intValue();
                }
            }
            return 0;
        } catch (Exception e) {
            throw new SystemException(e);
        } finally {
            HibernateUtil.closeSession(session);
        }
    }

    public static int countByFeatured(long groupId, long[] categoryIds) throws SystemException {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            StringMaker query = new StringMaker();
            query.append("SELECT COUNT(*) AS COUNT_VALUE FROM ShoppingItem ");
            query.append("INNER JOIN ShoppingCategory ON ");
            query.append("ShoppingCategory.categoryId = ");
            query.append("ShoppingItem.categoryId ");
            query.append("WHERE ");
            query.append("ShoppingCategory.groupId = ? AND (");
            if ((categoryIds != null) && (categoryIds.length > 0)) {
                query.append("(");
                for (int i = 0; i < categoryIds.length; i++) {
                    query.append("ShoppingItem.categoryId = ? ");
                    if (i + 1 < categoryIds.length) {
                        query.append("OR ");
                    }
                }
                query.append(") AND ");
            }
            query.append("ShoppingItem.featured = ? AND ");
            query.append("ShoppingItem.smallImage = ?");
            SQLQuery q = session.createSQLQuery(query.toString());
            q.addScalar(HibernateUtil.getCountColumnName(), Hibernate.LONG);
            QueryPos qPos = QueryPos.getInstance(q);
            qPos.add(groupId);
            for (int i = 0; i < categoryIds.length; i++) {
                qPos.add(categoryIds[i]);
            }
            qPos.add(true);
            qPos.add(true);
            Iterator itr = q.list().iterator();
            if (itr.hasNext()) {
                Long count = (Long) itr.next();
                if (count != null) {
                    return count.intValue();
                }
            }
            return 0;
        } catch (Exception e) {
            throw new SystemException(e);
        } finally {
            HibernateUtil.closeSession(session);
        }
    }

    public static int countByKeywords(long groupId, long[] categoryIds, String keywords) throws SystemException {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            StringMaker query = new StringMaker();
            query.append("SELECT COUNT(*) AS COUNT_VALUE FROM ShoppingItem ");
            query.append("INNER JOIN ShoppingCategory ON ");
            query.append("ShoppingCategory.categoryId = ");
            query.append("ShoppingItem.categoryId ");
            query.append("WHERE ");
            query.append("ShoppingCategory.groupId = ? AND (");
            if ((categoryIds != null) && (categoryIds.length > 0)) {
                query.append("(");
                for (int i = 0; i < categoryIds.length; i++) {
                    query.append("ShoppingItem.categoryId = ? ");
                    if (i + 1 < categoryIds.length) {
                        query.append("OR ");
                    }
                }
                query.append(") AND ");
            }
            query.append("(ShoppingItem.name LIKE ? OR ");
            query.append("ShoppingItem.description LIKE ? OR ");
            query.append("ShoppingItem.properties LIKE ?))");
            keywords = '%' + keywords + '%';
            SQLQuery q = session.createSQLQuery(query.toString());
            q.addScalar(HibernateUtil.getCountColumnName(), Hibernate.LONG);
            QueryPos qPos = QueryPos.getInstance(q);
            qPos.add(groupId);
            for (int i = 0; i < categoryIds.length; i++) {
                qPos.add(categoryIds[i]);
            }
            qPos.add(keywords);
            qPos.add(keywords);
            qPos.add(keywords);
            Iterator itr = q.list().iterator();
            if (itr.hasNext()) {
                Long count = (Long) itr.next();
                if (count != null) {
                    return count.intValue();
                }
            }
            return 0;
        } catch (Exception e) {
            throw new SystemException(e);
        } finally {
            HibernateUtil.closeSession(session);
        }
    }

    public static int countBySale(long groupId, long[] categoryIds) throws SystemException {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            StringMaker query = new StringMaker();
            query.append("SELECT COUNT(*) AS COUNT_VALUE FROM ShoppingItem ");
            query.append("INNER JOIN ShoppingCategory ON ");
            query.append("ShoppingCategory.categoryId = ");
            query.append("ShoppingItem.categoryId ");
            query.append("WHERE ");
            query.append("ShoppingCategory.groupId = ? AND (");
            if ((categoryIds != null) && (categoryIds.length > 0)) {
                query.append("(");
                for (int i = 0; i < categoryIds.length; i++) {
                    query.append("ShoppingItem.categoryId = ? ");
                    if (i + 1 < categoryIds.length) {
                        query.append("OR ");
                    }
                }
                query.append(") AND ");
            }
            query.append("ShoppingItem.sale = ? AND ");
            query.append("ShoppingItem.smallImage = ?");
            SQLQuery q = session.createSQLQuery(query.toString());
            q.addScalar(HibernateUtil.getCountColumnName(), Hibernate.LONG);
            QueryPos qPos = QueryPos.getInstance(q);
            qPos.add(groupId);
            for (int i = 0; i < categoryIds.length; i++) {
                qPos.add(categoryIds[i]);
            }
            qPos.add(true);
            qPos.add(true);
            Iterator itr = q.list().iterator();
            if (itr.hasNext()) {
                Long count = (Long) itr.next();
                if (count != null) {
                    return count.intValue();
                }
            }
            return 0;
        } catch (Exception e) {
            throw new SystemException(e);
        } finally {
            HibernateUtil.closeSession(session);
        }
    }

    public static List findByFeatured(long groupId, long[] categoryIds, int numOfItems) throws SystemException {
        int countByFeatured = countByFeatured(groupId, categoryIds);
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            StringMaker query = new StringMaker();
            query.append("SELECT {ShoppingItem.*} FROM ShoppingItem ");
            query.append("INNER JOIN ShoppingCategory ON ");
            query.append("ShoppingCategory.categoryId = ");
            query.append("ShoppingItem.categoryId ");
            query.append("WHERE ");
            query.append("ShoppingCategory.groupId = ? AND (");
            if ((categoryIds != null) && (categoryIds.length > 0)) {
                query.append("(");
                for (int i = 0; i < categoryIds.length; i++) {
                    query.append("ShoppingItem.categoryId = ? ");
                    if (i + 1 < categoryIds.length) {
                        query.append("OR ");
                    }
                }
                query.append(") AND ");
            }
            query.append("ShoppingItem.featured = ? AND ");
            query.append("ShoppingItem.smallImage = ?");
            SQLQuery q = session.createSQLQuery(query.toString());
            q.addEntity("ShoppingItem", ShoppingItemImpl.class);
            QueryPos qPos = QueryPos.getInstance(q);
            qPos.add(groupId);
            for (int i = 0; i < categoryIds.length; i++) {
                qPos.add(categoryIds[i]);
            }
            qPos.add(true);
            qPos.add(true);
            return QueryUtil.randomList(q, HibernateUtil.getDialect(), countByFeatured, numOfItems);
        } catch (Exception e) {
            throw new SystemException(e);
        } finally {
            HibernateUtil.closeSession(session);
        }
    }

    public static List findByKeywords(long groupId, long[] categoryIds, String keywords, int begin, int end) throws SystemException {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            StringMaker query = new StringMaker();
            query.append("SELECT {ShoppingItem.*} FROM ShoppingItem ");
            query.append("INNER JOIN ShoppingCategory ON ");
            query.append("ShoppingCategory.categoryId = ");
            query.append("ShoppingItem.categoryId ");
            query.append("WHERE ");
            query.append("ShoppingCategory.groupId = ? AND (");
            if ((categoryIds != null) && (categoryIds.length > 0)) {
                query.append("(");
                for (int i = 0; i < categoryIds.length; i++) {
                    query.append("ShoppingItem.categoryId = ? ");
                    if (i + 1 < categoryIds.length) {
                        query.append("OR ");
                    }
                }
                query.append(") AND ");
            }
            query.append("(ShoppingItem.name LIKE ? OR ");
            query.append("ShoppingItem.description LIKE ? OR ");
            query.append("ShoppingItem.properties LIKE ?))");
            keywords = '%' + keywords + '%';
            SQLQuery q = session.createSQLQuery(query.toString());
            q.addEntity("ShoppingItem", ShoppingItemImpl.class);
            QueryPos qPos = QueryPos.getInstance(q);
            qPos.add(groupId);
            for (int i = 0; i < categoryIds.length; i++) {
                qPos.add(categoryIds[i]);
            }
            qPos.add(keywords);
            qPos.add(keywords);
            qPos.add(keywords);
            return QueryUtil.list(q, HibernateUtil.getDialect(), begin, end);
        } catch (Exception e) {
            throw new SystemException(e);
        } finally {
            HibernateUtil.closeSession(session);
        }
    }

    public static List findBySale(long groupId, long[] categoryIds, int numOfItems) throws SystemException {
        int countBySale = countBySale(groupId, categoryIds);
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            StringMaker query = new StringMaker();
            query.append("SELECT {ShoppingItem.*} FROM ShoppingItem ");
            query.append("INNER JOIN ShoppingCategory ON ");
            query.append("ShoppingCategory.categoryId = ");
            query.append("ShoppingItem.categoryId ");
            query.append("WHERE ");
            query.append("ShoppingCategory.groupId = ? AND (");
            if ((categoryIds != null) && (categoryIds.length > 0)) {
                query.append("(");
                for (int i = 0; i < categoryIds.length; i++) {
                    query.append("ShoppingItem.categoryId = ? ");
                    if (i + 1 < categoryIds.length) {
                        query.append("OR ");
                    }
                }
                query.append(") AND ");
            }
            query.append("ShoppingItem.sale = ? AND ");
            query.append("ShoppingItem.smallImage = ?");
            SQLQuery q = session.createSQLQuery(query.toString());
            q.addEntity("ShoppingItem", ShoppingItemImpl.class);
            QueryPos qPos = QueryPos.getInstance(q);
            qPos.add(groupId);
            for (int i = 0; i < categoryIds.length; i++) {
                qPos.add(categoryIds[i]);
            }
            qPos.add(true);
            qPos.add(true);
            return QueryUtil.randomList(q, HibernateUtil.getDialect(), countBySale, numOfItems);
        } catch (Exception e) {
            throw new SystemException(e);
        } finally {
            HibernateUtil.closeSession(session);
        }
    }

    private static String _getCategoryIds(List categoryIds) {
        StringMaker sm = new StringMaker();
        for (int i = 0; i < categoryIds.size(); i++) {
            sm.append("categoryId = ? ");
            if ((i + 1) != categoryIds.size()) {
                sm.append("OR ");
            }
        }
        return sm.toString();
    }
}
