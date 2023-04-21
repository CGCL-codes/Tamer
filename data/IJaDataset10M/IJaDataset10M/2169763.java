package portal.presentation.cafe.admin;

import hambo.community.FriendsManager;
import hambo.app.base.RedirectorBase;
import hambo.app.util.DOMUtil;
import hambo.app.util.Link;
import hambo.community.ObjectFriend;

public class cfadminphnbredirector extends RedirectorBase {

    public void processPage() {
        FriendsManager fm = new FriendsManager();
        int i = -1;
        String nick = (String) getParameter("nn");
        String phnb = (String) getParameter("phnb");
        String b3 = (String) getParameter("savephnb");
        String amountofcash = (String) getParameter("nbcash");
        String fuid = (String) getParameter("fuid");
        Link gbslink = new Link("cfadmingbsettings");
        if (b3 != null) {
            gbslink.addParam("c", amountofcash);
            gbslink.addParam("f", fuid);
            gbslink.addParam("n", nick);
            i = fm.updateMobileNumber(fuid, phnb);
        }
        throwRedirect(gbslink);
    }
}
