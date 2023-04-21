package es.randres.jdo.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import es.randres.jdo.client.presenter.LoginPresenter.Display;

/**
 * @author randres
 *
 */
public class LoginView extends Composite implements Display {

    private static final Binder binder = GWT.create(Binder.class);

    @UiField
    TextBox textUsername;

    @UiField
    Label messageLabel;

    @UiField
    Button btnLogin;

    interface Binder extends UiBinder<Widget, LoginView> {
    }

    public LoginView() {
        initWidget(binder.createAndBindUi(this));
    }

    @Override
    public HasClickHandlers getLoginButton() {
        return btnLogin;
    }

    @Override
    public HasValue<String> getUsername() {
        return textUsername;
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void setMessageLabel(String message) {
        messageLabel.setText(message);
    }
}
