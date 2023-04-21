package com.zwl.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.Criterion;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;

/**
 * Generic Manager that talks to GenericDao to CRUD POJOs.
 *
 * <p>Extend this interface if you want typesafe (no casting necessary) managers
 * for your domain objects.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
public interface GenericManager<T, PK extends Serializable> {

    /**
     * Generic method used to get all objects of a particular type. This
     * is the same as lookup up all rows in a table.
     * @return List of populated objects
     */
    List<T> getAll();

    /**
     * Generic method to get an object based on class and identifier. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if
     * nothing is found.
     *
     * @param id the identifier (primary key) of the object to get
     * @return a populated object
     * @see org.springframework.orm.ObjectRetrievalFailureException
     */
    T get(PK id);

    /**
     * 保存新增或更新对�?     */
    void save(final T entity);

    /**
     * Generic method to delete an object based on class and id
     * @param id the identifier (primary key) of the object to remove
     */
    void remove(PK id);

    Page<T> search(final Page<T> page, List<PropertyFilter> filters);

    /**
	 * 按Criteria分页查询.
	 * 
	 * @param page 分页参数.
	 * @param criterions 数量可变的Criterion.
	 * 
	 * @return 分页查询结果.附带结果列表及所有查询输入参�?
	 */
    public Page<T> findPage(final Page<T> page, final Criterion... criterions);

    /**
	 * 按HQL分页查询.
	 * 
	 * @param page 分页参数. 注意不支持其中的orderBy参数.
	 * @param hql hql语句.
	 * @param values 命名参数,按名称绑�?
	 * 
	 * @return 分页查询结果, 附带结果列表及所有查询输入参�?
	 */
    public Page<T> findPage(final Page<T> page, final String hql, final Map<String, ?> values);

    /**
	 * 按HQL分页查询.
	 * 
	 * @param page 分页参数. 注意不支持其中的orderBy参数.
	 * @param hql hql语句.
	 * @param values 数量可变的查询参�?按顺序绑�?
	 * 
	 * @return 分页查询结果, 附带结果列表及所有查询输入参�?
	 */
    public Page<T> findPage(final Page<T> page, final String hql, final Object... values);

    /**
	 * 按属性查找对象列�? 匹配方式为相�?
	 */
    public List<T> findBy(final String propertyName, final Object value);

    /**
	 * 按属性查找唯�?���? 匹配方式为相�?
	 */
    public T findUniqueBy(final String propertyName, final Object value);

    /**
	 * 按HQL查询对象列表.
	 * 
	 * @param values
	 *            数量可变的参�?按顺序绑�?
	 */
    public <X> List<X> find(final String hql, final Object... values);

    /**
	 * 按HQL查询对象列表.
	 * 
	 * @param values
	 *            命名参数,按名称绑�?
	 */
    public <X> List<X> find(final String hql, final Map<String, ?> values);

    /**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values
	 *            数量可变的参�?按顺序绑�?
	 */
    public <X> X findUnique(final String hql, final Object... values);

    /**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values
	 *            命名参数,按名称绑�?
	 */
    public <X> X findUnique(final String hql, final Map<String, ?> values);
}
