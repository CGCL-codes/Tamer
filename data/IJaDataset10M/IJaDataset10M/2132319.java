package se.mushroomwars.gameplay;

import java.nio.channels.SocketChannel;

public class Player {

    private SocketChannel socket;

    private int id;

    private String nickName;

    private boolean admin;

    public Player(SocketChannel socket, int id) {
        this.socket = socket;
        this.id = id;
        nickName = "NOT_SET";
        admin = false;
    }

    public int getID() {
        return id;
    }

    public SocketChannel getSocket() {
        return socket;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        if (admin) this.nickName = "[Admin]" + nickName; else this.nickName = nickName;
    }

    public void setAdmin(boolean state) {
        admin = state;
    }

    public boolean isAdmin() {
        return admin;
    }
}
