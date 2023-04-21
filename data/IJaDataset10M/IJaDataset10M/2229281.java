package blomo.util.loadbalancing;

/**
 * @author Malte Schulze
 * 
 * Interface for threads created in load balancers.
 */
public interface RunTask {

    /**
	 * 
	 */
    public void start();

    /**
	 * @return the currently set method executor
	 */
    public MethodExecutorWrapper getMethodExecutorWrapper();

    /**
	 * @param mew
	 */
    public void setMethodExecutor(MethodExecutorWrapper mew);

    /**
	 * 
	 */
    public void interrupt();

    /**
	 * @return true if nothing is processed at the time
	 */
    public boolean isReady();

    /**
	 * @return true if the thread is alive
	 */
    public boolean isAlive();

    /**
	 * @param i
	 * @throws InterruptedException 
	 */
    public void join(long i) throws InterruptedException;
}
