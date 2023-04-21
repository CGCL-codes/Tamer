package net.sourceforge.log4cluster.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import net.sourceforge.log4cluster.uuid.UUID;
import org.apache.log4j.helpers.LogLog;

/**
 * Thread that listenes on a multicast address and sends everybody who sends a packet
 * a fixed response
 *  
 * message-format: UUID of request, UUID of sender, message 
 *  
 * @author hugo
 */
public class GroupResponder extends Thread {

    private MulticastSocket m_sock;

    private boolean m_running = true;

    private DatagramPacket m_response;

    private UUID m_member_uuid;

    private byte[] m_response_arr;

    public GroupResponder(InetSocketAddress addr, byte[] response) throws IOException {
        try {
            m_sock = new MulticastSocket(addr.getPort());
            m_sock.joinGroup(addr.getAddress());
        } catch (IOException ioe) {
            LogLog.error("can't listen on multicast " + addr, ioe);
            throw ioe;
        }
        setName("GroupResponder on " + addr);
        setDaemon(true);
        m_member_uuid = new UUID();
        m_response_arr = new byte[UUID.LENGTH + UUID.LENGTH + response.length];
        m_member_uuid.writeTo(m_response_arr, UUID.LENGTH);
        System.arraycopy(response, 0, m_response_arr, UUID.LENGTH + UUID.LENGTH, response.length);
        m_response = new DatagramPacket(m_response_arr, 0, m_response_arr.length);
    }

    @Override
    public void run() {
        byte[] buf = new byte[UUID.LENGTH];
        DatagramPacket query = new DatagramPacket(buf, 0, buf.length);
        while (m_running) {
            try {
                m_sock.receive(query);
                System.out.println("received " + query);
            } catch (IOException e) {
                if (!m_running) return;
                LogLog.error("can't receive", e);
            }
            if (query.getLength() != UUID.LENGTH) {
                LogLog.warn("unexpected packet from " + query.getSocketAddress());
                continue;
            }
            m_response.setAddress(query.getAddress());
            m_response.setPort(query.getPort());
            System.arraycopy(buf, 0, m_response_arr, 0, UUID.LENGTH);
            try {
                m_sock.send(m_response);
            } catch (IOException e) {
                LogLog.warn("can't send packet to " + query.getAddress(), e);
            }
        }
    }

    public void close() {
        m_running = false;
        m_sock.close();
    }

    public static void main(String[] argv) throws IOException {
        InetSocketAddress addr = new InetSocketAddress(argv[0], Integer.parseInt(argv[1]));
        GroupResponder resp = new GroupResponder(addr, argv[2].getBytes());
        resp.run();
    }
}
