package com.pureperfect.purview;

import java.lang.annotation.Annotation;

/**
 * Interface for implementing validators using annotations.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public interface Validator<P extends ValidationProblem, I extends Object, A extends Annotation, T extends Object, V extends Object> {

    /**
	 * Run validation.
	 * 
	 * @param instance
	 *            the object instance that owns the value
	 * @param annotation
	 *            the validation annotation
	 * @param target
	 *            the thing that the annotation was attached to.
	 * @param value
	 *            the value to validate
	 * @return a {@link ValidationProblem ValidationProblem} if the value is
	 *         invalid or null if the value is valid.
	 */
    public P validate(I instance, A annotation, T target, V value);
}
