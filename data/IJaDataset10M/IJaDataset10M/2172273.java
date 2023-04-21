package net.woodstock.rockapi.validation.validators;

import net.woodstock.rockapi.validation.ObjectValidator;
import net.woodstock.rockapi.validation.ValidationContext;
import net.woodstock.rockapi.validation.ValidationException;
import net.woodstock.rockapi.validation.ValidationResult;
import net.woodstock.rockapi.validation.annotations.ValidateDoubleRange;

public class ValidatorDoubleRange extends AbstractObjectValidator {

    public ValidationResult validate(ValidationContext context) throws ValidationException {
        try {
            Double value = (Double) context.getValue();
            ValidateDoubleRange annotation = (ValidateDoubleRange) context.getAnnotation();
            if (value == null) {
                return context.getSuccessResult();
            }
            double doubleValue = value.doubleValue();
            if ((doubleValue < annotation.min()) || (doubleValue > annotation.max())) {
                return context.getErrorResult(this.getErrorMessage(annotation, context.getCanonicalName()));
            }
            return context.getSuccessResult();
        } catch (Exception e) {
            this.getLogger().info(e.getMessage(), e);
            throw new ValidationException(e);
        }
    }

    private String getErrorMessage(ValidateDoubleRange annotation, String name) throws ValidationException {
        return this.getMessage(ObjectValidator.MESSAGE_FIELD_ERROR_RANGE, name, new Double(annotation.min()), new Double(annotation.max()));
    }
}
