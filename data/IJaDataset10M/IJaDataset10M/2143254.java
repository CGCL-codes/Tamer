package net.grinder.engine.agent;

import java.io.OutputStream;
import net.grinder.engine.common.EngineException;

/**
 * Interface implemented by classes that can start a worker.
 *
 * @author Philip Aston
 * @version $Revision: 3832 $
 */
interface WorkerFactory {

    Worker create(OutputStream outputStream, OutputStream errorStream) throws EngineException;
}
