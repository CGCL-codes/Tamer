package de.creatronix.artist3k.controller.form;

import org.apache.struts.action.ActionForm;

public class LoginForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    private String username = null;

    private String password = null;

    public String getUsername() {
        return (this.username);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return (this.password);
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
