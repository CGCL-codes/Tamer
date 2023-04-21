package com.moodykettle.unittests.network;

import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class MyServer {

    private static int port = 1234;

    public void startMyServerThread(Socket s) {
        Thread t = new Thread(new MyServerThread(s));
        t.start();
    }

    public class MyServerThread implements Runnable {

        private Socket socket = null;

        public MyServerThread(Socket s) {
            this.socket = s;
        }

        @Override
        public void run() {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                Scanner in = new Scanner(socket.getInputStream());
                out.println("Hello from Myserver");
                out.flush();
                System.out.println("Connected to the client: " + socket.getInetAddress().toString());
                while (true) {
                    String serverMsg = "";
                    String clientMsg = in.nextLine();
                    System.out.println("(IN) Message from Client: " + clientMsg);
                    if (clientMsg.equalsIgnoreCase("bye")) {
                        serverMsg = "Bye!";
                        out.println(serverMsg);
                        out.flush();
                        System.out.println("(OUT) Sending Message to Client: " + serverMsg);
                        break;
                    } else {
                        serverMsg = "What you mean by: " + clientMsg + " ?";
                        out.println(serverMsg);
                        out.flush();
                        System.out.println("(OUT) Sending Message to Client: " + serverMsg);
                    }
                }
                System.out.println("(OUT) Disconnecting Client...");
                out.close();
                in.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            MyServer ms = new MyServer();
            ServerSocket ss = new ServerSocket(MyServer.port);
            while (true) {
                Socket s = ss.accept();
                ms.startMyServerThread(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
