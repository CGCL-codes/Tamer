package net.sf.hippopotam.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.sf.hippopotam.validator.BeanValidator;
import net.sf.hippopotam.Hippopotam;

/**
 *
 */
public class BeanProperty<T> {

    private final Object bean;

    private final String propertyName;

    private final Method getter;

    private final Method setter;

    private final BeanValidator beanValidator;

    public BeanProperty(Object bean, String propertyName) {
        this.bean = bean;
        this.propertyName = propertyName;
        this.getter = ObjectUtil.findGetter(bean, propertyName);
        if (getter == null) {
            throw new IllegalArgumentException(bean + "." + propertyName);
        }
        this.setter = ObjectUtil.findSetterByArgType(bean, propertyName, getPropertyType());
        beanValidator = Hippopotam.instance().getValidatorFactory().createBeanValidator(bean);
    }

    private Class getPropertyType() {
        return getter.getReturnType();
    }

    public T getValue() {
        try {
            return (T) getter.invoke(bean);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public void setValue(T value) {
        try {
            setter.invoke(bean, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public String toString() {
        return bean.getClass().getName() + "." + propertyName;
    }

    public boolean isAssignableFrom(Class clazz) {
        return getPropertyType().isAssignableFrom(clazz);
    }

    public boolean fitsConstraints(Object objectValue) {
        if (!(objectValue instanceof String)) {
            return true;
        }
        if (!beanValidator.containsMaxLength(propertyName)) {
            return true;
        }
        return ((String) objectValue).length() <= beanValidator.getMaxLength(propertyName);
    }

    public ConstraintsViolation getConstraintsViolation(Object objectValue) {
        return new ConstraintsViolation("moreThanMaxLength", new Object[] { beanValidator.getMaxLength(propertyName) });
    }
}
