package trb.fps.matchmaker;

import java.net.*;

class UDPServer {

    public static void main(String args[]) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] data = new byte[1024 * 4];
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(data, data.length);
            serverSocket.receive(receivePacket);
            System.out.println("RECEIVED: " + new String(receivePacket.getData()).substring(0, receivePacket.getLength()) + " " + receivePacket.getData().length);
            serverSocket.send(receivePacket);
        }
    }
}
