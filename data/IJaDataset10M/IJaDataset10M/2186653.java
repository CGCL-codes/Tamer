package org.encog.engine.network.train.gradient;

import org.encog.engine.concurrency.EngineTask;
import org.encog.engine.network.flat.FlatNetwork;

/**
 * An interface used to define gradient workers for flat networks.
 *
 */
public interface FlatGradientWorker extends EngineTask {

    /**
     * @return The weights for this worker.
     */
    double[] getWeights();

    /**
     * @return The network being trained by this thread.
     */
    FlatNetwork getNetwork();

    /**
     * @return The elapsed time for the last iteration of this worker.
     */
    long getElapsedTime();
}
