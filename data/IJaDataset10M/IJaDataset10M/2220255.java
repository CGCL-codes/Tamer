package org.qtitools.assessr.navbar;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.qtitools.assessr.user.LoginSession;

public class NavBar extends Panel {

    private static final long serialVersionUID = 1L;

    public NavBar(String name) {
        super(name);
        final Link logoutLink = new Link("logoutLink") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                final LoginSession session = (LoginSession) getSession();
                session.signOut();
                setResponsePage(getApplication().getHomePage());
            }
        };
        add(logoutLink);
        add(new Label("username", ((LoginSession) getSession()).getUser().getUsername()));
    }
}
