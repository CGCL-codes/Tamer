package net.test;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import net.DatagramReader;
import net.ReceiveVerifier;
import net.ReliableDatagramReader;
import net.ReliableDatagramWriter;
import net.SendVerifier;
import io.IO;
import io.Repository;

public class ReliableClient {

    public static void main(String[] args) throws Exception {
        Repository rep = Repository.getInstance();
        rep.put(SimpleObject.class);
        InetAddress host = InetAddress.getLocalHost();
        int port = 7777;
        SocketAddress address = new InetSocketAddress(host, port);
        DatagramSocket socket = new DatagramSocket(address);
        final ReliableDatagramWriter writer = new ReliableDatagramWriter(socket, new ReliableDatagramWriter.Listener() {

            public boolean handleThrown(ReliableDatagramWriter writer, Throwable th) {
                return false;
            }

            public boolean timeout(ReliableDatagramWriter writer, byte[] data, InetAddress addr, int port, long stamp) {
                long id = IO.getLong(data, 0);
                System.out.println(String.format("timeout due to undelivered packet #%s", id));
                System.exit(0);
                return false;
            }
        });
        new ReliableDatagramReader(socket, new ReliableDatagramReader.Listener() {

            public boolean handleThrown(DatagramReader reader, Throwable ex) {
                return false;
            }

            public void received(DatagramReader reader, byte[] data, InetAddress host, int port) {
                long id = IO.getLong(data, 0);
                boolean ack = IO.getBoolean(data, 8);
                if (ack) {
                    SendVerifier verifier = writer.getSendVerifier();
                    if (verifier.verify(data, host, port)) {
                        verifier.remove(data, host, port);
                        System.out.println(String.format("received: id #%s, ack %s now is %s", id, ack, System.currentTimeMillis()));
                    }
                }
            }
        }, new ReceiveVerifier.Listener() {

            public boolean expire(byte[] data, InetAddress host, int port, long stamp) {
                long id = IO.getLong(data, 0);
                System.out.println("client expiring: " + id);
                return false;
            }

            public void expired(int count) {
                System.out.println("" + count + " items expired");
            }
        });
        SimpleObject o = new SimpleObject();
        o.i = 7;
        writer.send(InetAddress.getLocalHost(), 8000, o);
    }
}
