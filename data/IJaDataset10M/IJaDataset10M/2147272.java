package core.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;
import core.utils.BeanUtils;

/**
 *
 * @author lx
 */
public class BaseDaoImpl<T> extends HibernateDaoSupport implements BaseDao<T> {

    private static Log log = LogFactory.getLog(BaseDaoImpl.class);

    public T get(Serializable id) {
        return (T) getHibernateTemplate().get(BaseDaoImpl.getBeanClass(getClass()), id);
    }

    public void saveOrUpdate(T bean) {
        getHibernateTemplate().saveOrUpdate(bean);
    }

    public T getOneBy(String prop, Object value) {
        Criteria c = createCriteria();
        c.add(Restrictions.eq(prop, value));
        return (T) c.uniqueResult();
    }

    public List<T> list(String hql) {
        return createQuery(hql).list();
    }

    public void delete(T bean) {
        getHibernateTemplate().delete(bean);
    }

    public void deleteById(Serializable id) {
        String hql = String.format("delete %s where id = ?", getBeanClass(getClass()).getName());
        getHibernateTemplate().bulkUpdate(hql, id);
    }

    public Query createQuery(String hql) {
        return getSession().createQuery(hql);
    }

    public Criteria createCriteria() {
        return getSession().createCriteria(getBeanClass(getClass()));
    }

    /**
     * 獲得翻頁的效果.參數為Criteria
     * @param start
     * @param limit
     * @param c
     * @return
     */
    public Page<T> pagedCriteria(long start, int limit, String sort, String dir, Criteria c) {
        Assert.notNull(c);
        if (!StringUtils.isEmpty(sort)) {
            if ("desc".equalsIgnoreCase(dir)) {
                c.addOrder(Order.desc(sort));
            } else {
                c.addOrder(Order.asc(sort));
            }
        }
        CriteriaImpl impl = (CriteriaImpl) c;
        Projection projection = impl.getProjection();
        List<CriteriaImpl.OrderEntry> orderEntries;
        try {
            orderEntries = (List) BeanUtils.forceGetProperty(impl, "orderEntries");
            BeanUtils.forceSetProperty(impl, "orderEntries", new ArrayList<T>());
        } catch (Exception e) {
            throw new InternalError(" Runtime Exception impossibility throw ");
        }
        int count = (Integer) c.setProjection(Projections.rowCount()).uniqueResult();
        c.setProjection(projection);
        if (projection == null) {
            c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
        try {
            BeanUtils.forceSetProperty(impl, "orderEntries", orderEntries);
        } catch (Exception e) {
            throw new InternalError(" Runtime Exception impossibility throw ");
        }
        c.setFirstResult((int) start);
        c.setMaxResults(limit);
        return new Page<T>(c.list(), count, start, limit);
    }

    /**
     * 獲得翻頁的效果，參數為hql
     * @param start
     * @param limit
     * @param hql
     * @param object
     * @return
     */
    public Page pagedQuery(long start, int limit, String hql, List<Object> params) {
        String hqlCount = "select count(*) " + removeOrders(removeSelect(hql));
        Query queryCount = createQuery(hqlCount);
        for (int i = 0; i < params.size(); i++) {
            queryCount.setParameter(i, params.get(i));
        }
        long count = (Long) queryCount.uniqueResult();
        if (count == 0) return new Page<Object>(new ArrayList(), count, start, limit);
        Query query = createQuery(hql);
        query.setFirstResult((int) start);
        query.setMaxResults(limit);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i, params.get(i));
        }
        return new Page<Object>(query.list(), count, start, limit);
    }

    public List<T> list(Query query) {
        return query.list();
    }

    public List<T> listExample(T exampleBean) {
        return createCriteria().add(Example.create(exampleBean)).list();
    }

    /**
     * 去除hql的select 子句，未考慮union的情況,用於pagedQuery.
     *
     * @see #pagedQuery(int , int , String ,Object[])
     */
    private static String removeSelect(String hql) {
        Assert.hasText(hql);
        int beginPos = hql.toLowerCase().indexOf("from");
        Assert.isTrue(beginPos != -1, " hql : " + hql + " must has a keyword 'from'");
        return hql.substring(beginPos);
    }

    /**
     * 去除hql的orderby 子句，用於pagedQuery.
     *
     * @see #pagedQuery(int, int, String, java.lang.Object[]) 
     */
    private static String removeOrders(String hql) {
        Assert.hasText(hql);
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(hql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public Page<T> page(long start, int limit, String sort, String dir, T exampleBean) {
        Criteria c = createCriteria();
        if (exampleBean != null) c.add(Example.create(exampleBean));
        return pagedCriteria(start, limit, sort, dir, c);
    }

    @Override
    public Object execute(HibernateCallback action) {
        return getHibernateTemplate().execute(action);
    }

    private static Class<?> getBeanClass(Class<?> clz) {
        Type type = clz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type types[] = pt.getActualTypeArguments();
            if (types.length == 0) return null; else return (Class<?>) types[0];
        }
        return null;
    }

    @Override
    public List<T> listAll() {
        Criteria cr = this.createCriteria();
        return cr.list();
    }

    @Override
    public List<T> listAll(String order) {
        Criteria cr = this.createCriteria();
        cr.addOrder(Order.asc(order));
        return cr.list();
    }

    @Override
    public List query(String hql) {
        Query q = this.getSession().createQuery(hql);
        return q.list();
    }

    @Override
    public List query(String hql, Object param) {
        Query q = this.getSession().createQuery(hql);
        q.setParameter(0, param);
        return q.list();
    }

    @Override
    public List query(String hql, Object[] params) {
        Query q = this.getSession().createQuery(hql);
        for (int i = 0; i < params.length; ++i) {
            q.setParameter(i, params[i]);
        }
        return q.list();
    }

    @Override
    public void batchSave(List<T> beans) {
        this.getHibernateTemplate().saveOrUpdateAll(beans);
    }

    @Override
    public Criteria createCriteria(Class<?> clazz) {
        return getSession().createCriteria(clazz);
    }
}
