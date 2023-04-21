package org.jdesktop.swingx.event;

import java.util.EventObject;
import org.jdesktop.swingx.BackgroundWorker;

/**
 * The Event object for events fired to BackgroundListeners. If data has been
 * published, it can be retrieved via the getData() method.
 *
 * @author rbair
 */
public class BackgroundEvent extends EventObject {

    private Object[] data;

    /** Creates a new instance of BackgroundEvent */
    public BackgroundEvent(BackgroundWorker source) {
        super(source);
    }

    /** Creates a new instance of BackgroundEvent */
    public BackgroundEvent(BackgroundWorker source, Object[] data) {
        super(source);
        this.data = data;
    }

    /**
     * @return the BackgroundWorker that fired the event
     */
    public BackgroundWorker getWorker() {
        return (BackgroundWorker) super.getSource();
    }

    /**
     * @return data associated with the event. In particular, this is used in the
     * <code>process</code> method of the BackgroundListener to retrieve data that
     * is to be displayed in the GUI.
     */
    public Object[] getData() {
        return data;
    }
}
