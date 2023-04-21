package com.angel.annotation.finders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import com.angel.annotation.exceptions.AnnotationManagerException;
import com.angel.annotation.interfaces.AnnotationObjectFinder;
import com.angel.common.helpers.ReflectionHelper;

/**
 *
 * @author William
 *
 */
public class AnnotationMethodFinder implements AnnotationObjectFinder {

    private static AnnotationObjectFinder instance;

    private AnnotationMethodFinder() {
        super();
    }

    public static synchronized AnnotationObjectFinder getInstance() {
        if (instance == null) {
            instance = new AnnotationMethodFinder();
        }
        return instance;
    }

    public <T extends Annotation> Annotation findAnnotationAt(Object object, Class<T> annotationClass) {
        Annotation annotationFound = null;
        Annotation annotation = null;
        int quantity = 0;
        Method[] methods = ReflectionHelper.getMethods(object);
        if (methods != null && methods.length > 0) {
            for (Method method : methods) {
                annotation = method.getAnnotation(annotationClass);
                if (annotation != null) {
                    annotationFound = annotation;
                    quantity++;
                }
            }
        }
        if (quantity > 1) {
            throw new AnnotationManagerException("Object has more than one annotation implementation for [" + annotationClass.getCanonicalName() + "].");
        }
        return annotationFound;
    }

    public <T extends Annotation> Object findTargetObjectFor(Object object, Class<T> annotationClass) {
        Annotation annotation = this.findAnnotationAt(object, annotationClass);
        if (annotation != null) {
            Method[] methods = ReflectionHelper.getMethods(object);
            if (methods != null && methods.length > 0) {
                for (Method method : methods) {
                    annotation = method.getAnnotation(annotationClass);
                    if (annotation != null) {
                        return method;
                    }
                }
            }
        }
        return null;
    }
}
