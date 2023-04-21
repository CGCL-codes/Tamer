package org.t2framework.lucy.impl;

import java.util.List;
import org.t2framework.commons.meta.BeanDesc;
import org.t2framework.commons.util.Assertion;
import org.t2framework.lucy.BeanDescStrategy;
import org.t2framework.lucy.Lucy;
import org.t2framework.lucy.exception.TooManyRegistrationException;

/**
 * <#if locale="en">
 * <p>
 * DefaultBeanDescStrategyImpl is a strategy class of BeanDescFactory.
 * 
 * </p>
 * <#else>
 * <p>
 * 
 * </p>
 * </#if>
 * 
 * @author shot
 */
public class DefaultBeanDescStrategyImpl implements BeanDescStrategy {

    protected final Lucy lucy;

    public DefaultBeanDescStrategyImpl(Lucy lucy) {
        this.lucy = lucy;
    }

    @Override
    public <T> BeanDesc<T> getBeanDesc(Class<T> beanClass, List<BeanDesc<T>> list) {
        Assertion.notNull(beanClass);
        Assertion.notNull(list);
        if (list.size() == 1) {
            return list.get(0);
        } else {
            for (BeanDesc<T> bd : list) {
                if (bd.hasName() == false) {
                    return bd;
                }
            }
            for (BeanDesc<T> bd : list) {
                if (bd.hasName()) {
                    return bd;
                }
            }
            throw new TooManyRegistrationException(beanClass);
        }
    }

    @Override
    public <T> T handleNoBeanDesc(Object key) {
        return null;
    }
}
