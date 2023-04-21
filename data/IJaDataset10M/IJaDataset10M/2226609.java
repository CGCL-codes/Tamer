package com.divrep.validator;

import java.io.Serializable;

public class DivRepEmailValidator implements DivRepIValidator<String> {

    private static DivRepEmailValidator singleton = new DivRepEmailValidator();

    public static DivRepEmailValidator getInstance() {
        return singleton;
    }

    public Boolean isValid(String value) {
        return org.apache.commons.validator.EmailValidator.getInstance().isValid(value);
    }

    public String getErrorMessage() {
        return "Please specify a valid email address.";
    }
}
