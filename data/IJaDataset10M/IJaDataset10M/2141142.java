package pl.org.minions.stigma.client.observers;

import pl.org.minions.stigma.game.command.Command;

/**
 * General interface for command handlers.
 * @see Command
 */
public interface CommandHandler {

    /**
     * Handles the given command.
     * @param command
     *            command to handle
     */
    void handleCommand(Command command);
}
