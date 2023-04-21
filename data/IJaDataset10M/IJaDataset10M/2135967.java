package com;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailValidator implements Validator {

    public void validate(FacesContext arg0, UIComponent arg1, Object mail) throws ValidatorException {
        String email = (String) mail;
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(email);
        boolean matchFound = m.matches();
        if (!matchFound) {
            FacesMessage message = new FacesMessage("email is not valid");
            throw new ValidatorException(message);
        }
    }
}
