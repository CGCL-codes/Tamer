package tetranoid.network.client;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import tetranoid.network.Message;
import tetranoid.network.MessageType;
import tetranoid.ui.ErrorWindow;

public class ClientLoop implements Runnable {

    ClientLoop(String adress, int port, Client client) {
        this.adress = adress;
        this.port = port;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            System.err.println("Client is connecting to server.");
            socket = new Socket(adress, port);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            shouldRun = true;
            while (socket.isConnected() && shouldRun) {
                if (in.ready() && shouldRun) {
                    client.processMessage(new Message(in.readLine()));
                }
                if (!messageQueue.isEmpty()) {
                    Message msg = messageQueue.remove();
                    out.append(msg.toString());
                    out.newLine();
                    out.flush();
                }
                Thread.yield();
            }
            if (socket.isConnected()) {
                Message msg = new Message(MessageType.UNREGISTER_PLAYER);
                msg.setParam("nick", client.getNick());
                out.append(msg.toString());
                out.newLine();
                out.flush();
                socket.close();
            }
        } catch (UnknownHostException ex) {
            ErrorWindow.showError(ErrorWindow.Text.SERVER_NOT_FOUND);
            shouldRun = false;
            return;
        } catch (IOException ex) {
            ErrorWindow.showError(ErrorWindow.Text.NETWORK_IO_ERROR);
            shouldRun = false;
            return;
        }
    }

    public synchronized void notifyServer(Message message) {
        messageQueue.add(message);
    }

    public void close() {
        shouldRun = false;
    }

    private String adress;

    private int port;

    private Client client;

    private Socket socket;

    private boolean shouldRun;

    private Queue<Message> messageQueue = new LinkedList<Message>();
}
