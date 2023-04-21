package iConfWeb.security;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SecurityBean implements ISecurityBean {

    private static String ADMIN = "admin";

    private static String CHAIRMAN = "chairman";

    private static String PCMEMBER = "pcmember";

    private static String REFEREE = "referee";

    private static String AUTHOR = "author";

    private static String CONFID = "confId";

    private static String INFOSUSER = "infosUser";

    private static String USERID = "userId";

    private static String ROLE = "role";

    private static String YES = "yes";

    private static String ERROR_MESSAGE = "errorMessage";

    private static String CONF_NOT_CHOOSEN = "You have to select a conference";

    private static String NOT_LOGGUED_MESSAGE = "You have to be loggued to access the required page";

    private static String NO_ENOUGH_PRIVILEGES = "You have no enough privileges to access the required page";

    public SecurityBean() {
        super();
    }

    public boolean isAuthor(HttpServletRequest request) {
        return this.checkRole(AUTHOR, request);
    }

    public boolean isAdmin(HttpServletRequest request) {
        return this.checkRole(ADMIN, request);
    }

    public boolean isReferee(HttpServletRequest request) {
        return this.checkRole(REFEREE, request);
    }

    public boolean isChairman(HttpServletRequest request) {
        return this.checkRole(CHAIRMAN, request);
    }

    public boolean isPcMember(HttpServletRequest request) {
        return this.checkRole(PCMEMBER, request);
    }

    public boolean isLoggued(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute(USERID);
        if (userId == null) return false;
        return true;
    }

    public boolean isConfChoosen(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long confId = (Long) session.getAttribute(CONFID);
        if (confId == null) return false;
        return true;
    }

    private boolean checkRole(String roleToCheck, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, String> infosUser = (Map<String, String>) session.getAttribute(INFOSUSER);
        if (infosUser != null) {
            if (roleToCheck.equals(AUTHOR)) {
                String author = infosUser.get(AUTHOR);
                if (author != null) {
                    return author.equals(YES);
                }
            }
            String role = infosUser.get(ROLE);
            if (role != null) {
                return role.equals(roleToCheck);
            }
        }
        return false;
    }

    public String getBadRoleView() {
        return "UserMainPage.htm";
    }

    public String getNotLogguedView() {
        return "Login.htm";
    }

    public String getConfNotChoosenView() {
        return "ChooseConference.htm";
    }

    public void setNotLogguedMessage(HttpServletRequest request) {
        setMessage(request, NOT_LOGGUED_MESSAGE);
    }

    public void setNotEnoughPrivilegesMessage(HttpServletRequest request) {
        setMessage(request, NO_ENOUGH_PRIVILEGES);
    }

    public void setConfNotChoosenMessage(HttpServletRequest request) {
        setMessage(request, CONF_NOT_CHOOSEN);
    }

    private void setMessage(HttpServletRequest request, String message) {
        request.getSession().setAttribute(ERROR_MESSAGE, message);
    }
}
