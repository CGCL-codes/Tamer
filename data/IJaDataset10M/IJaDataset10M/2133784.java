package org.thole.phiirc.client.controller.commands;

import org.thole.phiirc.client.controller.CmdParser;
import org.thole.phiirc.client.controller.Controller;

public class CmdNick extends AbstractCommandFather {

    public CmdNick(final CmdParser parser) {
        super(parser);
    }

    @Override
    public void execute(final String nick) {
        Controller.getInstance().getAction().doNick(nick);
    }
}
