package com.shenxg.test.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServerSocket {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(8080, 1, InetAddress.getByName("127.0.0.1"));
            Socket sc = ss.accept();
            OutputStream out = sc.getOutputStream();
            InputStream in = sc.getInputStream();
            byte[] buffer = new byte[2048];
            in.read(buffer);
            for (byte b : buffer) {
                System.out.print((char) b);
            }
            boolean loop = true;
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
