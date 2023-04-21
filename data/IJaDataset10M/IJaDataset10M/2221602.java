package org.jdamico.ircivelaclient.comm;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPConnection {

    private DatagramSocket socket;

    private DatagramPacket sendPacket, receivePacket;

    public UDPConnection(int port) {
        try {
            this.socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg, InetAddress address, int port) {
        System.out.println("send: " + msg);
        System.out.println("send: " + address);
        try {
            byte data[] = msg.getBytes();
            sendPacket = new DatagramPacket(data, data.length, address, port);
            socket.send(sendPacket);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public String receive() {
        try {
            byte dataReceived[] = new byte[1500];
            receivePacket = new DatagramPacket(dataReceived, dataReceived.length);
            socket.receive(receivePacket);
            String res = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("res: " + res);
            return res;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

    public InetAddress connect(String IP) {
        try {
            InetAddress address = InetAddress.getByName(IP);
            return address;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println("SEND");
        UDPConnection udp = new UDPConnection(6668);
        InetAddress address = udp.connect("127.0.0.1");
        udp.send("teste", address, 6669);
    }
}
