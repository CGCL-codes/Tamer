package org.bitdrive.ui.cli;

public class DisconnectCommand extends Command {

    private String cmd = "disconnect";

    private String cmdName = "Disconnect Command";

    public int getArgCount() {
        return 0;
    }

    public String getName() {
        return cmdName;
    }

    public String[] getActions() {
        return new String[] { cmd };
    }

    public boolean action(String cmd, String[] args) {
        System.out.println("Not implemented");
        return false;
    }

    public void shutdown() {
    }

    public String getHelp() {
        return "not implemented";
    }
}
