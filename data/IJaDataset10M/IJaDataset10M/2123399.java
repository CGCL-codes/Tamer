package org.aigebi.rbac.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.aigebi.rbac.bean.UserBean;
import org.aigebi.rbac.hibernate.dao.AgbUser;
import org.aigebi.rbac.to.UserTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Ligong Xu
 * @version $Id: UserDelete.java 1 2007-09-22 18:10:03Z ligongx $
 */
public class UserDelete extends BaseUserAction {

    private static final long serialVersionUID = -2968792201369210363L;

    private static Log log = LogFactory.getLog(UserDelete.class);

    private String userId;

    public UserDelete() {
        super();
    }

    public String input() throws Exception {
        setUserId(retrieveUserIdForDelete());
        String input = super.input();
        UserTO user = getUserManager().getUserById(Long.valueOf(getUserId()));
        setUserBean(new UserBean(user));
        return input;
    }

    /**expect request parameter userId */
    private String retrieveUserIdForDelete() {
        String userId = getServletRequest().getParameter("userId");
        return userId;
    }

    public String delete() throws Exception {
        List<String> msgArgs = new ArrayList<String>();
        msgArgs.add(getUserBean().getUsername());
        try {
            getUserManager().deleteUser(getUserBean().getId());
            addSessionMessage(getText("user.delete.success", msgArgs));
            cleanupSearchCache();
            return SUCCESS;
        } catch (Throwable t) {
            log.error("error deleting user", t);
            addActionError(getText("user.delete.error", msgArgs));
            return INPUT;
        }
    }

    private void cleanupSearchCache() {
        if (getSessionProfile().getUserSearch() != null && getSessionProfile().getUserSearch().get("result") != null) {
            List<AgbUser> results = (List<AgbUser>) getSessionProfile().getUserSearch().get("result");
            if (results != null) {
                List newResults = new ArrayList();
                for (AgbUser user : results) {
                    if (!getUserBean().getUsername().equalsIgnoreCase(user.getUsername())) newResults.add(user);
                }
                HashMap search = (HashMap) getSessionProfile().getUserSearch().get("search");
                getSessionProfile().saveUserSearch(search, newResults);
            }
        }
    }

    /**user id for user to be updated  */
    public String getUserId() {
        return userId;
    }

    public void setUserId(String pUserId) {
        userId = pUserId;
    }
}
