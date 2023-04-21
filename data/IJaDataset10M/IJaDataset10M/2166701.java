package hermes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author colincrist@hermesjms.com
 * @version $Id: EventManager.java,v 1.1 2006/10/29 07:37:22 colincrist Exp $
 */
public class EventManager {

    private List<ConnectionListener> listeners = new ArrayList<ConnectionListener>();

    public void notifyConnected(final Hermes hermes) {
        synchronized (listeners) {
            for (ConnectionListener listener : listeners) {
                listener.onConnectionOpen(hermes);
            }
        }
    }

    public void notifyDisconnected(final Hermes hermes) {
        synchronized (listeners) {
            for (ConnectionListener listener : listeners) {
                listener.onConnectionClosed(hermes);
            }
        }
    }

    public void addConnectionListener(ConnectionListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    public void removeConnectionListener(ConnectionListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }
}
