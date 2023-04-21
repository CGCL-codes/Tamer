package com.kenstevens.stratinit.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.kenstevens.stratinit.client.gwtservice.GWTNone;
import com.kenstevens.stratinit.client.gwtservice.GWTRegisterService;
import com.kenstevens.stratinit.client.gwtservice.GWTRegisterServiceAsync;
import com.kenstevens.stratinit.client.gwtservice.GWTResult;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Register implements EntryPoint {

    protected static final String HOME = "/index.html";

    final Label errorLabel = new Label();

    final Label usernameLabel = new Label("username: ");

    final TextBox usernameTextBox = new TextBox();

    final Label passwordLabel = new Label("password: ");

    final PasswordTextBox passwordTextBox = new PasswordTextBox();

    final Label emailLabel = new Label("email: ");

    final TextBox emailTextBox = new TextBox();

    final Button registerButton = new Button("register");

    private GWTRegisterServiceAsync registerServiceAsync = GWT.create(GWTRegisterService.class);

    /**
	 * This is the entry point method.
	 */
    public void onModuleLoad() {
        Grid grid = new Grid(4, 2);
        grid.setWidget(0, 0, usernameLabel);
        grid.setWidget(0, 1, usernameTextBox);
        grid.setWidget(1, 0, passwordLabel);
        grid.setWidget(1, 1, passwordTextBox);
        grid.setWidget(2, 0, emailLabel);
        grid.setWidget(2, 1, emailTextBox);
        grid.setWidget(3, 0, registerButton);
        RootPanel.get().add(errorLabel);
        RootPanel.get().add(grid);
        registerButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                register();
            }
        });
        emailTextBox.addKeyPressHandler(new KeyPressHandler() {

            public void onKeyPress(KeyPressEvent event) {
                if (event.getCharCode() == KeyCodes.KEY_ENTER) {
                    register();
                }
            }
        });
    }

    private void register() {
        if (registerServiceAsync == null) {
            registerServiceAsync = GWT.create(GWTRegisterService.class);
        }
        AsyncCallback<GWTResult<GWTNone>> callback = new AsyncCallback<GWTResult<GWTNone>>() {

            public void onFailure(Throwable caught) {
                errorLabel.setText(caught.getMessage());
            }

            public void onSuccess(GWTResult<GWTNone> result) {
                if (result.success) {
                    Window.open(HOME, "_self", "");
                    errorLabel.setText(result.getLastMessage());
                } else {
                    errorLabel.setText(result.getLastMessage());
                }
            }
        };
        registerServiceAsync.register(usernameTextBox.getText(), passwordTextBox.getText(), emailTextBox.getText(), callback);
    }
}
