package net.kano.joustsim.oscar.oscar.service.icbm;

import net.kano.joustsim.Screenname;
import java.util.Date;

public class MessageQueuedEvent extends ConversationEventInfo {

    private final Message msg;

    public MessageQueuedEvent(Message msg, Screenname screenname, Screenname buddy) {
        super(screenname, buddy, new Date());
        this.msg = msg;
    }

    public Message getMessage() {
        return msg;
    }
}
