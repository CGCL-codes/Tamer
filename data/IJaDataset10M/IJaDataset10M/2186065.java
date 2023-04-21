package net.grinder.engine.process;

import net.grinder.engine.common.EngineException;
import net.grinder.scriptengine.ScriptEngineService.WorkerRunnable;

/**
 * Something that can create a {@link WorkerRunnable}.
 *
 * @author Philip Aston
 */
interface WorkerRunnableFactory {

    /**
   * Factory method.
   *
   * @return A new {@code WorkerRunnable}.
   * @throws EngineException If creation failed.
   */
    WorkerRunnable create() throws EngineException;
}
