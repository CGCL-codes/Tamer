package com.outlandr.irc.client.events;

public class ConnectedEvent extends Event {

    private String text;

    public ConnectedEvent(String textReply) {
        this.text = textReply;
    }

    public String getText() {
        return text;
    }
}
