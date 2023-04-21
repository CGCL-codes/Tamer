package net.dataforte.canyon.support;

import java.io.File;
import java.util.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileObserver extends Observable implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(FileObserver.class);

    long lastModified;

    String file;

    public static final long DEFAULT_DELAY = 5000;

    Thread thread;

    long delay;

    boolean exitOnChange;

    public FileObserver(String s) {
        file = s;
        delay = DEFAULT_DELAY;
        start(null);
    }

    public FileObserver(String s, ThreadGroup group) {
        file = s;
        delay = DEFAULT_DELAY;
        start(group);
    }

    public FileObserver(String s, ThreadGroup group, boolean exitOnChange) {
        file = s;
        delay = DEFAULT_DELAY;
        this.exitOnChange = exitOnChange;
        start(group);
    }

    public boolean check() {
        File f = new File(file);
        long actualLastModified = f.lastModified();
        if (actualLastModified == 0l) {
            thread.interrupt();
            return false;
        } else if (lastModified != actualLastModified) {
            lastModified = actualLastModified;
            setChanged();
            notifyObservers();
            return true;
        } else {
            return false;
        }
    }

    public void start(ThreadGroup group) {
        File f = new File(file);
        lastModified = f.lastModified();
        if (group != null) thread = new Thread(group, this, "Observe: " + file); else thread = new Thread(this, "Observe: " + file);
        thread.start();
    }

    public void run() {
        try {
            for (; ; ) {
                Thread.sleep(delay);
                boolean res = check();
                if (exitOnChange && res) {
                    break;
                }
            }
        } catch (InterruptedException e) {
        } catch (Throwable t) {
            log.error("", t);
        }
    }
}
