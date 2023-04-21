package com.tcs.hrr.action;

import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import com.opensymphony.xwork2.ActionSupport;
import com.tcs.hrr.domain.User;
import com.tcs.hrr.service.UserManager;

public class UserAction extends ActionSupport implements SessionAware {

    private String oldpassword;

    private String newpassword;

    private Map<String, Object> session;

    private UserManager userManager;

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public void setSession(Map<String, Object> arg0) {
        this.session = arg0;
    }

    public String changePassword() throws Exception {
        System.out.println("oldpwd" + this.oldpassword);
        User user = (User) session.get("User");
        if (user.getPassword().equals(this.oldpassword)) {
            user.setPassword(this.newpassword);
            this.userManager.mergeUser(user);
        }
        return SUCCESS;
    }
}
