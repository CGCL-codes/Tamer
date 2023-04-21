package org.jowidgets.tools.validation;

import java.util.Collection;
import org.jowidgets.util.Assert;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

public class MandatoryValidator<VALIDATION_INPUT_TYPE> implements IValidator<VALIDATION_INPUT_TYPE> {

    private static final String MUST_NOT_BE_NULL = Messages.getString("MandatoryValidator.must_not_be_null");

    private final IValidationResult result;

    public MandatoryValidator() {
        this(MUST_NOT_BE_NULL);
    }

    public MandatoryValidator(final String messageText) {
        this(ValidationResult.error(messageText));
    }

    public MandatoryValidator(final IValidationResult errorResult) {
        Assert.paramNotNull(errorResult, "errorResult");
        this.result = errorResult;
    }

    @Override
    public IValidationResult validate(final VALIDATION_INPUT_TYPE validationInput) {
        if (validationInput == null) {
            return result;
        } else if (validationInput instanceof String && ((String) validationInput).trim().isEmpty()) {
            return result;
        } else if (validationInput instanceof Collection<?> && ((Collection<?>) validationInput).isEmpty()) {
            return result;
        }
        return ValidationResult.ok();
    }
}
