package webEditor.client.view;

import webEditor.client.Proxy;
import webEditor.client.WEStatus;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Registration
 *
 * Show the form for a prospective user to register.
 * They give their user name, password, real name (display name).... 
 *
 * @author Robert Bost <bostrt@appstate.edu>
 *
 */
public class Registration extends View {

    private static RegistrationUiBinder uiBinder = GWT.create(RegistrationUiBinder.class);

    interface RegistrationUiBinder extends UiBinder<Widget, Registration> {
    }

    @UiField
    Button registerButton;

    @UiField
    TextBox email;

    @UiField
    TextBox username;

    @UiField
    TextBox firstName;

    @UiField
    TextBox lastName;

    @UiField
    PasswordTextBox password;

    @UiField
    PasswordTextBox passwordConfirm;

    @UiField
    Label passwordStatus;

    @UiField
    ListBox sections;

    public Registration() {
        initWidget(uiBinder.createAndBindUi(this));
        Proxy.getSections(sections);
        registerButton.setText("Register");
    }

    /**
	 * This is the click handler for the registration submit button.
	 * If passwords match and the username is not taken then 
	 * the user is good to go. Register them! 
	 */
    @UiHandler("registerButton")
    void onRegisterClick(ClickEvent event) {
        if (!passwordsMatch(password, passwordConfirm) || !arePasswordsGood(password, passwordConfirm)) {
            Notification.notify(WEStatus.STATUS_ERROR, "Please check your password.");
            return;
        }
        Proxy.register(email.getText(), username.getText(), password.getText(), firstName.getText(), lastName.getText(), sections.getValue(sections.getSelectedIndex()));
    }

    /**
	 * This is the handler for the password field.
	 * The status label changes appropriately.  
	 */
    @UiHandler("password")
    void passwordChange(KeyUpEvent event) {
        Element e = passwordStatus.getElement();
        if (arePasswordsGood(password, passwordConfirm)) {
            if (passwordsMatch(password, passwordConfirm)) {
                e.removeClassName("passwords-not-match");
                e.addClassName("passwords-match");
                passwordStatus.setText("Passwords match");
            } else {
                e.removeClassName("passwords-match");
                e.addClassName("passwords-not-match");
                passwordStatus.setText("Passwords do not match");
            }
        } else {
            e.removeClassName("passwords-match");
            e.addClassName("passwords-not-match");
            passwordStatus.setText("Password is too short!");
        }
    }

    /**
	 * This is the handler for the passwordConfirm field.
	 * The status label changes appropriately.
	 */
    @UiHandler("passwordConfirm")
    void passwordConfirmChange(KeyUpEvent event) {
        Element e = passwordStatus.getElement();
        if (arePasswordsGood(password, passwordConfirm)) {
            if (passwordsMatch(password, passwordConfirm)) {
                e.removeClassName("passwords-not-match");
                e.addClassName("passwords-match");
                passwordStatus.setText("Passwords match");
            } else {
                e.removeClassName("passwords-match");
                e.addClassName("passwords-not-match");
                passwordStatus.setText("Passwords do not match");
            }
        } else {
            e.removeClassName("passwords-match");
            e.addClassName("passwords-not-match");
            passwordStatus.setText("Password is too short!");
        }
    }

    /**
	 * Check if both password fields have any characters in them.
	 * Password needs to be longer than NOTHING.
	 * TODO: Run wordlist on password. 
	 * 
	 * @return boolean
	 */
    private boolean arePasswordsGood(PasswordTextBox p1, PasswordTextBox p2) {
        if (p1.getText().length() > 0 || p2.getText().length() > 0) {
            return true;
        }
        return false;
    }

    /**
	 * Check if passwords match.
	 * 
	 * @return boolean 
	 */
    private boolean passwordsMatch(PasswordTextBox p1, PasswordTextBox p2) {
        if (p1.getText().equals(p2.getText())) return true;
        return false;
    }

    @Override
    public WEAnchor getLink() {
        return new WEAnchor("Register", this, "register");
    }
}
