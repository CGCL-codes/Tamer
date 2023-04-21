package com.wbillingsley.docwit.rsf.views;

import com.wbillingsley.docwit.rsf.viewparams.FrontViewParams;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

/**
 * Common static methods that the producers may call.
 *
 * @author William Billingsley
 */
public final class Common {

    /**
     * As this class only has static methods, we prevent instances from being
     * created.
     */
    private Common() {
    }

    /**
     * Fills the set of links in the site header.
     * @param tofill The RSF HTML tag to fill.
     * @param userId The user's string ID.
     */
    public static void fillSiteLinksBar(final UIContainer tofill, final String userId) {
        FrontViewParams fvp = new FrontViewParams();
        fvp.viewID = "front";
        UIInternalLink.make(tofill, "front", fvp);
        if (userId == null || userId.isEmpty()) {
            SimpleViewParameters svp = new SimpleViewParameters("settings");
            UIInternalLink.make(tofill, "settings", svp);
            UIOutput.make(tofill, "userId", userId);
        }
        UILink.make(tofill, "logout", "../../j_spring_security_logout");
    }
}
