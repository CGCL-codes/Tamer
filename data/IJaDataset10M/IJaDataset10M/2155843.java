package peer.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.endpoint.Message;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeID;
import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.util.JxtaBiDiPipe;
import org.apache.log4j.Logger;
import peer.Peer;
import peer.net.AccessManager;
import peer.net.util.MessageEncryptionUtility;

/**
 *
 * @author tpasquie
 */
public class ServiceCommunication {

    /** a timeout of zero (0) means we wait indefinitely */
    public static final int TIMEOUT = 0;

    private static final Logger logger = Logger.getLogger(ServiceCommunication.class);

    private JxtaBiDiPipe pipe;

    private PeerGroup group;

    public static int MAX_BUFFER_SIZE = 128 * 1024;

    public ServiceCommunication(JxtaBiDiPipe pipe, PeerGroup group) {
        this.pipe = pipe;
        this.group = group;
    }

    private Message createTextMessage(String data, PeerID destinationID) throws Exception {
        return createBytesMessage(data.getBytes(Charset.forName("UTF8")), destinationID);
    }

    private Message createBytesMessage(byte[] data, PeerID destinationID) throws Exception {
        if (data.length > MAX_BUFFER_SIZE) {
            throw new Exception("Message exceed maximum size");
        }
        Message msg = MessageEncryptionUtility.encrypt(data, destinationID, group);
        msg = MessageEncryptionUtility.sign(msg, group);
        return msg;
    }

    private Message createObjectMessage(Object data, PeerID destinationID) throws Exception {
        ByteArrayOutputStream bytesOut = null;
        ObjectOutputStream objectOut = null;
        try {
            bytesOut = new ByteArrayOutputStream();
            objectOut = new ObjectOutputStream(bytesOut);
            objectOut.writeObject(data);
            objectOut.flush();
            byte[] bytes = bytesOut.toByteArray();
            return createBytesMessage(bytes, destinationID);
        } finally {
            if (bytesOut != null) {
                bytesOut.close();
            }
            if (objectOut != null) {
                objectOut.close();
            }
        }
    }

    public String receiveTextMessage() throws Exception {
        return new String(receiveBytesMessage(), Charset.forName("UTF8"));
    }

    public byte[] receiveBytesMessage() throws Exception {
        Message msg = receiveMessage();
        MessageEncryptionUtility.verifySignature(msg, pipe.getRemotePeerAdvertisement().getPeerID(), group);
        return MessageEncryptionUtility.decrypt(msg, pipe.getRemotePeerAdvertisement().getPeerID(), group);
    }

    public Object receiveObjectMessage() throws Exception {
        Message msg = receiveMessage();
        MessageEncryptionUtility.verifySignature(msg, pipe.getRemotePeerAdvertisement().getPeerID(), group);
        byte[] data = MessageEncryptionUtility.decrypt(msg, pipe.getRemotePeerAdvertisement().getPeerID(), group);
        ByteArrayInputStream bytesInput = null;
        ObjectInputStream objectInput = null;
        try {
            bytesInput = new ByteArrayInputStream(data);
            objectInput = new ObjectInputStream(bytesInput);
            Object obj = objectInput.readObject();
            return obj;
        } finally {
            if (bytesInput != null) {
                bytesInput.close();
            }
            if (objectInput != null) {
                objectInput.close();
            }
        }
    }

    public void receiveStream(OutputStream output) throws Exception {
        int received = 0;
        int expected = 0;
        output.flush();
        do {
            byte[] buffer = receiveBytesMessage();
            received = buffer.length;
            int pos = (int) buffer[0];
            if (pos != expected) throw new Exception("Error received an unexpected stream element");
            expected = pos + 1;
            byte[] data = new byte[received - 1];
            System.arraycopy(buffer, 1, data, 0, data.length);
            output.write(data, pos * MAX_BUFFER_SIZE, data.length);
        } while (received == MAX_BUFFER_SIZE);
        output.flush();
    }

    private Message receiveMessage() throws Exception {
        if (!AccessManager.canAccess(group, pipe.getRemotePeerAdvertisement())) {
            throw new Exception("This peer is not authorized within this group");
        }
        try {
            Message msg = pipe.getMessage(TIMEOUT);
            if (msg == null) {
                throw new Exception("No message received");
            }
            return msg;
        } catch (Exception e) {
            throw new Exception("Error while receiving message.", e);
        }
    }

    public void sendTextMessage(String data) throws Exception {
        sendMessage(createTextMessage(data, pipe.getRemotePeerAdvertisement().getPeerID()));
    }

    public void sendBytesMessage(byte[] data) throws Exception {
        sendMessage(createBytesMessage(data, pipe.getRemotePeerAdvertisement().getPeerID()));
    }

    public void sendObjectMessage(Object data) throws Exception {
        sendMessage(createObjectMessage(data, pipe.getRemotePeerAdvertisement().getPeerID()));
    }

