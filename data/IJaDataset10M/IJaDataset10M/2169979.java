package jretro.game.blazer;

import java.util.*;
import java.io.IOException;
import jretro.common.network.*;
import jretro.game.blazer.message.*;

public class BlazerServerController extends Thread implements ServerController {

    private static final int SLEEP_TIME = 100;

    private Communicator communicator;

    private ClientContainer clientContainer;

    private CompositeMessage message = new CompositeMessage();

    private boolean running = true;

    private boolean hasMessages = false;

    private String serverName;

    public BlazerServerController(ClientContainer clientContainer) {
        this.clientContainer = clientContainer;
    }

    /**
	 * This method is called when a new client has connected to the server.
	 */
    public void clientConnected(Communicator clientCommunicator) {
        System.out.println("clientConnected");
        Client client = clientContainer.addClient(clientCommunicator);
        try {
            clientCommunicator.sendMessage(new NewPlayerMessage(serverName, client.getId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        addMessage(new AddPlayerMessage(client.getId(), client.getPlayerInfo()));
        for (Iterator i = clientContainer.getClients(); i.hasNext(); ) {
            Client c = (Client) i.next();
            try {
                clientCommunicator.sendMessage(new AddPlayerMessage(c.getId(), c.getPlayerInfo()));
            } catch (IOException e) {
            }
        }
    }

    /**
	 * This method is called when a client disconnects from the server.
	 */
    public void clientDisconnected(Communicator client) {
        System.out.println("clientDisconnected");
        Client c = clientContainer.removeClient(client);
        if (c != null) {
            System.out.println("SERVER: disconnected player #" + c.getId());
            addMessage(new RemovePlayerMessage(c.getId()));
        }
    }

    public void run() {
        running = true;
        while (isRunning()) {
            sendMessage();
            checkMessages();
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (Exception e) {
            }
        }
    }

    public synchronized boolean isRunning() {
        return running;
    }

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }

    public synchronized void addMessage(Message msg) {
        hasMessages = true;
        message.addMessage(msg);
    }

    public synchronized void sendMessage() {
        if (hasMessages == true) {
            try {
                communicator.sendMessage(message);
            } catch (IOException e) {
                System.out.println("IOException! communicator.sendMessage(msg)");
            }
            message.clear();
            hasMessages = false;
        }
    }

    public void close() {
        communicator.close();
    }

    private void checkMessages() {
        Message msg = communicator.getMessage();
        if (msg != null) {
            handleMessage(msg);
        }
    }

    private void handleMessage(Message msg) {
        addMessage(msg);
    }
}
