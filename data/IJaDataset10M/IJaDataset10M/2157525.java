package com.cashier3.client.gui;

import java.util.logging.Logger;
import com.cashier3.client.PrivateCashier;
import com.cashier3.client.services.LoginService;
import com.cashier3.client.services.LoginServiceAsync;
import com.cashier3.shared.ConstantDefinitions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import eu.maydu.gwt.validation.client.DefaultValidationProcessor;
import eu.maydu.gwt.validation.client.ValidationProcessor;
import eu.maydu.gwt.validation.client.actions.FocusAction;
import eu.maydu.gwt.validation.client.actions.StyleAction;
import eu.maydu.gwt.validation.client.validators.standard.RegularExpressionValidator;
import eu.maydu.gwt.validation.client.validators.strings.StringLengthValidator;

public class LoginScreen extends Grid {

    /**
	 * Create remote service proxies to talk to the server-side transaction
	 * services
	 */
    private final LoginServiceAsync loginService = GWT.create(LoginService.class);

    /**
	 * Get logger
	 */
    private static final Logger log = Logger.getLogger(PrivateCashier.class.getName());

    /**
	 * Some UI elements
	 */
    private TextBox mailBox;

    private PasswordTextBox passBox;

    private Button ok;

    private PrivateCashier parent;

    private ValidationProcessor validator;

    public LoginScreen(PrivateCashier parent) {
        super(3, 2);
        this.parent = parent;
        validator = new DefaultValidationProcessor();
        initializeUI();
        setWidget(0, 0, new HTML("Email:"));
        setWidget(0, 1, mailBox);
        setWidget(1, 0, new HTML("Password:"));
        setWidget(1, 1, passBox);
        setWidget(2, 0, ok);
    }

    private void initializeUI() {
        mailBox = new TextBox();
        passBox = new PasswordTextBox();
        ok = new Button("OK");
        ok.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (validator.validate("mail") && validator.validate("password")) handleLogIn();
            }
        });
        validator.addValidators("mail", new RegularExpressionValidator(mailBox, ConstantDefinitions.EMAIL_VALIDATION_REGEX, null).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new FocusAction()));
        validator.addValidators("password", new StringLengthValidator(passBox, ConstantDefinitions.MIN_PASSWORD_LENGTH, ConstantDefinitions.MAX_PASSWORD_LENGTH).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new FocusAction()));
    }

    private void handleLogIn() {
        mailBox.setEnabled(false);
        passBox.setEnabled(false);
        ok.setEnabled(false);
        loginService.login(mailBox.getValue(), passBox.getValue(), new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                parent.setLoggedInUser(null);
                mailBox.setText("");
                passBox.setText("");
                mailBox.setEnabled(true);
                passBox.setEnabled(true);
                ok.setEnabled(true);
            }

            @Override
            public void onSuccess(Void v) {
                parent.setLoggedInUser(mailBox.getValue());
                log.info("Logged in successfully");
                LoginScreen.this.removeFromParent();
                parent.onModuleLoad();
            }
        });
    }
}
