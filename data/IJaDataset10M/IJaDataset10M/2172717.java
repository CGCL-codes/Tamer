package jerklib.events.impl;

import jerklib.Channel;
import jerklib.Session;
import jerklib.events.IRCEvent;
import jerklib.events.JoinCompleteEvent;

public class JoinCompleteEventImpl implements JoinCompleteEvent {

    private final String rawEventData;

    private final Type type = IRCEvent.Type.JOIN_COMPLETE;

    private Session session;

    private final Channel channel;

    public JoinCompleteEventImpl(String rawEventData, Session session, Channel channel) {
        this.rawEventData = rawEventData;
        this.session = session;
        this.channel = channel;
    }

    public final Channel getChannel() {
        return channel;
    }

    public final Type getType() {
        return type;
    }

    public final String getRawEventData() {
        return rawEventData;
    }

    public final Session getSession() {
        return session;
    }

    public String toString() {
        return rawEventData;
    }
}
