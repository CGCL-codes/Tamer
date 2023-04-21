package v2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author NGUYEN, Viet Quang - Student number:
 * @version 2
 */
public class ServerV2 {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            usage();
            System.exit(-1);
        }
        int port = Integer.parseInt(args[0]);
        ServerV2 server = new ServerV2(port);
        server.run();
    }

    static void usage() {
        System.out.println("Usage:");
        System.out.println("\tjava Server port");
        System.out.println("Example:");
        System.out.println("\tjava Client 7384");
        System.out.println();
    }

    private int port;

    private ServerSocket serverSocket;

    /**
     * Constructor.
     * 
     * @param port
     *            int port number on which our server application will be
     *            listening.
     */
    public ServerV2(int port) {
        this.port = port;
    }

    /**
     * Server's main method.
     * 
     * @throws IOException
     */
    public void run() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server is up and running on port [" + port + "]");
        System.out.println("Press Ctrl-C to terminate the server.");
        int numClients = 0;
        while (true) {
            Socket client = serverSocket.accept();
            numClients++;
            serveClient(client, numClients);
        }
    }

    /**
     * Serves a client. The server will read a line from client, echo it back
     * and then terminate the connection.
     * 
     * @param client
     *            Socket
     * @param clientId
     *            int
     * @throws IOException
     */
    void serveClient(Socket client, int clientId) throws IOException {
        System.out.println("Client [" + clientId + ": " + client + "] connected!");
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String dataFromClient = reader.readLine();
        System.out.println("Client sent: " + dataFromClient);
        PrintWriter writer = new PrintWriter(client.getOutputStream());
        writer.println(dataFromClient);
        writer.flush();
        writer.close();
        reader.close();
        client.close();
        System.out.println("Client [" + clientId + ": " + client + "] disconnected!");
    }
}
