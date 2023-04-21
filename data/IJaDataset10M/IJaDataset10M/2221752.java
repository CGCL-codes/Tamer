package org.tomis.mvc.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.tomis.mvc.validation.annotation.NoSpaces;

/**
 * Project: tomis-mvc
 * @since 10.09.2010
 * @author Dan Persa
 */
public class NoSpacesValidator implements ConstraintValidator<NoSpaces, String> {

    @Override
    public void initialize(NoSpaces constraintAnnotation) {
    }

    @Override
    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        if (object == null) {
            return true;
        }
        String noSpacesPattern = "^[\\S]*$";
        boolean valid = false;
        if (object.getClass().toString().equals(String.class.toString())) {
            valid = ((String) object).matches(noSpacesPattern);
        } else {
            valid = ((Object) object).toString().matches(noSpacesPattern);
        }
        return valid;
    }
}
