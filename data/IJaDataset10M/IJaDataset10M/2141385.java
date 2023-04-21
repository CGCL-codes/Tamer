package com.sun.jmx.remote.internal;

import java.io.IOException;
import java.io.InterruptedIOException;
import com.sun.jmx.remote.util.ClassLogger;
import com.sun.jmx.remote.util.EnvHelp;

public abstract class ClientCommunicatorAdmin {

    public ClientCommunicatorAdmin(long period) {
        this.period = period;
        if (period > 0) {
            checker = new Checker();
            Thread t = new Thread(checker);
            t.setDaemon(true);
            t.start();
        } else checker = null;
    }

    /**
     * Called by a client to inform of getting an IOException.
     */
    public void gotIOException(IOException ioe) throws IOException {
        restart(ioe);
    }

    /**
     * Called by this class to check a client connection.
     */
    protected abstract void checkConnection() throws IOException;

    /**
     * Tells a client to re-start again.
     */
    protected abstract void doStart() throws IOException;

    /**
     * Tells a client to stop because failing to call checkConnection.
     */
    protected abstract void doStop();

    /**
     * Terminates this object.
     */
    public void terminate() {
        synchronized (lock) {
            if (state == TERMINATED) {
                return;
            }
            state = TERMINATED;
            lock.notifyAll();
            if (checker != null) checker.stop();
        }
    }

    private void restart(IOException ioe) throws IOException {
        synchronized (lock) {
            if (state == TERMINATED) {
                throw new IOException("The client has been closed.");
            } else if (state == FAILED) {
                throw ioe;
            } else if (state == RE_CONNECTING) {
                while (state == RE_CONNECTING) {
                    try {
                        lock.wait();
                    } catch (InterruptedException ire) {
                        InterruptedIOException iioe = new InterruptedIOException(ire.toString());
                        EnvHelp.initCause(iioe, ire);
                        throw iioe;
                    }
                }
                if (state == TERMINATED) {
                    throw new IOException("The client has been closed.");
                } else if (state != CONNECTED) {
                    throw ioe;
                }
            } else {
                state = RE_CONNECTING;
                lock.notifyAll();
            }
        }
        try {
            doStart();
            synchronized (lock) {
                if (state == TERMINATED) {
                    throw new IOException("The client has been closed.");
                }
                state = CONNECTED;
                lock.notifyAll();
            }
            return;
        } catch (Exception e) {
            logger.warning("restart", "Failed to restart: " + e);
            logger.debug("restart", e);
            synchronized (lock) {
                if (state == TERMINATED) {
                    throw new IOException("The client has been closed.");
                }
                state = FAILED;
                lock.notifyAll();
            }
            try {
                doStop();
            } catch (Exception eee) {
            }
            terminate();
            throw ioe;
        }
    }

    private class Checker implements Runnable {

        public void run() {
            myThread = Thread.currentThread();
            while (state != TERMINATED && !myThread.isInterrupted()) {
                try {
                    Thread.sleep(period);
                } catch (InterruptedException ire) {
                }
                if (state == TERMINATED || myThread.isInterrupted()) {
                    break;
                }
                try {
                    checkConnection();
                } catch (Exception e) {
                    synchronized (lock) {
                        if (state == TERMINATED || myThread.isInterrupted()) {
                            break;
                        }
                    }
                    e = (Exception) EnvHelp.getCause(e);
                    if (e instanceof IOException && !(e instanceof InterruptedIOException)) {
                        try {
                            restart((IOException) e);
                        } catch (Exception ee) {
                            logger.warning("Checker-run", "Failed to check connection: " + e);
                            logger.warning("Checker-run", "stopping");
                            logger.debug("Checker-run", e);
                            break;
                        }
                    } else {
                        logger.warning("Checker-run", "Failed to check the connection: " + e);
                        logger.debug("Checker-run", e);
                        break;
                    }
                }
            }
            if (logger.traceOn()) {
                logger.trace("Checker-run", "Finished.");
            }
        }

        private void stop() {
            if (myThread != null && myThread != Thread.currentThread()) {
                myThread.interrupt();
            }
        }

        private Thread myThread;
    }

    private final Checker checker;

    private long period;

    private static final int CONNECTED = 0;

    private static final int RE_CONNECTING = 1;

    private static final int FAILED = 2;

    private static final int TERMINATED = 3;

    private int state = CONNECTED;

    private final int[] lock = new int[0];

    private static final ClassLogger logger = new ClassLogger("javax.management.remote.misc", "ClientCommunicatorAdmin");
}
