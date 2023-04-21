package org.jbfilter.bean;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.PropertyUtils;
import org.jbfilter.core.exception.FilterException;

/**
 * A {@link PropertyAccessor} accessing the property by reflection.
 * @author Marcus Adrian
 * @param <E> the bean type
 * @param <T> the property type
 */
public class ReflectionPropertyAccessor<E, T> implements PropertyAccessor<E, T> {

    private String propertyPath;

    public String getPropertyPath() {
        return propertyPath;
    }

    public void setPropertyPath(String propertyPath) {
        this.propertyPath = propertyPath;
    }

    /**
	 * If the bean {@code E} is of type Movie and movie has an property director, then
	 * the {@code propertyPath} should be {@code "director"}. Supports also nested properties like
	 * {@code "director.name.last"}.
	 * @param propertyPath
	 */
    public ReflectionPropertyAccessor(String propertyPath) {
        super();
        this.propertyPath = propertyPath;
    }

    /**
	 * Retrieves the property by reflection.
	 * Reflection is a risky business so a {@link ClassCastException} will arise if
	 * the property isn't of the claimed type.
	 * @throws ClassCastException if incorrect type parameter
	 * @see org.jbfilter.bean.PropertyAccessor#getPropertyValue(java.lang.Object)
	 */
    public T getPropertyValue(E bean) {
        try {
            @SuppressWarnings("unchecked") T prop = (T) PropertyUtils.getProperty(bean, propertyPath);
            return prop;
        } catch (IllegalAccessException e) {
            throw new FilterException(e);
        } catch (InvocationTargetException e) {
            throw new FilterException(e);
        } catch (NoSuchMethodException e) {
            throw new FilterException(e);
        }
    }
}
