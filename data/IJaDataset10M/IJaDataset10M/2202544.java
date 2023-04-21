package org.suren.test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainClass {

    public static void main(String[] args) {
        String host = "localhost";
        for (int i = 1; i < 65536; i++) {
            try {
                Socket s = new Socket(host, i);
                System.out.println("There is a server on port " + i + " of " + host);
            } catch (UnknownHostException ex) {
                System.err.println(ex);
                break;
            } catch (IOException ex) {
            }
        }
    }
}
