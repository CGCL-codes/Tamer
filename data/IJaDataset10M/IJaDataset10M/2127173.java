package com.tysanclan.site.projectewok.pages.member;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.User;

public class PaypalSettingsPage extends AbstractSingleAccordionMemberPage {

    public PaypalSettingsPage(User user) {
        super("PayPal settings");
        final TextField<String> paypalAddress = new TextField<String>("address", new Model<String>(user.getPaypalAddress()));
        Form<User> settingsForm = new Form<User>("form") {

            private static final long serialVersionUID = 1L;

            @SpringBean
            private UserService userService;

            @Override
            protected void onSubmit() {
                userService.setPaypalAddress(getUser(), paypalAddress.getModelObject());
                setResponsePage(new OverviewPage());
            }
        };
        settingsForm.add(paypalAddress);
        getAccordion().add(settingsForm);
    }
}
