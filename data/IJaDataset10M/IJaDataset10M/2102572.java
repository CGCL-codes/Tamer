package com.wle.client.gui.customgwt.auth;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Timer;
import com.wle.client.core.api.ClientAPIService;
import com.wle.client.core.api.ServerExceptions;
import com.wle.client.core.events.GUIEventListenerAdapter;

public class AuthenticateWindow extends Window {

    private final String disabledStyle = "x-hidden";

    private Component parent;

    TextField<String> name;

    TextField<String> pass;

    private MessageBox box;

    public AuthenticateWindow(Component p) {
        this.parent = p;
        focusable = true;
        setWidth(400);
        setHeight(300);
        setLayout(new FitLayout());
        setTitle("Authentication");
        setHeaderVisible(true);
        setDraggable(false);
        this.focus();
        add(new AuthenticateForm());
    }

    public void show() {
        super.show();
        this.parent.disable();
        this.parent.setStyleName(disabledStyle);
    }

    private class AuthContinueListener extends SelectionListener<ComponentEvent> {

        private AuthEventHandler eh;

        public AuthContinueListener() {
            eh = new AuthEventHandler();
        }

        @Override
        public void componentSelected(ComponentEvent ce) {
            box = MessageBox.wait("Progress", "Authenticating", "");
            ClientAPIService.getInstance().authenticate(name.getValue(), pass.getValue());
        }
    }

    private class RegContinueListener extends SelectionListener<ComponentEvent> {

        private RegEventHandler eh;

        public RegContinueListener(String name, String pass) {
            eh = new RegEventHandler();
        }

        @Override
        public void componentSelected(ComponentEvent ce) {
            box = MessageBox.wait("Progress", "Registering", "");
            ClientAPIService.getInstance().register(name.getValue(), pass.getValue());
        }
    }

    private class RegListener extends SelectionListener<ComponentEvent> {

        @Override
        public void componentSelected(ComponentEvent ce) {
            hide();
            removeAll();
            add(new RegisterForm());
            show();
        }
    }

    private class AuthEventHandler extends GUIEventListenerAdapter {

        @Override
        public void authenticationFailed(ServerExceptions errors) {
            StringBuilder sb = new StringBuilder();
            for (String e : errors.getErrors()) {
                sb.append(e);
                sb.append("\n");
            }
            MessageBox.alert("Authentication Failed", sb.toString(), null);
            box.hide();
        }

        @Override
        public void authenticationPassed() {
            ClientAPIService.getInstance().listProjects();
            hide();
            removeAll();
            parent.enable();
            parent.removeStyleName(disabledStyle);
            box.hide();
        }
    }

    private class RegEventHandler extends GUIEventListenerAdapter {

        @Override
        public void registrationFailed(ServerExceptions errors) {
            hide();
            removeAll();
            add(new RegisterForm());
            show();
        }

        @Override
        public void registrationPassed() {
            hide();
            removeAll();
            add(new AuthenticateForm());
            show();
        }
    }

    private class AuthenticateForm extends FormPanel {

        public AuthenticateForm() {
            setFieldWidth(240);
            setLabelWidth(70);
            setButtonAlign(HorizontalAlignment.RIGHT);
            setWidth(390);
            setStyleAttribute("padding", "20");
            setFrame(false);
            setBorders(false);
            setInsetBorder(false);
            setBodyBorder(false);
            setHeaderVisible(false);
            add(new LabelField("Please enter your credentials to continue ..."));
            name = new TextField<String>();
            name.setFieldLabel("User Name");
            name.setEmptyText("Enter your User Name");
            name.setAllowBlank(false);
            name.setMinLength(4);
            add(name);
            pass = new TextField<String>();
            pass.setFieldLabel("Password");
            pass.setEmptyText("Enter your Password");
            pass.setPassword(true);
            pass.setAllowBlank(false);
            pass.setMinLength(4);
            add(pass);
            addButton(new Button("Continue", new AuthContinueListener()));
            addButton(new Button("Register", new RegListener()));
        }
    }

    private class RegisterForm extends FormPanel {

        public RegisterForm() {
            setFieldWidth(240);
            setLabelWidth(70);
            setButtonAlign(HorizontalAlignment.RIGHT);
            setWidth(390);
            setStyleAttribute("padding", "20");
            setFrame(false);
            setBorders(false);
            setInsetBorder(false);
            setBodyBorder(false);
            setHeaderVisible(false);
            add(new LabelField("Please enter the following information to create an account"));
            name.setFieldLabel("User Name");
            name.setEmptyText("Enter your User Name");
            name.setAllowBlank(false);
            name.setMinLength(4);
            add(name);
            pass.setFieldLabel("Password");
            pass.setEmptyText("Enter your Password");
            pass.setPassword(true);
            pass.setAllowBlank(false);
            pass.setMinLength(4);
            add(pass);
            TextField<String> pass2 = new TextField<String>();
            pass2.setFieldLabel("Password Verification");
            pass2.setEmptyText("Re-Enter your Password");
            pass2.setPassword(true);
            pass2.setAllowBlank(false);
            pass2.setMinLength(4);
            add(pass2);
            addButton(new Button("Continue", new RegContinueListener(name.getValue(), pass.getValue())));
        }
    }
}
