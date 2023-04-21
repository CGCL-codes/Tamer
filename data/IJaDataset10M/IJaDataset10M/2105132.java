package org.nakedobjects.nof.reflect.java.annotations;

import java.lang.reflect.Method;
import org.nakedobjects.applib.annotation.MaxLength;
import org.nakedobjects.noa.annotations.Annotation;
import org.nakedobjects.noa.annotations.MaxLengthAnnotation;

public class MaxLengthAnnotationFactory extends AbstractAnnotationFactory {

    public MaxLengthAnnotationFactory() {
        super(MaxLengthAnnotation.class);
    }

    /**
     * In readiness for supporting <tt>@Value</tt> in the future.
     */
    public Annotation process(Class clazz) {
        MaxLength annotation = (MaxLength) clazz.getAnnotation(MaxLength.class);
        return create(annotation);
    }

    public Annotation process(final Method method) {
        MaxLength annotation = (MaxLength) method.getAnnotation(MaxLength.class);
        return create(annotation);
    }

    public Annotation processParams(Method method, int paramNum) {
        java.lang.annotation.Annotation[] parameterAnnotations = method.getParameterAnnotations()[paramNum];
        for (int j = 0; j < parameterAnnotations.length; j++) {
            if (parameterAnnotations[j] instanceof MaxLength) {
                return create((MaxLength) parameterAnnotations[j]);
            }
        }
        return null;
    }

    private MaxLengthAnnotation create(MaxLength annotation) {
        return annotation == null ? null : new MaxLengthAnnotation(annotation.value());
    }
}
