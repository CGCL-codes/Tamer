package org.rox.hephantan.bai2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {

    static DatagramSocket socket;

    public static void main(String[] args) {
        DatagramPacket packet;
        BufferedReader reader;
        byte[] buf = new byte[256];
        String data;
        try {
            socket = new DatagramSocket();
            System.out.print("Enter a message: ");
            reader = new BufferedReader(new InputStreamReader(System.in));
            data = reader.readLine();
            packet = new DatagramPacket(data.getBytes(), data.length(), InetAddress.getByName("localhost"), 1001);
            socket.send(packet);
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            data = new String(packet.getData()).trim();
            System.out.println("Data returned from Server: " + data);
        } catch (SocketException se) {
            se.printStackTrace();
        } catch (UnknownHostException ue) {
            ue.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
}
