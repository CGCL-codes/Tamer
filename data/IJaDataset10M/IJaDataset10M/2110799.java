package player;

import java.net.Socket;
import server.Server;
import server.ServerNetwork;
import data.ChatData;
import data.Data;

/**
 *
 * @author willjasen
 *
 */
public class Human extends Player {

    protected ServerNetwork gameNetwork = null;

    protected ServerNetwork chatNetwork = null;

    protected String roomName;

    protected String loginToken;

    public Human() {
        super();
        gameNetwork = new ServerNetwork();
        chatNetwork = new ServerNetwork();
    }

    public Human(String username) {
        super(username);
    }

    public void addGameConnection(Socket gameSocket) {
        gameNetwork.setSocket(gameSocket);
    }

    public void addChatConnection(Socket chatSocket) {
        chatNetwork.setSocket(chatSocket);
    }

    public Human(Socket socket) {
        this();
    }

    public Human(String username, String loginToken) {
        this.username = username;
        this.loginToken = loginToken;
    }

    public void setHand(Hand hand) {
        super.hand = hand;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Socket getChatSocket() {
        return chatNetwork.getSocket();
    }

    public boolean hasGameSocket(Socket socket) {
        return gameNetwork.getSocket().equals(socket);
    }

    public synchronized void sendDataTo(Data data) {
        gameNetwork.sendData(data);
    }

    public synchronized void sendChatDataTo(ChatData data) {
        chatNetwork.sendData(data);
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public Socket getSocket() {
        return getGameSocket();
    }

    public Socket getGameSocket() {
        return gameNetwork.getSocket();
    }

    public void createGameThread(Server server) {
        gameNetwork.createGameThread(server);
    }

    public void createChatThread(Server server) {
        chatNetwork.createChatThread(server);
    }
}
