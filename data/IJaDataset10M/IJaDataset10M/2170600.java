package org.nakedobjects.nos.store.hibernate.property;

import java.lang.reflect.Method;
import java.util.Map;
import org.hibernate.HibernateException;
import org.hibernate.PropertyAccessException;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.property.Getter;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.Setter;

/**
 * Access properties of an object in a NakedObjects system, where that property
 * is a BusinessValueHolder or some other object which cannot be mapped using the
 * standard Hibernate property accessors.
 */
public abstract class AbstractNakedPropertyAccessor implements PropertyAccessor {

    public static final class NakedSetter implements Setter {

        private static final long serialVersionUID = 1L;

        private final Method getValueHolder;

        private final PropertyConverter converter;

        private final Class clazz;

        private final String name;

        NakedSetter(Method getValueHolder, PropertyConverter converter, Class clazz, String name) {
            this.getValueHolder = getValueHolder;
            this.converter = converter;
            this.clazz = clazz;
            this.name = name;
        }

        public Method getMethod() {
            return null;
        }

        public String getMethodName() {
            return null;
        }

        public void set(final Object target, final Object value, final SessionFactoryImplementor factory) throws HibernateException {
            try {
                Object valueHolder = getValueHolder.invoke(target, (Object[]) null);
                converter.setValue(valueHolder, value);
            } catch (Exception e) {
                throw new PropertyAccessException(e, "could not set a field value by reflection", true, clazz, name);
            }
        }
    }

    public static final class NakedGetter implements Getter {

        private static final long serialVersionUID = 1L;

        private final Method getValueHolder;

        private final PropertyConverter converter;

        private final boolean isNullable;

        private final Class clazz;

        private final String name;

        NakedGetter(Method getValueHolder, PropertyConverter converter, boolean isNullable, Class clazz, String name) {
            this.getValueHolder = getValueHolder;
            this.converter = converter;
            this.isNullable = isNullable;
            this.clazz = clazz;
            this.name = name;
        }

        public Object get(final Object target) throws HibernateException {
            try {
                Object valueHolder = getValueHolder.invoke(target, (Object[]) null);
                return converter.getPersistentValue(valueHolder, isNullable);
            } catch (Exception e) {
                throw new PropertyAccessException(e, "could not get a field value by reflection", false, clazz, name);
            }
        }

        public Method getMethod() {
            return null;
        }

        public String getMethodName() {
            return null;
        }

        public Class getReturnType() {
            return converter.getPersistentType();
        }

        public Object getForInsert(final Object target, final Map mergeMap, final SessionImplementor session) throws HibernateException {
            return get(target);
        }
    }

    protected Method getValueHolderMethod(final Class theClass, final String propertyName) throws PropertyNotFoundException {
        String naturalName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        Method getMethod = null;
        try {
            getMethod = theClass.getMethod("get" + naturalName, (Class[]) null);
        } catch (SecurityException se) {
            throw new PropertyNotFoundException("Cannot access method get" + naturalName + " in " + theClass + " : " + se.getMessage());
        } catch (NoSuchMethodException nsme) {
            try {
                getMethod = theClass.getMethod("get_" + naturalName, (Class[]) null);
            } catch (SecurityException se) {
                throw new PropertyNotFoundException("Cannot access method get_" + naturalName + " in " + theClass + " : " + se.getMessage());
            } catch (NoSuchMethodException nsme2) {
                throw new PropertyNotFoundException("Unknown property " + naturalName + " in " + theClass);
            }
        }
        return getMethod;
    }

    protected PropertyConverter getConverter(final Method getValueHolder) {
        return ConverterFactory.getInstance().getConverter(getValueHolder.getReturnType());
    }

    public Setter getSetter(final Class theClass, final String propertyName) throws PropertyNotFoundException {
        Method getValueHolder = getValueHolderMethod(theClass, propertyName);
        return new NakedSetter(getValueHolder, getConverter(getValueHolder), theClass, propertyName);
    }

    public Getter getGetter(final Class theClass, final String propertyName, final boolean isNullable) throws PropertyNotFoundException {
        Method getValueHolder = getValueHolderMethod(theClass, propertyName);
        return new NakedGetter(getValueHolder, getConverter(getValueHolder), isNullable, theClass, propertyName);
    }
}
