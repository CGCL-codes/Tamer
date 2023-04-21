package net.dharwin.common.tools.cli.api.defaultcommands;

import net.dharwin.common.tools.cli.api.CLIContext;
import net.dharwin.common.tools.cli.api.Command;
import net.dharwin.common.tools.cli.api.CommandResult;
import net.dharwin.common.tools.cli.api.CommandResult.CommandResultType;
import net.dharwin.common.tools.cli.api.annotations.CLICommand;

/**
 * Returns an exit result type. This will exit the application when executed.
 * @author Sean
 *
 */
@CLICommand(name = "exit", description = "Exits the application.")
public class ExitCommand extends Command<CLIContext> {

    @Override
    public CommandResult innerExecute(CLIContext context) {
        return new CommandResult(CommandResultType.EXIT, 0);
    }
}
