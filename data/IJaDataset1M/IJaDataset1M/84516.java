package org.jazzteam.shareideas.webapp.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 * 
 * 
 * @author ADLeR
 * @version $Rev: $
 */
public class RegisterForm extends ActionForm {

    private String login;

    private String email;

    private String password;

    private String confirm;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (login == null || login.trim().length() <= 3) {
            errors.add("login", new ActionMessage("Login can not be less than 3 characters."));
        }
        if (email == null || email.trim().length() <= 3) {
            errors.add("email", new ActionMessage("Email can not be less than 3 characters."));
        }
        if (password == null || password.trim().length() <= 3) {
            errors.add("password", new ActionMessage("Password can not be less than 3 characters."));
        }
        if (confirm == null || confirm.trim().length() <= 3) {
            errors.add("confirm", new ActionMessage("Password confirmation can not be empty."));
        }
        if (confirm != null && password != null) {
            if (!confirm.equals(password)) {
                errors.add("equals", new ActionMessage("Password and confirmation must be equals."));
            }
        }
        return errors;
    }
}
