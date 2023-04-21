package org.gwtoolbox.bean.client.validation.validator.jsr;

import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;

/**
 * @author Uri Boness
 */
public class NotNullConstraintValidator extends AbstractConstraintValidator<NotNull, Object> {

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return value != null;
    }
}
