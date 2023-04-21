package vlan.webgame.manage;

import vlan.webgame.manage.entity.Admin;
import vlan.webgame.manage.entity.AdminLogin;

public class LoginInfo {

    private Admin admin;

    private AdminLogin login;

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public AdminLogin getLogin() {
        return login;
    }

    public void setLogin(AdminLogin login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "LoginInfo [admin=" + admin + ", login=" + login + "]";
    }
}
