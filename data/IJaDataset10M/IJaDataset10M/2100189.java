package com.cidero.control;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.net.URL;
import java.net.MalformedURLException;
import org.cybergarage.http.HTTPStatus;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.event.*;
import com.cidero.swing.text.XMLStyledDocument;

/**
 *  Debug container for subscription response messages. See 
 *  DebugSubscriptionRequest.java comments for message details
 */
class DebugSubscriptionResponse extends DebugObj {

    private static final Logger logger = Logger.getLogger("com.cidero.control");

    SubscriptionResponse subRes;

    String friendlyName;

    String serviceType;

    /**
   * Constructor
   */
    public DebugSubscriptionResponse(Service service, SubscriptionResponse subRes) {
        this.subRes = subRes;
        if (subRes.getStatusCode() == HTTPStatus.OK) setStatus(DebugObj.STATUS_OK); else setStatus(DebugObj.STATUS_ERROR);
        serviceType = service.getServiceType().substring(29);
        friendlyName = service.getRootDevice().getFriendlyName();
    }

    /**
   * Single line string represention. Used in top pane of debug window
   */
    public String toSingleLineString() {
        return getTimeString() + " SubscribeResponse: " + friendlyName + ":" + serviceType + " Status: " + HTTPStatus.code2String(subRes.getStatusCode());
    }

    /**
   * Full (may be multiple lines) string represention. Used in bottom
   * pane of debug window
   */
    public String toString() {
        return subRes.toString();
    }

    /** 
   *  Append a text representation of the object to an XML-capable
   *  StyledDocument class.
   *
   * @param doc          XMLStyledDocument instance (normally displayed
   *                     in JTextPane)
   * @param autoFormat   Enable XML auto-formatting. Setting this to
   *                     false disables all the special XML-sensitive logic
   */
    public void append(XMLStyledDocument doc, boolean autoFormat) {
        doc.appendString("Subscription Response:\n\n" + subRes.toString());
    }

    static boolean enabled = true;

    public static void setEnabled(boolean flag) {
        enabled = flag;
    }

    public static boolean getEnabled() {
        return enabled;
    }

    public boolean isDisplayable() {
        return true;
    }
}
