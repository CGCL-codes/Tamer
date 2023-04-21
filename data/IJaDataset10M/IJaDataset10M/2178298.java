package org.commsuite.web.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.commsuite.web.beans.LanguageSelectionBean;

/**
 * @since 1.0
 * TODO: autor
 *
 */
public class PostalCodeValidator implements Validator {

    private static final String POSTAL_REGEX = "[0-9]{2}-[0-9]{3}";

    private static final String BUNDLE_NAME = "org.commsuite.web.locale.Locale";

    private static final String MESSAGE_POSTAL_ID = "MESSAGE_ERROR_POSTAL";

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        Pattern pattern = Pattern.compile(POSTAL_REGEX);
        Matcher matcher = pattern.matcher(value.toString());
        if (null != value && !("".equals(value.toString()))) {
            if (!matcher.matches()) {
                String message = LanguageSelectionBean.getDisplayString(BUNDLE_NAME, MESSAGE_POSTAL_ID, null, FacesContext.getCurrentInstance().getViewRoot().getLocale());
                throw new ValidatorException(new FacesMessage(message));
            }
        }
    }
}
