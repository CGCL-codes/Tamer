package org.gamegineer.client.internal.ui.console.commandlets.farm;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import java.io.PrintWriter;
import java.util.Map;
import net.jcip.annotations.Immutable;
import org.gamegineer.client.internal.ui.console.commandlets.CommandletMessages;
import org.gamegineer.client.ui.console.commandlet.CommandletException;
import org.gamegineer.client.ui.console.commandlet.ICommandlet;
import org.gamegineer.client.ui.console.commandlet.ICommandletContext;
import org.gamegineer.client.ui.console.commandlet.ICommandletHelp;
import org.gamegineer.server.core.IGameServer;

/**
 * A commandlet that displays information about the game servers in the local
 * farm.
 * 
 * <p>
 * This class is immutable.
 * </p>
 */
@Immutable
public final class GetLocalServersCommandlet implements ICommandlet, ICommandletHelp {

    /**
     * Initializes a new instance of the {@code GetLocalServersCommandlet}
     * class.
     */
    public GetLocalServersCommandlet() {
        super();
    }

    public void execute(final ICommandletContext context) throws CommandletException {
        assertArgumentNotNull(context, "context");
        if (!context.getArguments().isEmpty()) {
            throw new CommandletException(CommandletMessages.Commandlet_execute_tooManyArgs, CommandletMessages.Commandlet_output_tooManyArgs);
        }
        final Map<String, IGameServer> gameServers = FarmAttributes.GAME_SERVERS.ensureGetValue(context.getStatelet());
        final PrintWriter writer = context.getConsole().getDisplay().getWriter();
        if (gameServers.isEmpty()) {
            writer.println(Messages.GetLocalServersCommandlet_output_noGameServers);
        } else {
            for (final Map.Entry<String, IGameServer> entry : gameServers.entrySet()) {
                writer.println(Messages.GetLocalServersCommandlet_output_gameServerInfo(entry.getKey(), entry.getValue()));
            }
        }
    }

    public String getDetailedDescription() {
        return Messages.GetLocalServersCommandlet_help_detailedDescription;
    }

    public String getSynopsis() {
        return Messages.GetLocalServersCommandlet_help_synopsis;
    }
}
