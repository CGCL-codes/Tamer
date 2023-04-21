package se.sics.contiki.collect;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.*;

/**
 *
 */
public class UDPConnection extends SerialConnection {

    private final int port;

    private DatagramSocket serverSocket;

    public UDPConnection(SerialConnectionListener listener, int port) {
        super(listener);
        this.port = port;
    }

    @Override
    public String getConnectionName() {
        return "<UDP:" + port + ">";
    }

    @Override
    public void open(String comPort) {
        close();
        this.comPort = comPort == null ? "" : comPort;
        isClosed = false;
        try {
            serverSocket = new DatagramSocket(port);
            System.out.println("Opened UDP port: " + port);
            Thread readInput = new Thread(new Runnable() {

                public void run() {
                    byte[] data = new byte[1024];
                    try {
                        while (isOpen) {
                            DatagramPacket packet = new DatagramPacket(data, data.length);
                            serverSocket.receive(packet);
                            InetAddress addr = packet.getAddress();
                            System.out.println("UDP: received " + packet.getLength() + " bytes from " + addr.getHostAddress() + ":" + packet.getPort());
                            StringWriter strOut = new StringWriter();
                            PrintWriter out = new PrintWriter(strOut);
                            int payloadLen = packet.getLength() - 2;
                            out.printf("%d", 8 + payloadLen / 2);
                            long time = System.currentTimeMillis() / 1000;
                            out.printf(" %d %d 0", ((time >> 16) & 0xffff), time & 0xffff);
                            byte[] payload = packet.getData();
                            int seqno = payload[0] & 0xff;
                            int hops = 0;
                            byte[] address = addr.getAddress();
                            out.printf(" %d %d %d %d", ((address[14] & 0xff) + ((address[15] & 0xff) << 8)) & 0xffff, seqno, hops, 0);
                            int d = 0;
                            for (int i = 0; i < payloadLen; i += 2) {
                                d = (payload[i + 2] & 0xff) + ((payload[i + 3] & 0xff) << 8);
                                out.printf(" %d", d & 0xffff);
                            }
                            String line = strOut.toString();
                            serialData(line);
                        }
                        System.out.println("SerialConnection UDP terminated.");
                        closeConnection();
                    } catch (IOException e) {
                        lastError = "Error when reading from SerialConnection UDP: " + e;
                        System.err.println(lastError);
                        if (!isClosed) {
                            e.printStackTrace();
                            closeConnection();
                        }
                    }
                }
            }, "UDP thread");
            isOpen = true;
            serialOpened();
            readInput.start();
        } catch (Exception e) {
            lastError = "Failed to open UDP server at port " + port + ": " + e;
            System.err.println(lastError);
            e.printStackTrace();
            closeConnection();
        }
    }

    @Override
    protected void doClose() {
        if (serverSocket != null) {
            serverSocket.close();
            serverSocket = null;
        }
    }
}
