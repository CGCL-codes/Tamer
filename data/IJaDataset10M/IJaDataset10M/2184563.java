package org.eoti.net.mcast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MulticastServer<DATA> {

    protected Multicast mcast;

    public MulticastServer() throws UnknownHostException {
        mcast = new Multicast();
    }

    public MulticastServer(Multicast mcast) throws UnknownHostException {
        this.mcast = mcast;
    }

    public void notify(String eventType, DATA data) throws SocketException, IOException {
        notify(new MulticastEvent<DATA>(eventType, data));
    }

    public void notify(MulticastEvent<DATA> event) throws SocketException, IOException {
        DatagramSocket socket = new DatagramSocket();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(mcast.getMaxPacketSize());
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(event);
        out.flush();
        byte[] buf = baos.toByteArray();
        out.close();
        baos.close();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, mcast.getGroupAddress(), mcast.getPort());
        socket.send(packet);
        socket.close();
    }
}
