package AstralG;

import java.io.*;
import java.net.*;

public class ServerNet {

    int cport = 1357;

    Socket cs;

    ServerSocket ss;

    BufferedReader in;

    PrintWriter out;

    public ServerNet(int port) {
        this.cport = port;
        try {
            ss = new ServerSocket(cport);
            System.out.println("Opened port: " + cport);
            System.out.println("\nWaiting for a client to connect...");
            cs = ss.accept();
            System.out.println("Found client!");
            in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            out = new PrintWriter(cs.getOutputStream(), true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean sendData(String[] data) {
        try {
            out.println(data);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void exit() {
        try {
            out.close();
            in.close();
            cs.close();
            ss.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getReply() {
        System.out.println("Waiting for P2...");
        String str = "";
        try {
            str = in.readLine();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return str;
    }
}
