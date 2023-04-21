package de.mindmatters.faces.spring.context.propertyresolver;

import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @author andreas.kuhrwahl
 * @deprecated
 */
public class PropertyResolver1 extends PropertyResolver implements BeanFactoryAware {

    private BeanFactory beanFactory;

    private final PropertyResolver delegate;

    private String resolverProperty;

    public PropertyResolver1(final PropertyResolver delegate) {
        super();
        this.delegate = delegate;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public PropertyResolver getDelegate() {
        return delegate;
    }

    public Class getType(Object base, int index) throws EvaluationException, PropertyNotFoundException {
        return delegate.getType(base, index);
    }

    public Class getType(Object base, Object property) throws EvaluationException, PropertyNotFoundException {
        return delegate.getType(base, property);
    }

    public Object getValue(Object base, int index) throws EvaluationException, PropertyNotFoundException {
        return delegate.getValue(base, index);
    }

    public Object getValue(Object base, Object property) throws EvaluationException, PropertyNotFoundException {
        return delegate.getValue(base, property);
    }

    public boolean isReadOnly(Object base, int index) throws EvaluationException, PropertyNotFoundException {
        return delegate.isReadOnly(base, index);
    }

    public boolean isReadOnly(Object base, Object property) throws EvaluationException, PropertyNotFoundException {
        return delegate.isReadOnly(base, property);
    }

    public void setValue(Object base, int index, Object value) throws EvaluationException, PropertyNotFoundException {
        delegate.setValue(base, index, value);
    }

    public void setValue(Object base, Object property, Object value) throws EvaluationException, PropertyNotFoundException {
        delegate.setValue(base, property, value);
    }

    public String getResolverProperty() {
        return resolverProperty;
    }

    public void setResolverProperty(String resolverProperty) {
        this.resolverProperty = resolverProperty;
    }
}
