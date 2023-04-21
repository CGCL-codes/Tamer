package com.spring.rssReader.validator;

import com.spring.rssReader.User;
import com.spring.rssReader.web.IUserController;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Ronald Haring
 * Date: 4-jan-2004
 * Time: 18:46:56
 * To change this template use Options | File Templates.
 */
public class UserValidator implements Validator {

    private IUserController userController;

    public UserValidator() {
    }

    public boolean supports(Class clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
        User user = (User) obj;
        if (!userController.isUniqueUsername(user)) {
            errors.rejectValue("username", "notUnique", null, "username is not unique.");
        }
    }

    public IUserController getUserController() {
        return userController;
    }

    public void setUserController(IUserController userController) {
        this.userController = userController;
    }
}
