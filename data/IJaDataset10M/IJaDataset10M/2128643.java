package com.socket;

import java.io.*;
import java.net.*;
import java.util.*;

public class TimeClient {

    private static boolean usageOK(String[] args) {
        if (args.length != 1) {
            String msg = "usage is: " + "TimeClient server-name";
            System.out.println(msg);
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        args = new String[1];
        args[0] = "localhost";
        if (!usageOK(args)) System.exit(1);
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.err.println("Unable to create socket");
            e.printStackTrace();
            System.exit(1);
            return;
        }
        long time;
        try {
            byte[] buf = new byte[1];
            socket.send(new DatagramPacket(buf, 1, InetAddress.getByName(args[0]), 7654));
            DatagramPacket response = new DatagramPacket(new byte[8], 8);
            socket.receive(response);
            ByteArrayInputStream bs;
            bs = new ByteArrayInputStream(response.getData());
            DataInputStream ds = new DataInputStream(bs);
            time = ds.readLong();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return;
        }
        System.out.println(new Date(time));
        socket.close();
    }
}
