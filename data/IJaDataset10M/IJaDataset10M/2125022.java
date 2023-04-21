package com.flyox.game.fivetiger.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import com.flyox.game.fivetiger.tools.SubnetAddress;

public class IPClient {

    private boolean living = true;

    int port = 5000;

    InetAddress group;

    MulticastSocket socket;

    DatagramPacket packet;

    public IPClient() {
        SubnetAddress sa = new SubnetAddress();
        String s = sa.calculate();
        try {
            group = InetAddress.getByName(s);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public String getIP() {
        send();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void send() {
        byte[] data = "GET_SERVER".getBytes();
        packet = new DatagramPacket(data, data.length, group, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void receive() {
        try {
            socket = new MulticastSocket(port);
            socket.joinGroup(group);
            while (isLiving()) {
                byte[] data = new byte[50];
                packet = new DatagramPacket(data, data.length, group, port);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("receive:" + message);
            }
        } catch (Exception e1) {
            System.out.println("Error: " + e1);
        }
    }

    public boolean isLiving() {
        return living;
    }

    public void setLiving(boolean living) {
        this.living = living;
    }
}
