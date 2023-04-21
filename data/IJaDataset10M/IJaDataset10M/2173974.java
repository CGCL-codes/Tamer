package com.simconomy.usermanagement.gwt.client.wizards;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.simconomy.usermanagement.gwt.client.i18n.Messages;
import com.simconomy.usermanagement.gwt.client.services.UserManagementService;
import com.simconomy.usermanagement.gwt.client.services.UserManagementServiceAsync;
import com.simconomy.usermanagement.gwt.client.validation.EmailValidator;
import com.simconomy.usermanagement.gwt.client.validation.UserNameValidator;
import com.simconomy.widgets.client.events.FormListener;
import com.simconomy.widgets.client.validation.EqualValidator;
import com.simconomy.widgets.client.validation.SizeValidator;
import com.simconomy.widgets.client.wizards.impl.flow.FlowPage;
import com.simconomy.widgets.client.wizards.impl.flow.FlowWizard;

public class CreateUserWizard extends FlowWizard {

    private static final Messages messages = (Messages) GWT.create(Messages.class);

    private UserManagementServiceAsync service = UserManagementService.Util.getInstance();

    public void addPages() {
        CreateUserPage createUserPage = new CreateUserPage();
        createUserPage.addFormListener(new ProcessCreateUserForm());
        addPage(createUserPage);
    }

    class ProcessCreateUserForm implements FormListener {

        public void onBack(Widget sender) {
        }

        public void onCancel(Widget sender) {
        }

        public void onFinish(Widget sender) {
            CreateUserPage createUserPage = (CreateUserPage) sender;
            service.createUser(createUserPage.getUserName(), createUserPage.getPassword(), createUserPage.getEmail(), new AsyncCallback() {

                public void onSuccess(Object result) {
                    Window.open("/", "_self", "");
                }

                public void onFailure(Throwable caught) {
                }
            });
        }

        public void onNext(Widget sender) {
        }

        public void onReset(Widget sender) {
        }
    }

    class CreateUserPage extends FlowPage {

        private TextBox email = new TextBox();

        private TextBox userName = new TextBox();

        private PasswordTextBox password = new PasswordTextBox();

        private PasswordTextBox confirmPassword = new PasswordTextBox();

        public CreateUserPage() {
            super(messages.createUserStepName());
            add(messages.email(), email, new EmailValidator(false));
            add(messages.userName(), userName, new UserNameValidator());
            add(messages.password(), password, new SizeValidator(6, 20));
            add(messages.confirmPassword(), confirmPassword, new EqualValidator(password, messages.password()));
        }

        public boolean hasBackButton() {
            return false;
        }

        public boolean hasCancelButton() {
            return false;
        }

        public boolean hasFinishButton() {
            return true;
        }

        public boolean hasHeader() {
            return false;
        }

        public boolean hasNextButton() {
            return false;
        }

        protected String getEmail() {
            return email.getText();
        }

        protected String getPassword() {
            return password.getText();
        }

        protected String getUserName() {
            return userName.getText();
        }
    }
}
