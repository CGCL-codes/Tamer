package be.lassi.base;

import static be.lassi.util.Util.newArrayList;
import java.util.List;

/**
 * Collection of <code>Listener</code> objects.
 *
 */
public class Listeners {

    /**
     * Collection with listeners wrappend by this class.
     */
    private final List<Listener> listeners = newArrayList();

    /**
     * Adds given listener.
     *
     * @param listener the listener to be added
     */
    public void add(final Listener listener) {
        listeners.add(listener);
    }

    /**
     * Removes given listener.
     *
     * @param listener the listener to be removed
     */
    public void remove(final Listener listener) {
        listeners.remove(listener);
    }

    /**
     * Notify all listeners that a change occurred.
     */
    public void changed() {
        for (Listener listener : listeners) {
            listener.changed();
        }
    }

    /**
     * Gets the number of listeners.
     *
     * @return the number of listeners
     */
    public int size() {
        return listeners.size();
    }

    /**
     * Verifies if the listener collection wrapped by this class is empty.
     */
    public void assertEmpty() {
        if (!listeners.isEmpty()) {
            StringBuilder b = new StringBuilder();
            b.append("Listeners\n");
            for (Listener listener : listeners) {
                b.append("  ");
                b.append(listener);
                b.append("\n");
            }
            String msg = b.toString();
            throw new AssertionError(msg);
        }
    }
}
