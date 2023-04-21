package velox.spring.admin.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

public class AnnotationHelper {

    public static AnnotationSet getClassAnnotations(Class<?> beanType) {
        AnnotationSet dest = new AnnotationSet();
        if (!beanType.getSuperclass().equals(Object.class)) {
            dest.addAll(getClassAnnotations(beanType.getSuperclass()));
        }
        if (beanType.getInterfaces() != null) {
            for (Class<?> c : beanType.getInterfaces()) {
                dest.addAll(Arrays.asList(c.getAnnotations()));
            }
        }
        dest.addAll(Arrays.asList(beanType.getAnnotations()));
        return dest;
    }

    public static Map<String, AnnotationSet> getPropertyAnnotations(Class<?> beanType) {
        try {
            Map<String, AnnotationSet> dest = new HashMap<String, AnnotationSet>();
            BeanInfo beanInfo = Introspector.getBeanInfo(beanType);
            for (final PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                final AnnotationSet annotations = new AnnotationSet();
                annotations.addAll(Arrays.asList(AnnotationUtils.getAnnotations(propertyDescriptor.getReadMethod())));
                if (propertyDescriptor.getWriteMethod() != null) {
                    annotations.addAll(Arrays.asList(AnnotationUtils.getAnnotations(propertyDescriptor.getWriteMethod())));
                }
                ReflectionUtils.doWithFields(beanType, new FieldCallback() {

                    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                        if (field.getName().equals(propertyDescriptor.getName())) {
                            for (Annotation annotation : field.getAnnotations()) {
                                if (!annotations.contains(annotation)) {
                                    annotations.add(annotation);
                                }
                            }
                        }
                    }
                });
                dest.put(propertyDescriptor.getName(), annotations);
            }
            return dest;
        } catch (IntrospectionException io) {
            throw new RuntimeException(io);
        }
    }

    public static class AnnotationSet extends HashSet<Annotation> {

        private static final long serialVersionUID = 2903912166443340049L;

        public boolean containsAnnotationOfType(Class<? extends Annotation> annotationType) {
            return getAnnotationOfType(annotationType) != null;
        }

        @SuppressWarnings("unchecked")
        public <T extends Annotation> T getAnnotationOfType(Class<T> annotationType) {
            for (Annotation a : this) {
                if (annotationType.isAssignableFrom(a.getClass())) {
                    return (T) a;
                }
            }
            return null;
        }
    }
}
