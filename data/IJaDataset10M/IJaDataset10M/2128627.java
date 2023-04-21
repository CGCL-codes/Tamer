package org.robocup.msl.refbox.events;

import java.util.EventObject;

public class HistoryEvent extends EventObject {

    private static final long serialVersionUID = -5834378094207637720L;

    private String half = null;

    private String time = null;

    private String message = null;

    public HistoryEvent(final Object source, final String h, final String t, final String m) {
        super(source);
        this.half = h;
        this.time = t;
        this.message = m;
    }

    /**
     * @return the half
     */
    public String getHalf() {
        return this.half;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return this.time;
    }
}
