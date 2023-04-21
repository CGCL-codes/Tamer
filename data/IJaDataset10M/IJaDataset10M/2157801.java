package jtcpfwd.listener;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import jtcpfwd.util.FileReceiver;
import jtcpfwd.util.FileSender;
import jtcpfwd.util.PipedSocketImpl;
import jtcpfwd.util.WrappedPipedOutputStream;

public class FileListener extends Listener {

    public static final String SYNTAX = "File@<directory>";

    public static final Class[] getRequiredClasses() {
        return new Class[] { FileSender.class, FileReceiver.class, PipedSocketImpl.class, PipedSocketImpl.PipedSocket.class, WrappedPipedOutputStream.class };
    }

    private final List knownEntries = new ArrayList();

    private final File directory;

    public FileListener(String rule) throws Exception {
        directory = new File(rule);
        if (!directory.isDirectory()) throw new IOException("Not a directory: " + directory);
    }

    protected Socket tryAccept() throws IOException {
        while (!disposed) {
            String[] entries = directory.list();
            for (int i = 0; i < entries.length; i++) {
                if (knownEntries.contains(entries[i])) continue;
                knownEntries.add(entries[i]);
                if (!new File(directory, entries[i]).isDirectory()) continue;
                if (!new File(directory, entries[i] + "/ack").exists()) {
                    return buildSocket(new File(directory, entries[i]));
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }
        return null;
    }

    protected void tryDispose() throws IOException {
    }

    private Socket buildSocket(File basedir) throws IOException {
        if (!new File(basedir, "ack").createNewFile()) throw new IOException("Unable to create Ack file");
        PipedSocketImpl sock = new PipedSocketImpl();
        Socket socket = sock.createSocket();
        new FileSender(basedir, 'b', socket, sock.getLocalInputStream()).start();
        new FileReceiver(basedir, 'f', socket, sock.getLocalOutputStream()).start();
        return socket;
    }
}
