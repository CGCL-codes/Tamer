package org.eoti.io.console.shell.cmd;

import org.eoti.io.console.shell.ShellException;
import org.eoti.io.console.shell.ShellExtension;
import java.util.List;

public class ShellHelp extends ShellExtension {

    public ShellHelp() {
        super();
    }

    protected void computeFormat() {
        if (shell == null) {
            super.computeFormat();
            return;
        }
        int col1 = 1;
        for (String name : shell.getExtensions().keySet()) col1 = Math.max(col1, name.length());
        int col2 = getConsoleWidth() - col1 - 5;
        setFormat("  " + defaultFormat(col1) + ":  " + defaultFormat(col2) + "\n");
    }

    public void execute(List<String> arguments) throws ShellException {
        if (arguments.isEmpty()) {
            computeFormat();
            shell.format("Available Commands:\n");
            for (String name : shell.getExtensions().keySet()) {
                ShellExtension ext = shell.getExtension(name);
                format(name, ext.getUsage());
            }
            shell.format("%d extensions found.\n", shell.getExtensions().size());
            shell.println();
            return;
        }
        if (arguments.size() > 1) error("Incorrect format. Try 'help' or 'help command'.");
        String cmd = arguments.get(0);
        ShellExtension ext = shell.getExtension(cmd);
        if (ext == null) error("Unknown command: %s", cmd);
        shell.format("HELP %s:\n", cmd);
        ext.manual();
        shell.println();
    }

    public String getName() {
        return "help";
    }

    public String getUsage() {
        return "'help' for list of commands\n'help command' for detailed information.";
    }
}
