package org.telscenter.sail.webapp.presentation.validators.teacher;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.telscenter.sail.webapp.domain.impl.ChangeWorkgroupParameters;

/**
 * Validator for ChangeWorkgroupParamters object used to change workgroups
 * 
 * @author Sally Ahn
 * @version $Id: $
 */
public class ChangeWorkgroupParametersValidator implements Validator {

    /**
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
    @SuppressWarnings("unchecked")
    public boolean supports(Class clazz) {
        return ChangeWorkgroupParameters.class.isAssignableFrom(clazz);
    }

    /**
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
    public void validate(Object paramsIn, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "student", "error.no-student");
        if (errors.getErrorCount() != 0) {
            return;
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "offeringId", "error.no-student");
        if (errors.getErrorCount() != 0) {
            return;
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "periodId", "error.no-student");
        if (errors.getErrorCount() != 0) {
            return;
        }
        ChangeWorkgroupParameters params = (ChangeWorkgroupParameters) paramsIn;
        if (params.getWorkgroupFrom() != null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "workgroupFrom", "error.no-workgroupFrom");
            if (errors.getErrorCount() != 0) {
                return;
            }
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "workgroupToId", "error.no-workgroupTo");
        if (errors.getErrorCount() != 0) {
            return;
        }
    }
}
