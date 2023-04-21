package ru.autofan.form.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.autofan.form.LoginForm;
import ru.autofan.logic.UserService;

public class LoginFormValidator implements Validator {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public boolean supports(Class clazz) {
        return clazz.equals(LoginForm.class);
    }

    public void validate(Object target, Errors errors) {
        LoginForm form = (LoginForm) target;
        if (!this.userService.isUserExists(form.getLogin(), form.getPassword())) {
            errors.rejectValue("login", "label.invalidEmailOrPassword");
        }
    }
}
