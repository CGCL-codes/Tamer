package ru.scriptum.view.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class PasswordValidator implements Validator {

    String otherPassword = null;

    public void validate(FacesContext facesContext, UIComponent component, Object value) throws ValidatorException {
        if (otherPassword == null) {
            int i = 1;
        }
    }

    public String getOtherPassword() {
        return otherPassword;
    }

    public void setOtherPassword(String otherPassword) {
        this.otherPassword = otherPassword;
    }
}
