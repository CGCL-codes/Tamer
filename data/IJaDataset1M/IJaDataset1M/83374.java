package org.tru42.signal.processors.io;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import org.tru42.signal.lang.SObject;
import org.tru42.signal.lang.annotation.Sink;

public class TCPClient extends SObject {

    protected Socket socket;

    protected int port = 0;

    protected String host = "";

    protected InputStreamSignal streamSource;

    protected OutputStream outStream;

    private Thread listenThread;

    public TCPClient() {
        super();
        streamSource = new InputStreamSignal(this, "output");
        signals.put(streamSource.getName(), streamSource);
    }

    @Sink
    public void input(long timestamp, byte value) {
        if (outStream == null) return;
        try {
            outStream.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Sink
    public void port(Integer value) {
        this.port = value;
        close();
        initSocket();
    }

    @Sink
    public void host(String name) {
        this.host = name;
        close();
        initSocket();
    }

    @Override
    public void dispose() {
        super.dispose();
        close();
    }

    protected void close() {
        outStream = null;
        if (socket != null) try {
            socket.close();
        } catch (IOException e) {
        }
    }

    protected void initSocket() {
        if (listenThread != null) listenThread.interrupt();
        if (host == null || port < 0) return;
        listenThread = new Thread(new Runnable() {

            public void run() {
                try {
                    while (!Thread.interrupted()) {
                        socket = new Socket(Inet4Address.getByName(host), port);
                        System.out.println("TCP connected to " + host + ":" + port);
                        streamSource.setInputStream(socket.getInputStream());
                        outStream = socket.getOutputStream();
                        while (streamSource.getThread().isAlive()) Thread.sleep(500);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, getName() + " connect");
        listenThread.start();
    }
}
