package com.angel.common.providers.managers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import com.angel.annotation.managers.AnnotationManager;
import com.angel.common.helpers.AnnotationHelper;
import com.angel.common.helpers.ReflectionHelper;

/**
 * @author William
 *
 */
public class MethodExcludeValueAnnotationManager extends AnnotationManager {

    protected static final String METHOD_EXCLUDE_VALUE_ANNOTATION_ACTION = "METHOD_EXCLUDE_VALUE_ANNOTATION_ACTION";

    public Comparable<?> getExcludeValueFor(Object object) {
        return null;
    }

    @Override
    public Boolean canProcessAnnotationAt(Object object) {
        return AnnotationHelper.hasAnnotation(ReflectionHelper.getMethods(object), this.getClassAnnotation()) && AnnotationHelper.hasOnlyOneAnnotationInstance(ReflectionHelper.getMethods(object), this.getClassAnnotation());
    }

    @Override
    protected Annotation getAnnotationImpl(Object object) {
        Method method = null;
        if (this.canProcessAnnotationAt(object)) {
            method = ReflectionHelper.getAllMethodWith(this.getClassAnnotation(), object.getClass())[0];
        }
        return (method != null) ? method.getAnnotation(this.getClassAnnotation()) : null;
    }

    @Override
    public Class<? extends Annotation> getClassAnnotation() {
        return null;
    }
}
