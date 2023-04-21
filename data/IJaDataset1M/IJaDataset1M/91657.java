package com.burry.action;

import java.util.Map;
import applelew.util.Debug;
import com.burry.services.UserService;

public class UserAction extends BaseAction {

    private String userName;

    private String password;

    private String inviteUser;

    private String oneL;

    private String twoL;

    private String thdL;

    private String forL;

    public String getOneL() {
        return oneL;
    }

    public void setOneL(String oneL) {
        this.oneL = oneL;
    }

    public String getTwoL() {
        return twoL;
    }

    public void setTwoL(String twoL) {
        this.twoL = twoL;
    }

    public String getThdL() {
        return thdL;
    }

    public void setThdL(String thdL) {
        this.thdL = thdL;
    }

    public String getForL() {
        return forL;
    }

    public void setForL(String forL) {
        this.forL = forL;
    }

    public UserService getUs() {
        return us;
    }

    public void setUs(UserService us) {
        this.us = us;
    }

    private UserService us = new UserService();

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInviteUser() {
        return inviteUser;
    }

    public void setInviteUser(String inviteUser) {
        this.inviteUser = inviteUser;
    }

    public String createUser() {
        if (userName == null || password == null) return INPUT;
        Map result = us.createUser(userName, password, inviteUser);
        Debug.logInfo("createUser:" + result);
        return SUCCESS;
    }

    public String login() {
        Map result = us.login(userName, password);
        this.getRequest().getSession().setAttribute("userInfo", result);
        Debug.logInfo("loginUser:" + result);
        if (result == null || result.isEmpty()) return INPUT;
        return SUCCESS;
    }

    public String setRate() {
        us.setRate(oneL, twoL, thdL, forL);
        return SUCCESS;
    }

    public String rateSettingInit() {
        Map result = us.getRate();
        this.oneL = result.get("one_set").toString();
        this.twoL = result.get("two_set").toString();
        this.thdL = result.get("trd_set").toString();
        this.forL = result.get("for_set").toString();
        return SUCCESS;
    }
}
