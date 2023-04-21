package org.springframework.richclient.command;

import java.util.EventObject;
import org.springframework.richclient.util.Assert;

/**
 * An event object that originated from a {@link CommandRegistry}.
 *
 */
public class CommandRegistryEvent extends EventObject {

    private static final long serialVersionUID = 740046110521079234L;

    private final AbstractCommand command;

    /**
     * Creates a new {@code CommandRegistryEvent} for the given registry and command.
     *
     * @param source The command registry that originated the event. Must not be null.
     * @param command The command that the event relates to. Must not be null.
     * 
     * @throws IllegalArgumentException if either argument is null.
     */
    public CommandRegistryEvent(CommandRegistry source, AbstractCommand command) {
        super(source);
        Assert.required(source, "source");
        Assert.required(command, "command");
        this.command = command;
    }

    /**
     * Returns the command that the event relates to.
     *
     * @return The command, never null.
     */
    public AbstractCommand getCommand() {
        return command;
    }
}
