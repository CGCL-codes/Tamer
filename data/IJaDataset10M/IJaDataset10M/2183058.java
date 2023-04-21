package org.tomis.mvc.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.tomis.mvc.validation.annotation.Password;

/**
 * Project: tomis-mvc
 * @since 31.05.2010
 * @author Dan Persa
 */
public class EmailValidator implements ConstraintValidator<Password, String> {

    @Override
    public void initialize(Password constraintAnnotation) {
    }

    @Override
    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        if (object == null) return true;
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(?:[a-zA-Z]{2,6})$";
        boolean valid = false;
        if (object.getClass().toString().equals(String.class.toString())) {
            valid = ((String) object).matches(emailPattern);
        } else {
            valid = ((Object) object).toString().matches(emailPattern);
        }
        return valid;
    }
}
