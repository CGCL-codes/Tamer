package com.em.validation.client.validators.max;

import javax.validation.ConstraintValidatorContext;

public class MaxDoubleValidator extends MaxValidator<Double> {

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) return true;
        double unwrappedValue = value.doubleValue();
        return unwrappedValue <= this.maxValue;
    }
}
