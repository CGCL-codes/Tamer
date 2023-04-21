package remote.motecontrol.client;

import java.util.EventObject;

public class SessionEvent extends EventObject {

    private static final long serialVersionUID = 6198612453362418190L;

    public static final short DISCONNECTED = 0;

    public static final short CONNECTED = 1;

    public static final short AUTHENTICATE = 2;

    protected short id;

    public SessionEvent(Session source, short id) {
        super(source);
        this.id = id;
    }

    public short getId() {
        return id;
    }
}
