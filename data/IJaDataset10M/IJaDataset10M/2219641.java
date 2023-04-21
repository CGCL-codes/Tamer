package com.googlecode.beauti4j.core.web.gwt.client.data.validate;

import com.googlecode.beauti4j.gwt.databinding.client.Validator;

public class ValidateWhenValidator extends BaseValidator {

    public interface WhenCondition {

        public boolean evaluate();
    }

    private WhenCondition whenCondition;

    private Validator validator;

    public ValidateWhenValidator(String propertyLabel, WhenCondition whenCondition, Validator validator) {
        super(propertyLabel);
        this.whenCondition = whenCondition;
        this.validator = validator;
    }

    public String validate(Object value) {
        clearError();
        if (whenCondition != null && validator != null && whenCondition.evaluate()) {
            setError(validator.validate(value));
        }
        return getError();
    }
}
