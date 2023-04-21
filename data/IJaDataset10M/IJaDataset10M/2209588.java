package org.gamegineer.game.internal.core.commands;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import net.jcip.annotations.Immutable;
import org.gamegineer.engine.core.CommandBehavior;
import org.gamegineer.engine.core.EngineException;
import org.gamegineer.engine.core.IEngineContext;
import org.gamegineer.game.internal.core.GameAttributes;
import org.gamegineer.game.internal.core.StageVersion;

/**
 * A command that activates the next appropriate stage.
 * 
 * <p>
 * A stage submits this command to trigger the activation of the next
 * appropriate stage. If this command is received when the originating stage is
 * no longer the currently executing stage or is not at the same change level
 * when it submitted the command, it will be silently ignored.
 * </p>
 * 
 * <p>
 * This class is immutable.
 * </p>
 */
@CommandBehavior(writeLockRequired = true)
@Immutable
public final class ActivateStageCommand extends AbstractStageCommand {

    /**
     * Initializes a new instance of the {@code ActivateStageCommand} class.
     * 
     * @param sourceStageId
     *        The identifier of the stage that submitted the command; must not
     *        be {@code null}.
     * @param sourceStageVersion
     *        The version of the stage that submitted the command; must not be
     *        {@code null}.
     * 
     * @throws java.lang.NullPointerException
     *         If {@code sourceStageId} or {@code sourceStageVersion} is
     *         {@code null}.
     */
    public ActivateStageCommand(final String sourceStageId, final StageVersion sourceStageVersion) {
        super(sourceStageId, sourceStageVersion);
    }

    @Override
    protected void executeInternal(final IEngineContext context) throws EngineException {
        assertArgumentNotNull(context, "context");
        GameAttributes.ROOT_STAGE.getValue(context.getState()).activate(context);
    }
}
