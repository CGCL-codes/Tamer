package jerklib.events.impl;

import jerklib.Session;
import jerklib.events.IRCEvent;
import jerklib.events.NickInUseEvent;

public class NickInUseEventImpl implements NickInUseEvent {

    private final String inUseNick, rawEventData;

    private final Session session;

    private final IRCEvent.Type type = IRCEvent.Type.NICK_IN_USE;

    public NickInUseEventImpl(String inUseNick, String rawEventData, Session session) {
        super();
        this.inUseNick = inUseNick;
        this.rawEventData = rawEventData;
        this.session = session;
    }

    public String getInUseNick() {
        return inUseNick;
    }

    public String getRawEventData() {
        return rawEventData;
    }

    public Session getSession() {
        return session;
    }

    public Type getType() {
        return type;
    }
}