    public void sendStream(InputStream input) throws Exception {
        int read = 0;
        int alreadyread = 0;
        byte elementNb = 0;
        byte[] buffer = new byte[MAX_BUFFER_SIZE];
        do {
            read = input.read(buffer, alreadyread, MAX_BUFFER_SIZE - 1);
            alreadyread += read;
            byte[] data = new byte[read + 1];
            System.arraycopy(buffer, 0, data, 1, data.length - 1);
            data[0] = elementNb++;
            sendBytesMessage(data);
        } while (read == MAX_BUFFER_SIZE - 1);
    }

    private void sendMessage(Message msg) throws Exception {
        if (!AccessManager.canAccess(group, pipe.getRemotePeerAdvertisement())) {
            throw new Exception("This peer is not authorized within this group");
        }
        if (msg == null) {
            throw new Exception("Message is null");
        }
        if (pipe == null || !pipe.isBound()) {
            throw new Exception("Pipe is not valid");
        }
        try {
            pipe.sendMessage(msg);
        } catch (Exception e) {
            throw new Exception("Error while sending message", e);
        }
        logger.info("Message sent to " + pipe.getRemotePeerAdvertisement().getName());
    }

    public boolean isBound() {
        if (pipe == null) {
            return false;
        }
        return pipe.isBound();
    }

    public void close() throws Exception {
        pipe.close();
    }

    public PipeID getLocalPipeID() {
        return (PipeID) pipe.getPipeAdvertisement().getPipeID();
    }

    public PipeID getRemotePipeID() {
        return (PipeID) pipe.getRemotePipeAdvertisement().getPipeID();
    }

    public String getLocalName() {
        return Peer.getConfiguration().getString("user.name");
    }

    public String getRemoteName() {
        return pipe.getRemotePeerAdvertisement().getName();
    }

    private ArrayList<ServiceCommunication> getAllPipes() throws Exception {
        ArrayList<ServiceCommunication> results = new ArrayList<ServiceCommunication>();
        DiscoveryService discovery = group.getDiscoveryService();
        Enumeration<Advertisement> advertisements = discovery.getLocalAdvertisements(discovery.ADV, null, null);
        while (advertisements.hasMoreElements()) {
            try {
                Advertisement adv = advertisements.nextElement();
                if (adv.getAdvType().equals(ModuleSpecAdvertisement.getAdvertisementType())) {
                    ModuleSpecAdvertisement madv = (ModuleSpecAdvertisement) adv;
                    PipeAdvertisement pipeAdv = madv.getPipeAdvertisement();
                    JxtaBiDiPipe p = new JxtaBiDiPipe();
                    p.setReliable(true);
                    p.connect(group, pipeAdv);
                    results.add(new ServiceCommunication(p, group));
                }
            } catch (Exception e) {
                logger.error("Error while opening pipe for broadcast");
            }
        }
        return results;
    }

    public void broadcastTextMessage(String data) throws Exception {
        ArrayList<ServiceCommunication> coms = getAllPipes();
        for (ServiceCommunication c : coms) {
            Thread t = new BroadcastTextThread(c, data);
            t.start();
        }
    }

    public void broadcastBytesMessage(byte[] data) throws Exception {
        ArrayList<ServiceCommunication> coms = getAllPipes();
        for (ServiceCommunication c : coms) {
            Thread t = new BroadcastBytesThread(c, data);
            t.start();
        }
    }

    public void broadcastObjectMessage(Object data) throws Exception {
        ArrayList<ServiceCommunication> coms = getAllPipes();
        for (ServiceCommunication c : coms) {
            Thread t = new BroadcastObjectThread(c, data);
            t.start();
        }
    }

    private class BroadcastTextThread extends Thread {

        private String data = null;

        private ServiceCommunication com = null;

        public BroadcastTextThread(ServiceCommunication com, String data) {
            this.com = com;
            this.data = data;
        }

        @Override
        public void run() {
            try {
                com.sendTextMessage(data);
                com.close();
            } catch (Exception e) {
                logger.error("Error while broadcasting to " + com.getRemoteName(), e);
            }
        }
    }

    private class BroadcastBytesThread extends Thread {

        private byte[] data = null;

        private ServiceCommunication com = null;

        public BroadcastBytesThread(ServiceCommunication com, byte[] data) {
            this.com = com;
            this.data = data;
        }

        @Override
        public void run() {
            try {
                com.sendBytesMessage(data);
                com.close();
            } catch (Exception e) {
                logger.error("Error while broadcasting to " + com.getRemoteName(), e);
            }
        }
    }

    private class BroadcastObjectThread extends Thread {

        private Object data = null;

        private ServiceCommunication com = null;

        public BroadcastObjectThread(ServiceCommunication com, Object data) {
            this.com = com;
            this.data = data;
        }

        @Override
        public void run() {
            try {
                com.sendObjectMessage(data);
                com.close();
            } catch (Exception e) {
                logger.error("Error while broadcasting to " + com.getRemoteName(), e);
            }
        }
    }
}
