package net.redlightning.jbittorrent.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.*;
import java.net.*;
import net.redlightning.jbittorrent.Utils;

public class UDPHandler {

    private final byte[] connection_id = Utils.longToByteArray(0x41727101980L);

    private byte[] action = Utils.intToByteArray(0x0);

    private byte[] transaction_id = Utils.generateID();

    private byte[] client_id = Utils.generateID();

    DatagramSocket theSocket = null;

    int serverPort = 9999;

    /**
	 * 
	 */
    public UDPHandler() {
        try {
            theSocket = new DatagramSocket();
            InetAddress theServer = InetAddress.getLocalHost();
            theSocket.connect(theServer, serverPort);
            System.out.println("Client socket created");
        } catch (SocketException exceSocket) {
            System.out.println("Socket creation error  : " + exceSocket.getMessage());
        } catch (UnknownHostException exceHost) {
            System.out.println("Socket host unknown : " + exceHost.getMessage());
        }
    }

    /**
	 * 
	 */
    public void connectToServer() {
        DatagramPacket theSendPacket;
        DatagramPacket theReceivedPacket;
        InetAddress theServerAddress;
        byte[] outBuffer;
        byte[] inBuffer;
        inBuffer = new byte[500];
        outBuffer = new byte[50];
        try {
            String message = "genux";
            outBuffer = message.getBytes();
            System.out.println("Message sending is : " + message);
            theServerAddress = theSocket.getLocalAddress();
            theSendPacket = new DatagramPacket(outBuffer, outBuffer.length, theServerAddress, serverPort);
            theSocket.send(theSendPacket);
            theReceivedPacket = new DatagramPacket(inBuffer, inBuffer.length);
            theSocket.receive(theReceivedPacket);
            System.out.println("Client - server response : " + new String(theReceivedPacket.getData(), 0, theReceivedPacket.getLength()));
            theSocket.close();
        } catch (IOException ExceIO) {
            System.out.println("Client getting data error : " + ExceIO.getMessage());
        }
    }

    public static void main(String[] args) {
        UDPHandler theClient = new UDPHandler();
        theClient.connectToServer();
    }
}
