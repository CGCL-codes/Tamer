package org.bs.mdi.gui;

import javax.swing.KeyStroke;

/**
 * Helper subclass of {@Command}
 */
public class CommandImpl extends Command {

    public CommandImpl(String name, String label, boolean indirect, int mnemonic) {
        super(name, label, indirect, mnemonic);
    }

    public CommandImpl(String name, String label, boolean indirect, int mnemonic, String description, String help, String iconKey, KeyStroke accelerator) {
        super(name, label, indirect, mnemonic, description, help, iconKey, accelerator);
    }

    protected void doExecute() {
    }

    public void processMessage(Object source, int type, Object argument) {
    }
}
