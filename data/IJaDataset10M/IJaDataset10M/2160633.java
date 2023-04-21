package org.melanesia.beans;

import java.util.List;
import java.util.Map;
import org.melanesia.beans.exceptions.PropertyBeanInstantiationException;
import org.melanesia.conf.Config;

/**
 * Composite property setter. This class implements setting values for composite
 * properties as described in {@link Setter#set(Object, Object, Map)}.
 * 
 * @author marcin.kielar
 * 
 */
class CompositeSetter implements Setter {

    /**
     * The accessor chain, i.e. list of <code>PropertyAccessor</code>s, which,
     * invoked sequentially will eventually lead to accessing the composite
     * property value.
     */
    private final List<PropertyAccessor> propertyAccessorList;

    /**
     * Constructs the <code>CompositeSetter</code>.
     * 
     * @param propertyAccessorList
     *            accessor chain
     */
    CompositeSetter(final List<PropertyAccessor> propertyAccessorList) {
        this.propertyAccessorList = propertyAccessorList;
    }

    @Override
    public void set(final Object bean, final Object value, final Map<String, Class<?>> intermediateBeanClasses) {
        StringBuilder propertyPathBuilder = new StringBuilder();
        Object currentBean = bean;
        for (int i = 0, size = propertyAccessorList.size(); i < size; i++) {
            PropertyAccessor accessor = propertyAccessorList.get(i);
            if (i + 1 == size) {
                accessor.set(currentBean, value, intermediateBeanClasses);
            } else {
                if (propertyPathBuilder.length() != 0) {
                    propertyPathBuilder.append(Config.getCompositePropertySeparator());
                }
                propertyPathBuilder.append(accessor.getPropertyName());
                Object subBean = accessor.get(currentBean);
                if (subBean == null) {
                    Class<?> subBeanClass = null;
                    String propertyPath = propertyPathBuilder.toString();
                    if (intermediateBeanClasses != null && intermediateBeanClasses.containsKey(propertyPath)) {
                        subBeanClass = intermediateBeanClasses.get(propertyPath);
                    } else {
                        subBeanClass = accessor.getPropertyClass();
                    }
                    subBean = createBeanInstance(subBeanClass);
                    accessor.set(currentBean, subBean, intermediateBeanClasses);
                }
                currentBean = subBean;
            }
        }
    }

    /**
     * Creates new instance of given class.
     * 
     * @param beanClass
     *            bean class to instantiate
     * @return instance of given class
     */
    private Object createBeanInstance(final Class<?> beanClass) {
        try {
            return beanClass.newInstance();
        } catch (Throwable t) {
            throw new PropertyBeanInstantiationException("Could not instantiate class \"" + beanClass + "\".", t);
        }
    }

    @Override
    public Class<?> getAcceptedType() {
        return propertyAccessorList.get(propertyAccessorList.size() - 1).getPropertyClass();
    }
}
