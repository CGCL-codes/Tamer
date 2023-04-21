package com.vayoodoot.research;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * User: Sachin Shetty
 * Date: Dec 25, 2006
 * Time: 9:14:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerTest {

    public static void main(String args[]) throws Exception {
        ServerSocket serverSocket = null;
        try {
            try {
                serverSocket = new ServerSocket(18991);
            } catch (IOException e) {
                System.out.println("Could not listen on port: 4444" + e);
                System.exit(-1);
            }
            System.out.println("MainServer Started...");
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Accept failed: 4444");
                System.exit(-1);
            }
            System.out.println("Client Connection Accepted");
            System.out.println("Inet Address:" + clientSocket.getInetAddress());
            System.out.println("Inet Address:" + clientSocket.getPort());
            Socket kkSocket = new Socket("localhost", clientSocket.getPort());
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedWriter out1 = new BufferedWriter(new OutputStreamWriter(kkSocket.getOutputStream()));
            System.out.println("Writing Lines");
            out1.write("Hey Hey");
            out1.flush();
        } finally {
            serverSocket.close();
        }
    }
}
