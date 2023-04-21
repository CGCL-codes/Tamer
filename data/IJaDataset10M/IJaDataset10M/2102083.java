package com.innovatio.validator.handlers;

import org.apache.log4j.Logger;
import com.innovatio.validator.annotations.ValidateCreditCard;
import com.innovatio.validator.Validable;
import com.innovatio.validator.exceptions.ValidatorException;
import com.innovatio.validator.ValidationErrors;
import com.innovatio.utils.general.CommonsUtils;

/**
 * This code is property of Innovatio Software Solutions, Inc.
 * Project PaySmart
 * User: Ramon
 * Date: Mar 4, 2009
 * Time: 11:01:47 AM
 */
public class ValidateCreditCardHandler implements ValidationHandler<ValidateCreditCard> {

    private static Logger logger = Logger.getLogger(ValidateCreditCardHandler.class);

    public ValidationErrors.Error validate(ValidateCreditCard annotation, Validable container, String fieldName, String value) throws ValidatorException {
        if (!CommonsUtils.isStringEmpty(value)) {
        }
        return null;
    }
}
