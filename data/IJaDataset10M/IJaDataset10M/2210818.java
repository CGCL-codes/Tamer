package fastforward.meta.annotation.converter.global;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import fastforward.meta.abstraction.Metadata;

/**
 * Converter for single value annotation. The value of the value annotation
 * method is used as the metadata property value.
 * 
 */
@SuppressWarnings("unchecked")
public class GlobalMetadataFromSingleValueAnnotation implements GlobalMetadataAnnotationConverter {

    public void update(String[] propertyNames, Metadata metadata, Annotation annotation) {
        if (propertyNames.length != 1) throw new IllegalArgumentException("Cannot use " + this.getClass().getName() + " as a converter with multiple property names");
        Method[] methods = annotation.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals("value") && method.getParameterTypes().length == 0) {
                try {
                    Object value = method.invoke(annotation, (Object[]) null);
                    metadata.setAttribute(propertyNames[0], value);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
