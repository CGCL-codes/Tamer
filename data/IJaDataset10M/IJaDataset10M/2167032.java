package org.rip.ftp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Command {

    protected FtpProcess process;

    private static final Logger LOG = LoggerFactory.getLogger(Command.class);

    public void pre() {
        LOG.trace("pre");
    }

    public void execute() {
        LOG.trace("execute");
    }

    public void post() {
        LOG.trace("post");
    }

    public String syntax() {
        return "(unimplemented command)";
    }

    public void parseCommand(String line) throws ParseException {
    }
}
