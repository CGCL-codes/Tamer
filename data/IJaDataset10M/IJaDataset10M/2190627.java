package com.google.gwt.validation.client.constraints;

import javax.validation.ConstraintValidatorContext;

/**
 * <strong>EXPERIMENTAL</strong> and subject to change. Do not use this in
 * production code.
 * <p>
 * {@link javax.validation.constraints.Size} constraint validator implementation
 * for a array of {@code int}s.
 */
public class SizeValidatorForArrayOfInt extends AbstractSizeValidator<int[]> {

    public final boolean isValid(int[] value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return isLengthValid(value.length);
    }
}
