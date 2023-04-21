package pl.olek.jruce;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.logging.Logger;

/**
 *
 * @author anaszko
 */
public abstract class GroupReceiver {

    public static final Logger logger = Logger.getLogger(GroupReceiver.class.getName());

    public abstract void receive(InetSocketAddress incomingAddress, InputStream input) throws Exception;

    Daemon daemon;

    public GroupReceiver(String groupName) {
        try {
            final MulticastSocket socket = new MulticastSocket(extractPort(groupName));
            final InetAddress address = InetAddress.getByName(extractIp(groupName));
            socket.joinGroup(address);
            socket.setSoTimeout(400);
            daemon = new Daemon() {

                @Override
                public void run() {
                    try {
                        byte[] buffer = new byte[4096];
                        while (isRunning()) {
                            try {
                                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                                socket.receive(packet);
                                receive(new InetSocketAddress(packet.getAddress(), packet.getPort()), new ByteArrayInputStream(packet.getData(), 0, packet.getLength()));
                            } catch (SocketTimeoutException e) {
                            }
                        }
                        socket.leaveGroup(address);
                        socket.close();
                    } catch (Exception e) {
                    }
                }
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void join() {
        daemon.join();
    }

    public void close() {
        daemon.stop();
    }

    public static Integer extractPort(String groupName) {
        int code = Integer.MAX_VALUE & groupName.hashCode();
        int result = 1024 + (code % (Short.MAX_VALUE - 1024));
        return result;
    }

    public static String extractIp(String groupName) {
        int code = Integer.MAX_VALUE & groupName.hashCode();
        String result = "225." + (code % 225) + "." + ((code / 2) % 225) + "." + ((code / 3) % 225);
        return result;
    }
}
