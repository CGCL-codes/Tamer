package com.mvu.banana.guest.client.gen;

import com.google.gwt.user.client.ui.PasswordTextBox;
import com.mvu.banana.guest.client.LoginPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.mvu.banana.common.client.ValueDTO;

public class LoginPanelDTO extends ValueDTO<LoginPanel> {

    public String emailInput;

    public String passwordInput;

    public String username;

    public String errorMessage;

    public void LoginPanelDTO() {
    }

    @Override
    public void prepare(LoginPanel p0) {
        if (p0.emailInput == null) {
            p0.emailInput = new TextBox();
        }
        ;
        p0.emailInput.setName("emailInput");
        if (p0.passwordInput == null) {
            p0.passwordInput = new PasswordTextBox();
        }
        ;
        p0.passwordInput.setName("passwordInput");
    }

    @Override
    public void copy(LoginPanel p0) {
        this.emailInput = p0.emailInput.getValue();
        this.passwordInput = p0.passwordInput.getValue();
    }

    @Override
    public void update(LoginPanel p0) {
        p0.username = this.username;
        p0.errorMessage = this.errorMessage;
    }
}
