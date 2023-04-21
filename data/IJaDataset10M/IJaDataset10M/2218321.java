package net.java.sipmack.sip.event;

import javax.sip.message.Message;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.util.EventObject;

public class UnknownMessageEvent extends EventObject {

    private static final long serialVersionUID = 239781911809975348L;

    public UnknownMessageEvent(Message source) {
        super(source);
    }

    public String getMessage() {
        return (source == null) ? "" : source.toString();
    }

    public String getMessageName() {
        if (source instanceof Request) {
            return (source == null) ? "" : ((Request) source).getMethod();
        } else {
            return (source == null) ? "" : ((Response) source).getStatusCode() + " " + ((Response) source).getReasonPhrase();
        }
    }
}
