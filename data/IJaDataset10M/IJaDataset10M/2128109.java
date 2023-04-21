package org.monet.backmobile.service.results;

import org.monet.backmobile.model.UserInfo;
import org.monet.backmobile.service.Result;

public class LoginResult extends Result {

    private UserInfo userInfo;

    public LoginResult() {
    }

    public LoginResult(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
