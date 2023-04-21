package com.acv.webapp.validator;

import com.acv.webapp.common.BaseValidator;
import com.acv.webapp.util.PhoneNumber;
import com.opensymphony.xwork2.validator.ValidationException;

/**
 * PhoneNumberRequiredValidator to validate if the phoneNumber exists.
 *
 * @author John
 * @modified by Minxia Han
 * @version     %I%, %G%
 * @since       1.0
 */
public class PhoneNumberRequiredValidator extends BaseValidator {

    /**
	 * Validation for phone number required
	 *
	 * The getFieldName() method returns the field name declared in the validation file.
	 *
	 */
    public void validate(Object object) throws ValidationException {
        try {
            PhoneNumber phone = (PhoneNumber) getFieldValue(getFieldName(), object);
            if (phone == null || !phone.phoneExist()) {
                String fieldName = getFieldName() + "." + PhoneNumber.PRIMARY_FIELD;
                addFieldError(fieldName, object);
            }
        } catch (ValidationException e) {
            throw e;
        }
    }
}
