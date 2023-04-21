package intellibitz.sted.event;

import java.util.EventObject;

/**
 * holds the source Thread information
 */
public class StatusEvent extends EventObject implements Cloneable {

    private IStatusEventSource statusEventSource;

    private String status;

    /**
     * @param src the source for this event
     */
    public StatusEvent(IStatusEventSource src) {
        super(src);
        statusEventSource = src;
    }

    /**
     * @return IStatusEventSource the Source which generated this Event
     */
    public IStatusEventSource getEventSource() {
        return statusEventSource;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
