package info.jtrac.wicket;

import info.jtrac.wicket.yui.*;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;

/**
 * only used in case acegi - cas authentication is being used
 * then will force redirect to configured cas login page url
 * see JtracApplication.java for more details
 */
public class CasLoginPage extends WebPage {

    public CasLoginPage() {
        String loginUrl = JtracApplication.get().getCasLoginUrl();
        getRequestCycle().setRequestTarget(new RedirectRequestTarget(loginUrl));
    }
}
