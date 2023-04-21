package com.otatop.dvdLibrary.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.otatop.dvdLibrary.data.User;
import com.otatop.dvdLibrary.data.UserDataManager;

public class StrutsSecurityInterceptor extends AbstractInterceptor {

    private static String LOGIN_ATTEMPT = "login_attempt";

    private static String USERNAME = "username";

    private static String PASSWORD = "password";

    private UserDataManager userDataManager;

    private static boolean checkForFirstInstall = true;

    public void setUserDataManager(UserDataManager userDataManager) {
        this.userDataManager = userDataManager;
    }

    public String intercept(ActionInvocation invocation) throws Exception {
        if (checkForFirstInstall && userDataManager.getUserCount() == 0) {
            User superAdmin = new User();
            superAdmin.setUsername("admin");
            superAdmin.setPassword("admin");
            userDataManager.createUser(superAdmin);
        }
        final ActionContext context = invocation.getInvocationContext();
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession(true);
        User user = SecurityContext.getCurrentUser();
        if (user == null) {
            if (userDataManager.getUserCount() == 1) {
                User currentUser = userDataManager.getUser("admin", "admin");
                if (currentUser != null) {
                    SecurityContext.setCurrentUser(currentUser);
                    return "login-success";
                }
            } else {
                String loginAttempt = request.getParameter(LOGIN_ATTEMPT);
                if (loginAttempt != null && !loginAttempt.equals("")) {
                    if (processLoginAttempt(request, session)) {
                        return "login-success";
                    } else {
                        Object action = invocation.getAction();
                        if (action instanceof ValidationAware) {
                            ((ValidationAware) action).addActionError("Username or password incorrect.");
                        }
                    }
                }
            }
            return "login";
        } else {
            return invocation.invoke();
        }
    }

    /**
	 * Attempt to process the user's login attempt delegating the work to the 
	 * SecurityManager.
	 */
    public boolean processLoginAttempt(HttpServletRequest request, HttpSession session) {
        String username = request.getParameter(USERNAME);
        String password = request.getParameter(PASSWORD);
        User currentUser = userDataManager.getUser(username, password);
        if (currentUser != null) {
            SecurityContext.setCurrentUser(currentUser);
            return true;
        } else {
            return false;
        }
    }
}
