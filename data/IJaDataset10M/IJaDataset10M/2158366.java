package net.sf.babble.plugins.dcc.events;

import java.util.EventObject;
import net.sf.babble.plugins.dcc.DccFileSession;

/**
 *
 * @author  ben
 */
public class FileTransferCompletedEvent extends EventObject {

    /**
     * Holds value of property session.
     */
    private DccFileSession session;

    /** Creates a new instance of FileTransferCompletedEvent */
    public FileTransferCompletedEvent(Object source) {
        super(source);
        session = (DccFileSession) source;
    }

    /**
     * Getter for property session.
     * @return Value of property session.
     */
    public DccFileSession getSession() {
        return this.session;
    }
}
