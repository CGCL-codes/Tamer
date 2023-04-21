package com.google.code.sapien.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.code.sapien.interceptor.UserAware;
import com.google.code.sapien.model.User;
import com.google.code.sapien.service.UserService;
import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

/**
 * Registration Action, controls the creation of new user registrations.
 * @author Adam
 * @version $Id: RegisterAction.java 25 2009-05-26 04:23:01Z a.ruggles $
 * 
 * Created on Feb 8, 2009 at 6:30:58 PM 
 */
public class RegisterAction extends ActionSupport implements UserAware {

    /**
     * The <code>Logger</code> is used by the application to generate a log messages.
     */
    private static final Logger LOG = LoggerFactory.getLogger(RegisterAction.class);

    /**
	 * Serial Version UID.
	 */
    private static final long serialVersionUID = -6388358536078189124L;

    /**
	 * The logged in user.
	 */
    private User currentUser;

    /**
	 * Properties used to register a new user account.
	 */
    private String username, email, password, password2;

    /**
	 * The user service.
	 */
    protected final UserService userService;

    /**
	 * Constructs a registration action.
	 * @param userService The user service.
	 */
    @Inject
    public RegisterAction(final UserService userService) {
        super();
        this.userService = userService;
    }

    /**
	 * {@inheritDoc}
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
    @Override
    @Action(value = "register", results = { @Result(name = "input", location = "registration.jsp") })
    public String execute() {
        final User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setActive(Boolean.TRUE);
        user.setPassword(password);
        if (userService.getByUsername(username) != null) {
            addFieldError("username", getText("register.username-in-use", "The username is already registered to another user."));
        }
        if (userService.getByEmail(email) != null) {
            addFieldError("email", getText("register.email-in-use", "The email address is already registered to another user."));
        }
        if (getFieldErrors().size() > 0) {
            return INPUT;
        }
        userService.add(user, currentUser);
        LOG.debug("New user has been registered successfully: {}", user);
        return SUCCESS;
    }

    /**
	 * Returns email.
	 * @return the email.
	 */
    @RequiredStringValidator(message = "Required Field.", key = "validation.required")
    @EmailValidator(message = "Enter a valid email.", key = "validation.email")
    public String getEmail() {
        return email;
    }

    /**
	 * Returns password.
	 * @return the password.
	 */
    @RequiredStringValidator(message = "Required Field.", key = "validation.required")
    @StringLengthFieldValidator(trim = true, minLength = "3", maxLength = "16", message = "Must be between ${minLength} and ${maxLength} characters.", key = "validation.length")
    public String getPassword() {
        return password;
    }

    /**
	 * Returns password2.
	 * @return the password2.
	 */
    @FieldExpressionValidator(expression = "password2.equals(password)", message = "Passwords do not match.", key = "validation.match.passwords")
    @RequiredStringValidator(message = "Required Field.", key = "validation.required")
    public String getPassword2() {
        return password2;
    }

    /**
	 * Returns username.
	 * @return the username.
	 */
    @RequiredStringValidator(message = "Required Field.", key = "validation.required")
    @StringLengthFieldValidator(trim = true, minLength = "3", maxLength = "16", message = "Must be between ${minLength} and ${maxLength} characters.", key = "validation.length")
    public String getUsername() {
        return username;
    }

    /**
	 * Prepares the registration page.
	 * @return SUCCESS.
	 */
    @Action(value = "registration", results = { @Result(name = "success", location = "registration.jsp") })
    @SkipValidation
    public String registration() {
        return SUCCESS;
    }

    /**
	 * Sets email.
	 * @param email the email to set.
	 */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
	 * {@inheritDoc}
	 * @see com.google.code.sapien.interceptor.UserAware#setCurrentUser(User)
	 */
    public void setCurrentUser(final User currentUser) {
        this.currentUser = currentUser;
    }

    /**
	 * Sets password.
	 * @param password the password to set.
	 */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
	 * Sets password2.
	 * @param password2 the password2 to set.
	 */
    public void setPassword2(final String password2) {
        this.password2 = password2;
    }

    /**
	 * Sets username.
	 * @param username the username to set.
	 */
    public void setUsername(final String username) {
        this.username = username;
    }
}
