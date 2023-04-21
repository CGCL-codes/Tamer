package lif.webclient.validator;

import lif.core.util.SystemUtil;
import lif.webclient.service.LifUserService;
import lif.webclient.view.UserBean;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by IntelliJ IDEA.
 * User: gavin
 * Date: Apr 21, 2008
 * Time: 9:04:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChangeDetailsValidator implements Validator {

    private LifUserService lifUserService;

    public boolean supports(Class clazz) {
        return clazz.equals(UserBean.class);
    }

    public void validate(Object object, Errors errors) {
        UserBean user = (UserBean) object;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contactName1", "ccuser.contact1.required", "Contact Name 1 is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "ccuser.password.required", "Password is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "ccuser.confirmPassword.required", "Confirm Password is required");
        if (user.getEmail() != null && !user.getEmail().trim().equals("")) {
            if (!SystemUtil.isValideEmail(user.getEmail())) {
                errors.rejectValue("email", "ccuser.email.invalid", "Format of the email address is invalid");
            }
        }
        if (user.getConfirmPassword() != null && user.getConfirmPassword().length() > 0) {
            if (!user.getPassword().equals(user.getConfirmPassword())) {
                errors.rejectValue("password", "ccuser.password.requiredValidation", "Password and Confirm Password should be same");
            }
        }
        if (errors.getErrorCount() > 0) {
            user.setPassword("");
            user.setConfirmPassword("");
        }
    }

    public LifUserService getLifUserService() {
        return lifUserService;
    }

    public void setLifUserService(LifUserService lifUserService) {
        this.lifUserService = lifUserService;
    }
}
