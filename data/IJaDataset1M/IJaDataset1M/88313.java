package libomv.utils;

public class TimeoutEvent<T> {

    private boolean fired = false;

    private T object = null;

    public synchronized void reset() {
        this.fired = false;
        this.object = null;
        this.notifyAll();
    }

    public synchronized void set(T object) {
        this.object = object;
        this.fired = (object != null);
        this.notifyAll();
    }

    /**
	 * Wait on the timeout to be triggerd or until the timeout occurres
	 * 
	 * @param timeout
	 *            The amount of milliseconds to wait. -1 will wait indefinitely
	 * @return
	 * @throws InterruptedException
	 */
    public synchronized T waitOne(long timeout) throws InterruptedException {
        if (!fired) if (timeout < 0) wait(timeout); else wait();
        return object;
    }
}
