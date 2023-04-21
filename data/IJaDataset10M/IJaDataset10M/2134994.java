package org.open18.notification;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.faces.Renderer;
import org.open18.action.Scorecard;

@Name("notifications")
public class Notifications {

    @In
    private Recipient recipient;

    @In
    private Renderer renderer;

    @In
    private FacesMessages facesMessages;

    @In(create = true)
    private Scorecard scorecard;

    private boolean sent;

    public void sendScorecard() {
        scorecard.load();
        renderer.render("/email/scorecard-notification.xhtml");
        facesMessages.add("The scorecard has been sent to {0}.", recipient.getName());
        recipient.reset();
        sent = true;
    }

    @BypassInterceptors
    public boolean isSent() {
        return sent;
    }
}
