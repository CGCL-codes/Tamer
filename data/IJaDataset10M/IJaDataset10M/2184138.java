package com.boc.botv.web.validator;

import com.boc.botv.model.BetInstance;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author mathieu
 */
public class BetValidator implements Validator {

    public boolean supports(Class clazz) {
        return BetInstance.class.equals(clazz);
    }

    public void validate(Object obj, Errors errors) {
        BetInstance betinstance = (BetInstance) obj;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "betAmount", "field.required", "Required field");
        if (!errors.hasFieldErrors("betAmount")) {
            if (betinstance.getBetAmount() <= 0) errors.rejectValue("betAmount", "not_zero", "Can't be free!");
        }
    }
}
