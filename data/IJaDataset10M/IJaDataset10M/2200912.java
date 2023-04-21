package com.outlandr.irc.client;

import com.outlandr.irc.client.events.Event;

public interface IRCListener {

    void handleEvent(Event event);
}
