package org.gamegineer.engine.core.extensions.commandeventmediator;

import org.gamegineer.engine.core.IEngineContext;

/**
 * Superclass for all event objects used to notify listeners that an engine
 * command has been executed.
 * 
 * <p>
 * This class is not intended to be extended by clients.
 * </p>
 */
public abstract class CommandExecutedEvent extends CommandEvent implements ICommandExecutedEvent {

    /** Serializable class version number. */
    private static final long serialVersionUID = -3477502806379860236L;

    /**
     * Initializes a new instance of the {@code CommandExecutedEvent} class.
     * 
     * @param source
     *        The context representing the engine that fired the event; must not
     *        be {@code null}.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If {@code source} is {@code null}.
     */
    protected CommandExecutedEvent(@SuppressWarnings("hiding") final IEngineContext source) {
        super(source);
    }
}
