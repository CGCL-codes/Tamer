package com.em.validation.client.validators.decimalmin;

import javax.validation.ConstraintValidatorContext;

public class DecimalMinIntegerValidator extends DecimalMinValidator<Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) return true;
        int unwrappedValue = value.intValue();
        return unwrappedValue >= this.minValue;
    }
}
