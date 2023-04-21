package portal.presentation.messenger;

import com.lutris.appserver.server.httpPresentation.HttpPresentationComms;
import org.w3c.dom.Element;
import hambo.app.util.DOMUtil;
import hambo.app.util.Link;
import hambo.app.base.VirtualPortalPage;

/**
 * This virtual HTML page draws the marketing message.
 * 
 */
public class mmsg_msgeventmain extends VirtualPortalPage {

    public mmsg_msgeventmain(HttpPresentationComms comms) {
        super(comms, "mmsg_msgeventmain", false);
    }

    public boolean processData() {
        try {
            String buddy = getContext().getSessionAttributeAsString("buddy");
            if (buddy == null) buddy = "buddy_zaheed33.gif";
            Element gubbe = getElement("buddy");
            if (gubbe != null) DOMUtil.appendAttribute(gubbe, "src", buddy);
        } catch (Exception e) {
        }
        Link link = new Link("pmsg_msgeventmain");
        Element url = getElement("the_url");
        url.setAttribute("value", link.toString());
        return true;
    }

    public boolean processTemplate() {
        return true;
    }
}
