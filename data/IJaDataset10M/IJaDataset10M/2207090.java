package com.nokia.ats4.appmodel.model.domain;

import java.util.Collection;
import java.util.Iterator;

/**
 * A base interface for {@link com.nokia.ats4.appmodel.model.domain.UserAction} and
 * {@link com.nokia.ats4.appmodel.model.domain.SystemResponse} that store commands.
 *
 * @author Timo Sillanp&auml;&auml;
 * @version $Revision: 2 $
 */
public interface CommandList<T extends TestCommand> {

    /**
     * Returns the commands.
     */
    public Collection<T> getCommands();

    /**
     * Adds a new command.
     */
    public void addCommand(T command);

    /**
     * A convenience method to add several commands at once.
     */
    public void addCommands(Iterator<T> commands);

    /**
     * Removes the specified command.
     */
    public void removeCommand(T command);

    /**
     * Removes all commands.
     */
    public void removeCommands();

    /**
     * Removes all commands that are flagged as automatically generated.
     */
    public void removeGeneratedCommands();
}
