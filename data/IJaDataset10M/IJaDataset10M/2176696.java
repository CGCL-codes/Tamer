package info.jtrac.wicket;

import info.jtrac.domain.User;
import info.jtrac.util.ValidationUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.BoundCompoundPropertyModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * user edit form
 */
public class UserFormPage extends BasePage {

    private WebPage previous;

    public void setPrevious(WebPage previous) {
        this.previous = previous;
    }

    public UserFormPage() {
        User user = new User();
        user.setLocale(getJtrac().getDefaultLocale());
        add(new UserForm("form", user));
    }

    public UserFormPage(User user) {
        add(new UserForm("form", user));
    }

    /**
     * wicket form
     */
    private class UserForm extends Form {

        private User user;

        private String password;

        private String passwordConfirm;

        private boolean sendNotifications;

        private JtracFeedbackMessageFilter filter;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPasswordConfirm() {
            return passwordConfirm;
        }

        public void setPasswordConfirm(String passwordConfirm) {
            this.passwordConfirm = passwordConfirm;
        }

        public boolean isSendNotifications() {
            return sendNotifications;
        }

        public void setSendNotifications(boolean sendNotifications) {
            this.sendNotifications = sendNotifications;
        }

        public UserForm(String id, final User user) {
            super(id);
            this.user = user;
            if (user.getId() == 0) {
                sendNotifications = true;
            }
            FeedbackPanel feedback = new FeedbackPanel("feedback");
            filter = new JtracFeedbackMessageFilter();
            feedback.setFilter(filter);
            add(feedback);
            final BoundCompoundPropertyModel model = new BoundCompoundPropertyModel(this);
            setModel(model);
            Button delete = new Button("delete") {

                @Override
                public void onSubmit() {
                    int count = getJtrac().loadCountOfHistoryInvolvingUser(user);
                    if (count > 0) {
                        UserForm.this.error(localize("user_delete.notPossible"));
                        return;
                    }
                    String heading = localize("user_delete.confirm");
                    String line1 = localize("user_delete.line1");
                    String warning = localize("user_delete.line2");
                    ConfirmPage confirm = new ConfirmPage(UserFormPage.this, heading, warning, new String[] { line1 }) {

                        public void onConfirm() {
                            getJtrac().removeUser(user);
                            JtracSession.get().refreshPrincipal();
                            setResponsePage(new UserListPage());
                        }
                    };
                    setResponsePage(confirm);
                }
            };
            delete.setDefaultFormProcessing(false);
            if (!getPrincipal().isSuperUser() || user.getId() == getPrincipal().getId() || user.getId() <= 1) {
                delete.setVisible(false);
            }
            add(delete);
            final TextField loginName = new TextField("user.loginName");
            loginName.setRequired(true);
            loginName.add(new ErrorHighlighter());
            loginName.setOutputMarkupId(true);
            add(new HeaderContributor(new IHeaderContributor() {

                public void renderHead(IHeaderResponse response) {
                    response.renderOnLoadJavascript("document.getElementById('" + loginName.getMarkupId() + "').focus()");
                }
            }));
            loginName.add(new AbstractValidator() {

                protected void onValidate(IValidatable v) {
                    String s = (String) v.getValue();
                    User temp = getJtrac().loadUser(s);
                    if (temp != null && temp.getId() != user.getId()) {
                        error(v);
                    }
                }

                @Override
                protected String resourceKey() {
                    return "user_form.loginId.error.exists";
                }
            });
            loginName.add(new AbstractValidator() {

                protected void onValidate(IValidatable v) {
                    String s = (String) v.getValue();
                    if (!ValidationUtils.isValidLoginName(s)) {
                        error(v);
                    }
                }

                @Override
                protected String resourceKey() {
                    return "user_form.loginId.error.invalid";
                }
            });
            add(loginName);
            WebMarkupContainer locked = new WebMarkupContainer("locked");
            if (user.getId() != 0 && user.getId() != getPrincipal().getId()) {
                locked.add(new CheckBox("user.locked"));
            } else {
                locked.setVisible(false);
            }
            add(locked);
            add(new TextField("user.name").setRequired(true).add(new ErrorHighlighter()));
            add(new TextField("user.email").setRequired(true).add(new ErrorHighlighter()));
            final Map<String, String> locales = getJtrac().getLocales();
            List<String> localeKeys = new ArrayList<String>(locales.keySet());
            DropDownChoice localeChoice = new DropDownChoice("user.locale", localeKeys, new IChoiceRenderer() {

                public Object getDisplayValue(Object o) {
                    return locales.get(o);
                }

                public String getIdValue(Object o, int i) {
                    return o.toString();
                }
            });
            add(localeChoice);
            WebMarkupContainer hide = new WebMarkupContainer("hide");
            if (user.getId() > 0) {
                hide.setVisible(false);
            }
            add(hide);
            final PasswordTextField passwordField = new PasswordTextField("password");
            add(passwordField);
            passwordField.setRequired(false);
            final PasswordTextField confirmPasswordField = new PasswordTextField("passwordConfirm");
            confirmPasswordField.setRequired(false);
            confirmPasswordField.add(new ErrorHighlighter());
            add(confirmPasswordField);
            add(new AbstractFormValidator() {

                public FormComponent[] getDependentFormComponents() {
                    return new FormComponent[] { passwordField, confirmPasswordField };
                }

                public void validate(Form form) {
                    String a = (String) passwordField.getConvertedInput();
                    String b = (String) confirmPasswordField.getConvertedInput();
                    if ((a != null && !a.equals(b)) || (b != null && !b.equals(a))) {
                        confirmPasswordField.error(localize("user_form.passwordConfirm.error"));
                    }
                }
            });
            WebMarkupContainer hideSendNotifications = new WebMarkupContainer("hideSendNotifications");
            add(hideSendNotifications);
            if (getPrincipal().getId() != user.getId()) {
                hideSendNotifications.add(new CheckBox("sendNotifications"));
            } else {
                hideSendNotifications.setVisible(false);
            }
            add(new Link("cancel") {

                public void onClick() {
                    if (previous == null) {
                        setResponsePage(new OptionsPage());
                    } else {
                        if (previous instanceof UserListPage) {
                            ((UserListPage) previous).setSelectedUserId(user.getId());
                        }
                        setResponsePage(previous);
                    }
                }
            });
        }

        @Override
        public boolean process() {
            if (user.getId() > 0) {
                user = getJtrac().loadUser(user.getId());
            }
            return super.process();
        }

        @Override
        protected void validate() {
            filter.reset();
            super.validate();
        }

        @Override
        protected void onSubmit() {
            if (password != null) {
                getJtrac().storeUser(user, password, sendNotifications);
            } else if (user.getId() == 0) {
                getJtrac().storeUser(user, null, true);
            } else {
                getJtrac().storeUser(user);
            }
            JtracSession.get().refreshPrincipalIfSameAs(user);
            if (previous == null) {
                UserListPage page = new UserListPage();
                page.setSelectedUserId(user.getId());
                setResponsePage(page);
            } else {
                if (previous instanceof UserListPage) {
                    ((UserListPage) previous).setSelectedUserId(user.getId());
                }
                if (previous instanceof SpaceAllocatePage) {
                    SpaceAllocatePage sap = (SpaceAllocatePage) previous;
                    previous = new SpaceAllocatePage(sap.getSpaceId(), sap.getPrevious(), user.getId());
                }
                setResponsePage(previous);
            }
        }
    }
}
