package ipcards;

import java.util.*;
import javax.swing.JOptionPane;

/**
The top-level data class.
*/
public class Room {

    public Table table;

    public ArrayList<Player> players = new ArrayList<Player>();

    public Player localPlayer;

    public Controller controller;

    public Room(Controller controller) {
        this.table = new Table();
        table.setRoom(this);
        this.controller = controller;
    }

    public void addPlayer(Player p) {
        players.add(p);
        p.setRoom(this);
        if (localPlayer == null) localPlayer = p;
    }

    public void setLocalPlayer(Player p) {
        localPlayer = p;
    }

    public void removePlayer(Player p) {
        players.remove(p);
    }

    public Table getTable() {
        return table;
    }

    public void showDialog(String args) {
        JOptionPane.showMessageDialog(controller.view, args);
    }
}
