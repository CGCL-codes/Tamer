package portal.presentation.payment;

import com.lutris.appserver.server.httpPresentation.HttpPresentationComms;
import org.w3c.dom.Element;
import hambo.app.util.DOMUtil;
import hambo.app.util.Link;
import hambo.app.base.VirtualPortalPage;

/**
 * This virtual HTML page draws the marketing message.
 * 
 */
public class mmsg_bonuscash extends VirtualPortalPage {

    public mmsg_bonuscash(HttpPresentationComms comms) {
        super(comms, "mmsg_bonuscash", false);
    }

    public boolean processData() {
        try {
            String buddy = getContext().getSessionAttributeAsString("buddy");
            if (buddy == null) buddy = "buddy_zaheed33.gif";
            Element gubbe = getElement("buddy");
            if (gubbe != null) DOMUtil.appendAttribute(gubbe, "src", buddy);
        } catch (Exception e) {
        }
        Link link = new Link("pmsg_bonuscash");
        Element url = getElement("the_url");
        url.setAttribute("value", link.toString());
        Link urltofriendpop = new Link("tipafriend");
        Element tip = getElement("tipafriend");
        tip.setAttribute("value", urltofriendpop.toString());
        return true;
    }

    public boolean processTemplate() {
        return true;
    }
}
