package org.gamegineer.game.internal.core.system.stages;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import net.jcip.annotations.Immutable;
import org.gamegineer.engine.core.IEngineContext;
import org.gamegineer.game.core.system.AbstractStageStrategy;
import org.gamegineer.game.core.system.IStage;

/**
 * The strategy for the root stage.
 * 
 * <p>
 * The root stage manages the user-defined stages specified in the game
 * configuration. Once the root stage is complete, the game itself is over.
 * </p>
 * 
 * <p>
 * This class is immutable.
 * </p>
 */
@Immutable
public final class RootStageStrategy extends AbstractStageStrategy {

    /**
     * Initializes a new instance of the {@code RootStageStrategy} class.
     */
    public RootStageStrategy() {
        super();
    }

    public boolean isComplete(final IStage stage, final IEngineContext context) {
        assertArgumentNotNull(stage, "stage");
        assertArgumentNotNull(context, "context");
        return true;
    }
}
