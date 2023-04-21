package logop.security;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import logop.UserHome;
import logop.model.User;

public class UserSession {

    private User user;

    private String loginName;

    private String loginPassword;

    private UserHome userHome;

    public boolean isLoggedIn() {
        return user != null;
    }

    public String logout() {
        user = null;
        return "login";
    }

    public String getUserLocale() {
        return "de";
    }

    public String actionLogin() {
        User userRet = userHome.getUserByLogin(loginName);
        if (userRet == null || !userRet.getPassword().equals(loginPassword)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "login not found or password wrong", ""));
            return null;
        } else {
            user = userRet;
            return "home";
        }
    }

    public User getUser() {
        user = userHome.getUserByLogin("stzel");
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public UserHome getUserHome() {
        return userHome;
    }

    public void setUserHome(UserHome userHome) {
        this.userHome = userHome;
    }
}
