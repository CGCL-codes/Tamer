package org.jpos.q2.cli;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;
import org.jpos.q2.CLI;

public class UPTIME implements CLI.Command {

    public void exec(CLI cli, String[] args) throws Exception {
        cli.println(ISOUtil.millisToString(cli.getQ2().getUptime()));
    }
}
