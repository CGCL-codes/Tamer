package org.vrspace.server.command;

import org.vrspace.server.*;

/**
Say something
sends set_say <args> to client
*/
public class say implements Command {

    public void exec(Request r) throws Exception {
        String cmd = r.getClient().getClassName() + " " + r.getClient().getId() + " say " + r.getEventValue();
        r.getClient().request(new Request(r.getClient(), cmd));
    }
}
