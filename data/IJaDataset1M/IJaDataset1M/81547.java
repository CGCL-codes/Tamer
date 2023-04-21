package org.mvz.gwttest.client;

import java.util.Map;
import org.mvz.gwt.client.ActionLink;
import org.mvz.gwt.client.DynamicForm;
import org.mvz.gwt.client.Lightbox;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class LoginWidget {

    private DynamicForm.Handler handler = new DynamicForm.Handler() {

        public void onCancel(DynamicForm sender) {
            lightbox.hide();
        }

        public void onSubmit(DynamicForm sender, Map formValues) {
            lightbox.hide();
            Window.alert("login");
        }
    };

    private Lightbox lightbox;

    private DynamicForm loginForm = new DynamicForm();

    ActionLink loginLink = new ActionLink("Login", new ClickListener() {

        public void onClick(Widget w) {
            lightbox.show();
            loginForm.focus();
        }
    });

    public Label getLink() {
        return loginLink;
    }

    public LoginWidget() {
        loginForm.setSubmitButtonText("Login");
        loginForm.addTextField("login", "Username:", false, DynamicForm.minimumLengthValidator(4));
        loginForm.addTextField("password", "Password:", true, DynamicForm.minimumLengthValidator(4));
        loginForm.setHandler(handler);
        loginForm.addStyleName("loginForm");
        lightbox = new Lightbox(loginForm, true);
    }
}
