package nl.gridshore.samples.raffle.web.springmvc.validator;

import static nl.gridshore.samples.raffle.web.springmvc.validator.ValidatorConstants.REQUIRED;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by IntelliJ IDEA.
 * User: jettro
 * Date: Dec 23, 2007
 * Time: 8:19:37 AM
 * Validator class for validating Prize objects
 */
public class PrizeValidator implements Validator {

    public boolean supports(Class clazz) {
        return PrizeValidator.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "title", REQUIRED, REQUIRED);
        ValidationUtils.rejectIfEmpty(errors, "description", REQUIRED, REQUIRED);
    }
}
