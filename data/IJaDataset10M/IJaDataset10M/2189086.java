package multichatserver;

import java.io.*;
import java.net.*;

public class Client {

    int id;

    String nick;

    BufferedReader in;

    PrintWriter out;

    Socket sock;

    int port;

    String addr;

    public Client(int id, Socket sock) {
        this.nick = "unknown";
        this.sock = sock;
        addr = sock.getInetAddress().getHostAddress();
        port = sock.getPort();
        try {
            this.sock.setTcpNoDelay(true);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }
}
