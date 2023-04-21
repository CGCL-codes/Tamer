package jxmpp.com.code.google.core.listeners;

import com.google.inject.Inject;
import jxmpp.com.code.google.core.events.EventAggregator;
import jxmpp.com.code.google.core.events.concrete.ReconnectEvent;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionListener;

/**
 * Created by IntelliJ IDEA.
 * User: ternovykh
 * Date: 27.07.11
 * Time: 11:11
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionStateListener implements ConnectionListener {

    private static final Logger log = Logger.getLogger(ConnectionStateListener.class.getName());

    @Inject
    public ConnectionStateListener(EventAggregator eventAggregator) {
        if (eventAggregator == null) throw new NullPointerException("Illegal null-reference eventAggregator");
        this.eventAggregator = eventAggregator;
    }

    public void connectionClosed() {
        log.info("Connection closed");
    }

    public void connectionClosedOnError(Exception e) {
        log.info("Connection closed by error", e);
    }

    public void reconnectingIn(int i) {
    }

    public void reconnectionSuccessful() {
        log.info("Successfully reconnected");
        eventAggregator.publish(new ReconnectEvent());
    }

    public void reconnectionFailed(Exception e) {
        log.info("Reconnection failed", e);
    }

    private EventAggregator eventAggregator;
}
