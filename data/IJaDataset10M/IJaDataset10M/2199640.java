package de.fmf.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InfoServer implements Runnable {

    public static final String INFOSERVERADDRESS = "140.5.1.101";

    public static final int INFOSERVERPORT = 9999;

    private static InfoServer thisClass;

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    private BufferedReader in;

    private int port;

    private ServerSocket serverSocket;

    private Socket clientSocket;

    private String command = "";

    public static void main(String[] args) {
        thisClass = new InfoServer(INFOSERVERPORT);
    }

    public InfoServer(int port) {
        this.port = port;
        Thread t = new Thread(this);
        t.start();
    }

    public InfoServer(StringBuffer info) {
    }

    @Override
    public void run() {
        System.out.println("INFOSERVER READY");
        try {
            serverSocket = new ServerSocket(port);
            while (!command.equals(Iserver.COMMAND_SHUTDOWN)) {
                openListener();
            }
            clientSocket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openListener() {
        Date dStart = new Date(System.currentTimeMillis());
        try {
            clientSocket = serverSocket.accept();
            System.out.print("\n### " + sdf.format(dStart) + " ###\t\t");
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            processRequest();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processRequest() throws IOException {
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            String[] info = inputLine.split("\\#");
            for (int i = 0; i < info.length; i++) {
                System.out.println(info[i]);
            }
        }
    }
}

class InfoServerRequest implements Runnable {

    private StringBuffer info;

    public InfoServerRequest(StringBuffer info) {
        this.info = info;
    }

    @Override
    public void run() {
        Socket kkSocket = null;
        try {
            kkSocket = new Socket(InfoServer.INFOSERVERADDRESS, InfoServer.INFOSERVERPORT);
            postInfo(kkSocket);
        } catch (Exception e) {
        }
    }

    private void postInfo(Socket socket) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(info);
            out.close();
            socket.close();
            out = null;
            socket = null;
        } catch (Exception e) {
            System.out.println("ERROR INFOSERVERREQUEST: " + info);
            e.printStackTrace();
        }
    }
}
