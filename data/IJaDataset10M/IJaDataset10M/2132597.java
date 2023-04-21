package org.jbfilter.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.jbfilter.bean.PropertyAccessor;
import org.jbfilter.bean.fcomps.single.ContainsStringFilterComponentBean;

class _ContainsStringFilterComponentBeanImpl<E> extends _ContainsStringFilterComponentImpl<E> implements ContainsStringFilterComponentBean<E> {

    private _ContainsStringFilterComponentBeanSupport<E, ContainsStringFilterComponentBean<E>> support = new _ContainsStringFilterComponentBeanSupport<E, ContainsStringFilterComponentBean<E>>(this);

    _ContainsStringFilterComponentBeanImpl() {
        super();
    }

    _ContainsStringFilterComponentBeanImpl(String id) {
        this(id, id);
    }

    _ContainsStringFilterComponentBeanImpl(String id, String propertyPath) {
        super(id);
        setPropertyPath(propertyPath);
    }

    _ContainsStringFilterComponentBeanImpl(String id, PropertyAccessor<E, String> propertyAccessor) {
        super(id);
        setPropertyAccessor(propertyAccessor);
    }

    @Override
    public void setNullTestingEnabled(boolean enabled) {
        support.setNullTestingEnabled(enabled);
    }

    @Override
    public boolean isNullTestingEnabled() {
        return support.isNullTestingEnabled();
    }

    @Override
    public String getPropertyValue(E bean) {
        return support.getPropertyValue(bean);
    }

    @Override
    public void setPropertyAccessor(PropertyAccessor<E, String> propertyAccessor) {
        support.setPropertyAccessor(propertyAccessor);
    }

    @Override
    public void filterOut(LinkedList<E> coll) {
        support.filterOut(coll);
    }

    @Override
    public List<E> filter(Collection<E> toFilter) {
        return support.filter(toFilter);
    }

    @Override
    public boolean pass(E bean) {
        return support.pass(bean);
    }

    @Override
    public String getPropertyPath() {
        return support.getPropertyPath();
    }

    @Override
    public void setPropertyPath(String propertyPath) {
        support.setPropertyPath(propertyPath);
    }
}
