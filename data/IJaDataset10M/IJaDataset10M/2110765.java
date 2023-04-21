package net;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class InfoPipe extends Thread {

    ServerSocket incoming;

    LinkedList<ClientPipe> clients;

    LinkedList<String> commands;

    boolean stopFlag;

    public InfoPipe(int port) throws Exception {
        incoming = new ServerSocket(port);
        clients = new LinkedList<ClientPipe>();
        commands = new LinkedList<String>();
        start();
    }

    public void initShutdown() {
        stopFlag = true;
        try {
            incoming.close();
        } catch (IOException e) {
        }
        ;
    }

    public synchronized boolean areCommandsReady() {
        return commands.size() > 0;
    }

    public synchronized String getNextCommand() {
        if (commands.size() == 0) return null;
        String s = commands.removeFirst();
        return s;
    }

    public synchronized void addCommand(String s) {
        commands.addLast(s);
    }

    public synchronized void removeClient(ClientPipe s) {
        clients.remove(s);
    }

    public synchronized void addClient(Socket s) {
        clients.add(new ClientPipe(this, s));
    }

    public void run() {
        Socket s;
        while (!stopFlag) {
            try {
                s = incoming.accept();
                addClient(s);
            } catch (Exception e) {
            }
        }
    }
}
