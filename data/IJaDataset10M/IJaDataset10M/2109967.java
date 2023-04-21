package org.esb.jmx;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MultiCastClient extends Thread {

    MulticastSocket socket;

    public MultiCastClient() throws IOException {
        socket = new MulticastSocket(6000);
        socket.setReuseAddress(true);
        InetAddress address = InetAddress.getByName("239.255.0.1");
        socket.joinGroup(address);
    }

    public void run() {
        try {
            DatagramPacket packet;
            byte[] buf = new byte[256];
            packet = new DatagramPacket(buf, buf.length);
            while (true) {
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Quote of the Moment: " + received);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
