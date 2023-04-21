package com.germinus.xpression.content_editor.action;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.MappedPropertyDescriptor;
import org.apache.commons.beanutils.PropertyUtils;

public class OurBeanUtils {

    public static void copyProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
        Object target = bean;
        int delim = name.lastIndexOf(PropertyUtils.NESTED_DELIM);
        if (delim >= 0) {
            try {
                target = PropertyUtils.getProperty(bean, name.substring(0, delim));
            } catch (NoSuchMethodException e) {
                return;
            }
            name = name.substring(delim + 1);
        }
        String propName = null;
        Class type = null;
        int index = -1;
        String key = null;
        propName = name;
        int i = propName.indexOf(PropertyUtils.INDEXED_DELIM);
        if (i >= 0) {
            int k = propName.indexOf(PropertyUtils.INDEXED_DELIM2);
            try {
                index = Integer.parseInt(propName.substring(i + 1, k));
            } catch (NumberFormatException e) {
                ;
            }
            propName = propName.substring(0, i);
        }
        int j = propName.indexOf(PropertyUtils.MAPPED_DELIM);
        if (j >= 0) {
            int k = propName.indexOf(PropertyUtils.MAPPED_DELIM2);
            try {
                key = propName.substring(j + 1, k);
            } catch (IndexOutOfBoundsException e) {
                ;
            }
            propName = propName.substring(0, j);
        }
        if (target instanceof DynaBean) {
            DynaClass dynaClass = ((DynaBean) target).getDynaClass();
            DynaProperty dynaProperty = dynaClass.getDynaProperty(propName);
            if (dynaProperty == null) {
                return;
            }
            type = dynaProperty.getType();
        } else {
            PropertyDescriptor descriptor = null;
            try {
                descriptor = PropertyUtils.getPropertyDescriptor(target, name);
                if (descriptor == null) {
                    return;
                }
            } catch (NoSuchMethodException e) {
                return;
            }
            type = calculatePropertyType(descriptor);
        }
        if (type == null) {
            return;
        }
        setPropertyValue(target, index, key, propName, type, value);
    }

    private static Class calculatePropertyType(PropertyDescriptor descriptor) {
        if (descriptor instanceof MappedPropertyDescriptor) {
            if (((MappedPropertyDescriptor) descriptor).getMappedWriteMethod() == null) {
                return null;
            }
            return ((MappedPropertyDescriptor) descriptor).getMappedPropertyType();
        } else if (descriptor instanceof IndexedPropertyDescriptor) {
            if (((IndexedPropertyDescriptor) descriptor).getIndexedWriteMethod() == null) {
                return null;
            }
            return ((IndexedPropertyDescriptor) descriptor).getIndexedPropertyType();
        } else {
            if (descriptor.getWriteMethod() == null) {
                return null;
            }
            return descriptor.getPropertyType();
        }
    }

    private static void setPropertyValue(Object target, int index, String key, String propName, Class type, Object value) throws InvocationTargetException, IllegalAccessException {
        if (index >= 0) {
            Converter converter = ConvertUtils.lookup(type.getComponentType());
            if (converter != null) {
                value = converter.convert(type, value);
            }
            try {
                PropertyUtils.setIndexedProperty(target, propName, index, value);
            } catch (NoSuchMethodException e) {
                throw new InvocationTargetException(e, "Cannot set " + propName);
            }
        } else if (key != null) {
            try {
                PropertyUtils.setMappedProperty(target, propName, key, value);
            } catch (NoSuchMethodException e) {
                throw new InvocationTargetException(e, "Cannot set " + propName);
            }
        } else {
            Converter converter = ConvertUtils.lookup(type);
            if (converter != null) {
                value = converter.convert(type, value);
            }
            try {
                PropertyUtils.setSimpleProperty(target, propName, value);
            } catch (NoSuchMethodException e) {
                throw new InvocationTargetException(e, "Cannot set " + propName);
            }
        }
    }
}
