package com.ariessoftpro.events.web;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class SaveEventValidator implements Validator {

    @SuppressWarnings("unchecked")
    @Override
    public boolean supports(Class clazz) {
        return SaveEventCommand.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SaveEventCommand saveEventCommand = (SaveEventCommand) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "events.field.title.error");
        if (saveEventCommand.getDay().equals("") || saveEventCommand.getMonth().equals("") || saveEventCommand.getYear().equals("")) {
            errors.rejectValue("date", "events.field.date.error");
        }
    }
}
