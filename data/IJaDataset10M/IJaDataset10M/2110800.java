package com.tuton.javanet.chapter02;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Sender {

    private String host = "localhost";

    private int port = 8000;

    private Socket socket;

    private static int stopWay = 1;

    private final int NATRUALWAY = 1;

    private final int SUNDDENWAY = 2;

    private final int SOCKETWAY = 3;

    private final int OUTPUTWAY = 4;

    public Sender() throws IOException {
        this.socket = new Socket(host, port);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length > 0) {
            stopWay = Integer.parseInt(args[0]);
            System.out.println("stopWayNumber = " + stopWay);
        }
        new Sender().send();
    }

    private void send() throws IOException, InterruptedException {
        PrintWriter pw = getWriter(socket);
        for (int i = 1; i < 20; i++) {
            String msg = "Hello: " + 1;
            pw.println(msg);
            System.out.println("send: " + msg);
            Thread.sleep(18000);
            if (2 == i) {
                if (this.SUNDDENWAY == stopWay) {
                    System.out.println("Sundenly shut down this program...");
                    System.exit(0);
                } else if (this.SOCKETWAY == stopWay) {
                    socket.close();
                    System.out.println("Socket close exit this program....");
                    break;
                } else if (this.OUTPUTWAY == stopWay) {
                    pw.close();
                    System.out.println("OutputStream close before shut down this program..");
                }
            }
            if (this.NATRUALWAY == stopWay) {
                socket.close();
            }
        }
    }

    private PrintWriter getWriter(Socket socket2) throws IOException {
        OutputStream socketOut = socket.getOutputStream();
        return new PrintWriter(socketOut, true);
    }
}
