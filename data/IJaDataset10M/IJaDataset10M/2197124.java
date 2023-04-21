package net.sf.sail.core.beans.event;

import javax.swing.event.ChangeEvent;
import net.sf.sail.core.beans.SessionContext;

public class SessionEvent extends ChangeEvent {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5853408559447392235L;

    public static final int INITIATE = 1;

    public static final int START = 2;

    public static final int END = 3;

    int id;

    public SessionEvent(SessionContext source, int id) {
        super(source);
        this.id = id;
    }

    public int getID() {
        return id;
    }
}
