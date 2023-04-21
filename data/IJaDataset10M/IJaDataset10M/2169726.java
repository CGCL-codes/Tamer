package org.t2framework.commons.meta.spi;

import org.t2framework.commons.meta.BeanDesc;
import org.t2framework.commons.meta.PackageDescHolder;
import org.t2framework.commons.meta.impl.BeanDescImpl;
import org.t2framework.commons.meta.impl.SimpleBeanDescImpl;

/**
 * <#if locale="en">
 * <p>
 * Default implementation of BeanDescCreator.
 * 
 * </p>
 * <#else>
 * <p>
 * 
 * </p>
 * </#if>
 * 
 * @author shot
 * @see org.t2framework.commons.meta.spi.BeanDescCreator
 */
public class DefaultBeanDescCreatorImpl implements BeanDescCreator {

    @Override
    public <T> BeanDesc<T> createBeanDesc(Class<? extends T> componentClass) {
        return createBeanDesc(componentClass, DEFAULT_PACKAGEDESC_HOLDER);
    }

    @Override
    public <T> BeanDesc<T> createBeanDesc(Class<? extends T> componentClass, PackageDescHolder holder) {
        return new BeanDescImpl<T>(componentClass, holder);
    }

    @Override
    public <T> BeanDesc<T> createBeanDesc(T t, Class<? extends T> componentClass) {
        return createBeanDesc(t, componentClass, DEFAULT_PACKAGEDESC_HOLDER);
    }

    @Override
    public <T> BeanDesc<T> createBeanDesc(T t, Class<? extends T> componentClass, PackageDescHolder holder) {
        return new SimpleBeanDescImpl<T>(t, componentClass, holder);
    }
}
