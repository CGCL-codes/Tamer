package goose.users;

import goose.BasicGooseAction;
import goose.model.User;
import goose.service.IUserManager;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

/**
 * 
 */
public class AddUserAction extends BasicGooseAction {

    private static final long serialVersionUID = -3243050552787643923L;

    private String login;

    private String password;

    private String passwordConfirmation;

    public String execute() throws Exception {
        if (isInvalid(getLogin())) return INPUT;
        if (isInvalid(getPassword())) return INPUT;
        if (isInvalid(getPasswordConfirmation())) return INPUT;
        if (!getPassword().equals(getPasswordConfirmation())) {
            addActionError(getText("password.confirmation.invalid"));
            return INPUT;
        }
        try {
            User user = new User(login, password);
            IUserManager userManager = gooseService.getUserManager();
            userManager.addUser(user, true);
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return INPUT;
    }

    private boolean isInvalid(String value) {
        return (value == null || value.length() == 0);
    }

    public String getLogin() {
        return login;
    }

    @RequiredStringValidator(message = "login is required", key = "login.required", shortCircuit = true, trim = true)
    @StringLengthFieldValidator(message = "login is invalid", key = "login.length.invalid", shortCircuit = true, trim = true, minLength = "5", maxLength = "12")
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    @RequiredStringValidator(message = "password is required", key = "password.required", shortCircuit = true, trim = true)
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    @RequiredStringValidator(message = "password confirmation is required", key = "password.confirmation.required", shortCircuit = true, trim = true)
    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}
