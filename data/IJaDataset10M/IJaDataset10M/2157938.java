package org.granite.generator.as3.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entity reflection for converting validation annotations to Flex.
 * 
 * @author William DRAI
 * @author Franck WOLFF
 */
public class JavaValidatableEntityBean extends JavaEntityBean {

    private Map<JavaProperty, List<JavaConstraint>> constraints = new HashMap<JavaProperty, List<JavaConstraint>>();

    public JavaValidatableEntityBean(JavaTypeFactory provider, Class<?> type, URL url, String metaAnnotationName, List<String> specialAnnotationNames, Map<String, String> nameConversions) {
        super(provider, type, url);
        Class<? extends Annotation> metaAnnotationClass = loadMetaAnnotationClass(type, metaAnnotationName);
        if (metaAnnotationClass == null) return;
        for (JavaProperty property : properties.values()) {
            List<JavaConstraint> javaConstraints = new ArrayList<JavaConstraint>();
            List<Annotation> constraintAnnotations = new ArrayList<Annotation>();
            for (Annotation annotation : property.getDeclaredAnnotations()) {
                Class<? extends Annotation> annotationClass = annotation.annotationType();
                if (annotationClass.isAnnotationPresent(metaAnnotationClass) || specialAnnotationNames.contains(annotationClass.getName())) constraintAnnotations.add(annotation); else {
                    Method value = null;
                    try {
                        value = annotationClass.getMethod("value");
                    } catch (NoSuchMethodException e) {
                    }
                    if (value != null && value.getReturnType().isArray() && value.getReturnType().getComponentType().isAnnotation() && value.getReturnType().getComponentType().isAnnotationPresent(metaAnnotationClass)) {
                        try {
                            Annotation[] annotationList = (Annotation[]) value.invoke(annotation);
                            constraintAnnotations.addAll(Arrays.asList(annotationList));
                        } catch (Exception e) {
                        }
                    }
                }
            }
            for (Annotation constraint : constraintAnnotations) {
                List<String[]> attributes = new ArrayList<String[]>();
                for (Method attribute : constraint.annotationType().getDeclaredMethods()) {
                    if (Modifier.isPublic(attribute.getModifiers()) && !Modifier.isStatic(attribute.getModifiers()) && attribute.getParameterTypes().length == 0) {
                        Object value = null;
                        try {
                            value = attribute.invoke(constraint);
                        } catch (Exception e) {
                            continue;
                        }
                        if (value != null && (!value.getClass().isArray() || Array.getLength(value) > 0)) attributes.add(new String[] { attribute.getName(), escape(value) });
                    }
                }
                String constraintName = constraint.annotationType().getSimpleName();
                if (nameConversions.containsKey(constraintName)) constraintName = nameConversions.get(constraintName);
                javaConstraints.add(new JavaConstraint(constraintName, attributes));
            }
            if (!javaConstraints.isEmpty()) constraints.put(property, javaConstraints);
        }
    }

    public Map<JavaProperty, List<JavaConstraint>> getConstraints() {
        return constraints;
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Annotation> loadMetaAnnotationClass(Class<?> type, String metaAnnotationName) {
        try {
            return (Class<? extends Annotation>) type.getClassLoader().loadClass(metaAnnotationName);
        } catch (Exception e) {
            return null;
        }
    }

    private static String escape(Object value) {
        if (value.getClass().isArray()) {
            StringBuilder sb = new StringBuilder();
            final int length = Array.getLength(value);
            boolean first = true;
            for (int i = 0; i < length; i++) {
                Object item = Array.get(value, i);
                if (item == null) continue;
                if (first) first = false; else sb.append(", ");
                sb.append(escape(item, true));
            }
            return sb.toString();
        }
        return escape(value, false);
    }

    private static String escape(Object value, boolean array) {
        if (value instanceof Class<?>) return ((Class<?>) value).getName();
        if (value.getClass().isEnum()) return ((Enum<?>) value).name();
        value = value.toString().replace("&", "&amp;").replace("\"", "&quot;");
        if (array) value = ((String) value).replace(",", ",,");
        return (String) value;
    }
}
