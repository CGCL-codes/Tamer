package org.datascooter.test.example;

import org.datascooter.impl.Data;

public abstract class User extends Data {

    private static final long serialVersionUID = 2930691979288560392L;

    String login;

    String password;

    public User() {
    }

    public User(String login, String password) {
        super();
        this.login = login;
        this.password = password;
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
}
