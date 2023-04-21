package com.teracode.prototipogwt.frontend.client.login.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import com.teracode.prototipogwt.domain.usecasesdto.LoginRequest;
import com.teracode.prototipogwt.frontend.client.login.presenter.LoginPresenter.ILoginView;

/**
 * @author Maxi 
 */
public class LoginView extends Composite implements ILoginView, Editor<LoginRequest> {

    private static LoginViewUiBinder uiBinder = GWT.create(LoginViewUiBinder.class);

    interface LoginViewUiBinder extends UiBinder<HTMLPanel, LoginView> {
    }

    interface Driver extends SimpleBeanEditorDriver<LoginRequest, LoginView> {
    }

    Driver driver = GWT.create(Driver.class);

    @UiField
    TextBox email;

    @UiField
    PasswordTextBox password;

    @UiField
    InputElement submitField;

    public void createView() {
        HTMLPanel htmlPanel = uiBinder.createAndBindUi(this);
        initWidget(htmlPanel);
        driver.initialize(this);
        driver.edit(new LoginRequest());
    }

    @Override
    public Driver getDriver() {
        return driver;
    }

    @Override
    public InputElement getSubmitField() {
        return submitField;
    }
}
