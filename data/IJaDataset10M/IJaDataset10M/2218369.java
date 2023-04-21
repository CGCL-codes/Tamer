package edina.chalice.ws.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import edina.chalice.ws.PersistentURILookupCommand;

/**
 * Validator for {@link PersistentURILookupCommand}.
 * 
 * @author Brian O'Hare
 *
 */
public class PersistentURICommandValidator extends AbstractChaliceCommandValidator {

    private static final Logger logger = Logger.getLogger(PersistentURICommandValidator.class);

    private static final String IDENTIFIER = "id";

    private static final String INVALID_IDENTIFIER = "Invalid identifier - ";

    private static final String REQUIRED = "required";

    private String identifierValidationRegex;

    @Override
    public void validate(Object target, Errors errors) {
        PersistentURILookupCommand command = (PersistentURILookupCommand) target;
        String id = StringUtils.defaultString(command.getId());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, IDENTIFIER, REQUIRED);
        if (errors.hasErrors()) return;
        if (!(id.length() == 40)) {
            logger.warn(INVALID_IDENTIFIER + id);
            errors.reject(IDENTIFIER, id);
            return;
        }
        logger.info("Validating persistent id: " + id);
        Pattern pattern = Pattern.compile(getIdentifierValidationRegex());
        Matcher matcher = pattern.matcher(id);
        boolean found = false;
        while (matcher.find()) {
            found = true;
            break;
        }
        if (found) {
            logger.info("Persistent id: " + id + " is valid.");
        } else {
            logger.warn(INVALID_IDENTIFIER + id);
            errors.reject(IDENTIFIER, id);
        }
    }

    @Override
    public boolean supports(Class clazz) {
        return PersistentURILookupCommand.class.isAssignableFrom(clazz);
    }

    /**
	 * @return the identifierValidationRegex
	 */
    public String getIdentifierValidationRegex() {
        return identifierValidationRegex;
    }

    /**
	 * @param identifierValidationRegex the identifierValidationRegex to set
	 */
    public void setIdentifierValidationRegex(String identifierValidationRegex) {
        this.identifierValidationRegex = identifierValidationRegex;
    }
}
