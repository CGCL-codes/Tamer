package org.grailrtls.server.event;

import org.grailrtls.server.Hub;

public class HubConnectionEvent extends HubEvent {

    public final boolean connected;

    public HubConnectionEvent(Hub hub, boolean connected) {
        super(hub);
        this.connected = connected;
    }
}
