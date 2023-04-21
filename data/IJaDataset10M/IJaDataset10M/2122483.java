package org.gamegineer.engine.core.extensions.commandeventmediator;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import org.gamegineer.common.core.util.concurrent.TaskUtils;
import org.gamegineer.engine.core.EngineException;
import org.gamegineer.engine.core.IEngine;

/**
 * A facade for exercising the functionality of the command event mediator
 * extension.
 */
public final class CommandEventMediatorFacade {

    /**
     * Initializes a new instance of the {@code CommandEventMediatorFacade}
     * class.
     */
    private CommandEventMediatorFacade() {
        super();
    }

    /**
     * Adds the specified command listener to the engine.
     * 
     * @param engine
     *        The engine; must not be {@code null}.
     * @param listener
     *        The command listener; must not be {@code null}.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If {@code listener} is already a registered command listener.
     * @throws java.lang.NullPointerException
     *         If {@code engine} or {@code listener} is {@code null}.
     * 
     * @see ICommandEventMediator#addCommandListener(org.gamegineer.engine.core.IEngineContext,
     *      ICommandListener)
     */
    public static void addCommandListener(final IEngine engine, final ICommandListener listener) {
        assertArgumentNotNull(engine, "engine");
        try {
            engine.executeCommand(CommandEventMediatorCommands.createAddCommandListenerCommand(listener));
        } catch (final EngineException e) {
            throw TaskUtils.launderThrowable(e);
        }
    }

    /**
     * Removes the specified command listener from the engine.
     * 
     * @param engine
     *        The engine; must not be {@code null}.
     * @param listener
     *        The command listener; must not be {@code null}.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If {@code listener} is not a registered command listener.
     * @throws java.lang.NullPointerException
     *         If {@code engine} or {@code listener} is {@code null}.
     * 
     * @see ICommandEventMediator#removeCommandListener(org.gamegineer.engine.core.IEngineContext,
     *      ICommandListener)
     */
    public static void removeCommandListener(final IEngine engine, final ICommandListener listener) {
        assertArgumentNotNull(engine, "engine");
        try {
            engine.executeCommand(CommandEventMediatorCommands.createRemoveCommandListenerCommand(listener));
        } catch (final EngineException e) {
            throw TaskUtils.launderThrowable(e);
        }
    }
}
