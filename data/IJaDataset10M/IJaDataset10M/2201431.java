package com.innovatio.validator.handlers;

import com.innovatio.utils.validations.InputValidationUtils;
import com.innovatio.validator.Validable;
import com.innovatio.validator.ValidationErrors;
import com.innovatio.validator.annotations.ValidateNumber;
import com.innovatio.validator.exceptions.ValidatorException;
import org.apache.commons.lang.math.NumberUtils;

/**
 * This code is property of Innovatio Software Solutions, Inc.
 * User: Edxe
 * Date: Jun 23, 2009
 * Time: 12:19:34 AM
 */
public class ValidateNumberHandler implements ValidationHandler<ValidateNumber> {

    public ValidationErrors.Error validate(ValidateNumber annotation, Validable container, String fieldName, String value) throws ValidatorException {
        if (!InputValidationUtils.isValidStringField(value, annotation.minLen(), annotation.maxLen()) || !NumberUtils.isNumber(value)) {
            return new ValidationErrors.Error(fieldName, annotation.i18nBundleKey());
        }
        return null;
    }
}
