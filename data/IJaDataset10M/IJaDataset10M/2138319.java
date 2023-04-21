package ua.org.hatu.daos.gwt.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * TODO: javadocs 
 * 
 * @author zeus (alex.pogrebnyuk@gmail.com)
 * @author dmytro (pogrebniuk@gmail.com)
 *
 */
public class SignInPanel extends Composite {

    private static SignInFormUiBinder uiBinder = GWT.create(SignInFormUiBinder.class);

    interface SignInFormUiBinder extends UiBinder<Widget, SignInPanel> {
    }

    @UiField
    TextBox userTextBox;

    @UiField
    PasswordTextBox passwordTextBox;

    @UiField
    Button signInButton;

    public SignInPanel() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public TextBox getUserTextBox() {
        return userTextBox;
    }

    public PasswordTextBox getPasswordTextBox() {
        return passwordTextBox;
    }

    public Button getSignInButton() {
        return signInButton;
    }
}
