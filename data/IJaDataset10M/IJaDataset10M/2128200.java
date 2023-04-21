package webservicesapi.command;

import java.util.Set;

/**
 * Holds a group of commands.
 *
 * @author Ben Leov
 */
public interface CommandSet {

    /**
     * Commands this set contains.
     *
     * @return Returns the commands that this command set contains.
     */
    Set<Command> getCommands();
}
