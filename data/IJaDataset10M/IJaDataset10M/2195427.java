package org.gamegineer.engine.internal.core.extensions.commandeventmediator;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import net.jcip.annotations.Immutable;
import org.gamegineer.engine.core.AbstractInvertibleCommand;
import org.gamegineer.engine.core.CommandBehavior;
import org.gamegineer.engine.core.EngineException;
import org.gamegineer.engine.core.IEngineContext;
import org.gamegineer.engine.core.IInvertibleCommand;
import org.gamegineer.engine.core.extensions.commandeventmediator.ICommandEventMediator;
import org.gamegineer.engine.core.extensions.commandeventmediator.ICommandListener;

/**
 * A command that adds a command listener.
 * 
 * <p>
 * This class is immutable.
 * </p>
 */
@CommandBehavior(writeLockRequired = true)
@Immutable
public final class AddCommandListenerCommand extends AbstractInvertibleCommand<Void> {

    /** The command listener to add. */
    private final ICommandListener m_listener;

    /**
     * Initializes a new instance of the {@code AddCommandListenerCommand}
     * class.
     * 
     * @param listener
     *        The command listener to add; must not be {@code null}.
     * 
     * @throws java.lang.NullPointerException
     *         If {@code listener} is {@code null}.
     */
    public AddCommandListenerCommand(final ICommandListener listener) {
        assertArgumentNotNull(listener, "listener");
        m_listener = listener;
    }

    public Void execute(final IEngineContext context) throws EngineException {
        assertArgumentNotNull(context, "context");
        final ICommandEventMediator mediator = context.getExtension(ICommandEventMediator.class);
        if (mediator == null) {
            throw new EngineException(Messages.Common_commandEventMediatorExtension_unavailable);
        }
        mediator.addCommandListener(context, m_listener);
        return null;
    }

    public IInvertibleCommand<Void> getInverseCommand() {
        return new RemoveCommandListenerCommand(m_listener);
    }
}
