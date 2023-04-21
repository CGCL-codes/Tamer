package tictactoe.multiplayer.server;

import tictactoe.Player;
import tictactoe.exceptions.network.NetworkException;
import tictactoe.exceptions.network.UnableToOpenChannelException;
import tictactoe.multiplayer.Message;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread {

    private Socket socket;

    private Server server;

    private ClientHandler oponent;

    private BufferedReader in;

    private PrintWriter out;

    private boolean receive;

    private boolean connected = false;

    private Player player;

    private Logger log;

    public ClientHandler(Socket socket, Server server) throws NetworkException {
        log = Logger.getLogger("serverLog");
        this.socket = socket;
        this.server = server;
        receive = true;
        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())), true);
            log.info("Succesfuly created client thread");
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            throw new UnableToOpenChannelException(ex);
        }
    }

    @Override
    public void run() {
        log.info("ClientThread listening...");
        server.addClientHandler(this);
        while (receive) {
            try {
                receive(in.readLine());
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        server.removeClientHandler(this);
        log.info("ClientThread stopped listening...");
    }

    public void processException(Exception ex) {
        String msg = "exception " + ex.getMessage();
        send(msg);
    }

    private void terminateConnection() {
        receive = false;
        interrupt();
        try {
            server.unregisterPlayer(player);
        } catch (NetworkException ok) {
        }
        server.removeClientHandler(this);
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void receive(String message) {
        log.info("Received message: " + message);
        if (message == null) {
            terminateConnection();
        }
        message = message.trim().toLowerCase();
        if (message.startsWith(Message.CONNECT)) {
            receiveConnect(message.trim().substring(Message.CONNECT.length() + 1));
        }
        if (message.equals(Message.GET_PLAYERS)) {
            receiveGetPlayers();
        }
        if (message.startsWith(Message.PLAY_WITH)) {
            receivePlayWith(message.substring(Message.PLAY_WITH.length() + 1));
        }
        if (message.startsWith(Message.ACCEPT)) {
            receiveAccept(message.substring(Message.ACCEPT.length() + 1));
        }
        if (message.startsWith(Message.MOVE)) {
            receiveMove(message.substring(Message.MOVE.length() + 1));
        }
        if (message.equals(Message.DISCONNECT)) {
            receiveDisconnect();
        }
    }

    private void receiveConnect(String name) {
        Player p = new Player(name);
        try {
            server.registerPlayer(p, this);
            connected = true;
            player = p;
            sendConnected();
        } catch (NetworkException ex) {
            processException(ex);
        }
    }

    private void receiveGetPlayers() {
        log.info("getPlayers received");
        sendPlayers();
    }

    private void receivePlayWith(String name) {
        if (!connected) {
            processException(new NetworkException("You are not connected"));
        } else {
            Player p = new Player(name);
            try {
                ClientHandler h = server.getHandler(p);
                h.sendWantPlay(player);
            } catch (NetworkException ex) {
                processException(ex);
            }
        }
    }

    private void receiveAccept(String name) {
        if (!connected) {
            processException(new NetworkException("You are not connected"));
        } else {
            Player p = new Player(name);
            try {
                oponent = server.getHandler(p);
                oponent.sendAccepted(player, this);
                server.unregisterPlayer(player);
            } catch (NetworkException ex) {
                processException(ex);
            }
        }
    }

    private void receiveMove(String point) {
        if (!connected) {
            processException(new NetworkException("You are not connected"));
        } else if (oponent == null) {
            processException(new NetworkException("You have no oponent"));
        } else {
            oponent.sendOtherMove(point);
        }
    }

    private void receiveDisconnect() {
        if (oponent != null) {
            oponent.sendOtherDisconnected();
        }
        terminateConnection();
    }

    private void send(String message) {
        log.info("Sending message: " + message + " to " + player.getName());
        out.println(message);
    }

    private void sendConnected() {
        send(Message.CONNECTED);
    }

    private void sendPlayers() {
        log.info("Sending players");
        String msg = Message.PLAYERS + " " + server.getPlayerString();
        send(msg);
    }

    private void sendWantPlay(Player player) {
        String msg = Message.WANT_PLAY + " " + player.getName();
        send(msg);
    }

    private void sendAccepted(Player p, ClientHandler oponent) {
        this.oponent = oponent;
        try {
            server.unregisterPlayer(player);
            String msg = Message.ACCEPTED + " " + p.getName();
            send(msg);
        } catch (NetworkException ex) {
            processException(ex);
        }
    }

    private void sendOtherMove(String move) {
        String msg = Message.OTHER_MOVE + " " + move;
        send(msg);
    }

    private void sendOtherDisconnected() {
        send(Message.OTHER_DISCONNECTED);
    }

    public void sendServerDown() {
        send(Message.SERVER_DOWN);
        terminateConnection();
    }
}
