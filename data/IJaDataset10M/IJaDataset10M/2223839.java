package com.sax.michael.annotations.forms;

public class LoginForm {

    private String j_username;

    private String j_password;

    private String loginFailed;

    public String getJ_username() {
        return j_username;
    }

    public void setJ_username(String jUsername) {
        j_username = jUsername;
    }

    public String getJ_password() {
        return j_password;
    }

    public void setJ_password(String jPassword) {
        j_password = jPassword;
    }

    public String getLoginFailed() {
        return loginFailed;
    }

    public void setLoginFailed(String loginFailed) {
        this.loginFailed = loginFailed;
    }
}
