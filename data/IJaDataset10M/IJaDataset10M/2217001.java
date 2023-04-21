package org.thole.phiirc.client.controller.commands;

import org.apache.commons.lang.StringUtils;
import org.thole.phiirc.client.controller.CmdParser;
import org.thole.phiirc.client.controller.Controller;

public class CmdNames extends AbstractCommandFather {

    public CmdNames(final CmdParser parser) {
        super(parser);
    }

    @Override
    public void execute(final String chan) {
        if (StringUtils.isBlank(chan)) {
            Controller.getInstance().getAction().doNames();
        } else {
            Controller.getInstance().getAction().doNames(chan);
        }
    }
}
