package ihaterobots;

/**
 *
 * @author cghislai
 */
public abstract class GameTask implements Runnable {

    private final Object lock;

    private boolean running;

    private Object data;

    private Thread thread;

    private final String name;

    public GameTask(String name) {
        lock = new Object();
        this.name = name;
    }

    public void start() {
        if (running) {
            return;
        }
        thread = new Thread(this, name);
        thread.start();
        running = true;
    }

    public final void publish(Object value) {
        synchronized (lock) {
            data = value;
        }
    }

    public Object getValue() {
        synchronized (lock) {
            return data;
        }
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public final void run() {
        try {
            running = true;
            execute();
            onSucceeded();
            running = false;
        } catch (Exception e) {
            onException(e);
            running = false;
        }
    }

    public final void interupt() {
        if (!running) {
            thread.interrupt();
            onInterrupt();
        }
    }

    public abstract void execute();

    protected void onInterrupt() {
    }

    protected void onSucceeded() {
    }

    protected void onException(Throwable t) {
        throw new RuntimeException(t);
    }
}
