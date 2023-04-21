package sample.sso;

import SSO.Auth;

public class AuthSimpleImpl implements Auth {

    public String getUserRoles(String user, String pass) throws Exception {
        if (!("admin".equals(user) && "admin".equals(pass))) throw new Exception();
        return "ADMIN";
    }
}
