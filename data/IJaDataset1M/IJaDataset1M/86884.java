package org.springframework.webflow.samples.birthdate;

import java.util.Calendar;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class BirthDateValidator implements Validator {

    public boolean supports(Class clazz) {
        return clazz.equals(BirthDate.class);
    }

    public void validate(Object obj, Errors errors) {
        BirthDate birthDate = (BirthDate) obj;
        validateBirthdateForm(birthDate, errors);
        validateCardForm(birthDate, errors);
    }

    public void validateBirthdateForm(BirthDate birthDate, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "noName", "Please specify your name.");
        ValidationUtils.rejectIfEmpty(errors, "date", "noDate", "Please specify your birth date.");
    }

    public void validateCardForm(BirthDate birthDate, Errors errors) {
        if (birthDate.isSendCard()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(birthDate.getDate());
            if (cal.get(Calendar.MONTH) == 11) {
                errors.reject("tooGoodForCards", "You're born in December--you're too good for a silly card!");
            } else {
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailAddress", "noEmail", "Please specify your email address.");
            }
        }
    }
}
