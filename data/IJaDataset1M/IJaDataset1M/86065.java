package olr.presentation;

import org.w3c.dom.html.HTMLAnchorElement;
import com.lutris.appserver.server.httpPresentation.HttpPresentationException;

/**
 * @version $Id: SciecomHeader.java,v 1.5 2004/08/02 18:53:59 roku Exp $
 */
public class SciecomHeader extends ExtendedHttpPresentation {

    public SciecomHeader() {
    }

    public boolean loggedInUserRequired() {
        return true;
    }

    public String handleDefault() throws HttpPresentationException {
        return showPage();
    }

    public String showPage() {
        SciecomHeaderHTML page = new SciecomHeaderHTML();
        try {
            HTMLAnchorElement addExpert = page.getElementAddExpert();
            HTMLAnchorElement htmlanchorelement = page.getElementAddInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page.toDocument();
    }
}
