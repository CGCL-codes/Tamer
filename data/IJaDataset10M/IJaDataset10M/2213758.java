package com.pragprog.dhnako.carserv.wicket.ui.security;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import com.pragprog.dhnako.carserv.wicket.app.NakedObjectsAuthenticatedWebSession;
import com.pragprog.dhnako.carserv.wicket.ui.NakedObjectsPanel;

public class UserPanel extends NakedObjectsPanel {

    private static final long serialVersionUID = 1L;

    public UserPanel(String id, Class<? extends Page> signoutClass) {
        super(id);
        NakedObjectsAuthenticatedWebSession session = getNakedObjectsAuthenticatedWebSession();
        AuthenticationSession authenticationSession = session.getAuthenticationSession();
        add(new Label("fullname", authenticationSession != null ? authenticationSession.getUserName() : "(null)"));
        add(new BookmarkablePageLink("signout", signoutClass));
    }
}
