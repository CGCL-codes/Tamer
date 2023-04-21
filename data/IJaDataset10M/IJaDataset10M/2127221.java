package it.ilz.hostingjava.valves.controlli;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 *
 * @author andrea
 */
public class File extends Thread {

    static final int TIMEOUT = 60 * 60 * 1000;

    /** Creates a new instance of File */
    public File(Closeable logger) {
        this.logger = logger;
    }

    public synchronized void aggiornaTempo(long controllo) {
        lastControllo = controllo;
    }

    public void run() {
        while (!stop) {
            try {
                Thread.sleep(TIMEOUT);
                synchronized (this) {
                    if (System.currentTimeMillis() - lastControllo > TIMEOUT) {
                        try {
                            logger.close();
                        } catch (IOException ex) {
                        } finally {
                            return;
                        }
                    }
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private boolean stop = false;

    public synchronized void stopCheck() {
        stop = true;
    }

    private long lastControllo;

    private Closeable logger;
}
