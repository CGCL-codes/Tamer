package com.art.anette.client.network;

import java.io.IOException;
import java.io.StreamCorruptedException;
import com.art.anette.client.database.ClientDBControl;
import com.art.anette.common.logging.LogController;
import com.art.anette.common.logging.Logger;
import com.art.anette.common.network.DataResponse;
import com.art.anette.common.network.Ping;
import com.art.anette.common.network.Pong;
import com.art.anette.common.network.Response;

/**
 * Dieser Thread wartet auf Daten vom Server und leitet sie für die weitere
 * Verarbeitung zum Datenbank-Controller weiter.
 *
 * @author Alexander von Bremen-Kühne, Markus Groß
 */
public class SyncDownThread extends Thread {

    /**
     * Der Datenbank-Controller.
     */
    private ClientDBControl cdbc;

    /**
     * Gibt an, ob der Thread pausiert ist.
     */
    private boolean paused;

    private static final Logger logger = LogController.getLogger(SyncDownThread.class);

    /**
     * Erstellt einen neuen SyncDown-Thread.
     *
     * @param cdbc Der Datenbank-Controller
     */
    public SyncDownThread(final ClientDBControl cdbc) {
        this.cdbc = cdbc;
        paused = false;
    }

    /**
     * Liefert zurück, ob der Thread pausiert ist.
     *
     * @return True, falls der Thread pausiert ist.
     */
    public synchronized boolean isPaused() {
        return paused;
    }

    /**
     * Pausiert den Thread.
     */
    public synchronized void pause() {
        paused = true;
    }

    /**
     * Friert den Thread ein.
     */
    private synchronized void freeze() {
        try {
            wait();
        } catch (InterruptedException ex) {
            interrupt();
        }
    }

    /**
     * Weckt den Thread auf.
     */
    public synchronized void wakeup() {
        paused = false;
        notify();
    }

    /**
     * Solange der Stream nicht gültig ist, schläft der Thread.
     * Wenn der Stream gültig ist, wartet der Thread auf Daten vom Server.
     * Falls die Verbindung unterbrochen wird, wird dem Netzwerk-Controller
     * dies durch einen Verbindung-Abbruch mitgeteilt. Der Thread legt sich dann
     * wieder schlafen, bis eine neue Verbindung besteht.
     */
    @Override
    public void run() {
        while (!isInterrupted()) {
            while (!NetworkControl.getInstance().isConnected() && !isInterrupted()) {
                pause();
                while (isPaused() && !isInterrupted()) {
                    freeze();
                }
            }
            boolean problem = false;
            try {
                if (NetworkControl.getInstance().isConnected()) {
                    final Response obj = NetworkControl.getInstance().receive();
                    if (obj instanceof Ping) {
                        Ping ping = (Ping) obj;
                        cdbc.getSyncUpThread().addObject(new Pong(ping.getValue()));
                        cdbc.getSyncUpThread().wakeup();
                    } else if (obj instanceof DataResponse) {
                        DataResponse dataResponse = (DataResponse) obj;
                        cdbc.adoptList(dataResponse.getObjects());
                    }
                }
            } catch (StreamCorruptedException ex) {
                problem = true;
                logger.severe("Different versions of the server and client detected", ex);
            } catch (ClassNotFoundException ex) {
                problem = true;
                logger.severe(null, ex);
            } catch (IOException ex) {
                problem = true;
                if (!com.art.anette.server.network.NetworkControl.wasCausedByClosedSocket(ex)) {
                    logger.severe("Problem reading from server", ex);
                } else {
                    logger.info("Lost the connection to the server", ex);
                }
            }
            if (problem) {
                NetworkControl.getInstance().disconnect(false, false, false);
            }
        }
    }
}
