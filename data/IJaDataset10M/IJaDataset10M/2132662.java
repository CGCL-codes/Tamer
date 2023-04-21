package de.creepsmash.server.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import de.creepsmash.server.Client;

/**
 * A list of the players participating in a game.
 */
public class PlayerList implements Iterable<PlayerInGame> {

    private List<PlayerInGame> players;

    /**
	 * Create a new, empty one.
	 */
    public PlayerList() {
        this.players = new ArrayList<PlayerInGame>();
    }

    /**
	 * Returns an iterator over the players.
	 * @return an iterator that yields the players in the right order.
	 */
    public Iterator<PlayerInGame> iterator() {
        return this.players.iterator();
    }

    /**
	 * Returns true if this PlayerList contains zero players.
	 * @return true is this list is empty.
	 */
    public boolean isEmpty() {
        return this.players.isEmpty();
    }

    /**
	 * Add a player, which will be wrapped in a PlayerInGame object.
	 * @param c the client to add. Must not be null.
	 */
    public void add(Client c) {
        if (c == null) {
            throw new IllegalArgumentException("'c' was null");
        }
        this.players.add(new PlayerInGame(c));
        Collections.sort(this.players, new Comparator<PlayerInGame>() {

            public int compare(PlayerInGame p1, PlayerInGame p2) {
                Client c1 = p1.getClient();
                Integer id1 = c1.getClientID();
                Client c2 = p2.getClient();
                Integer id2 = c2.getClientID();
                return id1.compareTo(id2);
            }
        });
    }

    /**
	 * Returns the player with the given id.
	 * @param clientId the clientId to look for
	 * @return the player with the given id, or null if it can't be found.
	 */
    public PlayerInGame get(int clientId) {
        int i = find(clientId);
        return i == -1 ? null : this.players.get(i);
    }

    /**
	 * Returns the player with the given username.
	 * @param userName the userName to look for
	 * @return the player with the given username, or null if there is it
	 * can't be found.
	 */
    public PlayerInGame get(String userName) {
        for (PlayerInGame p : this.players) {
            if (p.getClient().getUserName().equals(userName)) {
                return p;
            }
        }
        return null;
    }

    /**
	 * Find the player with the given id.
	 * @param clientId the clientId to look for
	 * @return an index into the 'players' list
	 */
    private int find(int clientId) {
        for (int i = 0; i < this.players.size(); i++) {
            if (this.players.get(i).getClient().getClientID() == clientId) {
                return i;
            }
        }
        return -1;
    }

    /**
	 * Remove the player with the given id from the list.
	 * @param clientId the clientId to look for
	 * @return the player that was remove; or null if it could not be found
	 */
    public PlayerInGame remove(int clientId) {
        int i = find(clientId);
        if (i == -1) {
            return null;
        } else {
            return this.players.remove(find(clientId));
        }
    }

    /**
	 * Returns the successor of the player with the given id (the next one on
	 * the playing field).
	 * @param clientId the clientId to look for
	 * @return the successor of the player with 'clientId', or null it that one
	 * could not be found
	 */
    public PlayerInGame succ(int clientId) {
        int i = find(clientId);
        if (i == -1) {
            return null;
        }
        return this.players.get(i == this.players.size() - 1 ? 0 : i + 1);
    }

    /**
	 * Return the number of players in this list.
	 * @return the number of players in this list.
	 */
    public int size() {
        return this.players.size();
    }

    /**
	 * Changes the players' order randomly.
	 */
    public void shuffle() {
        List<PlayerInGame> oldList = this.players;
        List<PlayerInGame> newList = new LinkedList<PlayerInGame>();
        Collections.shuffle(oldList);
        Random random = new Random();
        while (!oldList.isEmpty()) {
            int i = random.nextInt(oldList.size());
            newList.add(oldList.remove(i));
        }
        this.players = newList;
    }
}
