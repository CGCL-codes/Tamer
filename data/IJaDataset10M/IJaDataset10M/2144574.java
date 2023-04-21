package net.sf.swarmnet.node.access;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.apache.log4j.Logger;
import net.sf.swarmnet.common.util.ByteHelper;
import net.sf.swarmnet.common.util.Const;
import net.sf.swarmnet.node.SwarmNode;

/**
 * TODO document
 * 
 * @author jan
 */
public final class AnnounceThread extends Thread {

    private static final Logger LOG = Logger.getLogger(AnnounceThread.class);

    private final SwarmNode mNode;

    private final int mPort;

    private final int mTaskServerPort;

    /**
   * Constructor
   * 
   * @param node
   * @param taskServerPort
   */
    public AnnounceThread(SwarmNode node, int accessPort, int taskServerPort) {
        mNode = node;
        mPort = accessPort;
        mTaskServerPort = taskServerPort;
    }

    @Override
    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(0);
            byte[] msg = createMessage();
            int port = Integer.parseInt(mNode.getProperty(Const.BROADCAST_PORT_KEY, Const.BROADCAST_PORT_DEF));
            while (true) {
                InetAddress group = InetAddress.getByName(Const.BROADCAST_GROUP);
                DatagramPacket packet = new DatagramPacket(msg, msg.length, group, port);
                socket.send(packet);
                Thread.sleep(10 * 1000);
            }
        } catch (Exception e) {
            LOG.error("Unable to send announce message. " + e.getMessage(), e);
        } finally {
            if (null != socket) {
                socket.close();
            }
        }
    }

    private byte[] createMessage() {
        byte[] msg = new byte[Const.ANNOUNCE_MESSAGE.length + 4];
        System.arraycopy(Const.ANNOUNCE_MESSAGE, 0, msg, 0, Const.ANNOUNCE_MESSAGE.length);
        System.arraycopy(ByteHelper.charToByteArray((char) mPort), 0, msg, Const.ANNOUNCE_MESSAGE.length, 2);
        System.arraycopy(ByteHelper.charToByteArray((char) mTaskServerPort), 0, msg, Const.ANNOUNCE_MESSAGE.length + 2, 2);
        return msg;
    }
}
