package ipcards.net;

import ipcards.*;
import ipcards.Controller;
import ipcards.Room;
import ipcards.Table;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;

/**
* The main server for IPCards. Listens for connections from clients.
* Contains the {@link Room} which contains the {@link Table} objects.
* 
* @author Ricky Vincent
*/
public class CardsServer implements Runnable {

    private int port = Controller.DEFAULT_PORT;

    private ServerSocket server;

    public static Vector<Client> clients;

    private Controller controller;

    private int turn = 0;

    public static final char SEE = 0;

    public static final char CONTROL = 1;

    public static final char PERMISSION = 2;

    private boolean serverRunning = true;

    public int[] newgamecheck = new int[1];

    public String gamename = "";

    /**
	 * Constructs a new server to listen for connections from clients on the specified port.
	 * @param port The port to listen for incoming connections.
	 * @throws IOException 
	 */
    public CardsServer(int port, Controller controller) throws IOException {
        this.controller = controller;
        this.port = port;
        this.controller.setCardsServer(this);
        server = new ServerSocket(port);
        clients = new Vector<Client>();
        System.out.println("new server");
    }

    /**
	 * Constructs a new server to listen for connections from clients.
	 * Listens on the default port.
	 * @throws IOException 
	 */
    public CardsServer(Controller controller) throws IOException {
        this.controller = controller;
        server = new ServerSocket(port);
        this.controller.setCardsServer(this);
    }

    /**
	 * Listens for incoming connections. Creates a new {@link Client} object for each new connection.
	 */
    private void listen() {
        Client newClient;
        while (serverRunning) {
            try {
                newClient = new Client(server.accept(), controller.getRoom().table, this);
                Thread thread = new Thread(newClient);
                thread.start();
                clients.add(newClient);
                int i = 0;
                int j = 0;
                for (Client c : clients) {
                    for (Client d : clients) {
                        if (!(c.getTable().getRoom().players.contains(d.thisplayer))) {
                            c.getTable().getRoom().players.add(d.thisplayer);
                            c.getTable().allPlayers.put(j, "TheHorseWithNoName");
                            System.out.println("updating client list:" + i + ":" + j);
                        }
                        j++;
                    }
                    i++;
                }
                if (clients.size() == 1) {
                    clients.get(0).myTurn(true);
                }
                if (controller.getRoom().table.getPlayer() == null) {
                    System.out.println("fail");
                }
                for (Card card : controller.getRoom().table.cards) {
                    card.addPermission(newClient.thisplayer, PERMISSION, controller.getRoom().table.getPlayer());
                    card.addPermission(newClient.thisplayer, CONTROL, controller.getRoom().table.getPlayer());
                    card.addPermission(newClient.thisplayer, SEE, controller.getRoom().table.getPlayer());
                }
                String playerlist = "";
                for (int key : clients.get(0).getTable().allPlayers.keySet()) {
                    playerlist += key;
                    playerlist += ":";
                    playerlist += (String) (clients.get(0).getTable().allPlayers.get(key));
                    playerlist += ":";
                }
                for (Client c : clients) {
                    c.sendCommand(ipcards.events.PlayerAction.SEND_NAME_HASHMAP, playerlist);
                    c.sendCommandToAllX(ipcards.events.PlayerAction.SEND_NAME_HASHMAP, playerlist);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Implemented from <code>Runnable</code> interface. Called when thread starts.
	 */
    public void run() {
        listen();
    }

    /**
	 * Stops this server.
	 */
    public void stopServer() {
        try {
            server.close();
            serverRunning = false;
        } catch (IOException e) {
            serverRunning = false;
        }
    }

    /**
	 * @return the controller
	 */
    public Controller getController() {
        return controller;
    }

    public int numPlayers() {
        return clients.size();
    }

    public void resetLocalFlip() {
        for (Client c : clients) {
            c.resetLocalFlip();
        }
    }

    public void nextTurn() {
        if (clients.size() != 1) {
            turn++;
            if (turn >= clients.size()) {
                turn = 0;
            }
            clients.get(turn).myTurn(true);
            clients.get(turn).sendCommandToAll(ipcards.events.PlayerAction.END_TURN, turn + "");
        } else {
            clients.get(0).myTurn(true);
        }
    }

    public void nextTurnReverse() {
        if (clients.size() != 1) {
            turn--;
            if (turn < 0) {
                turn = clients.size() - 1;
            }
            clients.get(turn).myTurn(true);
            clients.get(turn).sendCommandToAll(ipcards.events.PlayerAction.END_TURN, turn + "");
        } else {
            clients.get(0).myTurn(true);
        }
    }

    public int getTurn() {
        return turn;
    }

    public void newgame(Client source, int response) {
        if (response == 10) {
            newgamecheck = new int[clients.size()];
            for (int i = 0; i < newgamecheck.length; i++) {
                newgamecheck[i] = -1;
            }
            response = 0;
        }
        int clientnum = 0;
        for (Client c : clients) {
            if (source == c) {
                newgamecheck[clientnum] = response;
            }
            clientnum++;
        }
    }

    public boolean newGameAllRespondedQuery() {
        for (int i = 0; i < newgamecheck.length; i++) {
            if (newgamecheck[i] == -1) {
                return false;
            }
        }
        return true;
    }

    public boolean newGameApproved() {
        for (int i = 0; i < newgamecheck.length; i++) {
            if (newgamecheck[i] == 1) {
                return false;
            }
        }
        return true;
    }
}
