package emulator.shell.commands;

import emulator.EmulatorException;
import emulator.shell.DebugShell;
import emulator.shell.ShellCommand;

public class Reset implements ShellCommand {

    @Override
    public boolean execute(DebugShell shell, String[] arg) throws EmulatorException {
        shell.getCpu().reset();
        return false;
    }

    @Override
    public void printHelp(DebugShell shell) {
        shell.getOut().println("command syntax:");
        shell.getOut().println("reset - reset CPU");
    }
}
