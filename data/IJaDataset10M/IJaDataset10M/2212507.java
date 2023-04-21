package org.nakedobjects.viewer.nuthatch.telnet;

import org.nakedobjects.object.application.ApplicationClass;
import org.nakedobjects.utility.NakedObjectRuntimeException;
import org.nakedobjects.viewer.nuthatch.DefaultController;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TelnetServer {

    private ServerSocket socket;

    private final ApplicationClass[] classes;

    public TelnetServer(final ApplicationClass[] classes) {
        super();
        this.classes = classes;
    }

    public void run() {
        try {
            socket = new ServerSocket(9876);
            while (true) {
                final Socket connection = socket.accept();
                new Thread(new Runnable() {

                    public void run() {
                        DefaultController c = new DefaultController();
                        c.setClasses(classes);
                        c.setInput(new TelnetInput(connection));
                        c.setView(new TelnetView(connection));
                        c.init();
                    }
                });
            }
        } catch (IOException e) {
            throw new NakedObjectRuntimeException("Failed to start up server", e);
        }
    }

    public void shutdown() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ignore) {
            }
        }
    }
}
