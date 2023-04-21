package ndsapp.service;

import ndsapp.domain.Rule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RuleValidator implements Validator {

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    @SuppressWarnings("unchecked")
    public boolean supports(Class clazz) {
        return Rule.class.equals(clazz);
    }

    public void validate(Object obj, Errors errors) {
    }
}
