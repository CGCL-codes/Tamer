package jtcpfwd.listener;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InternalListener extends Listener {

    public static final String SYNTAX = "Internal@<name>";

    private static Map boundListeners = new HashMap();

    public static synchronized InternalListener getListener(String name) {
        return (InternalListener) boundListeners.get(name);
    }

    private final List socketsToAccept = new ArrayList();

    public InternalListener(String rule) throws Exception {
        synchronized (InternalListener.class) {
            if (boundListeners.containsKey(rule.toLowerCase())) {
                throw new IOException("Bind failed: " + rule);
            }
            boundListeners.put(rule, this);
        }
    }

    public synchronized void addSocketToAccept(Socket socket) {
        socketsToAccept.add(socket);
        notifyAll();
    }

    protected synchronized Socket tryAccept() throws IOException {
        try {
            while (!disposed && socketsToAccept.size() == 0) wait();
            if (disposed) return null;
            return (Socket) socketsToAccept.remove(0);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    protected synchronized void tryDispose() throws IOException {
        disposed = true;
        notifyAll();
    }

    ;
}
