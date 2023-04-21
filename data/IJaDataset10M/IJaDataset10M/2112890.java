package portal.presentation.cafe.wap;

import hambo.user.HamboUser;
import hambo.app.base.ProtectedPortalPage;
import portal.presentation.cafe.web.cfStatic;
import hambo.app.util.DOMUtil;
import hambo.config.ConfigManager;
import hambo.community.FriendsManager;
import hambo.community.GuestbookManager;
import hambo.community.CommunityApplication;
import hambo.app.util.Link;

/**
 * Page used to Edit Guestbook Notifications via Email
 * WAP Version
 *
 */
public class cfwgbnoti extends ProtectedPortalPage {

    /**
     * Default constructor. Does nothing except calling the constructor
     * of the superclass with the page id as an argument. The page id 
     * is set in the page_id variable that is inherited from PortalPage.
     */
    public cfwgbnoti() {
        super("cfwgbnoti");
    }

    /**
    * The over-ridden method that is called automatically 
    * by {@link PortalPage}.run().
    */
    public void processPage() {
        if (!CommunityApplication.USE_GUESTBOOK) {
            Link l = new Link("cfwmain");
            throwRedirect(l);
        }
        FriendsManager fm = cfStatic.isUserRegistered(comms, user_id);
        String user_uid = getContext().getSessionAttributeAsString("user_uid");
        GuestbookManager gm = new GuestbookManager(user_uid);
        String email = gm.getEmail();
        String visible = gm.getIsVisible() + "";
        String nbemail = gm.getEmailNb() + "";
        String nbsms = gm.getSMSNb() + "";
        if (email != null && !email.trim().equals("")) {
            DOMUtil.setAttribute(getElement("email"), "value", email);
        } else if (gm.getEmail() != null && !gm.getEmail().trim().equals("")) {
            DOMUtil.setAttribute(getElement("email"), "value", gm.getEmail());
        } else if (getContext().getSessionAttribute(HamboUser.EXTERNAL_EMAIL_ADDRESS) != null) {
            DOMUtil.setAttribute(getElement("email"), "value", getContext().getSessionAttributeAsString(HamboUser.EXTERNAL_EMAIL_ADDRESS));
        } else {
            DOMUtil.setAttribute(getElement("email"), "value", user_id + ConfigManager.getConfig("server").getProperty("email_domain"));
        }
    }
}
