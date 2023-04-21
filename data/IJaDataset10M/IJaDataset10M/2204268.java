package org.gamegineer.engine.internal.core.extensions.commandeventmediator;

import net.jcip.annotations.Immutable;
import org.gamegineer.engine.core.ICommand;
import org.gamegineer.engine.core.IEngineContext;
import org.gamegineer.engine.core.extensions.commandeventmediator.ICommandExecutingEvent;

/**
 * An implementation of
 * {@link org.gamegineer.engine.core.extensions.commandeventmediator.ICommandExecutingEvent}
 * to which implementations of
 * {@link org.gamegineer.engine.core.extensions.commandeventmediator.CommandExecutingEvent}
 * can delegate their behavior.
 * 
 * <p>
 * This class is immutable.
 * </p>
 */
@Immutable
final class CommandExecutingEventDelegate extends CommandEventDelegate implements ICommandExecutingEvent {

    /**
     * Initializes a new instance of the {@code CommandExecutingEventDelegate}
     * class.
     * 
     * @param context
     *        The engine context; must not be {@code null}.
     * @param command
     *        The command associated with the event; must not be {@code null}.
     */
    CommandExecutingEventDelegate(final IEngineContext context, final ICommand<?> command) {
        super(context, command);
    }
}
