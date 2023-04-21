package iConfWeb.web;

import iConfWeb.bean.ManageUsersBean;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;
import business.domain.actors.User;
import business.manager.interfaces.UserManager;

/**
 * Controller used with the views related to the management of users by the super admin.
 * This class extends SimpleController.
 * @author St�phane
 *
 */
public class ManageUsersController extends SimpleFormController {

    /**
	 * The userManager used to perform actions on users.
	 */
    private UserManager userManager;

    /**
	 * The ManageUsersBean used with the view.
	 */
    private ManageUsersBean mub;

    /**
	 * Logger.
	 */
    protected final Log log = LogFactory.getLog(getClass());

    /**
	 * Constructor.
	 */
    public ManageUsersController() {
    }

    /**
	 * Getter.
	 * @return userManager
	 */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
	 * Setter.
	 * @param userManager
	 */
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        log.info("onSubmit delete user");
        ManageUsersBean mub = (ManageUsersBean) command;
        if (mub.getActionType().equals("delete")) {
            request.getSession().setAttribute("mub", mub);
            return new ModelAndView(new RedirectView("ManageUsersValidation.htm"));
        } else {
            return new ModelAndView(new RedirectView("SuperAdminMainPage.htm"));
        }
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        request.getSession().setAttribute("errorMessage", null);
        mub = (ManageUsersBean) request.getSession().getAttribute("mub");
        if (mub == null) {
            initializeListOfUsers(request);
        } else if (!mub.getActionType().equals("back")) {
            initializeListOfUsers(request);
        }
        return mub;
    }

    private void initializeListOfUsers(HttpServletRequest request) {
        String superAdminLogin = (String) request.getSession().getAttribute("userId");
        List<User> users = userManager.getAllRegisteredUsers();
        HashMap<String, String> usersNames = new HashMap<String, String>();
        String surname = null;
        String firstname = null;
        String userName = null;
        String login = null;
        for (User user : users) {
            surname = user.getSurname();
            firstname = user.getFirstname();
            userName = surname + " " + firstname;
            login = user.getLogin();
            if (!login.equals(superAdminLogin)) {
                usersNames.put(userName, login);
            }
        }
        request.getSession().setAttribute("usersNames", usersNames);
        log.info("list of users added in the context");
        mub = new ManageUsersBean();
        mub.setUserManager(userManager);
    }
}
