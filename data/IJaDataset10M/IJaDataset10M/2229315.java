package com.peterhi.util;

import java.util.Set;
import java.util.HashSet;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class Client implements Runnable {

    private DatagramSocket socket;

    private DatagramPacket packet = new DatagramPacket(new byte[2048], 2048);

    private SocketAddress serverAddress = new InetSocketAddress("localhost", 10000);

    private Set<SocketAddress> peers = new HashSet<SocketAddress>();

    private Node root;

    private Traverser t;

    public Client() throws Exception {
        socket = new DatagramSocket();
        root = new Node(2, socket.getLocalSocketAddress());
        t = new Traverser(root);
        new Thread(this).start();
        send(serverAddress, Common.REGISTER);
        System.out.println("press enter to exit");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        send(serverAddress, Common.KILL_PEER);
        System.exit(0);
    }

    public void send(SocketAddress sa, String message) throws IOException {
        byte[] data = message.getBytes();
        DatagramPacket send = new DatagramPacket(data, data.length);
        send.setSocketAddress(sa);
        socket.send(send);
    }

    public void run() {
        while (true) {
            try {
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                onReceived(message, packet);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void onReceived(String message, DatagramPacket packet) throws Exception {
        if (message.startsWith(Common.REGISTER_OK)) {
            send(serverAddress, Common.GET_PEERS);
        } else if (message.startsWith(Common.GET_PEERS_OK)) {
            String[] array = message.split(" ");
            for (int i = 1; i < array.length; i++) {
                String[] subArray = array[i].substring(1).split(":");
                SocketAddress sa = new InetSocketAddress(subArray[0], Integer.parseInt(subArray[1]));
                peers.add(sa);
                t.append(new Node(2, sa));
                send(sa, Common.PEER_CONTACT);
            }
            t.print();
            for (SocketAddress item : peers) {
                System.out.println("found peer: " + item);
            }
        } else if (message.startsWith(Common.NEW_PEER)) {
            String[] array = message.split(" ");
            String[] subArray = array[1].substring(1).split(":");
            SocketAddress sa = new InetSocketAddress(subArray[0], Integer.parseInt(subArray[1]));
            peers.add(sa);
            t.append(new Node(2, sa));
            t.print();
            System.out.println("new peer: " + message + " now " + peers.size());
        } else if (message.startsWith(Common.KILL_PEER)) {
            String[] array = message.split(" ");
            String[] subArray = array[1].substring(1).split(":");
            SocketAddress sa = new InetSocketAddress(subArray[0], Integer.parseInt(subArray[1]));
            peers.remove(sa);
            t.remove(t.get(sa));
            t.print();
            System.out.println("kill peer: " + message + " now " + peers.size());
        } else if (message.startsWith(Common.PEER_CONTACT)) {
            System.out.println(packet.getSocketAddress() + " contacted you P2P.");
            send(packet.getSocketAddress(), Common.PEER_RESPONSE);
        } else if (message.startsWith(Common.PEER_RESPONSE)) {
            System.out.println(packet.getSocketAddress() + " responded your P2P request.");
        }
    }
}
