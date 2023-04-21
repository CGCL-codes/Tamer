package org.bims.bimswebaccess.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Class to provide a validator method to check if a expression matches with
 * an specific regular expresion.
 * @author Oscar Mora Perez
 */
public class RegExpValidator implements Validator {

    /**
     * Default constructor.
     */
    public RegExpValidator() {
    }

    /**
     * Validates an expresion, checking if it matches with a regular expression.
     * @param facesContext FacesContext instance to sent messages
     * @param uIComponent component instance which contains double
     * @param object double to validate
     * @throws javax.faces.validator.ValidatorException triggered if any
     * problem occurs
     */
    public final void validate(final FacesContext facesContext, final UIComponent uIComponent, final Object object) throws ValidatorException {
        String stringToValidate = (String) object;
        Pattern p = Pattern.compile(((String) (uIComponent.getAttributes().get("regExp"))));
        Matcher m = p.matcher(stringToValidate);
        boolean matchFound = m.matches();
        if (!matchFound) {
            FacesMessage message = new FacesMessage();
            message.setSummary("Regular expression (" + p.toString() + ") does not match with the content");
            message.setSeverity(FacesMessage.SEVERITY_FATAL);
            throw new ValidatorException(message);
        }
    }
}
