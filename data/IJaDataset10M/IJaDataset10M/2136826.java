package com.gwtent.client.validate.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RegularValidator implements ConstraintValidator<Regular, Object> {

    private String regex;

    public void initialize(Regular r) {
        regex = r.regex();
    }

    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) return isValid("", regex); else return isValid(value.toString(), regex);
    }

    protected native boolean isValid(String s, String regex);

    public String getRegex() {
        return regex;
    }

    ;
}
