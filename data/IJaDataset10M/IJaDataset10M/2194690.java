package core;

import java.util.EventObject;

public class MsgEvent extends EventObject {

    private String body, from, to;

    public MsgEvent(Object arg0, String body, String from, String to) {
        super(arg0);
        this.body = body;
        this.from = from;
        this.to = to;
    }

    public String getBody() {
        return body;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
