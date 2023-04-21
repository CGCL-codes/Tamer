package org.xsocket.server;

import org.xsocket.server.MultithreadedServer;

/**
*
* @author grro@xsocket.org
*/
public abstract class AbstractServerTest {

    protected static final IMultithreadedServer createServer(int port, IHandler handler) {
        IMultithreadedServer server = null;
        do {
            try {
                server = new MultithreadedServer(port);
                server.setHandler(handler);
                Thread t = new Thread(server);
                t.start();
                do {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ignore) {
                    }
                } while (!server.isRunning());
            } catch (Exception be) {
                be.printStackTrace();
                port++;
                if (server != null) {
                    server.shutdown();
                    server = null;
                }
            }
        } while (server == null);
        return server;
    }
}
