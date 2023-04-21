package com.pz.net;

import com.pz.util.MessageMannager;
import com.pz.util.Log;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import static com.pz.util.Log.d;
import static com.pz.util.ByteBufferHelper.getCommand;
import static com.pz.net.Command.*;

/**
 *
 * @author jannek
 */
public class Client implements Runnable {

    public final int id;

    public final String name;

    protected int key;

    private final MasterServerLink link;

    private Thread thread = null;

    private boolean running = false;

    private Other server;

    private NetworkListener listener;

    protected DatagramChannel udp;

    private ByteBuffer buffer;

    private MessageMannager mm;

    private boolean udpConfirmedByMasterServer;

    private long udpExpired;

    protected Client(int id, int key, String name, MasterServerLink link) throws IOException {
        this.id = id;
        this.key = key;
        this.name = name;
        this.link = link;
        mm = MessageMannager.getMannager();
        buffer = ByteBuffer.allocate(MessageMannager.MAX_LENGHT + 50);
        udp = DatagramChannel.open();
        udp.configureBlocking(false);
        udp.socket().bind(null);
        d("Client is bound on port: " + udp.socket().getLocalPort());
        udpConfirmedByMasterServer = false;
    }

    public boolean udpConfirmed() {
        return udpConfirmedByMasterServer;
    }

    protected void connect(Other server, NetworkListener listener) {
        this.server = server;
        this.listener = listener;
        server.connect(listener);
    }

    public void run() {
        while (running) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
            try {
                update();
            } catch (IOException ex) {
                d(ex);
                running = listener.onUdpError(ex);
            }
        }
        thread = null;
    }

    public void runAsThread() {
        if (!running && thread == null) {
            thread = new Thread(this);
            running = true;
            thread.start();
        }
    }

    public void stopThread() {
        running = false;
    }

    public synchronized void update() throws IOException {
        long time = System.currentTimeMillis();
        buffer.clear();
        while (udp.receive(buffer) != null) {
            buffer.flip();
            switch(getCommand(buffer)) {
                case MASTER_UDP_CONFIRM:
                    if (key == buffer.getInt()) {
                        udpConfirmedByMasterServer = true;
                        udpExpired = time + 120000;
                    }
                    break;
                case UDP_MESSAGE:
                    if (server != null && server.id == buffer.getInt() && server.key == buffer.getInt()) {
                        short messageId = buffer.getShort();
                        server.sendConfirm(messageId, udp);
                        server.input(messageId, buffer.getShort(), mm.get(buffer));
                    }
                    break;
                case UDP_CONFIRM:
                    if (server != null && server.id == buffer.getInt() && server.key == buffer.getInt()) server.confirm(buffer.getShort());
                    break;
            }
            buffer.clear();
        }
        if (udpExpired < time) link.clientUdp();
        server.flush(udp);
    }

    public boolean send(ByteBuffer data) {
        if (data == null || data.remaining() > MessageMannager.MAX_LENGHT) {
            return false;
        }
        return server.send(data);
    }
}
