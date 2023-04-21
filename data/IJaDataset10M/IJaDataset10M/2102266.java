package com.agilejava.bignumbers.core;

import java.math.BigDecimal;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.WrapDynaBean;

/**
 * An implementation of the
 * {@link com.agilejava.bignumbers.core.VariableResolver }interface that
 * obtains variable values from a bean. It basically wraps a Bean with a
 * {@link com.agilejava.bignumbers.core.VariableResolver }interface.
 * 
 * @author Wilfred Springer
 */
public class BeanVariableResolver implements VariableResolver {

    /**
   * The DynBean providing access to the Bean's properties.
   */
    private DynaBean bean;

    /**
   * Constructs a new instance of a BeanVariableResolver, accepting the Bean to
   * be wrapped.
   * 
   * @param bean
   *          The Bean to be wrapped by this VariableResolver implementation.
   */
    public BeanVariableResolver(Object bean) {
        this.bean = new WrapDynaBean(bean);
    }

    public BigDecimal resolve(String variableName) {
        try {
            Object value = bean.get(variableName);
            if (value instanceof BigDecimal) {
                return (BigDecimal) value;
            } else {
                return (BigDecimal) ConvertUtils.convert(value.toString(), BigDecimal.class);
            }
        } catch (IllegalArgumentException iae) {
            return null;
        } catch (ConversionException ce) {
            return null;
        }
    }
}
