package com.tenline.pinecone.platform.web.store.client.views;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;
import com.tenline.pinecone.platform.web.store.client.Messages;

/**
 * @author Bill
 *
 */
public class RegisterWindow extends Window {

    /**
	 * 
	 */
    public RegisterWindow() {
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setAutoHide(false);
        setResizable(false);
        setSize(350, 200);
        setHeading(((Messages) Registry.get(Messages.class.getName())).register());
        setLayout(new FitLayout());
        FormPanel registerForm = new FormPanel();
        registerForm.setHeaderVisible(false);
        TextField<String> emailField = new TextField<String>();
        emailField.setAllowBlank(false);
        emailField.setFieldLabel(((Messages) Registry.get(Messages.class.getName())).email());
        emailField.setRegex("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        emailField.getMessages().setBlankText(((Messages) Registry.get(Messages.class.getName())).emailBlankWarning());
        emailField.getMessages().setRegexText(((Messages) Registry.get(Messages.class.getName())).emailRegexWarning());
        TextField<String> passwordField = new TextField<String>();
        passwordField.setPassword(true);
        passwordField.setAllowBlank(false);
        passwordField.setFieldLabel(((Messages) Registry.get(Messages.class.getName())).password());
        passwordField.getMessages().setBlankText(((Messages) Registry.get(Messages.class.getName())).passwordBlankWarning());
        TextField<String> confirmationField = new TextField<String>();
        confirmationField.setPassword(true);
        confirmationField.setAllowBlank(false);
        confirmationField.setFieldLabel(((Messages) Registry.get(Messages.class.getName())).confirmation());
        confirmationField.getMessages().setBlankText(((Messages) Registry.get(Messages.class.getName())).confirmationBlankWarning());
        registerForm.add(emailField);
        registerForm.add(passwordField);
        registerForm.add(confirmationField);
        add(registerForm);
    }
}
