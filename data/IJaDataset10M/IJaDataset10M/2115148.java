package net.sf.freesimrc.managers;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Handler for all (incoming) text communication. A message received on
 * a certain frequency is forwarded to all listeners, which have registered
 * themselves on that frequency. The class is thread-safe.
 */
public class CommManager {

    /**
     * Holds a list of all frequencies, which have at least one listener
     * registered for it. Each entry holds a list of listeners.
     */
    protected Hashtable<String, Vector<CommListener>> listeners;

    /**
     * Constructs an instance with no registered listeners.
     */
    public CommManager() {
        listeners = new Hashtable<String, Vector<CommListener>>();
    }

    /**
     * Forwards the given message to all listeners registered for the given
     * frequency.
     * @param frequency The frequency on which the message was sent. This can be
     * any non-<code>null</code>, non-empty string. Usually actual frequencies like
     * 117.30 MHz are represented as "@17300".
     * @param message The message that was received.
     */
    public synchronized void newMessage(String frequency, CommMessage message) {
        if (frequency != null && frequency.length() > 0 && message != null) {
            if (listeners.containsKey(frequency)) {
                Vector<CommListener> group = listeners.get(frequency);
                for (int i = 0; i < group.size(); i++) {
                    CommListener listener = group.elementAt(i);
                    listener.messageReceived(frequency, message);
                }
            }
            if (listeners.containsKey("@sounds")) {
                listeners.get("@sounds").firstElement().messageReceived(frequency, message);
            }
        }
    }

    /**
     * Registers the given listener for the given frequency. If a listener wants to
     * register for more than one frequency, this method has to be called multiple
     * times (for each frequency).
     * @param listener The listener to be registered.
     * @param frequency The frequency the listener is to be registered for. If the
     * given listener is already registered for this frequency, the method does
     * nothing.
     */
    public synchronized void addCommListener(CommListener listener, String frequency) {
        if (frequency != null && frequency.length() > 0 && listener != null) {
            if (listeners.containsKey(frequency)) {
                Vector<CommListener> group = listeners.get(frequency);
                if (!group.contains(listener)) {
                    group.add(listener);
                }
            } else {
                Vector<CommListener> group = new Vector<CommListener>();
                listeners.put(frequency, group);
                group.add(listener);
            }
        }
    }

    /**
     * Removes the registration of the given listener from the given frequency.
     * @param listener The listener to be deregistered.
     * @param frequency The frequency the listener is to be deregistered from.
     * If the given listener is not registered for this frequency, the method
     * does nothing.
     */
    public synchronized void removeCommListener(CommListener listener, String frequency) {
        if (frequency != null && frequency.length() > 0 && listener != null) {
            if (listeners.containsKey(frequency)) {
                Vector<CommListener> group = listeners.get(frequency);
                group.remove(listener);
                if (group.isEmpty()) {
                    listeners.remove(frequency);
                }
            }
        }
    }
}
