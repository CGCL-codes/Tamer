package org.dcm4chee.usr.war.pages;

import org.apache.wicket.authentication.panel.SignInPanel;
import org.apache.wicket.markup.html.WebPage;

/**
 * @author Robert David <robert.david@agfa.com>
 * @version $Revision$ $Date$
 * @since 28.09.2009
 */
public class LoginPage extends WebPage {

    private static final long serialVersionUID = 1L;

    public LoginPage() {
        add(new SignInPanel("signInPanel"));
    }
}
