package org.masukomi.serverWatcher;

import java.applet.Applet;
import java.util.Iterator;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.masukomi.serverWatcher.alerters.AimAlerter;
import org.masukomi.serverWatcher.alerters.GenericAlertRecepient;
import org.masukomi.serverWatcher.alerters.YahooAlerter;
import com.wilko.jaim.JaimEvent;
import com.wilko.jaim.JaimEventListener;

/**
 * @author
 */
public class AlertUser extends Applet implements JaimEventListener {

    private static Log log = LogFactory.getLog(AlertUser.class);

    private static AimAlerter aimAlerter = AimAlerter.getInstance();

    private static YahooAlerter yahooAlerter = YahooAlerter.getInstance();

    Vector alertRecipients;

    Server server = null;

    public AlertUser(Server server) {
        this.alertRecipients = server.getAlertRecepients();
        this.server = server;
    }

    public void sendAlert() {
        getToolkit().beep();
        if (alertRecipients == null || alertRecipients.size() == 0) {
            return;
        }
        try {
            StringBuffer body = new StringBuffer();
            if (!server.getDisplayName().equals(server.getUrlToWatch())) {
                body.append(server.getDisplayName());
            }
            body.append(" \n");
            body.append(server.getUrlToWatch());
            body.append(" \n");
            if (server.getStatus() == 0) {
                if (server.getMatchString() != null && server.getMatchString().length() > 0) {
                    body.append(" either did not respond in ");
                    body.append(String.valueOf(ServerWatcherStorage.getInstance().getHttpTimeoutInterval() / 1000L));
                    body.append("seconds, returned an error, \nor did not contain \"");
                    body.append(server.getMatchString());
                    body.append("\"\nPlease remember that this is a CASE SENSITIVE match.");
                } else {
                    body.append(" did not respond in a timely fashion.");
                }
                if (!server.isHttpTest()) {
                    body.append("\nSince this was a ping test it means that either the tested server");
                    body.append("\nis down or that there is a problem in the network between ServerWatcher");
                    body.append("\nand it.\n");
                }
                body.append("\nThis was detected at ");
                body.append(server.getInitialDowntime().toString());
                body.append("\n");
            } else {
                body.append("\nis back online!");
            }
            Iterator it = alertRecipients.iterator();
            while (it.hasNext()) {
                GenericAlertRecepient gar = (GenericAlertRecepient) it.next();
                if (gar.getStatus() != GenericAlertRecepient.SEND_NO_ALERTS) {
                    if (gar.getStatus() == GenericAlertRecepient.SEND_ALL_ALERTS) {
                        gar.sendAlert(body.toString(), server);
                    } else if (gar.getStatus() == GenericAlertRecepient.SEND_SERVER_DOWN_ALERTS && server.getStatus() == Server.SERVER_DOWN) {
                        gar.sendAlert(body.toString(), server);
                    } else if (gar.getStatus() == GenericAlertRecepient.SEND_SERVER_UP_ALERTS && server.getStatus() == Server.SERVER_UP) {
                        gar.sendAlert(body.toString(), server);
                    }
                }
            }
        } catch (Throwable t) {
            log.error(t);
        }
    }

    public void receiveEvent(JaimEvent arg0) {
    }
}
