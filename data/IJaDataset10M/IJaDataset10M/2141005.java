package com.foo.web;

import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * User: Altug Bilgin ALTINTAS
 * Date: 15.May.2010
 * Time: 15:29:50
 */
public class LoginPage extends WebPage {

    public LoginPage() {
        add(new LoginForm("LoginForm"));
    }
}

class LoginForm extends Form {

    private String username;

    private String password;

    public LoginForm(String id) {
        super(id);
        final FeedbackPanel feedback = new FeedbackPanel("Hata");
        add(feedback);
        setModel(new CompoundPropertyModel(this));
        add(new RequiredTextField("username"));
        add(new PasswordTextField("password"));
    }

    @Override
    protected void onSubmit() {
        AuthenticatedWebSession session = AuthenticatedWebSession.get();
        if (session.signIn(username, password)) {
            setDefaultResponsePageIfNecessary();
        } else {
            error("Hatali giris");
        }
    }

    private void setDefaultResponsePageIfNecessary() {
        if (!continueToOriginalDestination()) {
            setResponsePage(getApplication().getHomePage());
        }
    }
}
