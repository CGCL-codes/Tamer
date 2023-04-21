package com.google.inject.internal;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation utilities.
 *
 * @author crazybob@google.com (Bob Lee)
 */
public class Annotations {

    /**
   * Returns true if the given annotation is retained at runtime.
   */
    public static boolean isRetainedAtRuntime(Class<? extends Annotation> annotationType) {
        Retention retention = annotationType.getAnnotation(Retention.class);
        return !(retention == null || retention.value() != RetentionPolicy.RUNTIME);
    }
}
