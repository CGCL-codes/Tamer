package org.nextframework.core.config;

import java.lang.annotation.Annotation;
import org.nextframework.validation.PropertyValidator;
import org.nextframework.validation.ValidatorAnnotationExtractor;

/**
 * @author rogelgarcia
 * @since 22/01/2006
 * @version 1.1
 */
public interface ValidatorRegistry {

    PropertyValidator getPropertyValidator(Class<? extends Annotation> key);

    PropertyValidator getTypeValidator(Class<?> key);

    PropertyValidator getTypeValidator(String key);

    ValidatorAnnotationExtractor getExtractor();
}
