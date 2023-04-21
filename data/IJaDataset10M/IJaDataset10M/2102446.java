package net.assimilator.tools.webster;

import java.io.File;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Description
 *
 * @author Mike T. Miller
 * @version $Id:$
 */
public class FileWatcher {

    private static final long DEFAULT_DAMPING_TIME = 0;

    private static final long DEFAULT_CYCLE_TIME = 5000;

    private static final int WATCHER_THREAD_POOL_SIZE = 100;

    private Map<File, Monitor> monitors;

    private long cycleTime;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(WATCHER_THREAD_POOL_SIZE);

    public FileWatcher() {
        this(DEFAULT_CYCLE_TIME);
    }

    public FileWatcher(long cycleTime) {
        monitors = new HashMap<File, Monitor>();
        this.cycleTime = cycleTime;
    }

    public void watch(File path, FileListener listener) {
        watch(path, listener, DEFAULT_DAMPING_TIME);
    }

    public void watch(File path, FileListener listener, long dampingTime) {
        Monitor monitor = monitors.get(path);
        if (monitor == null) {
            monitor = path.isFile() ? new FileMonitor(this, path, dampingTime) : new DirectoryMonitor(this, path, dampingTime);
            monitors.put(path, monitor);
            scheduler.scheduleAtFixedRate(monitor, 0, cycleTime, TimeUnit.MILLISECONDS);
        }
        monitor.addListener(listener);
    }

    public void watch(File path, FileListener listener, long dampingTime, long lastModified) {
        Monitor monitor = monitors.get(path);
        if (monitor == null) {
            monitor = path.isFile() ? new FileMonitor(this, path, dampingTime, lastModified) : new DirectoryMonitor(this, path, dampingTime);
            scheduler.scheduleAtFixedRate(monitor, 0, cycleTime, TimeUnit.MILLISECONDS);
        }
        monitor.addListener(listener);
    }

    public void removeWatch(File path) {
        monitors.remove(path);
    }

    public static void main(String[] args) {
        FileWatcher watcher = new FileWatcher();
        watcher.watch(new File(args[0]), new FileListener() {

            public void fileChanged(FileChangedEvent event) {
                System.out.println(event.getPath().getName() + " was " + (event.wasRemoved() ? "removed" : "changed"));
            }
        }, 1000);
        while (true) try {
            Thread.sleep(10000);
        } catch (InterruptedException unused) {
        }
    }

    abstract class Monitor implements Runnable {

        File path;

        FileWatcher watcher;

        long dampingTime;

        long lastModified;

        final Set<FileListener> listeners;

        Monitor(FileWatcher watcher, File path, long dampingTime) {
            this.path = path;
            this.dampingTime = dampingTime;
            this.watcher = watcher;
            listeners = Collections.synchronizedSet(new HashSet<FileListener>());
        }

        void addListener(FileListener listener) {
            listeners.add(listener);
        }

        void notifyListeners(boolean removed) {
            FileChangedEvent event = new FileChangedEvent(path, removed);
            for (FileListener listener : listeners) listener.fileChanged(event);
        }

        boolean isModified(File watched) {
            return lastModified != watched.lastModified();
        }
    }

    class FileMonitor extends Monitor {

        boolean damped;

        FileMonitor(FileWatcher watcher, File path, long dampingTime) {
            super(watcher, path, dampingTime);
            damped = false;
            lastModified = path.lastModified();
        }

        FileMonitor(FileWatcher watcher, File path, long dampingTime, long lastModifed) {
            super(watcher, path, dampingTime);
            damped = false;
            this.lastModified = lastModifed;
        }

        public void run() {
            if (path.lastModified() == 0) {
                notifyListeners(true);
                watcher.removeWatch(path);
                throw new FileRemovedError();
            } else if (isModified(path)) {
                lastModified = path.lastModified();
                damp(path);
                notifyListeners(false);
            }
        }

        private void damp(File watched) {
            while (!damped) {
                try {
                    Thread.sleep(dampingTime);
                } catch (InterruptedException unused) {
                }
                damped = !isModified(watched);
                lastModified = watched.lastModified();
            }
        }
    }

    class DirectoryMonitor extends Monitor {

        final Collection<File> knownFiles;

        DirectoryMonitor(FileWatcher watcher, File path, long dampingTime) {
            super(watcher, path, dampingTime);
            knownFiles = new ArrayList<File>();
            updateKnownFiles();
        }

        public void run() {
            addWatchersForNewFiles();
            updateKnownFiles();
        }

        private void addWatchersForNewFiles() {
            synchronized (knownFiles) {
                for (File file : path.listFiles()) if (!knownFiles.contains(file)) {
                    knownFiles.add(file);
                    for (FileListener listener : listeners) watcher.watch(file, listener, dampingTime, 0L);
                }
            }
        }

        private void updateKnownFiles() {
            synchronized (knownFiles) {
                knownFiles.clear();
                knownFiles.addAll(Arrays.asList(path.listFiles()));
            }
        }

        void addListener(FileListener listener) {
            super.addListener(listener);
            synchronized (knownFiles) {
                for (File file : knownFiles) if (file.isFile()) watcher.watch(file, listener, dampingTime);
            }
        }
    }

    class FileRemovedError extends Error {

        public Throwable fillInStackTrace() {
            return this;
        }
    }
}
