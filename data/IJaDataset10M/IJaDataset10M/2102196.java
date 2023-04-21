package com.sun.midp.io.j2me.datagram;

import java.io.*;
import javax.microedition.io.*;
import com.sun.midp.i3test.*;

/**
 * Test for a blocked read operation for a datagram. 
 */
public class TestDatagramWouldBlock extends TestCase implements Runnable {

    /** Port number for datagram server socket */
    static final int PORT = 1234;

    /** Message string sent from client to server */
    static final String strTestMsg = "klaatu barada nikto\r\n";

    /** Server datagram */
    UDPDatagramConnection server;

    /** client datagram */
    UDPDatagramConnection client;

    /** No of bytes in a message */
    private int numBytesToSend;

    /** Datagrm sent from client */
    private Datagram sendDatagram = null;

    /** Datagram received at server */
    private Datagram receivedDatagram = null;

    /** Flag that indicates that data is read at server */
    boolean readData = false;

    /** Thread that is blocked on the read */
    Thread t1;

    /**
     * Run method to open the server datagram and block on a read.
     */
    public void run() {
        try {
            server = (UDPDatagramConnection) Connector.open("datagram://:" + PORT);
            synchronized (this) {
                notifyAll();
            }
            receivedDatagram = createDatagramToReceive();
            server.receive(receivedDatagram);
        } catch (IOException ioe) {
            System.out.println("TestDatagram reader thread failed with:");
            ioe.printStackTrace();
        } finally {
            synchronized (this) {
                readData = true;
                notifyAll();
            }
        }
    }

    /**
     * Open the client connection and start the thread to call the run
     * method. It waits until the server is available.
     */
    void setUp() {
        try {
            client = (UDPDatagramConnection) Connector.open("datagram://localhost:" + PORT);
            t1 = new Thread(this);
            t1.start();
            synchronized (this) {
                while (server == null) {
                    try {
                        wait(1200);
                    } catch (InterruptedException e) {
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("TestDatagram setUp failed with:");
            ioe.printStackTrace();
        }
    }

    /**
     * In this test, client datagram socket sends a packet to server. 
     * Upon receiving the packet at server, message is printed back.
     */
    void testWouldBlock() {
        assertNotNull("Verify datagram socket open", client);
        sendDatagram = createDatagramToSend("datagram://localhost:" + PORT);
        try {
            client.send(sendDatagram);
        } catch (IOException ioe) {
            cleanUp();
            System.out.println("Cannot send datagram, send thrown " + ioe);
            return;
        }
        synchronized (this) {
            if (!readData) {
                try {
                    wait(2000);
                } catch (InterruptedException e) {
                    System.out.println("Catch interrupt");
                }
            }
        }
        byte[] buf = receivedDatagram.getData();
        String rcvdMsg = new String(buf);
        assertTrue("Verify data received at server", readData);
    }

    /**
     * Clean up after the test. 
     */
    void cleanUp() {
        try {
            client.close();
            server.close();
        } catch (IOException e) {
            System.out.println("TestSocket cleanUp failed with:");
            e.printStackTrace();
        }
        receivedDatagram = null;
        sendDatagram = null;
    }

    /**
     * Creates a datagram packet to be sent by client
     *
     * @param address Target host address
     * @return Datagram object to be sent
     */
    private Datagram createDatagramToSend(String address) {
        sendDatagram = null;
        numBytesToSend = strTestMsg.length();
        byte[] buf = new byte[numBytesToSend];
        buf = strTestMsg.getBytes();
        try {
            sendDatagram = client.newDatagram(buf, numBytesToSend, address);
        } catch (IOException ioe) {
            cleanUp();
            System.out.println("Cannot create Datagram. IOException: " + ioe);
        }
        return sendDatagram;
    }

    /**
     * Creates a datagram packet to be received at server
     *
     * @return Empty object to receive datagram
     */
    private Datagram createDatagramToReceive() {
        receivedDatagram = null;
        numBytesToSend = strTestMsg.length();
        try {
            receivedDatagram = server.newDatagram(numBytesToSend);
        } catch (IOException ioe) {
            cleanUp();
            System.out.println("Cannot create Datagram. IOException: " + ioe);
        }
        return receivedDatagram;
    }

    /**
     * Run the test by setting up datagram client in main thread and datagram
     * server in a separate thread. The server thread blocks for read 
     * operation. The client writes a data packet to server. The test passes
     * successfully when server receives the data back from client properly.
     */
    public void runTests() {
        setUp();
        declare("TestDatagramWouldBlock");
        testWouldBlock();
        cleanUp();
    }
}
