package data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import broadcaster.BroadcastI;
import soct2.TSOperation;
import soct2.TSWorkspace;

public class TSMulticastClient implements TSClientI, BroadcastI {

    private MulticastSocket multicastSocket = null;

    private String address = "224.11.4.2";

    private int port = 12345;

    public TSMulticastClient() {
        this(5000);
        try {
            multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(InetAddress.getByName(address));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TSMulticastClient(int port) {
        super();
        this.port = port;
    }

    public void sendOperation(TSOperation op) {
        ObjectOutputStream oos = null;
        try {
            DatagramPacket paquet;
            byte[] tampon;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(op);
            oos.flush();
            tampon = bos.toByteArray();
            paquet = new DatagramPacket(tampon, tampon.length, InetAddress.getByName(address), port);
            multicastSocket.send(paquet);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void register(TSWorkspace ws) {
    }

    public void unregister(TSWorkspace ws) {
    }

    public void broadcast(TSWorkspace src, TSOperation op) {
        this.sendOperation(op);
    }

    public BroadcastI clone() {
        return null;
    }
}
