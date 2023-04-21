package com.eirikb.shared.events;

import com.eirikb.client.Client;

/**
 *
 * @author eirikb
 */
public class PartEvent extends Event {

    private String nick;

    private String partMsg;

    public PartEvent() {
    }

    public PartEvent(String nick, String partMsg) {
        this.nick = nick;
        this.partMsg = partMsg;
    }

    public void execute(Client client) {
        client.getChat().removeNick(nick);
        client.getChat().addChat("< " + nick + " forlot chatten (" + partMsg + ")");
    }
}
