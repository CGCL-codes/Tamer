package case4;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import case3.Chat;

public class chatServer {

    private ObjectInputStream fromClient;

    private ApplicationController control;

    protected static Chat theChatModel;

    protected static HashMap<String, ObjectOutputStream> streamMap;

    public static void main(String[] args) throws ClassNotFoundException, Exception {
        new chatServer();
    }

    public chatServer() throws ClassNotFoundException, Exception {
        ServerSocket server = new ServerSocket(3008);
        theChatModel = new Chat();
        streamMap = new HashMap<String, ObjectOutputStream>();
        boolean keepRunning = true;
        while (keepRunning) {
            Socket clientSocket = server.accept();
            clientConnection client = new clientConnection(clientSocket);
            new Thread(client).start();
        }
    }

    public class clientConnection implements Runnable {

        private ObjectOutputStream toClient;

        private Socket client;

        public clientConnection(Socket socket) {
            this.client = socket;
        }

        @SuppressWarnings("unchecked")
        public void run() {
            control = new ApplicationController();
            try {
                fromClient = new ObjectInputStream(client.getInputStream());
                toClient = new ObjectOutputStream(client.getOutputStream());
                toClient.flush();
                boolean keepRunning = true;
                while (keepRunning) {
                    System.out.println("Waiting for command fromClient");
                    ComBean comBean = (ComBean) fromClient.readObject();
                    System.out.println("Got it, Thanks");
                    ArrayList params = comBean.getParameters();
                    params.add(toClient);
                    params.add(client);
                    control.handleRequest(comBean.getCommand(), comBean.getParameters());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
