package org.jivesoftware.smack;

/**
 * The AbstractConnectionListener class provides an empty implementation for all
 * methods defined by the {@link ConnectionListener} interface. This is a
 * convenience class which should be used in case you do not need to implement
 * all methods.
 * 
 * @author Henning Staib
 */
public class AbstractConnectionListener implements ConnectionListener {

    public void connectionClosed() {
    }

    public void connectionClosedOnError(Exception e) {
    }

    public void reconnectingIn(int seconds) {
    }

    public void reconnectionFailed(Exception e) {
    }

    public void reconnectionSuccessful() {
    }
}
