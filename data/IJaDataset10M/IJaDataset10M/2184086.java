package com.em.validation.client.validators.max;

import java.math.BigInteger;
import javax.validation.ConstraintValidatorContext;

public class MaxBigIntegerValidator extends MaxValidator<BigInteger> {

    @Override
    public boolean isValid(BigInteger value, ConstraintValidatorContext context) {
        if (value == null) return true;
        Long minValueLong = Long.valueOf(this.maxValue);
        BigInteger minValue = BigInteger.valueOf(minValueLong);
        return minValue.compareTo(value) >= 0;
    }
}
