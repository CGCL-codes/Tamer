package spring25.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Component
public final class BoundObject6Validator {

    private final transient Logger log = LoggerFactory.getLogger(getClass());

    public boolean validate(final BoundObject6 boundObject6, final Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "input1", "input1empty", "input1 is empty");
        if (boundObject6.getInput1().compareTo("something") != 0) {
            errors.rejectValue("input1", "input1NotSomething", "input1 must contain something");
        }
        return (!errors.hasErrors());
    }
}
