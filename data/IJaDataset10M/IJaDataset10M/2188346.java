package org.jdonkey.client;

import java.io.*;
import java.net.*;
import org.jdonkey.proto.*;
import org.jdonkey.proto.messages.*;

public class Listener extends Thread {

    ServerSocket serverSocket;

    OutputStream outToServer;

    InputStream inFromServer;

    Object listener;

    boolean running;

    boolean clientConnected;

    public Listener(int port) throws Exception {
        super();
        setName(getClass().getName());
        serverSocket = new ServerSocket(port);
        running = true;
        start();
    }

    public int getLocalPort() {
        return serverSocket.getLocalPort();
    }

    void hexdump(byte buffer[]) {
        int bytes_per_row = 16;
        char digits[] = new char[bytes_per_row * 3];
        char chars[] = new char[bytes_per_row];
        for (int l = 0; l < bytes_per_row; l++) {
            digits[l * 3] = ' ';
            digits[(l * 3) + 1] = ' ';
            digits[(l * 3) + 2] = ' ';
            chars[l] = ' ';
        }
        for (int i = 0; i < buffer.length; i++) {
            int m = buffer[i];
            if (m < 0) m += 256;
            int j = m % 16;
            int k = (m - j) / 16;
            digits[(i % bytes_per_row) * 3] = Character.forDigit(k, 16);
            digits[(i % bytes_per_row) * 3 + 1] = Character.forDigit(j, 16);
            if (Character.isLetterOrDigit((char) buffer[i])) {
                chars[i % bytes_per_row] = (char) buffer[i];
            } else {
                chars[i % bytes_per_row] = '.';
            }
            if (((i + 1) % bytes_per_row == 0) || ((i + 1) == buffer.length)) {
                System.err.print(digits);
                System.err.println(chars);
                for (int l = 0; l < bytes_per_row; l++) {
                    digits[l * 3] = ' ';
                    digits[(l * 3) + 1] = ' ';
                    chars[l] = ' ';
                }
            }
        }
    }

    void readMessage() throws Exception {
        byte b;
        byte buffer[];
        Message m;
        b = (byte) inFromServer.read();
        if (b == Constants.donkeyTag) {
            int len = 0;
            int readen = 0;
            int l;
            for (int i = 0; i < 4; i++) {
                l = inFromServer.read();
                if (l < 0) l += 256;
                len += Math.pow(256, i) * l;
            }
            buffer = new byte[len];
            while (readen < len) {
                l = inFromServer.read(buffer, readen, len - readen);
                if (l < 0) {
                    System.err.println("error reading " + b + "bytes from server");
                } else {
                    readen += l;
                }
            }
            System.err.println("received " + len + " bytes");
            System.err.println("unimplemented server command: " + Integer.toHexString((buffer[0] + 128) % 256));
            hexdump(buffer);
            m = null;
            if (m != null) {
            }
        } else {
            System.err.println("skipping invalid byte: " + b);
        }
    }

    public void run() {
        OutputStream outToServer;
        InputStream inFromServer;
        while (running) {
            try {
                Socket connectionSocket = serverSocket.accept();
                clientConnected = true;
                outToServer = connectionSocket.getOutputStream();
                inFromServer = connectionSocket.getInputStream();
                try {
                    while (running && clientConnected) {
                        if (inFromServer.available() >= 5) {
                            System.out.println("message from server to read");
                            readMessage();
                        } else {
                            yield();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
                connectionSocket.close();
            } catch (Exception e) {
                if (running) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    public void disconnect() {
        ServerSocket sock;
        running = false;
        sock = serverSocket;
        serverSocket = null;
        try {
            sock.close();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
