package org.avaje.ebean.bean;

/**
 * Bean that is aware of EntityBeanIntercept.
 * <p>
 * This interface and implementation of these methods is added to Entity Beans
 * via instrumentation. These methods have a funny _ebean_ prefix to avoid any
 * clash with normal methods these beans would have. These methods are not for
 * general application consumption.
 * </p>
 */
public interface EntityBean {

    /**
	 * Return the intercept for this object.
	 */
    public EntityBeanIntercept _ebean_getIntercept();

    /**
	 * Create a copy of this entity bean.
	 * <p>
	 * This occurs when a bean is changed. The copy represents the bean as it
	 * was initially (oldValues) before any changes where made. This is used for
	 * optimistic concurrency control.
	 * </p>
	 */
    public Object _ebean_createCopy();

    /**
	 * Return the fields in their index order.
	 */
    public String[] _ebean_getFieldNames();

    /**
	 * Set the value of a field of an entity bean of this type.
	 * <p>
	 * Note that using this method bypasses any interception that otherwise
	 * occurs on entity beans. That means lazy loading and oldValues creation.
	 * </p>
	 * 
	 * @param fieldIndex
	 *            the index of the field
	 * @param entityBean
	 *            the entityBean of this type to modify
	 * @param value
	 *            the value to set
	 */
    public void _ebean_setField(int fieldIndex, Object entityBean, Object value);

    /**
	 * Set the field value with interception.
	 */
    public void _ebean_setFieldIntercept(int fieldIndex, Object entityBean, Object value);

    /**
	 * Return the value of a field from an entity bean of this type.
	 * <p>
	 * Note that using this method bypasses any interception that otherwise
	 * occurs on entity beans. That means lazy loading.
	 * </p>
	 * 
	 * @param fieldIndex
	 *            the index of the field
	 * @param entityBean
	 *            the entityBean to get the value from
	 */
    public Object _ebean_getField(int fieldIndex, Object entityBean);

    /**
	 * Return the field value with interception.
	 */
    public Object _ebean_getFieldIntercept(int fieldIndex, Object entityBean);
}
