package org.pachyderm.woc;

import org.apache.log4j.Logger;
import org.pachyderm.apollo.app.CXSession;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.apollo.core.CXAuthContext;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import er.extensions.foundation.ERXProperties;

/**
 * @author jarcher
 *
 */
public class UserAuthenticationPage extends MCPage {

    private static Logger LOG = Logger.getLogger(UserAuthenticationPage.class.getName());

    private static final long serialVersionUID = 8144525765552591386L;

    public String username = null;

    public String password = null;

    public Boolean authenticationDidFail = false;

    public String message = "";

    public String tempString;

    /**
	 * @param context
	 */
    public UserAuthenticationPage(WOContext context) {
        super(context);
        getLocalContext().takeValueForKey("minimal", "style");
    }

    public WOComponent loginAction() {
        LOG.info("loginAction: [[ LOGIN ]]");
        CXSession session = (CXSession) session();
        if (session.getSessionPerson() != null) {
            session.delSessionPerson();
        }
        CXAuthContext context = new CXAuthContext();
        context.setUsername(username);
        context.setPassword(password);
        if (session.isAuthenticWithContext(context)) {
            session.setTimeOut(1800.0);
            return pageWithName(StartingPoint.class.getName());
        }
        message = context.getContextMessage();
        authenticationDidFail = true;
        password = null;
        return context().page();
    }

    public WOComponent forgotPasswordAction() {
        return pageWithName(UserForgotPassword.class.getName());
    }

    public String bodyTopBarColor() {
        return ERXProperties.stringForKeyWithDefault("decor.bodyTopBarColor", "white");
    }

    public String bodyGroundColor() {
        return ERXProperties.stringForKeyWithDefault("decor.bodyGroundColor", "lightgrey");
    }

    public String panelEdgeColor() {
        return ERXProperties.stringForKeyWithDefault("decor.panelEdgeColor", "grey");
    }

    public String panelGroundColor() {
        return ERXProperties.stringForKeyWithDefault("decor.panelGroundColor", "grey");
    }

    public String blokTextColor() {
        return ERXProperties.stringForKeyWithDefault("decor.blokTextColor", "white");
    }

    public String blokBackColor() {
        return ERXProperties.stringForKeyWithDefault("decor.blokBackColor", "lightgrey");
    }

    public String blokEdgeColor() {
        return ERXProperties.stringForKeyWithDefault("decor.blokEdgeColor", "blue");
    }

    public String blokTextHover() {
        return ERXProperties.stringForKeyWithDefault("decor.blokTextHover", "darkgrey");
    }

    public String blokBackHover() {
        return ERXProperties.stringForKeyWithDefault("decor.blokBackHover", "black");
    }

    public String blokTextInert() {
        return ERXProperties.stringForKeyWithDefault("decor.blokTextInert", "darkgrey");
    }

    public String blokBackInert() {
        return ERXProperties.stringForKeyWithDefault("decor.blokBackInert", "black");
    }
}
