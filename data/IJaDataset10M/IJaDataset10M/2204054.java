package org.encog.app.analyst;

import org.encog.ml.train.MLTrain;

/**
 * Reports the progress of the Encog Analyst. If you would like to use this with
 * an Encog StatusReportable object, use the bridge utilituy object:
 * 
 * org.encog.app.analyst.util.AnalystReportBridge
 * 
 */
public interface AnalystListener {

    /**
	 * Request stop the entire process.
	 */
    void requestShutdown();

    /**
	 * Request to cancel current command.
	 */
    void requestCancelCommand();

    /**
	 * @return True if the entire process should be stopped.
	 */
    boolean shouldShutDown();

    /**
	 * @return True if the current command should be stopped.
	 */
    boolean shouldStopCommand();

    /**
	 * Report that a command has begun.
	 * @param total The total parts.
	 * @param current The current part.
	 * @param name The name of that command.
	 */
    void reportCommandBegin(int total, int current, String name);

    /**
	 * Report that a command has ended.
	 * @param canceled True if this command was canceled.
	 */
    void reportCommandEnd(boolean canceled);

    /**
	 * Report that training has begun.
	 */
    void reportTrainingBegin();

    /**
	 * Report that training has ended.
	 */
    void reportTrainingEnd();

    /**
	 * Report progress on training.
	 * @param train The training object.
	 */
    void reportTraining(MLTrain train);

    /**
	 * Report progress on a task.
	 * @param total The total number of commands.
	 * @param current The current command.
	 * @param message The message.
	 */
    void report(int total, int current, String message);
}
