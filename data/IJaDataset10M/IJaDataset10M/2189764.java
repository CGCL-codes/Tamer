package vtc;

import java.util.*;
import java.io.*;

class VtcTimeTaggedEvent {

    private Object event_;

    private Date timeTag_;

    public VtcTimeTaggedEvent(Object event, Date timeTag) {
        event_ = event;
        timeTag_ = timeTag;
    }

    public Object getEvent() {
        return event_;
    }

    public void setEvent(Object event) {
        event_ = event;
    }

    public Date getTimeTag() {
        return timeTag_;
    }

    public void setTimeTag(Date timeTag) {
        timeTag_ = timeTag;
    }
}
