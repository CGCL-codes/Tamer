package com.jivespaces.util;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.impl.CriteriaImpl.OrderEntry;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.Assert;

/**
 * @author <a href="mailto:rory.cn@gmail.com">somebody</a>
 * @since 2006-6-25 0:44:51
 * @version $Id HiberanteUtils.java$
 */
public abstract class HibernateUtils {

    private static String removeSelect(String sql) {
        Assert.notNull(sql, "sql must be specified ");
        int beginPos = sql.toLowerCase().indexOf("from");
        Assert.isTrue(beginPos != -1, " the sql : " + sql + " must has a keyword 'from'");
        return sql.substring(beginPos);
    }

    private static String removeOrders(String sql) {
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static int getTotalCount(HibernateTemplate hibernateTemplate, String queryString, boolean isNamedQuery, String[] paramNames, Object[] paramValues) throws IllegalArgumentException, DataAccessException {
        if (StringUtils.isBlank(queryString)) {
            throw new IllegalArgumentException(" queryString can't be blank ");
        }
        String countQueryString = " select count (*) " + removeSelect(removeOrders(queryString));
        List countList;
        if (isNamedQuery) {
            countList = hibernateTemplate.findByNamedParam(countQueryString, paramNames, paramValues);
        } else {
            countList = hibernateTemplate.find(countQueryString, paramValues);
        }
        return ((Integer) countList.get(0)).intValue();
    }

    public static List getPageResult(Criteria criteria, int offset, int maxPageItems) throws HibernateException {
        criteria.setFirstResult(offset);
        criteria.setMaxResults(maxPageItems);
        return criteria.list();
    }

    public static PaginationSupport findByCriteria(HibernateTemplate hibernateTemplate, final DetachedCriteria criteria, final int firstResult, final int maxResults) throws DataAccessException {
        return (PaginationSupport) hibernateTemplate.execute(new HibernateCallback() {

            @SuppressWarnings("unchecked")
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria executableCriteria = criteria.getExecutableCriteria(session);
                OrderEntry[] orderEntries = HibernateUtils.getOrders(executableCriteria);
                executableCriteria = HibernateUtils.removeOrders(executableCriteria);
                Projection projection = HibernateUtils.getProjection(executableCriteria);
                int totalCount = ((Integer) executableCriteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
                executableCriteria.setProjection(projection);
                if (projection == null) {
                    executableCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
                }
                executableCriteria = HibernateUtils.addOrders(executableCriteria, orderEntries);
                List items = HibernateUtils.getPageResult(executableCriteria, firstResult, maxResults);
                return new PaginationSupport(items, totalCount, firstResult, maxResults);
            }
        }, true);
    }

    public static int getTotalCount(HibernateTemplate hibernateTemplate, final DetachedCriteria criteria) throws DataAccessException {
        return ((Integer) hibernateTemplate.execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria executableCriteria = criteria.getExecutableCriteria(session);
                executableCriteria = HibernateUtils.removeOrders(executableCriteria);
                int totalCount = ((Integer) executableCriteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
                return new Integer(totalCount);
            }
        }, true)).intValue();
    }

    public static List findByCriteria(HibernateTemplate hibernateTemplate, final DetachedCriteria criteria) throws DataAccessException {
        return (List) hibernateTemplate.execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria executableCriteria = criteria.getExecutableCriteria(session);
                OrderEntry[] orderEntries = HibernateUtils.getOrders(executableCriteria);
                executableCriteria = HibernateUtils.removeOrders(executableCriteria);
                Projection projection = HibernateUtils.getProjection(executableCriteria);
                executableCriteria.setProjection(projection);
                if (projection == null) {
                    executableCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
                }
                executableCriteria = HibernateUtils.addOrders(executableCriteria, orderEntries);
                return executableCriteria.list();
            }
        }, true);
    }

    public static Projection getProjection(Criteria criteria) {
        CriteriaImpl impl = (CriteriaImpl) criteria;
        return impl.getProjection();
    }

    @SuppressWarnings("unchecked")
    public static OrderEntry[] getOrders(Criteria criteria) {
        CriteriaImpl impl = (CriteriaImpl) criteria;
        Field field = getOrderEntriesField(criteria);
        try {
            return (OrderEntry[]) ((List) field.get(impl)).toArray(new OrderEntry[0]);
        } catch (Exception e) {
            throw new InternalError(" Runtime Exception impossibility can't throw ");
        }
    }

    public static Criteria removeOrders(Criteria criteria) {
        CriteriaImpl impl = (CriteriaImpl) criteria;
        try {
            Field field = getOrderEntriesField(criteria);
            field.set(impl, new ArrayList());
            return impl;
        } catch (Exception e) {
            throw new InternalError(" Runtime Exception impossibility can't throw ");
        }
    }

    @SuppressWarnings("unchecked")
    public static Criteria addOrders(Criteria criteria, OrderEntry[] orderEntries) {
        CriteriaImpl impl = (CriteriaImpl) criteria;
        try {
            Field field = getOrderEntriesField(criteria);
            for (int i = 0; i < orderEntries.length; i++) {
                List innerOrderEntries = (List) field.get(criteria);
                innerOrderEntries.add(orderEntries[i]);
            }
            return impl;
        } catch (Exception e) {
            throw new InternalError(" Runtime Exception impossibility can't throw ");
        }
    }

    private static Field getOrderEntriesField(Criteria criteria) {
        Assert.notNull(criteria, " criteria is requried. ");
        try {
            Field field = CriteriaImpl.class.getDeclaredField("orderEntries");
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            throw new InternalError();
        }
    }
}
