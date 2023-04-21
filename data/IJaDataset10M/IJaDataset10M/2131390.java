package com.google.code.openperfmon.web;

import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Anonymous
public class LoginAction extends ActionSupport {

    private String login;

    private String password;

    private boolean rememberMe;

    @Override
    public String execute() throws Exception {
        login = "";
        password = "";
        rememberMe = true;
        return SUCCESS;
    }

    public String auth() throws Exception {
        if (UserSession.authenticate(login, password, rememberMe)) {
            return SUCCESS;
        } else {
            addActionError("Invalid username or password");
            return "doLogin";
        }
    }

    public String logout() throws Exception {
        UserSession.logout();
        return SUCCESS;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
