package de.domainframework.common.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OnlyAllowedCharacterValidator implements ConstraintValidator<OnlyAllowedCharacter, String> {

    @Override
    public void initialize(OnlyAllowedCharacter arg0) {
    }

    @Override
    public boolean isValid(String object, ConstraintValidatorContext context) {
        if (object == null) {
            return true;
        }
        if (object.contains("#")) {
            return false;
        }
        return true;
    }
}
