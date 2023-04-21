package se.uu.it.cats.brick.network;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import se.uu.it.cats.brick.Logger;

public class ConnectionListener implements Runnable {

    private static volatile boolean _canListen = true;

    public static boolean canListen() {
        return _canListen;
    }

    public static void setListen(boolean l) {
        _canListen = l;
    }

    public void run() {
        while (true) {
            if (canListen()) {
                BTConnection btc = Bluetooth.waitForConnection();
                if (btc != null) {
                    Logger.println("Received connection from: " + btc.getAddress());
                    ConnectionManager.getInstance().openConnection(btc);
                } else {
                    Logger.println("ConnectionListener got NP");
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
            }
        }
    }
}
