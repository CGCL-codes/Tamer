package edu.luc.cs.trull.task;

import EDU.oswego.cs.dl.util.concurrent.Executor;
import EDU.oswego.cs.dl.util.concurrent.QueuedExecutor;
import org.apache.log4j.Logger;

/**
 * A Worker that uses a single-threaded QueuedExecutor to perform its Task.
 * @see edu.luc.cs.trull.task.Task
 */
public class QueuedTaskWorker extends AbstractTaskWorker {

    private static Logger logger = Logger.getLogger(QueuedTaskWorker.class);

    private static final Executor executor = new QueuedExecutor();

    protected final Executor getExecutor() {
        return executor;
    }

    public QueuedTaskWorker(final Task task) {
        super(task);
        setStart();
    }

    public QueuedTaskWorker(final TaskFactory taskFactory) {
        super(taskFactory);
        setStart();
    }

    private void setStart() {
        setStart(new Runnable() {

            public void run() {
                logger.debug(QueuedTaskWorker.this + ".start.run()");
                try {
                    if (state.get() == STOPPED) return;
                    if (firstTime) {
                        firstTime = false;
                    } else {
                        logger.debug(QueuedTaskWorker.this + ".start.run() restarting task");
                        task.restart();
                    }
                    if (state.get() != RUNNING) return;
                    executor.execute(repeat);
                } catch (InterruptedException e) {
                    logger.debug(QueuedTaskWorker.this + ".start.run() interrupted, returning");
                    Thread.currentThread().interrupt();
                } finally {
                    logger.debug(QueuedTaskWorker.this + ".start.run() returning");
                }
            }
        });
    }

    /**
   * A Runnable that executes one step of the task and reschedules itself
   * for execution as long as the worker has not been suspended. 
   */
    protected final Runnable repeat = new Runnable() {

        public void run() {
            try {
                logger.debug(QueuedTaskWorker.this + ".repeat.run()");
                if (state.get() != RUNNING) return;
                if (task.hasNext()) {
                    task.next();
                    if (state.get() == RUNNING) {
                        logger.debug(QueuedTaskWorker.this + ".repeat.run() rescheduling itself");
                        executor.execute(repeat);
                    }
                } else {
                    logger.debug(QueuedTaskWorker.this + ".repeat.run() scheduling termination");
                    state.set(STOPPED);
                    scheduleTermination();
                }
            } catch (InterruptedException e) {
                logger.debug(QueuedTaskWorker.this + ".repeat.run() interrupted, returning");
                Thread.currentThread().interrupt();
            } finally {
                logger.debug(QueuedTaskWorker.this + ".repeat.run() returning");
            }
        }
    };

    public void resume() {
        logger.debug(this + ".resume()");
        if (state.commit(SUSPENDED, RUNNING)) {
            try {
                executor.execute(repeat);
            } catch (InterruptedException e) {
                logger.debug(QueuedTaskWorker.this + ".resume() interrupted, returning");
                Thread.currentThread().interrupt();
            } finally {
                logger.debug(QueuedTaskWorker.this + ".resume() returning");
            }
        }
    }
}
