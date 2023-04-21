package org.forzaframework.util;

import org.apache.commons.beanutils.PropertyUtils;
import java.lang.annotation.Annotation;
import java.util.List;
import java.beans.PropertyDescriptor;

/**
 * @author cesarreyes
 *         Date: 12-ago-2008
 *         Time: 15:27:54
 */
public abstract class ClassUtils extends org.apache.commons.lang.ClassUtils {

    public static boolean hasAnnotation(Class clazz, Class toAnnotation) {
        for (Annotation annotation : clazz.getAnnotations()) {
            if (annotation.annotationType().equals(toAnnotation)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasSuperclass(Class clazz, Class toClass) {
        List superclasses = getAllSuperclasses(clazz);
        for (Object superclass : superclasses) {
            if (superclass.equals(toClass)) {
                return true;
            }
        }
        return false;
    }

    public static Class getPropertyClass(Class clazz, String property) {
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(clazz);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getName().equals(property)) {
                return propertyDescriptor.getPropertyType();
            }
        }
        return null;
    }
}
