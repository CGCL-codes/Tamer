package controller;

import controller.javacc.*;
import model.Files;
import model.FileNode;
import model.Config;
import view.Messages;
import java.util.*;
import java.io.FileInputStream;

/** Group state of all the working threads */
enum State {

    RUNNING, PAUSED, STOPPED
}

/** Working (parsing) thread */
public class Processor implements Runnable, Observer {

    /** Minimum number of working threads */
    public static final int MIN_THREADS = 1;

    /** Maximum number of working threads */
    public static final int MAX_THREADS = 5;

    /** Processor state */
    private static volatile State state_ = State.STOPPED;

    /** Thread number */
    private final int number_;

    /** Errors number */
    private static int errno_;

    /**
	 * Creation not allowed for other classes.
	 * @param i thread number (unique among working threads) 
	 */
    private Processor(int i) {
        number_ = i;
    }

    /**
	 * @param threadsNum number of working threads. 
	 * Must be in range [ {@link #MIN_THREADS}, {@link #MAX_THREADS} ]
	 * @throws IllegalArgumentException if improper 'threadsNum' was given
	 * @throws IllegalStateException if processor is already running
	 * (only 1 instance at a time allowed)
	 */
    public static void start(final int threadsNum) {
        final Messages msg = Messages.getInstance();
        final Router router = Router.getInstance();
        if (threadsNum < MIN_THREADS || threadsNum > MAX_THREADS) throw new IllegalArgumentException(msg.get("wrong threads number"));
        synchronized (Processor.class) {
            if (state_ != State.STOPPED) throw new IllegalStateException(msg.get("only stopped processor can be run")); else {
                state_ = State.RUNNING;
                errno_ = 0;
                router.dispatch(Router.Event.FILES_START);
            }
        }
        new Thread(new Runnable() {

            public void run() {
                long t1 = System.currentTimeMillis();
                try {
                    router.dispatch(Router.Event.FILES_READ_START);
                    Files.getInstance().readTree();
                    router.dispatch(Router.Event.FILES_READ_END);
                    Thread[] threads = new Thread[threadsNum];
                    for (int i = 0; i < threadsNum; i++) {
                        threads[i] = new Thread(new Processor(i));
                        threads[i].start();
                    }
                    for (int i = 0; i < threadsNum; i++) {
                        threads[i].join();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                    router.dispatch(Router.Event.ERROR, ex.getMessage());
                }
                long t2 = System.currentTimeMillis();
                long t = t2 - t1;
                String time = "" + (int) (t / 1000) + "." + t % 1000 + "s";
                stop();
                router.dispatch(Router.Event.FILES_ALL_END, time);
                System.err.println("bledow: " + errno_);
            }
        }).start();
    }

    /** Pause all the working threads */
    public static void pause() {
        synchronized (Processor.class) {
            if (state_ == State.RUNNING) {
                state_ = State.PAUSED;
                Router.getInstance().dispatch(Router.Event.FILES_PAUSE);
            }
        }
    }

    /** Resume all the working threads (if they are paused) */
    public static void resume() {
        synchronized (Processor.class) {
            if (state_ == State.PAUSED) {
                state_ = State.RUNNING;
                Router.getInstance().dispatch(Router.Event.FILES_RESUME);
                Processor.class.notifyAll();
            }
        }
    }

    /** Stop all the processing threads */
    public static void stop() {
        synchronized (Processor.class) {
            if (state_ == State.PAUSED || state_ == State.RUNNING) {
                state_ = State.STOPPED;
                Processor.class.notifyAll();
                Router.getInstance().dispatch(Router.Event.FILES_STOP);
            }
        }
    }

    /** Parse as many files as you can */
    public void run() {
        final Files ft = Files.getInstance();
        final Router router = Router.getInstance();
        final Messages msg = Messages.getInstance();
        FileNode node;
        FileInputStream is;
        PHP parser = null;
        router.dispatch(Router.Event.LOG, new Formatter().format(msg.get("thread %d started"), number_).toString());
        try {
            while ((node = ft.getNext()) != null) {
                try {
                    is = new FileInputStream(node.getFilePath());
                } catch (java.io.FileNotFoundException ex) {
                    router.dispatch(Router.Event.FILE_ERROR, node.getFilePath());
                    continue;
                }
                if (parser == null) {
                    parser = new PHP(is);
                    parser.addObserver(this);
                } else parser.ReInit(is);
                synchronized (Processor.class) {
                    if (state_ == State.PAUSED) Processor.class.wait();
                    if (state_ == State.STOPPED) break;
                }
                router.dispatch(Router.Event.FILE_START, node.getFilePath());
                try {
                    parser.setTabSize(Integer.parseInt(Config.getInstance().getProperty("tabWidth")));
                    parser.parse(node.getFilePath());
                } catch (Throwable e) {
                    router.dispatch(Router.Event.FILE_ERROR, node.getFilePath() + ": " + e.getMessage());
                    e.printStackTrace(System.err);
                    errno_++;
                }
                router.dispatch(Router.Event.FILE_END, node);
            }
        } catch (InterruptedException e) {
        }
        router.dispatch(Router.Event.LOG, new Formatter().format(msg.get("thread %d stopped"), number_).toString());
    }

    public void update(Observable o, Object data) {
        Files.getInstance().updateNode(new model.FileInclusion((ParserObserverArgs) data));
    }
}
