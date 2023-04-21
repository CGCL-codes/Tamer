package net.sf.hdkp.ui;

import net.sf.hdkp.cmd.AbstractUICommand;

public class ExitCommand extends AbstractUICommand {

    @Override
    public void exec() {
        System.exit(0);
    }
}
