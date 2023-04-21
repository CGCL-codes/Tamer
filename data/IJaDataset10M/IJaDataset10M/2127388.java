package search.spring.validator;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import search.model.User;
import search.model.dao.UserDao;
import search.spring.command.UserCommand;

public class RegistrationValidator extends AccountValidator {

    private UserDao userDao;

    public boolean supports(Class clazz) {
        return clazz.equals(UserCommand.class);
    }

    public void validate(Object command, Errors errors) {
        super.validate(command, errors);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.account.password.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password2", "error.account.password2.empty");
        UserCommand userCommand = (UserCommand) command;
        if (StringUtils.hasText(userCommand.getUsername())) {
            User user = userDao.getUserByUsername(userCommand.getUsername());
            if (user != null && !user.getId().equals(userCommand.getId())) errors.rejectValue("username", "error.account.user.exists");
        }
        if (StringUtils.hasText(userCommand.getEmail())) {
            User user = userDao.getUserByEmail(userCommand.getEmail());
            if (user != null && !user.getId().equals(userCommand.getId())) errors.rejectValue("email", "error.account.email.exists");
        }
        log.debug(errors.getErrorCount() + " errors in registration validation.");
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
