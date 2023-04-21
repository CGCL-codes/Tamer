package org.ourgrid.common.statistics.beans.pair;

import org.ourgrid.common.statistics.beans.aggregator.AG_Job;
import org.ourgrid.common.statistics.beans.aggregator.AG_Login;
import org.ourgrid.common.statistics.beans.aggregator.AG_User;
import org.ourgrid.common.statistics.beans.peer.Login;
import org.ourgrid.common.statistics.beans.peer.User;
import org.ourgrid.peer.status.util.Toolkit;

public class LoginPair implements AGPair {

    private final Login login;

    private final AG_Login loginAg;

    public LoginPair(Login login, AG_Login loginAg) {
        this.login = login;
        this.loginAg = loginAg;
    }

    public void addAGChildren(Object children) {
        loginAg.getJobs().add((AG_Job) children);
    }

    public UserPair createParentPair() {
        return new UserPair(getParent(), Toolkit.convertUser(getParent()));
    }

    public AG_Login getAGObject() {
        return loginAg;
    }

    public Login getObject() {
        return login;
    }

    public User getParent() {
        return login.getUser();
    }

    public void setAGParent(Object parent) {
        loginAg.setUser((AG_User) parent);
    }
}
