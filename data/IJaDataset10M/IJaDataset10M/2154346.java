package batclient;

import java.net.*;
import java.io.*;

public class BattleClientIO {

    public static final int DEFAULT_PORT = 1234;

    public static final String DEFAULT_SERVER = "localhost";

    private Socket socket;

    private BufferedReader in;

    private PrintWriter out;

    public BattleClientIO(InetAddress addr, int io_port) throws IOException {
        socket = new Socket(addr, io_port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
    }

    public BattleClientIO() throws IOException {
        this(InetAddress.getByName(BattleClientIO.DEFAULT_SERVER), BattleClientIO.DEFAULT_PORT);
    }

    public BattleClientIO(String addr_name) throws IOException {
        this(InetAddress.getByName(addr_name), BattleClientIO.DEFAULT_PORT);
    }

    public BattleClientIO(String addr_name, int io_port) throws IOException {
        this(InetAddress.getByName(addr_name), io_port);
    }

    public void closeSocket() throws IOException {
        socket.close();
    }

    public String sendCommand(String command) {
        String reply;
        try {
            out.println(command);
            reply = in.readLine();
        } catch (IOException e) {
            reply = "ERROR: IOException during sendCommand";
        }
        return reply;
    }

    public void sendAcknowledgement(String command) {
        out.println(command);
    }

    public String getReply() {
        String reply;
        try {
            reply = in.readLine();
        } catch (IOException e) {
            reply = "ERROR: IOException during getReply";
        }
        return reply;
    }

    public static void main(String[] args) throws IOException {
        InetAddress addr = InetAddress.getByName(null);
        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, BattleClientIO.DEFAULT_PORT);
        try {
            System.out.println("socket = " + socket);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            for (int i = 0; i < 10; i++) {
                out.println("howdy " + i);
                String str = in.readLine();
                System.out.println(str);
            }
            out.println("END");
        } finally {
            System.out.println("closing...");
            socket.close();
        }
    }
}
