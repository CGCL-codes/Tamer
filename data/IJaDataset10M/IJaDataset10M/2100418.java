package blueprint4j.utils;

/**
* Used by log to maintain a new ID per thread
*/
public abstract class LogThread {

    private static VectorLogThread threads = new VectorLogThread();

    private boolean please_close = false;

    private boolean active = false;

    private Thread internal_thread = null;

    protected abstract void run();

    public LogThread() {
        super();
        threads.add(this);
    }

    public long getThreadId() {
        return ThreadId.findThreadId(internal_thread).getId();
    }

    protected boolean mustShutdown() {
        return please_close;
    }

    public void startShutdown() {
        please_close = true;
    }

    public boolean isActive() {
        return active;
    }

    private void do_run() {
        active = true;
        run();
        active = false;
        internal_thread = null;
        ThreadId.removeThread();
    }

    public void start() {
        internal_thread = new Thread() {

            public void run() {
                do_run();
            }
        };
        internal_thread.start();
    }

    public void sleep(long ms) throws InterruptedException {
        Thread.sleep(ms);
    }

    public void sleep(long ms, int nano) throws InterruptedException {
        Thread.sleep(ms, nano);
    }

    public static void startShutdownAll() {
        for (int i = 0; i < threads.size(); i++) {
            threads.get(i).startShutdown();
        }
    }

    public static boolean allShutdown() {
        for (int i = 0; i < threads.size(); i++) {
            if (threads.get(i).isActive()) {
                return true;
            }
        }
        return false;
    }

    public static void waitForShutdownAll(int timeout_ms) throws InterruptedException {
        for (int counter = 0; counter < timeout_ms; counter += 10) {
            if (allShutdown()) {
                return;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ie) {
                Log.debug.out(ie);
            }
        }
        String thread_list = "";
        for (int i = 0; i < threads.size(); i++) {
            if (threads.get(i).isActive()) {
                thread_list += threads.get(i).getClass().getName() + "\n";
            }
        }
        throw new InterruptedException("Thread close not possible after " + timeout_ms + "ms forced applicaitons close anyway.\n" + thread_list);
    }
}
