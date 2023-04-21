package org.meruvian.yama.validator;

import org.meruvian.yama.form.PersonForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author vick
 *
 */
public class UserValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return PersonForm.class.equals(clazz);
    }

    public void validate(Object val, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "username", "username.empty");
        ValidationUtils.rejectIfEmpty(errors, "password", "password.empty");
    }
}
