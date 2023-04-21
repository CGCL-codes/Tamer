package ru.scriptum.view.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.validator.ValidatorException;
import ru.scriptum.view.util.FacesUtils;

public abstract class ValidationExceptionCreator extends StringValidator {

    public static void createValidationException(String messageId, UIComponent component) {
        FacesMessage message = FacesUtils.getMessage(messageId);
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        ((UIInput) component).setValid(false);
        throw new ValidatorException(message);
    }
}
