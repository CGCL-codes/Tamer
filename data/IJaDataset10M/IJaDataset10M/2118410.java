package org.jwatter.toolkit.shell.commands;

import org.jwatter.toolkit.shell.CommandExecutionException;
import org.jwatter.toolkit.shell.Shell;

public class ExitCommand extends AbstractCommand implements Command {

    protected Shell shell;

    public ExitCommand(Shell shell) {
        super(new String[] { "q", "quit", "exit" }, "quit the shell");
        this.shell = shell;
    }

    public void execute(String... args) throws CommandExecutionException {
        shell.exit();
    }
}
