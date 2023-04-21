package org.thole.phiirc.client.controller.commands;

import org.thole.phiirc.client.controller.CmdParser;

public abstract class AbstractCommandFather {

    private CmdParser parser;

    protected AbstractCommandFather(final CmdParser parser) {
        this.setParser(parser);
    }

    public abstract void execute(String cmd);

    public CmdParser getParser() {
        return parser;
    }

    private void setParser(final CmdParser parser) {
        this.parser = parser;
    }
}
