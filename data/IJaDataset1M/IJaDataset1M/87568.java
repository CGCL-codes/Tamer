package jerklib.events.impl;

import jerklib.Channel;
import jerklib.Session;
import jerklib.events.KickEvent;

public class KickEventImpl implements KickEvent {

    private final Type type = Type.KICK_EVENT;

    private final String byWho, who, message, rawEventData, userName, hostName;

    private final Channel channel;

    private final Session session;

    public KickEventImpl(String rawEventData, Session session, String byWho, String userName, String hostName, String who, String message, Channel channel) {
        this.rawEventData = rawEventData;
        this.session = session;
        this.byWho = byWho;
        this.userName = userName;
        this.hostName = hostName;
        this.who = who;
        this.message = message;
        this.channel = channel;
    }

    public String byWho() {
        return byWho;
    }

    public String getWho() {
        return who;
    }

    public String getHostName() {
        return hostName;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    public Channel getChannel() {
        return channel;
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

    public String toString() {
        return "KickEventImpl[session:" + getSession() + "]";
    }
}
