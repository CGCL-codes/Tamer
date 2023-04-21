package org.DragonPokerServer.dbclasses;

import java.util.ArrayList;
import org.DragonPokerServer.Classes.Hand;
import org.DragonPokerServer.Classes.Player;
import org.DragonPokerServer.Thread.ThreadUser;

/**
 *
 * @author Administrator
 */
public class Table {

    private int id;

    private int dimension;

    ArrayList<Player> users = new ArrayList<Player>();

    Room room;

    Hand hand;

    public Table() {
        setNullUsers();
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public ArrayList<Player> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Player> users) {
        this.users = users;
    }

    public void setNullUsers() {
        for (int i = 0; i < dimension; i++) users.add(null);
    }

    public void setUserInTable(int idPlace, Player user) {
        users.set(idPlace, user);
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }
}
