package verifier.task;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Use a job pool to compute a task in parallel. The parallelism comes from
 * tasks which are dependent on any task given. (Any dependent tasks can be
 * computed in parallel.)
 * 
 * @author kyle
 * 
 */
public class Pool {

    private final int _threads;

    private final Queue<Task> _ready;

    private volatile boolean _running = false;

    /**
	 * @param threads
	 *            Use this number of threads in parallel to execute tasks.
	 */
    public Pool(int threads) {
        _threads = threads;
        _ready = new LinkedList<Task>();
    }

    /**
	 * Start the threads.
	 */
    public synchronized void start() {
        if (_running) return;
        _running = true;
        for (int lcv = 0; lcv < _threads; lcv++) {
            new Thread(new Runnable() {

                public void run() {
                    try {
                        thread();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
            }).start();
        }
    }

    /**
	 * Stop the threads.
	 */
    public synchronized void stop() {
        _running = false;
        notifyAll();
    }

    /**
	 * @param task
	 *            Schedule this task to run.
	 */
    public synchronized void run(Task task) {
        _ready.add(task);
        notify();
    }

    /**
	 * Dequeue a task. If no task is available, this call will block until one
	 * becomes available.
	 * 
	 * @return This method returns the dequeued task.
	 */
    public synchronized Task getJob() {
        while (_running && _ready.size() == 0) try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return _ready.poll();
    }

    /**
	 * @return This method returns true if the pool is running
	 */
    public boolean running() {
        return _running;
    }

    private void thread() {
        while (_running) {
            Task job = getJob();
            if (job != null) try {
                job.run();
            } catch (RuntimeException e) {
                stop();
            }
        }
    }
}
