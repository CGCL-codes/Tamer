package org.gwtoolbox.bean.client.validation.validator.jsr;

import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Max;

/**
 * @author Uri Boness
 */
public class MaxConstraintValidator extends AbstractConstraintValidator<Max, Number> {

    public boolean isValid(Number value, ConstraintValidatorContext context) {
        return value == null || value.longValue() <= attributes.getLong("value");
    }
}
