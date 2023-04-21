package ru.aslanov.test;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * Created: Jul 15, 2010 11:39:00 AM
 *
 * @author Sergey Aslanov
 */
public class HttpServer {

    public static void main(String[] args) throws Exception {
        System.out.println("Waiting for connection...");
        ServerSocket serverSocket = new ServerSocket(8888);
        final Socket socket = serverSocket.accept();
        System.out.println("Socket accepted");
        final InputStream is = socket.getInputStream();
        int ch;
        while ((ch = is.read()) >= 0) {
            System.out.print((char) ch);
            if (ch == '&') System.out.println();
        }
    }
}
