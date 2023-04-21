package audictiv.client.ui.website.header.loginRegisterBar;

import audictiv.client.ui.website.header.loginRegisterBar.login.Login;
import audictiv.client.ui.website.header.loginRegisterBar.register.Register;
import audictiv.shared.LoginAnswer;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

public class LoginRegisterBar extends FlowPanel {

    private Button homeButton;

    private Login loginPanel;

    private Register registerPanel;

    public LoginRegisterBar() {
        initializeComponents();
        setStyles();
        makeHandlers();
        buildPanel();
    }

    private void initializeComponents() {
        homeButton = new Button("Home", new ClickHandler() {

            public void onClick(ClickEvent event) {
                History.newItem("Home");
            }
        });
        loginPanel = new Login();
        registerPanel = new Register();
    }

    private void setStyles() {
        homeButton.setStyleName("button");
    }

    private void buildPanel() {
        this.add(homeButton);
        this.add(loginPanel);
        this.add(registerPanel);
    }

    private void makeHandlers() {
    }

    public void loadLoggedInterface(LoginAnswer loggedInfo) {
        registerPanel.setVisible(false);
        loginPanel.loadLoggedInterface(loggedInfo);
    }

    public void unloadLoggedInterface() {
        registerPanel.setVisible(true);
        loginPanel.unloadLoggedInterface();
    }
}
