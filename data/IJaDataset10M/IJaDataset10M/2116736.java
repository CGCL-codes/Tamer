package com.jirclib.event;

/**
 * Created by IntelliJ IDEA.
 * User: Aelin
 * Date: May 12, 2009
 * Time: 3:27:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class PartEvent extends IRCEvent {

    protected String channel;

    protected String user;

    public PartEvent(String channel, String user) {
        this.channel = channel;
        this.user = user;
    }

    public String getChannel() {
        return channel;
    }

    public String getUser() {
        return user;
    }
}
