package net.dharwin.common.tools.cli.sample.client.commands;

import net.dharwin.common.tools.cli.api.Command;
import net.dharwin.common.tools.cli.api.CommandResult;
import net.dharwin.common.tools.cli.api.annotations.CLICommand;
import net.dharwin.common.tools.cli.api.console.Console;
import net.dharwin.common.tools.cli.sample.client.SampleCLIContext;

/**
 * Print the username of the current logged in user.
 * @author Sean
 *
 */
@CLICommand(name = "who", description = "Prints the username of the logged in user.")
public class WhoCommand extends Command<SampleCLIContext> {

    @Override
    public CommandResult innerExecute(SampleCLIContext context) {
        Console.info("Username of logged in user: [" + context.getLoggedInUser() + "].");
        return CommandResult.OK;
    }
}
