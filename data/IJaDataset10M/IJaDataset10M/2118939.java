package org.i0o.webplus.dao.impl.hibernate3;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.i0o.utilplus.GenericsUtils;
import org.i0o.webplus.dao.IDomainObjectDao;
import org.i0o.webplus.model.IDomainObject;
import org.i0o.webplus.support.Page;

/**
 * @author <a href="mailto:781131@gmail.com">HTF</a>
 */
@SuppressWarnings("unchecked")
public abstract class DomainObjectDao<D extends IDomainObject> extends BaseHibernateDao implements IDomainObjectDao<D> {

    /**
	 * 当前类的泛型
	 */
    private Class<?> entityClass = GenericsUtils.getGenericClass(this.getClass());

    @Override
    public void doDelete(D o) {
        super.getHibernateTemplate().delete(o);
    }

    @Override
    public void doDeleteById(Serializable id) {
        super.genericDeleteById(this.entityClass, id);
    }

    @Override
    public void doDeleteByIds(Serializable[] ids) {
        for (Serializable id : ids) {
            this.doDeleteById(id);
        }
    }

    @Override
    public void doSave(D o) {
        super.getHibernateTemplate().save(o);
    }

    @Override
    public void doSaveAll(Collection<D> list) {
        for (D o : list) {
            this.doSave(o);
        }
    }

    @Override
    public void doSaveOrUpdate(D o) {
        super.getHibernateTemplate().saveOrUpdate(o);
    }

    @Override
    public void doUpdate(D o) {
        super.getHibernateTemplate().update(o);
    }

    @Override
    public void doUpdateAll(Collection<D> list) {
        for (D o : list) {
            this.doUpdate(o);
        }
    }

    @Override
    public void doSaveOrUpdateAll(Collection<D> list) {
        for (D o : list) {
            this.doSaveOrUpdate(o);
        }
    }

    @Override
    public Long getCount() {
        Criteria criteria = super.getSession().createCriteria(this.entityClass);
        return (Long) criteria.setProjection(Projections.count("*")).uniqueResult();
    }

    @Override
    public List<D> findAll() {
        return (List<D>) super.genericGetAll(entityClass);
    }

    @Override
    public List<D> findBy(String name, Object value) {
        Criteria criteria = super.getSession().createCriteria(this.entityClass);
        criteria.add(Restrictions.eq(name, value));
        return criteria.list();
    }

    @Override
    public List<D> findBy(String[] names, Object[] values) {
        Criteria criteria = super.getSession().createCriteria(this.entityClass);
        for (int i = 0; i < names.length; i++) {
            criteria.add(Restrictions.eq(names[i], values[i]));
        }
        return criteria.list();
    }

    @Override
    public List<D> findBy(Map<String, Object> param) {
        Set<String> keySet = param.keySet();
        Criteria criteria = super.getSession().createCriteria(this.entityClass);
        for (String keyString : keySet) {
            criteria.add(Restrictions.eq(keyString, param.get(keyString)));
        }
        return criteria.list();
    }

    @Override
    public D findById(Serializable id) {
        return (D) super.genericGet(this.entityClass, id);
    }

    @Override
    public List<D> findByLike(String name, String value) {
        Criteria criteria = super.getSession().createCriteria(this.entityClass);
        criteria.add(Restrictions.like(name, value));
        return criteria.list();
    }

    @Override
    public D findUniqueBy(String name, Object value) {
        Criteria criteria = super.getSession().createCriteria(this.entityClass);
        criteria.add(Restrictions.eq(name, value));
        return (D) criteria.uniqueResult();
    }

    @Override
    public boolean isNotUnique(D domainObject, String names) {
        return false;
    }

    @Override
    public D loadById(Serializable id) {
        return (D) super.getHibernateTemplate().load(entityClass, id);
    }

