package com.rolfje.speedforge.webapp.panels;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebResponse;
import com.rolfje.speedforge.webapp.SFWebSession;
import com.rolfje.speedforge.webapp.pages.Login;

public class UserPanel extends Panel {

    public UserPanel(String id) {
        super(id);
        add(new Label("fullname", new PropertyModel(this, "session.soapSession.user.unix_name")));
        add(new Link("signout") {

            @Override
            public boolean isVisible() {
                return SFWebSession.get().isAuthenticated();
            }

            @Override
            public void onClick() {
                SFWebSession.get().forgetme((WebResponse) getResponse());
                SFWebSession.get().invalidate();
                setResponsePage(Login.class);
            }
        });
        add(new Link("signin") {

            @Override
            public void onClick() {
                throw new RestartResponseAtInterceptPageException(Login.class);
            }

            @Override
            public boolean isVisible() {
                return !SFWebSession.get().isAuthenticated();
            }
        });
    }
}
