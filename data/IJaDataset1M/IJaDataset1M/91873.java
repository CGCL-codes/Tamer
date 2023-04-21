package it.hotel.controller.user;

import it.hotel.model.user.User;
import it.hotel.model.user.manager.IUserManager;
import it.hotel.system.SystemConstants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * 
 *
 */
public class UserAuthenticatorController extends SimpleFormController {

    private IUserManager userManager;

    /**
	 * @throws
	 * @return
	 */
    @Override
    protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        User user = (User) command;
        user = userManager.getUser(user.getUserName(), user.getPassword());
        if (user != null && !"guest".equals(user.getRole().getName())) {
            ((IUserContainer) getApplicationContext().getBean(SystemConstants.USER_CONTAINER)).setUser(user);
            return new ModelAndView(getSuccessView());
        }
        return new ModelAndView(getFormView());
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }
}
