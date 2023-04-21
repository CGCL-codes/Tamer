package gemini.castor.ui.client.page.navigation.loginwidget;

import gemini.basic.model.User;
import gemini.castor.ui.client.ServiceCatalog;
import gemini.castor.ui.client.constants.I18nConstants;
import gemini.castor.ui.client.mvp.callback.AbstractAsyncCallback;
import gemini.castor.ui.client.mvp.gin.CastorGinjector;
import gemini.castor.ui.client.page.RootEvent;
import gemini.castor.ui.client.page.widget.TextFieldTextNumber;
import gemini.castor.ui.client.utils.GuiUtils;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import ch.elca.gwt.mvp.client.EventBus;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.validation.client.InvalidConstraint;
import com.google.gwt.validation.client.interfaces.IValidator;

public class LoginFormWidget extends Composite {

    private LoginObject loginObject = null;

    private IValidator<LoginObject> validator;

    private I18nConstants constants;

    @UiTemplate("LoginFormWidget.ui.xml")
    interface MyUiBinder extends UiBinder<Panel, LoginFormWidget> {
    }

    private static final MyUiBinder binder = GWT.create(MyUiBinder.class);

    public LoginFormWidget() {
        initWidget(binder.createAndBindUi(this));
        validator = GWT.create(LoginObject.class);
        constants = GWT.create(I18nConstants.class);
        loadData();
    }

    @UiField
    HTML htmError;

    @UiField
    TextFieldTextNumber tbxUsername;

    @UiField
    PasswordTextBox tbxPassword;

    @UiField
    CheckBox chxRemember;

    @UiField
    Button btnLogin;

    @UiHandler("btnLogin")
    void onClickLogin(ClickEvent event) {
        if (isValidate()) {
            checkUserAuthentication();
        }
    }

    private boolean isValidate() {
        bind();
        resetStyle();
        boolean result = true;
        Set<InvalidConstraint<LoginObject>> invalidConstraints = validator.validate(loginObject);
        Iterator<InvalidConstraint<LoginObject>> it = invalidConstraints.iterator();
        while (it.hasNext()) {
            if (result) {
                result = false;
            }
            InvalidConstraint<LoginObject> ic = it.next();
            showError(ic.getItemName(), ic.getMessage());
        }
        return result;
    }

    private void bind() {
        getLoginObject().setUsername(tbxUsername.getText());
        loginObject.setPassword(tbxPassword.getText());
        loginObject.setRemember(chxRemember.getValue());
    }

    private LoginObject getLoginObject() {
        if (loginObject == null) {
            loginObject = new LoginObject();
        }
        return loginObject;
    }

    private void resetStyle() {
        htmError.setVisible(false);
        tbxUsername.removeStyleDependentName("Error");
        tbxPassword.removeStyleDependentName("Error");
    }

    private void showError(String widgetName, String errorString) {
        if (widgetName.equals("username")) {
            tbxUsername.addStyleDependentName("Error");
        } else if (widgetName.equals("password")) {
            tbxPassword.addStyleDependentName("Error");
        }
        showError(constants.emptyField());
    }

    private void showError(String error) {
        htmError.setVisible(true);
        htmError.setStyleName("htmLoginError");
        htmError.setHTML(error);
    }

    private void loadData() {
        tbxUsername.setText(Cookies.getCookie("sid"));
        tbxPassword.setText(Cookies.getCookie("spin"));
        chxRemember.setValue("Y".equals(Cookies.getCookie("isRemember")));
        htmError.setStyleName("htmLoginInfo");
        htmError.setHTML(constants.loginFormInfo());
    }

    private EventBus getEventBus() {
        return CastorGinjector.INSTANCE.getEventBus();
    }

    public void checkUserAuthentication() {
        final String username = loginObject.getUsername();
        getEventBus().fireEvent(new RootEvent(RootEvent.ForwardType.MASK));
        ServiceCatalog.getUserService().getUserByName(username, new AbstractAsyncCallback<User>() {

            @Override
            public void onSuccess(User result) {
                getEventBus().fireEvent(new RootEvent(RootEvent.ForwardType.UN_MASK));
                Date expires = new Date(System.currentTimeMillis() + GuiUtils.DURATION);
                if (result != null && result.getPassword().equals(loginObject.getPassword())) {
                    if (loginObject.isRemember()) {
                        Cookies.setCookie("sid", loginObject.getUsername(), expires, null, "/", false);
                        Cookies.setCookie("spin", loginObject.getPassword(), expires, null, "/", false);
                        Cookies.setCookie("isRemember", "Y", expires, null, "/", false);
                    } else {
                        Cookies.removeCookie("sid");
                        Cookies.removeCookie("spin");
                        Cookies.removeCookie("isRemember");
                    }
                    forwardToInfoScreen(result);
                } else {
                    showError(constants.htmLoginFalse());
                }
            }
        });
    }

    private void forwardToInfoScreen(User result) {
        LoginEvent loginSuccessEvent = new LoginEvent(LoginEvent.ForwardType.SUCCESS);
        loginSuccessEvent.setLoggedInUser(result);
        fireEvent(loginSuccessEvent);
    }

    public HandlerRegistration addLoginFormHandler(LoginHandler handler) {
        return this.addHandler(handler, LoginEvent.getType());
    }
}
