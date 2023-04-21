package pl.lims.client;

import pl.lims.client.common.UserLoginRegisterResponse;
import pl.lims.client.common.model.User;
import pl.lims.client.services.UserService;
import pl.lims.client.services.UserServiceAsync;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LoginRegisterWindow extends Window {

    private final UserServiceAsync userService = GWT.create(UserService.class);

    private Tabs tabs;

    public LoginRegisterWindow(Tabs tabs) {
        this.tabs = tabs;
        setClosable(false);
        setWidth(365);
        setHeight(260);
        setHeading("User Login");
        setLayout(new FitLayout());
        TabPanel panel = new TabPanel();
        TabItem loginTab = createLoginTab();
        TabItem registerTab = createRegisterTab();
        panel.add(loginTab);
        panel.add(registerTab);
        add(panel, new FitData(4));
    }

    private TabItem createRegisterTab() {
        TabItem tabItem = new TabItem("Register");
        FormPanel form = new FormPanel();
        form.setFrame(false);
        form.setHeading("Create a new account.");
        form.setWidth(350);
        form.setLayout(new FlowLayout());
        FieldSet fieldSet = new FieldSet();
        FormLayout layout = new FormLayout();
        layout.setLabelWidth(75);
        fieldSet.setLayout(layout);
        final TextField<String> nameField = new TextField<String>();
        nameField.setFieldLabel("Name");
        fieldSet.add(nameField);
        final TextField<String> passField = new TextField<String>();
        passField.setPassword(true);
        passField.setFieldLabel("Password");
        fieldSet.add(passField);
        final TextField<String> emailField = new TextField<String>();
        emailField.setFieldLabel("Email");
        fieldSet.add(emailField);
        form.add(fieldSet);
        form.setButtonAlign(HorizontalAlignment.CENTER);
        Button registerButton = new Button("Register");
        form.addButton(registerButton);
        registerButton.addListener(Events.Select, new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                String name = nameField.getValue();
                String pass = passField.getValue();
                String email = emailField.getValue();
                if (name == null || pass == null || email == null) {
                    Info.display("Incorrect register data", "Name, password and email must not be empty!");
                    return;
                }
                userService.register(name, pass, email, new AsyncCallback<UserLoginRegisterResponse>() {

                    public void onFailure(Throwable caught) {
                        Info.display("Error while registering account!", caught.getMessage());
                    }

                    public void onSuccess(UserLoginRegisterResponse result) {
                        if (!result.isSuccess()) {
                            Info.display("The account not registered!", result.getMessage());
                        } else {
                            User user = result.getUser();
                            Info.display("Registered :)", "Registered successfuly. You've been automaticly logged in as " + user.getName() + " level: " + user.getUserType());
                            hide();
                            tabs.init(user);
                        }
                    }
                });
            }
        });
        tabItem.add(form);
        return tabItem;
    }

    private TabItem createLoginTab() {
        TabItem tabItem = new TabItem("Login");
        FormPanel form = new FormPanel();
        form.setHeading("Login using existing user.");
        form.setWidth(350);
        form.setLayout(new FlowLayout());
        FieldSet fieldSet = new FieldSet();
        FormLayout layout = new FormLayout();
        layout.setLabelWidth(75);
        fieldSet.setLayout(layout);
        final TextField<String> fieldName = new TextField<String>();
        fieldName.setFieldLabel("Name");
        fieldSet.add(fieldName);
        final TextField<String> fieldPassword = new TextField<String>();
        fieldPassword.setPassword(true);
        fieldPassword.setFieldLabel("Password");
        fieldSet.add(fieldPassword);
        form.add(fieldSet);
        form.setButtonAlign(HorizontalAlignment.CENTER);
        Button loginButton = new Button("Login");
        form.addButton(loginButton);
        loginButton.addListener(Events.Select, new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                final String name = fieldName.getValue();
                String pass = fieldPassword.getValue();
                if (name == null || pass == null) {
                    Info.display("Loggin in Error!", "Name or password is empty!");
                    return;
                }
                userService.login(name, pass, new AsyncCallback<UserLoginRegisterResponse>() {

                    public void onSuccess(UserLoginRegisterResponse result) {
                        if (!result.isSuccess()) {
                            Info.display("Couldn't log in", result.getMessage());
                        } else {
                            User user = result.getUser();
                            Info.display("Logged in", "Logged in successfuly as " + user.getName() + " level: " + user.getUserType());
                            hide();
                            tabs.init(user);
                        }
                    }

                    public void onFailure(Throwable caught) {
                        Info.display("Error while loggin in.", caught.getMessage());
                    }

                    ;
                });
            }
        });
        tabItem.add(form);
        return tabItem;
    }
}
