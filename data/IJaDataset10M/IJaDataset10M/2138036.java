package de.neihtin.bacilli.backend.net;

import de.neihtin.bacilli.backend.Backend;
import java.net.Socket;

/**
 *
 * @author clonejo
 */
public class ServerSocketHandler implements Runnable {

    private Backend backend;

    private Socket socket;

    public ServerSocketHandler(Backend backend, Socket socket) {
        this.backend = backend;
        this.socket = socket;
    }

    @Override
    public void run() {
        backend.addViewListener(new NetViewListener(this));
    }
}
