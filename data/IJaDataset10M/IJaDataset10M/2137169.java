package org.gudy.azureus2.ui.swt.updater.snippets;

import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * @author Olivier Chalouhi
 *
 */
public class Started {

    public static void main(String args[]) {
        boolean ok = false;
        try {
            while (!ok) {
                try {
                    ServerSocket server = new ServerSocket(6880, 50, InetAddress.getByName("127.0.0.1"));
                    ok = true;
                    server.close();
                } catch (Exception e) {
                    Logger.log("Exception while trying to bind on port 6880 : " + e);
                    Thread.sleep(1000);
                }
            }
        } catch (Exception e) {
            Logger.log("Exception while running Started : " + e);
        }
    }
}
