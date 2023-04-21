package net.lizhao.datahub.core.control;

import javax.swing.JMenuItem;

public class CmdMenuItem extends JMenuItem implements CmdHolder {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private CommandItem commandItem;

    @Override
    public CommandItem getCommandItem() {
        return commandItem;
    }

    @Override
    public void setCommandItem(CommandItem cmd) {
        commandItem = cmd;
    }
}
