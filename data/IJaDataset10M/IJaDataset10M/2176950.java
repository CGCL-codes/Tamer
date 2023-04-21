package v4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server - Processor
 * 
 * @author NGUYEN, Viet Quang - Student number: s3045708
 * @version 4
 */
public class ServerTCP {

    private ServerGUI gui;

    private int port;

    private ServerSocket serverSocket;

    public ServerTCP(int port) {
        this.port = port;
    }

    public void runTCP() throws IOException {
        port = gui.getPort();
        serverSocket = new ServerSocket(port);
        writeLogS();
        writeLogS("Server is up and running on port [" + port + "]");
        writeLogS("Press Ctrl-C to terminate the server.");
        writeLogS();
        int numClients = 0;
        while (true) {
            Socket client = serverSocket.accept();
            numClients++;
            serveClient(client, numClients);
        }
    }

    void serveClient(Socket client, int clientId) throws IOException {
        ClientHandlerThread clientHandler = new ClientHandlerThread(client, clientId);
        clientHandler.start();
    }

    void writeLogS() {
        writeLogS("");
    }

    void writeLogS(String log) {
        gui.writeLogS(log);
    }
}

class ClientHandlerThread extends Thread {

    private Socket client;

    private int clientId;

    private ServerGUI gui;

    public ClientHandlerThread(Socket client, int clientId) {
        this.client = client;
        this.clientId = clientId;
        writeLogS("Client [" + clientId + ": " + client + "] connected!");
        writeLogS();
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter writer = new PrintWriter(client.getOutputStream());
            String dataFromClient = reader.readLine();
            while (dataFromClient != null && dataFromClient.length() > 0) {
                writeLogS("Data receive from client: [" + dataFromClient + "] sending back !");
                writeLogS();
                writer.println(dataFromClient);
                writer.flush();
                dataFromClient = reader.readLine();
            }
            writer.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (Exception e) {
            }
            writeLogS("Client [" + clientId + ": " + client + "] disconnected!");
        }
    }

    void writeLogS() {
        writeLogS("");
    }

    void writeLogS(String log) {
        gui.writeLogS(log);
    }
}
