package test.longhall.connex;

import java.io.*;
import java.net.*;
import junit.framework.*;
import longhall.connex.*;

public class SocketLevelTests extends TestCase {

    private MulticastSocket testEndpoint;

    private Conduit conduit;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(SocketLevelTests.class);
    }

    public SocketLevelTests(String s) {
        super(s);
        System.out.println(s);
    }

    public void setUp() throws Exception {
        conduit = new Conduit();
        testEndpoint = new MulticastSocket(conduit.getMulticastPort());
        testEndpoint.joinGroup(conduit.getMulticastGroup());
    }

    public void tearDown() throws Exception {
        if (testEndpoint != null) {
            testEndpoint.leaveGroup(conduit.getMulticastGroup());
            testEndpoint.close();
        }
        if (conduit.isActive()) {
            conduit.shutdown();
        }
    }

    public void testSocketExists() throws Exception {
        OpenConduit c = new OpenConduit();
        c.startup();
        assertNotNull(c.getSocket());
    }

    public void testReadTimeout() throws Exception {
    }

    public void testWritePacket() throws Exception {
        conduit.send(new Subject("TEST"), new TestMessage(1));
        checkReceivedPacket(1);
        ;
    }

    public void testWriteManyPackets() throws Exception {
        conduit.startup();
        for (int i = 0; i < 100; i++) {
            conduit.send(new Subject("TEST"), new TestMessage(i));
            checkReceivedPacket(i);
        }
    }

    public void testWriteManyPacketsWithBuffering() throws Exception {
        conduit.startup();
        for (int i = 0; i < 100; i++) {
            conduit.send(new Subject("TEST"), new TestMessage(i));
        }
        for (int i = 0; i < 100; i++) {
            checkReceivedPacket(i);
        }
    }

    public void testReadPacket() throws Exception {
        conduit.startup();
        sendForgedPacket(0);
        checkReceivedMessage(0);
    }

    public void testReadManyPackets() throws Exception {
        conduit.startup();
        for (int i = 0; i < 100; i++) {
            sendForgedPacket(i);
            checkReceivedMessage(i);
        }
    }

    public void testReadManyPacketsWithBuffering() throws Exception {
        conduit.startup();
        for (int i = 0; i < 100; i++) {
            sendForgedPacket(i);
        }
        for (int i = 0; i < 100; i++) {
            checkReceivedMessage(i);
        }
    }

    public void testReadBogusData() throws Exception {
        conduit.startup();
        sendBogusData();
        try {
            Packet packet = (Packet) conduit.receive();
            assertTrue("Should have thrown an exception", false);
        } catch (Exception ex) {
        }
    }

    protected void checkReceivedPacket(int expectedSequenceNumber) throws Exception {
        byte[] buf = new byte[testEndpoint.getReceiveBufferSize()];
        DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
        testEndpoint.receive(receivePacket);
        assertTrue("No data received", receivePacket.getLength() != 0);
        ByteArrayInputStream array = new ByteArrayInputStream(receivePacket.getData());
        ObjectInputStream in = new ObjectInputStream(array);
        Packet p = (Packet) in.readObject();
        assertNotNull("No packet", p);
        assertEquals("Magic number", Packet.MAGIC_TAG, p.getMagic());
        assertEquals("Major version", Packet.MAJOR_VERSION, p.getMajor());
        assertEquals("Minor version", Packet.MINOR_VERSION, p.getMinor());
        assertEquals("Send subject", new Subject("TEST"), p.getSendSubject());
        TestMessage msgReceived = (TestMessage) p.getPayload();
        assertNotNull("No TestMessage", msgReceived);
        assertEquals("Packet out of sequence", expectedSequenceNumber, msgReceived.getSequenceNumber());
    }

    protected void checkReceivedMessage(int expectedSequence) throws Exception {
        Packet packet = (Packet) conduit.receive();
        assertNotNull(packet);
        assertNotNull(packet.getSendSubject());
        assertNotNull(packet.getPayload());
        TestMessage msg = (TestMessage) packet.getPayload();
        assertEquals("Out of sequence", expectedSequence, msg.getSequenceNumber());
    }

    protected void sendForgedPacket(int sequence) throws Exception {
        Packet p = new Packet(new Subject("TEST"), new TestMessage(sequence), sequence);
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(array);
        out.writeObject(p);
        out.flush();
        byte[] buf = array.toByteArray();
        DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, conduit.getMulticastGroup(), conduit.getMulticastPort());
        testEndpoint.send(sendPacket);
    }

    protected void sendBogusData() throws Exception {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(array);
        out.writeObject("Boo!");
        out.flush();
        byte[] buf = array.toByteArray();
        DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, conduit.getMulticastGroup(), conduit.getMulticastPort());
        testEndpoint.send(sendPacket);
    }

    class OpenConduit extends Conduit {

        public MulticastSocket getSocket() {
            return super.getSocket();
        }
    }
}

class TestMessage implements java.io.Serializable {

    private int a = 0xaabbccdd;

    private String b = "testString";

    private int sequenceNumber = -1;

    public TestMessage(int seq) {
        this.sequenceNumber = seq;
    }

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }
}
