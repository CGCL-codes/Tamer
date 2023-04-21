package net.sf.dz.view.tcp.client;

import net.sf.jukebox.sem.EventSemaphore;
import javax.swing.*;

public class ClientPanel extends JPanel {

    private ClientDaemon daemon;

    public ClientPanel(String remoteHost, int remotePort, boolean secure, String password) throws InterruptedException {
        daemon = new ClientDaemon(this, remoteHost, remotePort, secure, password);
        if (!daemon.start().waitFor()) {
            throw new IllegalStateException("Unable to start the client daemon, check the logs");
        }
    }

    public EventSemaphore getSemDown() {
        return daemon.getSemDown();
    }
}
