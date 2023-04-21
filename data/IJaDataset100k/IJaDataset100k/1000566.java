package net.sourceforge.acelogger.execution.manager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import net.sourceforge.acelogger.execution.LogController;

/**
 * Abstracts all common tasks for {@link ExecutorManager} based on {@link ExecutorService}.
 * 
 * @author Zardi (https://sourceforge.net/users/daniel_zardi)
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class BaseUtilConcurrentExecutorManager extends BaseExecutorManager {

    /**
	 * The service that will executor our tasks.
	 */
    private ExecutorService executor;

    /**
	 * Constructs a new BaseUtilConcurrentExecutorManager with the supplied identifier and executor
	 * service.
	 * 
	 * @param identifier
	 *            The string that identifies this executor manager.
	 * @param executor
	 *            The {@link ExecutorService} that will be used to execute our tasks.
	 * @since 1.0.0
	 */
    public BaseUtilConcurrentExecutorManager(String identifier, ExecutorService executor) {
        super(identifier);
        setExecutor(executor);
    }

    /**
	 * Sets the executor service that will be used to execute our tasks.
	 * 
	 * @param executor
	 *            The {@link ExecutorService} that will be used.
	 * @since 1.0.0
	 */
    private void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    /** {@inheritDoc} */
    public void execute(Runnable command) {
        executor.execute(command);
    }

    /** {@inheritDoc} */
    public boolean isTerminated() {
        return executor.isTerminated();
    }

    /** {@inheritDoc} */
    public boolean orderProperShutdown() {
        executor.shutdown();
        return executor.isShutdown();
    }

    /** {@inheritDoc} */
    public boolean awaitTermination(long terminationTimeout) {
        if (!executor.isTerminated()) {
            try {
                executor.awaitTermination(terminationTimeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                LogController.getInternalLogger().error("Interrupted while wainting for executor \"{0}\" shutdown", e, getIdentifier());
            }
        }
        return executor.isTerminated();
    }

    /** {@inheritDoc} */
    public List<Runnable> terminateAndRetrieveTasks() {
        return executor.shutdownNow();
    }
}
