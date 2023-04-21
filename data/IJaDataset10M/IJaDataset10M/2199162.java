package net.sf.bulimgr.web;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.StringResourceModel;

/**
 * Homepage
 */
public class GroupPage extends BasePage {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
    public GroupPage(final PageParameters parameters) {
        super();
        SignInSession session = (SignInSession) getSession();
        add(new Label(PAGETITLE, new StringResourceModel("page.title", this, null)));
        Link<String> linkLogout = new Link<String>(ID_LOGOUT) {

            private static final long serialVersionUID = -6018190891109969737L;

            @Override
            public void onClick() {
                SignInSession session = (SignInSession) getSession();
                session.invalidate();
                setResponsePage(GroupPage.class);
            }
        };
        Link<String> linkLogin = new Link<String>(ID_LOGIN) {

            private static final long serialVersionUID = -5473285244246590327L;

            @Override
            public void onClick() {
                setResponsePage(LoginPage.class);
            }
        };
        linkLogin.setVisible(!session.isSignedIn());
        add(linkLogin);
        linkLogout.setVisible(session.isSignedIn());
        add(linkLogout);
        add(new Link<String>(ID_REGISTER) {

            /** serialVersionUID */
            private static final long serialVersionUID = -8470500435506173579L;

            @Override
            public void onClick() {
                setResponsePage(LoginPage.class);
            }
        });
    }
}
