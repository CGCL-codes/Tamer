package au.gov.nla.aons.notifications.destinations.email;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;
import au.gov.nla.aons.notifications.domain.Notification;
import au.gov.nla.aons.notifications.exceptions.NotificationException;

public class VelocityContentCreator implements ContentCreator {

    private VelocityEngine velocityEngine;

    public String createContent(Notification notification) {
        try {
            StringWriter html = new StringWriter();
            Map notificationMap = new HashMap();
            notificationMap.put("notification", notification);
            VelocityEngineUtils.mergeTemplate(velocityEngine, notification.getClass().getSimpleName(), notificationMap, html);
            html.close();
            return html.toString();
        } catch (IOException ioe) {
            throw new NotificationException("Could not create notification: " + ioe.getMessage(), ioe);
        }
    }

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
}
