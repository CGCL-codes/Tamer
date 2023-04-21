package ar.com.beanutils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.beanutils.PropertyUtils;

public class BeanUtilsBean {

    public Set<PropertyDescriptor> getAllReadProperties(Object o) {
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(o);
        Set<PropertyDescriptor> ret = new HashSet<PropertyDescriptor>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getReadMethod() != null) {
                ret.add(propertyDescriptor);
            }
        }
        return ret;
    }

    public Set<PropertyDescriptor> getAllWriteProperties(Object o) {
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(o);
        Set<PropertyDescriptor> ret = new HashSet<PropertyDescriptor>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getWriteMethod() != null) {
                ret.add(propertyDescriptor);
            }
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public Set<PropertyPair> getPropertiesIntersection(Object o1, Object o2) {
        Map<String, PropertyDescriptor> properties1 = getPropertiesMap(o1);
        Map<String, PropertyDescriptor> properties2 = getPropertiesMap(o2);
        Set<PropertyPair> ret = new HashSet<PropertyPair>();
        for (String name : properties1.keySet()) {
            PropertyDescriptor sourceProperty = properties1.get(name);
            PropertyDescriptor targetProperty = properties2.get(name);
            if (sourceProperty != null && targetProperty != null && sourceProperty.getReadMethod() != null && targetProperty.getWriteMethod() != null) {
                ret.add(new PropertyPair(sourceProperty, targetProperty));
            }
        }
        return ret;
    }

    public Map<String, PropertyDescriptor> getPropertiesMap(Object o) {
        PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(o.getClass());
        Map<String, PropertyDescriptor> ret = new HashMap<String, PropertyDescriptor>();
        for (PropertyDescriptor property : properties) {
            if (!property.getName().equals("class")) {
                ret.put(property.getName(), property);
            }
        }
        return ret;
    }
}
