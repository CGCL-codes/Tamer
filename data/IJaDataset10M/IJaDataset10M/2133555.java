package org.util.reflection;

import org.util.PrimitiveUtils;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

public class PropertyUtils extends org.apache.commons.beanutils.PropertyUtils {

    private PropertyUtils() {
    }

    public static PropertyDescriptor getPropertyDescriptor(Class clazz, String name) {
        for (PropertyDescriptor desc : PropertyUtils.getPropertyDescriptors(clazz)) {
            if (desc.getName().equals(name)) return desc;
        }
        return null;
    }

    /** Gets the annotation for the property from either the read method or the write method. */
    public static Annotation getAnnotation(Class clazz, String propertyName, Class annotationType) {
        PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(clazz, propertyName);
        return getAnnotation(propertyDescriptor, annotationType);
    }

    public static Annotation getAnnotation(PropertyDescriptor propertyDescriptor, Class annotationType) {
        Method propertyMethod = propertyDescriptor.getWriteMethod();
        Annotation annotation = null;
        if (propertyMethod != null) annotation = propertyMethod.getAnnotation(annotationType);
        if (annotation == null) {
            propertyMethod = propertyDescriptor.getReadMethod();
            if (propertyMethod != null) annotation = propertyMethod.getAnnotation(annotationType);
        }
        return annotation;
    }

    public static boolean isCollectionOfType(PropertyDescriptor descriptor, Class type) {
        Class collectionType = getCollectionType(descriptor);
        if (collectionType != null) return type.isAssignableFrom(collectionType);
        return false;
    }

    public static boolean isCollectionOfPrimitives(PropertyDescriptor descriptor) {
        Class collectionType = getCollectionType(descriptor);
        if (collectionType != null) return PrimitiveUtils.isPrimitive(collectionType);
        return false;
    }

    public static Class getCollectionType(PropertyDescriptor descriptor) {
        if (descriptor.getReadMethod() != null) {
            Type genericType = descriptor.getReadMethod().getGenericReturnType();
            if (genericType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericType;
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                if (Collection.class.isAssignableFrom((Class<?>) parameterizedType.getRawType()) && typeArguments.length > 0) {
                    Type firstTypeArgument = parameterizedType.getActualTypeArguments()[0];
                    if (firstTypeArgument instanceof Class) return (Class) firstTypeArgument;
                }
            }
        }
        return null;
    }
}
