package org.aigebi.rbac.action;

import java.util.List;
import org.aigebi.rbac.admin.UserManager;
import org.aigebi.rbac.common.SessionProfile;
import org.aigebi.rbac.to.User;
import org.aigebi.rbac.bean.UserBean;

/**
 * user CRUD.
 * @author Ligong Xu
 * @version $Id: BaseUserAction.java 1 2007-09-22 18:10:03Z ligongx $
 */
public class BaseUserAction extends CrudActionSupport {

    private static final long serialVersionUID = 1887996775080001282L;

    private String userId;

    private UserBean mUserBean = new UserBean(new User());

    public BaseUserAction() {
    }

    public UserBean getUserBean() {
        return mUserBean;
    }

    public void setUserBean(UserBean pUserBean) {
        mUserBean = pUserBean;
    }

    UserManager mUserManager;

    public UserManager getUserManager() {
        return mUserManager;
    }

    public void setUserManager(UserManager pUserManager) {
        mUserManager = pUserManager;
    }

    /** clear mUserBean */
    protected void clear() {
        setUserBean(new UserBean(new User()));
    }

    /** update page menu info */
    public String input() throws Exception {
        setActiveMenu(SessionProfile.MENU_UserAdmin);
        return super.input();
    }

    /**expect request parameter userId */
    protected String retrieveUserIdForUpdate() {
        String userId = getServletRequest().getParameter("userId");
        return userId;
    }

    String presetState = "P";

    public String getPresetState() {
        return presetState;
    }

    public void setPresetState(String pPresetState) {
        presetState = pPresetState;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String pUserId) {
        userId = pUserId;
    }
}
