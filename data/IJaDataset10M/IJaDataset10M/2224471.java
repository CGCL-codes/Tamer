package owagate.util;

/**
 This class represents an object with a worker thread. Derived types implement
 the abstract run method and should respect thread interruption requests in a
 timely manner.
 */
public abstract class RunningObject implements Destroyable {

    /**
     This interface is used to notify a managing object at thread end.
     */
    public static interface NotificationSink {

        public void onEnd(RunningObject object);
    }

    /**
     Starts the worker thread and remembers the given sink.
     */
    public void start(NotificationSink sink) {
        mSink = sink;
        mThread.start();
    }

    /**
     Stops this object and waits for its worker thread to terminate.
     */
    public void destroy() {
        mInterrupted = true;
        if (!mThread.isAlive()) return;
        mThread.interrupt();
        try {
            mThread.join();
        } catch (Exception ex) {
        }
    }

    /** Constructs a new instance of RunningObject. */
    protected RunningObject() {
        mThread = new Thread(new Runnable() {

            public void run() {
                threadMain();
            }
        });
    }

    protected boolean interrupted() {
        return mInterrupted;
    }

    protected abstract void run() throws Exception;

    private final void threadMain() {
        try {
            run();
        } catch (Exception ex) {
        } finally {
            if (mSink != null && !mInterrupted) mSink.onEnd(this);
        }
    }

    private NotificationSink mSink = null;

    private final Thread mThread;

    private volatile boolean mInterrupted = false;
}