    @Override
    public Page pagedQueryByPageNo(String hql, int start, int pageSize, Object[] parameters) {
        return this.pagedQueryByStart(hql, start, pageSize, parameters);
    }

    @Override
    public Page pagedQueryByPageNo(String hql, int start, int pageSize, Map<String, Object> parameter) {
        return this.pagedQueryByStart(hql, start, pageSize, parameter);
    }

    @Override
    public Page pagedSqlQueryByPageNo(String sql, int start, int pageSize, Object[] parameters) {
        return this.pagedSqlQueryByPageNo(sql, start, pageSize, parameters);
    }

    @Override
    public Page pagedSqlQueryByPageNo(String sql, int start, int pageSize, Map<String, Object> parameters) {
        return this.pagedSqlQueryByPageNo(sql, start, pageSize, parameters);
    }

    @Override
    public Page pagedQueryByStart(String hql, int start, int pageSize, Object[] parameters) {
        long totalCount = this.getTotalCount(hql, parameters, true);
        if (totalCount < 1) {
            return new Page();
        }
        return new Page(start, totalCount, pageSize, this.queryPagedResult(hql, start, pageSize, parameters));
    }

    @Override
    public Page pagedQueryByStart(String hql, int start, int pageSize, Map<String, Object> parameters) {
        long totalCount = this.getTotalCount(hql, parameters, true);
        if (totalCount < 1) {
            return new Page();
        }
        return new Page(start, totalCount, pageSize, this.queryPagedResult(hql, start, pageSize, parameters));
    }

    @Override
    public Page pagedSqlQueryByStart(String sql, int start, int pageSize, Object[] parameters) {
        return null;
    }

    @Override
    public Page pagedSqlQueryByStart(String sql, int start, int pageSize, Map<String, Object> parameters) {
        return null;
    }

    /**
	 * 获取总行数
	 * 
	 * @param hql
	 * @param parameters
	 * @param b
	 * @return
	 */
    private long getTotalCount(String hql, Object[] parameters, boolean b) {
        int fromIndex = hql.toLowerCase().indexOf("from");
        if (fromIndex != -1) hql = hql.substring(fromIndex + 4);
        int index = hql.toLowerCase().lastIndexOf("order by");
        if (index != -1) hql = hql.substring(0, index);
        String newhql = "SELECT COUNT(*) FROM " + hql;
        Query query = super.createQuery(newhql, parameters);
        return Long.valueOf(query.uniqueResult().toString());
    }

    /**
	 * 获取总行数
	 * 
	 * @param hql
	 * @param parameters
	 * @param b
	 * @return
	 */
    private long getTotalCount(String hql, Map<String, Object> parameters, boolean b) {
        int fromIndex = hql.toLowerCase().indexOf("from");
        if (fromIndex != -1) hql = hql.substring(fromIndex + 4);
        int index = hql.toLowerCase().lastIndexOf("order by");
        if (index != -1) hql = hql.substring(0, index);
        String newhql = "SELECT COUNT(*) FROM " + hql;
        Query query = super.createQuery(newhql, parameters);
        return Long.valueOf(query.uniqueResult().toString());
    }

    /**
	 * 获取当前页数据
	 * 
	 * @param hql
	 * @param startIndex
	 * @param pageSize
	 * @param parameters
	 * @return
	 */
    private List<Object> queryPagedResult(String hql, int start, int pageSize, Object[] parameters) {
        Query query = super.createQuery(hql, parameters);
        query.setFirstResult(start);
        query.setMaxResults(pageSize);
        return query.list();
    }

    /**
	 * 获取当前页数据
	 * 
	 * @param hql
	 * @param startIndex
	 * @param pageSize
	 * @param parameters
	 * @return
	 */
    private List<Object> queryPagedResult(String hql, int start, int pageSize, Map<String, Object> parameters) {
        Query query = super.createQuery(hql, parameters);
        query.setFirstResult(start);
        query.setMaxResults(pageSize);
        return query.list();
    }
}
