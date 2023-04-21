package com.uk;

import com.uk.data.ejbs.IFaturaBean;
import com.uk.data.entities.User;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class LoginWindow extends Window {

    private TextField username;

    private TextField password;

    private IFaturaBean faturaBean;

    public LoginWindow(String caption, IFaturaBean faturaBean) {
        super(caption);
        this.faturaBean = faturaBean;
        this.init();
    }

    private void init() {
        CustomLayout vl = new CustomLayout("loginWindowLayout");
        username = new TextField();
        username.setRequired(true);
        username.setValidationVisible(true);
        vl.addComponent(username, "username");
        password = new TextField();
        password.setSecret(true);
        vl.addComponent(password, "password");
        Button loginButton = new Button("Login", new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                User user = faturaBean.login(username.getValue().toString(), password.getValue().toString());
                if (user == null) getWindow().showNotification("Perdoruesi nuk u autentifikua !"); else {
                    ((FaturaUKApplication) getWindow().getApplication()).setLoggedUser(user);
                    ((FaturaUKApplication) getWindow().getApplication()).loadMainLayout();
                }
            }
        });
        loginButton.setClickShortcut(KeyCode.ENTER);
        vl.addComponent(loginButton, "okbutton");
        this.addComponent(vl);
    }
}
