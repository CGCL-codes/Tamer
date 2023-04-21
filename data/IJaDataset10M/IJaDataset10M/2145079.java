package com.javampire.util.dao.generic;

import com.javampire.util.dao.RecordFactory;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * TODO: document this.
 *
 * @author <a href="mailto:cnagy@ecircle.de">Csaba Nagy</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2007/04/10 10:21:59 $
 */
public abstract class GenericBean<T extends GenericBean<T>> implements RecordFactory<T> {

    public abstract T newRecord();

    /**
     * Does nothing, override if needed.
     * @param recordId the record id of the record being constructed.
     */
    public void setRecordId(int recordId) {
    }

    public void copy(T record, T destination) {
        record.copyTo(destination);
    }

    @SuppressWarnings({ "unchecked" })
    public void copyTo(T destination) {
        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                final Method readMethod = descriptor.getReadMethod();
                final Method writeMethod = descriptor.getWriteMethod();
                if (readMethod != null && writeMethod != null && readMethod.isAnnotationPresent(AttrInfo.class)) {
                    final Object field = readMethod.invoke(this);
                    final Class<?> returnType = readMethod.getReturnType();
                    if (returnType.isAnnotationPresent(GenericBeanInfo.class)) {
                        final GenericBean otherField = (GenericBean) readMethod.invoke(destination);
                        ((GenericBean) field).copyTo(otherField);
                    } else {
                        writeMethod.invoke(destination, field);
                    }
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                final Method readMethod = descriptor.getReadMethod();
                final Method writeMethod = descriptor.getWriteMethod();
                if (readMethod != null && writeMethod != null && readMethod.isAnnotationPresent(AttrInfo.class)) {
                    final Object field = readMethod.invoke(this);
                    if (result.length() > 0) result.append(',');
                    result.append(descriptor.getDisplayName()).append('=');
                    appendField(result, field);
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private void appendField(StringBuffer result, Object field) {
        if (field == null) {
            result.append("null");
        } else if (field.getClass().isArray()) {
            result.append('[');
            final int length = Array.getLength(field);
            for (int i = 0; i < length; i++) {
                if (i > 0) result.append(", ");
                appendField(result, Array.get(field, i));
            }
            result.append(']');
        } else {
            result.append(field);
        }
    }

    public int hashCode() {
        int result = 0;
        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                final Method readMethod = descriptor.getReadMethod();
                final Method writeMethod = descriptor.getWriteMethod();
                if (readMethod != null && writeMethod != null && readMethod.isAnnotationPresent(AttrInfo.class)) {
                    final Object field = readMethod.invoke(this);
                    result = 29 * result + field.hashCode();
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
        return result;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GenericBean that = (GenericBean) o;
        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                final Method readMethod = descriptor.getReadMethod();
                final Method writeMethod = descriptor.getWriteMethod();
                if (readMethod != null && writeMethod != null && readMethod.isAnnotationPresent(AttrInfo.class)) {
                    final Object field_1 = readMethod.invoke(this);
                    final Object field_2 = readMethod.invoke(that);
                    if (readMethod.getReturnType().isArray()) {
                        if (!Arrays.deepEquals(new Object[] { field_1 }, new Object[] { field_2 })) {
                            return false;
                        }
                    } else {
                        if (!field_1.equals(field_2)) return false;
                    }
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
        return true;
    }
}
