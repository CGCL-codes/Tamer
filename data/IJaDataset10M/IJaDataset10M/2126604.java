package validators;

import javax.faces.*;
import javax.faces.validator.*;
import javax.faces.application.*;
import javax.faces.component.*;
import javax.faces.context.*;
import java.util.regex.*;

public class NewUserValidator implements Validator {

    public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException {
        String userInput = (String) object;
        Pattern userPattern = Pattern.compile("/^[a-zA-Z0-9_]{3,16}$/");
        Matcher userMatcher = userPattern.matcher(userInput);
        boolean isUser = userMatcher.matches();
        if (!userMatcher.matches()) {
            throw new ValidatorException(new FacesMessage("Invalid username, username must be between" + " 3 and 16 characters, using only alphanumeric characters and underscores (_)."));
        }
    }
}
